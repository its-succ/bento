package jp.co.esm.bento.web.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarServiceTest {

  @Autowired
  private CalendarService service;
  
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetHolidays_休日なし() {
    LocalDate from = LocalDate.of(2018, 3, 5);
    LocalDate to = LocalDate.of(2018, 3, 9);
    List<LocalDate> actual = service.getHolidays(from, to, "http://localhost:8080");
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  @Test
  public void testGetHolidays_休日あり() {
    LocalDate from = LocalDate.of(2018, 4, 30);
    LocalDate to = LocalDate.of(2018, 5, 4);
    List<LocalDate> actual = service.getHolidays(from, to, "http://localhost:8080");
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(3));
    
    assertThat(actual.get(0), is(LocalDate.of(2018, 4, 30)));
    assertThat(actual.get(1), is(LocalDate.of(2018, 5, 3)));
    assertThat(actual.get(2), is(LocalDate.of(2018, 5, 4)));
  }
}
