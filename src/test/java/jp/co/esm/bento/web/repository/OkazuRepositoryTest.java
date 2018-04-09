package jp.co.esm.bento.web.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

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
import jp.co.esm.bento.web.model.Okazu;

@RunWith(SpringRunner.class)
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
    okazu.setPrice(360L);
    okazu.setValue("ai");
    testDatas.add(repository.create(okazu));

    // 2件目データ投入
    okazu = new Okazu();
    okazu.setLabel("ゆうき");
    okazu.setPrice(411L);
    okazu.setValue("yuuki");
    testDatas.add(repository.create(okazu));

    // 3件目データ投入（曜日あり）
    okazu = new Okazu();
    okazu.setLabel("オムライス");
    okazu.setPrice(450L);
    okazu.setValue("higawari4");
    okazu.setDayofweek(4L);
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
    okazu.setPrice(380L);
    okazu.setValue("higawari0");
    Entity actual = repository.create(okazu);
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getProperty(Okazu.LABEL), is(okazu.getLabel()));
    assertThat(actual.getProperty(Okazu.PRICE), is(okazu.getPrice()));
    assertThat(actual.getProperty(Okazu.VALUE), is(okazu.getValue()));
    assertThat(actual.getProperty(Okazu.DAYOFWEEK), is(okazu.getDayofweek()));
  }

  @Test
  public void testRead() {
    Entity entity = testDatas.get(0);
    Okazu actual = repository.read(entity.getKey().getId());
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getLabel(), is(entity.getProperty(Okazu.LABEL)));
    assertThat(actual.getPrice(), is(entity.getProperty(Okazu.PRICE)));
    assertThat(actual.getValue(), is(entity.getProperty(Okazu.VALUE)));
    assertThat(actual.getDayofweek(), is(entity.getProperty(Okazu.DAYOFWEEK)));
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
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getLabel(), is(okazu.getLabel()));
    assertThat(actual.getPrice(), is(okazu.getPrice()));
    assertThat(actual.getValue(), is(okazu.getValue()));
    assertThat(actual.getDayofweek(), is(okazu.getDayofweek()));
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
    
    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(3));
    
    for (int i = 0; i < testDatas.size(); i++) {
      assertThat(actual.get(i).getLabel(), is(testDatas.get(i).getProperty(Okazu.LABEL)));
      assertThat(actual.get(i).getPrice(), is(testDatas.get(i).getProperty(Okazu.PRICE)));
      assertThat(actual.get(i).getValue(), is(testDatas.get(i).getProperty(Okazu.VALUE)));
      assertThat(actual.get(i).getDayofweek(), is(testDatas.get(i).getProperty(Okazu.DAYOFWEEK)));
    }
  }
}
