package jp.co.esm.bento.web;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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
  
  @Bean
  public ObjectMapper jsonObjectMapper() {
      JavaTimeModule module = new JavaTimeModule();
      module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(module);
      return mapper;
  }

  @Bean
  public Formatter<LocalDate> localDateFormatter() {
      return new Formatter<LocalDate>() {
          @Override
          public String print(LocalDate object, Locale locale) {
              return object.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          }
          @Override
          public LocalDate parse(String text, Locale locale) throws ParseException {
              return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          }
      };
  }
}
