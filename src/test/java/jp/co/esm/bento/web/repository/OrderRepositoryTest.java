package jp.co.esm.bento.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import jp.co.esm.bento.web.BentoWebApplication;
import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= BentoWebApplication.class)
@SpringBootTest
public class OrderRepositoryTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Autowired
  private OrderRepository repository;
  
  // テストデータ
  private List<Entity> testDatas;
  
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    testDatas = new ArrayList<>();
    createData();
  }
  
  @After
  public void tearDown() throws Exception {
    testDatas.clear();
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    Order order = new Order();
    order.setDate(LocalDate.of(2018, 2, 26));
    order.setOkazu("higawari1");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    Entity actual = repository.create(order);
    
    assertThat(actual, is(notNullValue()));
    Date date = DateUtil.localDateToDate(order.getDate());
    assertThat(actual.getProperty(Order.DATE), is(date));
    assertThat(actual.getProperty(Order.OKAZU), is(order.getOkazu()));
    assertThat(actual.getProperty(Order.GOHAN), is(order.getGohan()));
    assertThat(actual.getProperty(Order.MISO), is(order.getMiso()));
    assertThat(actual.getProperty(Order.USER_ID), is(order.getUserId()));
  }

  @Test
  public void testRead() {
    Entity entity = testDatas.get(0);
    Order actual = repository.read(entity.getKey().getId());
    
    assertThat(actual, is(notNullValue()));
    LocalDate date = DateUtil.dateToLocalDate((Date)entity.getProperty(Order.DATE));
    assertThat(actual.getDate(), is(date));
    assertThat(actual.getOkazu(), is(entity.getProperty(Order.OKAZU)));
    assertThat(actual.getGohan(), is(entity.getProperty(Order.GOHAN)));
    assertThat(actual.getMiso(), is(entity.getProperty(Order.MISO)));
    assertThat(actual.getUserId(), is(entity.getProperty(Order.USER_ID)));
  }

  @Test
  public void testUpdate() {
    Entity entity = testDatas.get(0);
    Order order = repository.read(entity.getKey().getId());
    
    order.setOkazu("ai");
    order.setGohan("hakumai");
    repository.update(order);

    // 更新後のエンティティを取得して検証
    Order actual = repository.read(entity.getKey().getId());
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getDate(), is(order.getDate()));
    assertThat(actual.getOkazu(), is(order.getOkazu()));
    assertThat(actual.getGohan(), is(order.getGohan()));
    assertThat(actual.getMiso(), is(order.getMiso()));
    assertThat(actual.getUserId(), is(order.getUserId()));
  }

  @Test
  public void testDelete() {
    Entity entity = testDatas.get(0);
    repository.delete(entity.getKey().getId());
    
    // 同じキーで取得できなければOK
    Order actual = repository.read(entity.getKey().getId());
    assertThat(actual, is(nullValue()));
  }

  @Test
  public void testList() {
    List<Order> actual = repository.list();
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(testDatas.size()));
    
    for (int i = 0; i < testDatas.size(); i++) {
      LocalDate date = DateUtil.dateToLocalDate((Date)testDatas.get(i).getProperty(Order.DATE));
      assertThat(actual.get(i).getDate(), is(date));
      assertThat(actual.get(i).getOkazu(), is(testDatas.get(i).getProperty(Order.OKAZU)));
      assertThat(actual.get(i).getGohan(), is(testDatas.get(i).getProperty(Order.GOHAN)));
      assertThat(actual.get(i).getMiso(), is(testDatas.get(i).getProperty(Order.MISO)));
      assertThat(actual.get(i).getUserId(), is(testDatas.get(i).getProperty(Order.USER_ID)));
    }
  }

  @Test
  public void testListByUserAndWeek_該当あり() {
    LocalDate week = LocalDate.of(2018, 3, 5);
    List<Order> actual = repository.listByUserIdAndWeek("hoge@esm.co.jp", week);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(5));
    
    for (int i = 0; i < 5; i++) {
      assertThat(actual.get(i).getDate(), is(week.plusDays(i)));
      assertThat(actual.get(i).getUserId(), is("hoge@esm.co.jp"));
    }
  }
  
  @Test
  public void testListByUserAndWeek_該当なし_ユーザID() {
    LocalDate week = LocalDate.of(2018, 3, 5);
    List<Order> actual = repository.listByUserIdAndWeek("hoge2@esm.co.jp", week);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }
  
  @Test
  public void testListByUserAndWeek_該当なし_日付() {
    LocalDate week = LocalDate.of(2018, 3, 26);
    List<Order> actual = repository.listByUserIdAndWeek("hoge@esm.co.jp", week);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(0));
  }

  /**
   * テストデータ投入
   * @throws ParseException 
   */
  private void createData() throws ParseException{
    
    // 1件目
    Order order = new Order();
    order.setDate(LocalDate.of(2018, 3, 2));
    order.setOkazu("higawari1");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 2件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 5));
    order.setOkazu("higawari1");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 3件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 6));
    order.setOkazu("higawari2");
    order.setGohan("higawari");
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 4件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 7));
    order.setOkazu("ai");
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 5件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 8));
    order.setOkazu("yuuki");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 6件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 9));
    order.setOkazu(null);
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 7件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 9));
    order.setOkazu(null);
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("fuga@esm.co.jp");
    testDatas.add(repository.create(order));

    // 8件目
    order = new Order();
    order.setDate(LocalDate.of(2018, 3, 12));
    order.setOkazu(null);
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));
  }
}
