package jp.co.esm.bento.web.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.repository.OrderRepository;
import jp.co.esm.bento.web.util.DateUtil;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  /**
   * 指定のユーザの指定の週の5日分の注文内容を取得します。
   * 
   * @param userId ユーザID
   * @param week 週の初めの日付
   * @return 該当する注文すべて（最大5件）
   */
  public List<Order> getOrders(String userId, LocalDate week)
  {
    List<Order> results = orderRepository.listByUserIdAndWeek(userId, week);
    if (results.isEmpty() || results.size() < 5) {
      // TODO 5日分の注文がない場合
      results = createDefaultOrders(results, userId, week);
    }
    return results;
  }
  
  /**
   * TODO クライアント側にお任せするならいらない
   * 指定の内容で5日分のデフォルト注文を返します。
   * 
   * @param orders DBに保存されている注文内容
   * @param userId ユーザID
   * @param week 週の初めの日付
   * @return 5日分の注文
   */
  private List<Order> createDefaultOrders(List<Order> orders, String userId, LocalDate week) {
    List<Order> results = new ArrayList<>(orders);
    List<LocalDate> listDate = DateUtil.weekDate(week);
    for (LocalDate date : listDate) {
      if (orders.stream().anyMatch(o -> o.getDate().equals(date))) {
        continue;
      }
      Order order = new Order();
      order.setDate(date);
      order.setUserId(userId);
      order.setGohan("");
      order.setOkazu("");
      order.setMiso(false);
      order.setPrice(0L);
      results.add(order);
    }
    // 日付でソート
    results.sort(Comparator.comparing(Order::getDate));
    return results;
  }
}
