package jp.co.esm.bento.web.controller;

import java.util.List;

import jp.co.esm.bento.web.model.GoogleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @GetMapping("{week}")
  public List<Object> getOrders(String week, @AuthenticationPrincipal GoogleUser user) {
    log.info("getOrders");
    log.info("user: {}", user);
    return null;
  }
}
