package jp.co.esm.bento.web.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
      results = createDefaultOrders(results, userId, week);
    }
    return results;
  }

  /**
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

    if (isNotOrder(orders, week)) {
      return Collections.emptyList();
    }
    for (LocalDate date : listDate) {
      if (orders.stream().anyMatch(o -> o.getDate().equals(date))) {
        continue;
      }
      Order order = new Order();
      order.setDate(date);
      order.setUserId(userId);
      order.setGohan("");
      order.setGohanLabel("");
      order.setGohanPrice(0L);
      order.setOkazu("");
      order.setGohanLabel("");
      order.setGohanPrice(0L);
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
        boolean noOrder = order.noOrder();
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
          result.setOkazuLabel(order.getOkazuLabel());
          result.setOkazuPrice(order.getOkazuPrice());
          result.setGohan(order.getGohan());
          result.setGohanLabel(order.getGohanLabel());
          result.setGohanPrice(order.getGohanPrice());
          result.setMiso(order.getMiso());
          result.setPrice(order.getPrice());
          orderRepository.update(result);
        }
      }
      if (orders.stream().allMatch(o -> o.noOrder())) {
        // すべて注文なしの場合は「注文しない」とみなす
        Order order = new Order();
        order.setDate(week);
        order.setUserId(userId);
        orderRepository.create(order);
      }
      transaction.commit();

    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }

  /**
   * 「この週は注文しない」状態かどうかを返します。
   * 空の注文が1件のみある場合は注文しないとします。
   *
   * @param orders 注文内容
   * @param week 週の初めの日付
   * @return
   */
  private boolean isNotOrder(List<Order> orders, LocalDate week) {
    if (orders.isEmpty()) {
      // 未登録の場合は対象外
      return false;
    }
    if (orders.size() > 1) {
      // 2件以上注文があれば対象外
      return false;
    }
    Order order = orders.get(0);
    if (order.getDate().equals(week)) {
      return order.noOrder();
    }
    return false;
  }
}
