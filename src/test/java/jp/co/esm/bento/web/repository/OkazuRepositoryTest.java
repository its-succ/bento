package jp.co.esm.bento.web.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import jp.co.esm.bento.web.model.Okazu;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= BentoWebApplication.class)
@SpringBootTest
public class OkazuRepositoryTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Autowired
  private OkazuRepository repository;
  
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
    Okazu okazu = new Okazu();
    okazu.setLabel("愛");
    okazu.setPrice(new Long(360));
    okazu.setValue("ai");
    testDatas.add(repository.create(okazu));

    // 2件目データ投入
    okazu = new Okazu();
    okazu.setLabel("ゆうき");
    okazu.setPrice(new Long(411));
    okazu.setValue("yuuki");
    testDatas.add(repository.create(okazu));

    // 3件目データ投入（曜日あり）
    okazu = new Okazu();
    okazu.setLabel("オムライス");
    okazu.setPrice(new Long(450));
    okazu.setValue("higawari4");
    okazu.setDayofweek(new Long(4));
    testDatas.add(repository.create(okazu));
  }

  @After
  public void tearDown() throws Exception {
    testDatas.clear();
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    Okazu okazu = new Okazu();
    okazu.setLabel("日替わり弁当");
    okazu.setPrice(new Long(380));
    okazu.setValue("higawari0");
    Entity actual = repository.create(okazu);
    
    assertNotNull("エンティティが作成されませんでした。", actual);
    assertEquals("エンティティのLabelの値が一致しません。", okazu.getLabel(), actual.getProperty(Okazu.LABEL));
    assertEquals("エンティティのPriceの値が一致しません。", okazu.getPrice(), actual.getProperty(Okazu.PRICE));
    assertEquals("エンティティのValueの値が一致しません。", okazu.getValue(), actual.getProperty(Okazu.VALUE));
    assertEquals("エンティティのDayofweekの値が一致しません。", okazu.getDayofweek(), actual.getProperty(Okazu.DAYOFWEEK));
  }

  @Test
  public void testRead() {
    Entity entity = testDatas.get(0);
    Okazu actual = repository.read(entity.getKey().getId());
    
    assertNotNull("指定のIDで取得できませんでした。", actual);
    assertEquals("モデルのLabelの値が一致しません。", entity.getProperty(Okazu.LABEL), actual.getLabel());
    assertEquals("モデルのPriceの値が一致しません。", entity.getProperty(Okazu.PRICE), actual.getPrice());
    assertEquals("モデルのValueの値が一致しません。", entity.getProperty(Okazu.VALUE), actual.getValue());
    assertEquals("モデルのDayofweekの値が一致しません。", entity.getProperty(Okazu.DAYOFWEEK), actual.getDayofweek());
  }

  @Test
  public void testUpdate() {
    Entity entity = testDatas.get(0);
    Okazu okazu = repository.read(entity.getKey().getId());
    
    okazu.setLabel("違うラベル名");
    okazu.setPrice(new Long(433));
    repository.update(okazu);

    // 更新後のエンティティを取得して検証
    Okazu actual = repository.read(entity.getKey().getId());
    assertEquals("更新後のLabelの値が一致しません。", okazu.getLabel(), actual.getLabel());
    assertEquals("更新後のPriceの値が一致しません。", okazu.getPrice(), actual.getPrice());
    assertEquals("更新後のValueの値が一致しません。", okazu.getValue(), actual.getValue());
    assertEquals("モデルのDayofweekの値が一致しません。", okazu.getDayofweek(), actual.getDayofweek());
  }

  @Test
  public void testDelete() {
    Entity entity = testDatas.get(0);
    repository.delete(entity.getKey().getId());
    
    // 同じキーで取得できなければOK
    Okazu actual = repository.read(entity.getKey().getId());
    assertNull(actual);
  }

  @Test
  public void testList() {
    List<Okazu> actual = repository.list();
    
    assertNotNull("エンティティが１つも取得できませんでした。", actual);
    assertEquals("取得した件数を一致しません。", 3, actual.size());
    
    for (int i = 0; i < testDatas.size(); i++) {
      assertEquals("モデルのLabelの値が一致しません。", testDatas.get(i).getProperty(Okazu.LABEL), actual.get(i).getLabel());
      assertEquals("モデルのPriceの値が一致しません。", testDatas.get(i).getProperty(Okazu.PRICE), actual.get(i).getPrice());
      assertEquals("モデルのValueの値が一致しません。", testDatas.get(i).getProperty(Okazu.VALUE), actual.get(i).getValue());
      assertEquals("モデルのDayofweekの値が一致しません。", testDatas.get(i).getProperty(Okazu.DAYOFWEEK), actual.get(i).getDayofweek());
    }
  }
}
