package jp.co.esm.bento.web.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.repository.OrderRepository;
import jp.co.esm.bento.web.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Orderのサービスクラスです。
 */
@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DatastoreService datastore;
  
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

  /**
   * 指定の内容で注文内容をOrderエンティティに登録または更新します。
   * 
   * @param orders 注文内容（最大5件）
   * @param userId ユーザID
   * @param week 週の初めの日付
   */
  public void createOrUpdateOrders(List<Order> orders, String userId, LocalDate week) {
    TransactionOptions options = TransactionOptions.Builder.withXG(true);
    Transaction transaction = datastore.beginTransaction(options);
    try {
      
      for (Order order : orders) {
        // 注文なしかどうか
        boolean noOrder = isNoOrder(order);
        // 対象の注文が登録済みか
        Order result = orderRepository.readByUserIdAndDate(userId, order.getDate());
        if (result == null) {
          // 注文なしは登録しない
          if (noOrder) continue;
          order.setUserId(userId);
          orderRepository.create(order);
        } else if (noOrder) {
          // 登録済み->注文なしのため削除
          orderRepository.delete(result.getId());
        } else {
          // 注文内容を更新
          result.setOkazu(order.getOkazu());
          result.setGohan(order.getGohan());
          result.setMiso(order.getMiso());
          result.setPrice(order.getPrice());
          orderRepository.update(result);
        }
      }
      transaction.commit();

    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }
  
  /**
   * 対象の注文が空かどうかチェックします。
   * @param order 注文内容
   * @return 注文なしの場合はtrue、ありの場合はfalse
   */
  private boolean isNoOrder(Order order) {
    // おかずチェック
    if (order.getOkazu() != null &&
        !order.getOkazu().isEmpty()) {
        return false;
    }
    // ごはんチェック
    if (order.getGohan() != null &&
        !order.getGohan().isEmpty()) {
      return false;
    }
    // おかず・ごはんなしで味噌汁ありはないのでチェックしない
    return true;
  }
}
