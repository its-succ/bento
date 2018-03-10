package jp.co.esm.bento.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateUtilTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testWeekDate_nullの場合は空() {
    List<LocalDate> actual = DateUtil.weekDate(null);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  @Test
  public void testWeekDate_正常() {
    LocalDate start = LocalDate.of(2018, 3, 5);
    List<LocalDate> actual = DateUtil.weekDate(start);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(5));
    
    assertThat(actual.get(0), is(LocalDate.of(2018, 3, 5)));
    assertThat(actual.get(1), is(LocalDate.of(2018, 3, 6)));
    assertThat(actual.get(2), is(LocalDate.of(2018, 3, 7)));
    assertThat(actual.get(3), is(LocalDate.of(2018, 3, 8)));
    assertThat(actual.get(4), is(LocalDate.of(2018, 3, 9)));
  }
}
