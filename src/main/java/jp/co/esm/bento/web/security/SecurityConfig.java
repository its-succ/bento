package jp.co.esm.bento.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private RestAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private RestSavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;

  @Override
  public void configure(WebSecurity web) {
    web
      .ignoring()
        .antMatchers("/", "/index.html", "/*.css", "/js/*.js");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
        .antMatchers("/api/**").authenticated()
        .and()
      .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
      .formLogin()
        .passwordParameter("google_id_token")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
    ;
  }

  @Bean
  public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler();
  }
}
