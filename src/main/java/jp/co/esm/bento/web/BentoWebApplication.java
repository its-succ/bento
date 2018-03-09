package jp.co.esm.bento.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

@SpringBootApplication
public class BentoWebApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(BentoWebApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(BentoWebApplication.class, args);
  }
  
  @Bean
  public DatastoreService cloudDatastoreService() {
    return DatastoreServiceFactory.getDatastoreService();
  }
}
