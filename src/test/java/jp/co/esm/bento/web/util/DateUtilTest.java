package jp.co.esm.bento.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
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
  public void testConvert_nullの場合はnull() {
    Date actual = DateUtil.convert(null);
    assertThat(actual, is(nullValue()));
  }

  @Test
  public void testConvert_空の場合はnull() {
    Date actual = DateUtil.convert("");
    assertThat(actual, is(nullValue()));
  }

  @Test
  public void testConvert_フォーマット違反の場合はnull() {
    Date actual = DateUtil.convert("20180309");
    assertThat(actual, is(nullValue()));
  }

  @Test
  public void testConvert_正常() {
    Date actual = DateUtil.convert("2018-03-09");
    assertThat(actual, is(notNullValue()));
    
  }

  @Test
  public void testWeekDate_nullの場合は空() {
    List<Date> actual = DateUtil.weekDate(null);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  @Test
  public void testWeekDate_空の場合は空() {
    List<Date> actual = DateUtil.weekDate("");
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  @Test
  public void testWeekDate_フォーマット違反の場合は空() {
    List<Date> actual = DateUtil.weekDate("20180309");
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  @Test
  public void testWeekDate_正常() {
    List<Date> actual = DateUtil.weekDate("2018-03-05");
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(5));
    
    assertThat(actual.get(0), is(DateUtil.convert("2018-03-05")));
    assertThat(actual.get(1), is(DateUtil.convert("2018-03-06")));
    assertThat(actual.get(2), is(DateUtil.convert("2018-03-07")));
    assertThat(actual.get(3), is(DateUtil.convert("2018-03-08")));
    assertThat(actual.get(4), is(DateUtil.convert("2018-03-09")));
  }
}
