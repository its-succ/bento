package jp.co.esm.bento.web.repository;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import jp.co.esm.bento.web.BentoWebApplication;
import jp.co.esm.bento.web.model.Gohan;

@RunWith(SpringJUnit4ClassRunner.class)
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
    
    assertNotNull("エンティティが作成されませんでした。", actual);
    assertEquals("エンティティのLabelの値が一致しません。", gohan.getLabel(), actual.getProperty(Gohan.LABEL));
    assertEquals("エンティティのPriceの値が一致しません。", gohan.getPrice(), actual.getProperty(Gohan.PRICE));
    assertEquals("エンティティのValueの値が一致しません。", gohan.getValue(), actual.getProperty(Gohan.VALUE));
  }

  @Test
  public void testRead() {
    Entity entity = testDatas.get(0);
    Gohan actual = repository.read(entity.getKey().getId());
    
    assertNotNull("指定のIDで取得できませんでした。", actual);
    assertEquals("モデルのLabelの値が一致しません。", entity.getProperty(Gohan.LABEL), actual.getLabel());
    assertEquals("モデルのPriceの値が一致しません。", entity.getProperty(Gohan.PRICE), actual.getPrice());
    assertEquals("モデルのValueの値が一致しません。", entity.getProperty(Gohan.VALUE), actual.getValue());
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
    assertEquals("更新後のLabelの値が一致しません。", gohan.getLabel(), actual.getLabel());
    assertEquals("更新後のPriceの値が一致しません。", gohan.getPrice(), actual.getPrice());
    assertEquals("更新後のValueの値が一致しません。", gohan.getValue(), actual.getValue());
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
      assertEquals("モデルのLabelの値が一致しません。", testDatas.get(i).getProperty(Gohan.LABEL), actual.get(i).getLabel());
      assertEquals("モデルのPriceの値が一致しません。", testDatas.get(i).getProperty(Gohan.PRICE), actual.get(i).getPrice());
      assertEquals("モデルのValueの値が一致しません。", testDatas.get(i).getProperty(Gohan.VALUE), actual.get(i).getValue());
    }
  }

}
