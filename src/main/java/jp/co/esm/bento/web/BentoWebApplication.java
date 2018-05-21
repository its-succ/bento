package jp.co.esm.bento.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import jp.co.esm.bento.web.service.MasterService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
@Log4j
public class BentoWebApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(BentoWebApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(BentoWebApplication.class, args);
    log.info("main::run finished");
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

  @Bean
  public ApplicationRunner initialDataCreator() {
    return new ApplicationRunner() {

      @Autowired
      private MasterService masterService;

      @Override
      public void run(ApplicationArguments args) throws Exception {

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
          // Running on DevAppServer

          if (masterService.getAllMaster().getGohan().isEmpty()) {
            log.info("create init data");
            masterService.loadData();
          }
        }
      }
    };
  }
}
