package jp.co.esm.bento.web.controller;

import java.util.List;

import jp.co.esm.bento.web.model.GoogleUser;
import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  // マスタサービス
  @Autowired
  private OrderService orderService;

  @GetMapping("/{week}")
  public List<Order> getOrders(@PathVariable String week, @AuthenticationPrincipal GoogleUser user) {
    log.info("getOrders");
    log.info("week: {}", week);
    log.info("user: {}", user);
    return orderService.getOrders(user.getUserId(), week);
  }
}
