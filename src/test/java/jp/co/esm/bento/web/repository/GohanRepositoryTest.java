package jp.co.esm.bento.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import jp.co.esm.bento.web.BentoWebApplication;
import jp.co.esm.bento.web.model.Gohan;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= BentoWebApplication.class)
@SpringBootTest
public class GohanRepositoryTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Autowired
  private GohanRepository repository;
  
  // テストデータ
  private List<Entity> testDatas;
  
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    testDatas = new ArrayList<>();
    createData();
  }
  
  /**
   * テストデータ投入
   */
  private void createData()
  {
    // 1件目データ投入
    Gohan gohan = new Gohan();
    gohan.setLabel("白米");
    gohan.setPrice(new Long(154));
    gohan.setValue("hakumai");
    testDatas.add(repository.create(gohan));

    // 2件目データ投入
    gohan = new Gohan();
    gohan.setLabel("日替わり健康米");
    gohan.setPrice(new Long(185));
    gohan.setValue("higawari");
    testDatas.add(repository.create(gohan));
  }

  @After
  public void tearDown() throws Exception {
    testDatas.clear();
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    Gohan gohan = new Gohan();
    gohan.setLabel("たきこみごはん");
    gohan.setPrice(new Long(108));
    gohan.setValue("takikomi");
    Entity actual = repository.create(gohan);
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getProperty(Gohan.LABEL), is(gohan.getLabel()));
    assertThat(actual.getProperty(Gohan.PRICE), is(gohan.getPrice()));
    assertThat(actual.getProperty(Gohan.VALUE), is(gohan.getValue()));
  }

  @Test
  public void testRead() {
    Entity entity = testDatas.get(0);
    Gohan actual = repository.read(entity.getKey().getId());
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getLabel(), is(entity.getProperty(Gohan.LABEL)));
    assertThat(actual.getPrice(), is(entity.getProperty(Gohan.PRICE)));
    assertThat(actual.getValue(), is(entity.getProperty(Gohan.VALUE)));
  }

  @Test
  public void testUpdate() {
    Entity entity = testDatas.get(0);
    Gohan gohan = repository.read(entity.getKey().getId());
    
    gohan.setLabel("違うラベル名");
    gohan.setPrice(new Long(433));
    repository.update(gohan);

    // 更新後のエンティティを取得して検証
    Gohan actual = repository.read(entity.getKey().getId());
    assertThat(actual.getLabel(), is(gohan.getLabel()));
    assertThat(actual.getPrice(), is(gohan.getPrice()));
    assertThat(actual.getValue(), is(gohan.getValue()));
  }

  @Test
  public void testDelete() {
    Entity entity = testDatas.get(0);
    repository.delete(entity.getKey().getId());
    
    // 同じキーで取得できなければOK
    Gohan actual = repository.read(entity.getKey().getId());
    assertNull(actual);
  }

  @Test
  public void testList() {
    List<Gohan> actual = repository.list();
    
    assertNotNull("エンティティが１つも取得できませんでした。", actual);
    assertEquals("取得した件数を一致しません。", 2, actual.size());
    
    for (int i = 0; i < testDatas.size(); i++) {
      assertThat(actual.get(i).getLabel(), is(testDatas.get(i).getProperty(Gohan.LABEL)));
      assertThat(actual.get(i).getPrice(), is(testDatas.get(i).getProperty(Gohan.PRICE)));
      assertThat(actual.get(i).getValue(), is(testDatas.get(i).getProperty(Gohan.VALUE)));
    }
  }

}
