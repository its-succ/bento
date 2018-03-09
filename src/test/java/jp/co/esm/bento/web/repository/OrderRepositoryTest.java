package jp.co.esm.bento.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
  
  // 日付変換
  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
  
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    testDatas = new ArrayList<>();
    createData();
  }
  
  /**
   * テストデータ投入
   * @throws ParseException 
   */
  private void createData() throws ParseException{
    
    // 1件目
    Order order = new Order();
    order.setDate(sdf.parse("20180305"));
    order.setOkazu("higawari1");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 2件目
    order = new Order();
    order.setDate(sdf.parse("20180306"));
    order.setOkazu("higawari2");
    order.setGohan("higawari");
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 3件目
    order = new Order();
    order.setDate(sdf.parse("20180307"));
    order.setOkazu("ai");
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 4件目
    order = new Order();
    order.setDate(sdf.parse("20180308"));
    order.setOkazu("yuuki");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));

    // 4件目
    order = new Order();
    order.setDate(sdf.parse("20180309"));
    order.setOkazu(null);
    order.setGohan(null);
    order.setMiso(false);
    order.setUserId("hoge@esm.co.jp");
    testDatas.add(repository.create(order));
}
  
  @After
  public void tearDown() throws Exception {
    testDatas.clear();
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    Order order = new Order();
    try {
      order.setDate(sdf.parse("20180226"));
    } catch (ParseException e) {
      fail();
    }
    order.setOkazu("higawari1");
    order.setGohan(null);
    order.setMiso(true);
    order.setUserId("hoge@esm.co.jp");
    Entity actual = repository.create(order);
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getProperty(Order.DATE), is(order.getDate()));
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
    assertThat(actual.getDate(), is(entity.getProperty(Order.DATE)));
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
    assertThat(actual.size(), is(5));
    
    for (int i = 0; i < testDatas.size(); i++) {
      assertThat(actual.get(i).getDate(), is(testDatas.get(i).getProperty(Order.DATE)));
      assertThat(actual.get(i).getOkazu(), is(testDatas.get(i).getProperty(Order.OKAZU)));
      assertThat(actual.get(i).getGohan(), is(testDatas.get(i).getProperty(Order.GOHAN)));
      assertThat(actual.get(i).getMiso(), is(testDatas.get(i).getProperty(Order.MISO)));
      assertThat(actual.get(i).getUserId(), is(testDatas.get(i).getProperty(Order.USER_ID)));
    }
  }
}
