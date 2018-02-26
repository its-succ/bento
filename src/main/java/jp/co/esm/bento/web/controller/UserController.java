package jp.co.esm.bento.web.controller;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private UserService userService;

  public UserController() {
    userService = UserServiceFactory.getUserService();
  }

  @GetMapping("current")
  User getCurrentUser() {
    return userService.getCurrentUser();
  }
}
