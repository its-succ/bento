package jp.co.esm.bento.web.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

  @Autowired
  private OrderService serivice;

  @MockBean
  private OrderRepository repository;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetOrders_データが1件も存在しない場合すべてデフォルトであること() {
    doReturn(Collections.emptyList()).when(repository)
      .listByUserIdAndWeek(anyString(), anyObject());

    String userId = "hoge@esm.co.jp";
    LocalDate week = LocalDate.of(2018, 3, 5);
    List<Order> actual = serivice.getOrders(userId, week);

    assertThat(actual, is(notNullValue()));

    IntStream.range(0, 5).forEach(i -> {
      assertThat(actual.get(i).getDate(), is(week.plusDays(i)));
      assertThat(actual.get(i).getUserId(), is("hoge@esm.co.jp"));
      assertThat(actual.get(i).getGohan(), is(""));
      assertThat(actual.get(i).getOkazu(), is(""));
      assertThat(actual.get(i).getMiso(), is(false));
      assertThat(actual.get(i).getPrice(), is(new Long(0)));
    });
  }

  @Test
  public void testGetOrders_データが5件に満たない場合はデフォルト値を含むこと() {
    String userId = "hoge@esm.co.jp";
    LocalDate week = LocalDate.of(2018, 3, 5);

    // 月、水、木のダミーデータ作成
    List<Order> demoData = createData(new int[] {1,3,4}, userId);
    doReturn(demoData).when(repository)
      .listByUserIdAndWeek(anyString(), anyObject());

    List<Order> actual = serivice.getOrders(userId, week);

    assertThat(actual, is(notNullValue()));

    // 火、金はデフォルトの値であること
    IntStream.of(1,4).forEach(i -> {
      assertThat(actual.get(i).getDate(), is(week.plusDays(i)));
      assertThat(actual.get(i).getUserId(), is("hoge@esm.co.jp"));
      assertThat(actual.get(i).getGohan(), is(""));
      assertThat(actual.get(i).getOkazu(), is(""));
      assertThat(actual.get(i).getMiso(), is(false));
      assertThat(actual.get(i).getPrice(), is(new Long(0)));
    });

    // 月、水、木はダミーデータであること
    assertThat(actual.get(0).getDate(), is(demoData.get(0).getDate()));
    assertThat(actual.get(0).getUserId(), is(demoData.get(0).getUserId()));
    assertThat(actual.get(0).getGohan(), is(demoData.get(0).getGohan()));
    assertThat(actual.get(0).getOkazu(), is(demoData.get(0).getOkazu()));
    assertThat(actual.get(0).getMiso(), is(demoData.get(0).getMiso()));
    assertThat(actual.get(0).getPrice(), is(demoData.get(0).getPrice()));

    assertThat(actual.get(2).getDate(), is(demoData.get(1).getDate()));
    assertThat(actual.get(2).getUserId(), is(demoData.get(1).getUserId()));
    assertThat(actual.get(2).getGohan(), is(demoData.get(1).getGohan()));
    assertThat(actual.get(2).getOkazu(), is(demoData.get(1).getOkazu()));
    assertThat(actual.get(2).getMiso(), is(demoData.get(1).getMiso()));
    assertThat(actual.get(2).getPrice(), is(demoData.get(1).getPrice()));

    assertThat(actual.get(3).getDate(), is(demoData.get(2).getDate()));
    assertThat(actual.get(3).getUserId(), is(demoData.get(2).getUserId()));
    assertThat(actual.get(3).getGohan(), is(demoData.get(2).getGohan()));
    assertThat(actual.get(3).getOkazu(), is(demoData.get(2).getOkazu()));
    assertThat(actual.get(3).getMiso(), is(demoData.get(2).getMiso()));
    assertThat(actual.get(3).getPrice(), is(demoData.get(2).getPrice()));
  }

  /**
   * 指定の曜日のOrderだけ作成します。
   * @param dayofweek 曜日
   * @param userId ユーザID
   * @return 作成したOrderのリスト
   */
  private List<Order> createData(int[] dayofweek, String userId) {
    List<Order> results = new ArrayList<>();
    LocalDate week = LocalDate.of(2018, 3, 5);
    for (int i = 0; i < dayofweek.length; i++) {
      Order order = new Order();
      order.setDate(week.plusDays(dayofweek[i]-1));
      order.setGohan(String.format("gohan%d", i));
      order.setOkazu(String.format("okazu%d", i));
      order.setMiso(true);
      order.setPrice(new Long(400+i));
      order.setUserId(userId);
      results.add(order);
    }
    return results;
  }

}
