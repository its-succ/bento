package jp.co.esm.bento.web.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jp.co.esm.bento.web.model.GoogleUser;
import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.service.CalendarService;
import jp.co.esm.bento.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  // 注文サービス
  @Autowired
  private OrderService orderService;
  
  // googleカレンダーサービス
  @Autowired
  private CalendarService calendarService;

  /**
   * 指定のユーザと週に該当する注文内容を取得します。
   * 
   * @param week 週の日付（月曜日始まり）
   * @param user ユーザ
   * @return 注文内容
   */
  @GetMapping("/{week}")
  public List<Order> getOrders(@PathVariable LocalDate week, @AuthenticationPrincipal GoogleUser user, HttpServletRequest request) {
    log.info("getOrders");
    log.info("week: {}", week);
    log.info("user: {}", user);
    // 指定の注文情報を取得
    List<Order> orders = orderService.getOrders(user.getUserId(), week);
    // 休日情報を設定
    setHoliday(orders, request.getRequestURL().toString());
    return orders;
  }

  /**
   * 指定のユーザと週に該当する注文内容を登録または更新します。
   * 
   * @param week 週の日付（月曜日始まり）
   * @param orders 注文内容
   * @param user ユーザ
   * @return 注文内容
   */
  @PostMapping("/{week}")
  public List<Order> updateOrders(@PathVariable LocalDate week, @RequestBody List<Order> orders,@AuthenticationPrincipal GoogleUser user) {
    // 指定の内容でDatastoreに反映
    orderService.createOrUpdateOrders(orders, user.getUserId(), week);
    // 登録結果を返す
    return orderService.getOrders(user.getUserId(), week);
  }

  /**
   * 指定の注文内容の注文日に対して休日かどうかを設定します。
   * 
   * @param orders 注文内容
   * @param requestUrl リクエストURL(認証で使用）
   */
  private void setHoliday(List<Order> orders, String requestUrl) {
    // Googleの祝日カレンダーより指定範囲の祝日を取得
    List<LocalDate> holidays = calendarService.GetHolidays(orders.get(0).getDate(), orders.get(4).getDate(), requestUrl);
    if (holidays == null || holidays.isEmpty()) {
      return;
    }
    // 注文日に祝日があれば設定
    orders.stream().filter(o -> holidays.contains(o.getDate())).forEach(o -> o.setHoliday(true));
  }
}
