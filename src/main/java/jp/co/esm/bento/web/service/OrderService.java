package jp.co.esm.bento.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.repository.OrderRepository;
import jp.co.esm.bento.web.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;
  
  /**
   * 指定のユーザの指定の週の
   * @param userId ユーザID
   * @param week 週の初めの日付
   * @return 該当する注文すべて（最大5件）
   */
  public List<Order> getOrders(String userId, String week)
  {
    List<Order> results = orderRepository.listByUserIdAndWeek(userId, week);
    // データが存在しない場合は空のOrderを作成する
    if (results.isEmpty()) {
      log.info("Order not found. It will be created by default value.");
      return createDefaultOrders(userId, week);
    }
    return results;
  }
  
  private List<Order> createDefaultOrders(String userId, String week) {
    List<Order> results = new ArrayList<Order>();
    List<Date> listDate = DateUtil.weekDate(week);
    log.info("date: {}", listDate);
    for (Date date : listDate) {
        Order order = new Order();
        order.setDate(date);
        order.setUserId(userId);
        results.add(order);
    }
    log.info("orders: {}", results);
    return results;
  }
}
