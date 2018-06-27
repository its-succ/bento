package jp.co.esm.bento.web.service;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import jp.co.esm.bento.web.model.Gohan;
import jp.co.esm.bento.web.model.Master;
import jp.co.esm.bento.web.model.Okazu;
import jp.co.esm.bento.web.repository.GohanRepository;
import jp.co.esm.bento.web.repository.OkazuRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterServiceTest {

  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Autowired
  private MasterService service;

  @MockBean
  private OkazuRepository okazuRepository;

  @MockBean
  private GohanRepository gohanRepository;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void getAllMaster_データなし正常() {
    doReturn(Collections.emptyList())
      .when(okazuRepository).list();

    doReturn(Collections.emptyList())
      .when(gohanRepository).list();

    Master actual = service.getAllMaster();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.getGohan().isEmpty(), is(true));
    assertThat(actual.getOkazu().isEmpty(), is(true));
  }

  @Test
  public void getAllMaster_データあり正常() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    Master actual = service.getAllMaster();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.getGohan().size(), is(gohanList.size()));
    assertThat(actual.getOkazu().size(), is(okazuList.size()));
  }

  @Test
  public void getAllOkazus_データなし正常() {
    doReturn(Collections.emptyList())
      .when(okazuRepository).list();

    List<Okazu> actual = service.getAllOkazus();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.isEmpty(), is(true));
  }

  @Test
  public void getAllOkazus_データあり正常() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> actual = service.getAllOkazus();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(okazuList.size()));
    // ソートの確認
    assertThat(actual.get(0).getValue(), is("ai"));
    assertThat(actual.get(1).getValue(), is("yuuki"));
    assertThat(actual.get(2).getValue(), is("higawari4"));
  }


  @Test
  public void getAllGohans_データなし正常() {
    doReturn(Collections.emptyList())
      .when(gohanRepository).list();

    List<Gohan> actual = service.getAllGohans();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.isEmpty(), is(true));
  }

  @Test
  public void getAllGohans_データあり正常() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> actual = service.getAllGohans();

    assertThat(actual, is(notNullValue()));
    assertThat(actual.size(), is(gohanList.size()));
    // ソートの確認
    assertThat(actual.get(0).getValue(), is("hakumai"));
    assertThat(actual.get(1).getValue(), is("higawari"));
  }

  @Test
  public void createOrUpdateOkazu_登録更新削除なし() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    // 追加・変更・削除なし
    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, never()).create(anyObject());
    verify(okazuRepository, never()).update(anyObject());
    verify(okazuRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateOkazu_重複のため登録なし() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    Okazu okazu = new Okazu() {{
      label = "追加";
      value = "ai"; // 重複値
      price = 560L;
    }};
    targets.add(okazu);
    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, never()).create(okazu);
    verify(okazuRepository, never()).update(anyObject());
    verify(okazuRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateOkazu_登録のみあり() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    Okazu okazu = new Okazu() {{
      label = "追加";
      value = "tuika";
      price = 560L;
    }};
    targets.add(okazu);
    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, times(1)).create(okazu);
    verify(okazuRepository, never()).update(anyObject());
    verify(okazuRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateOkazu_更新のみあり() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    Okazu okazu = targets.get(0);
    okazu.setLabel("updated");
    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, never()).create(anyObject());
    verify(okazuRepository, times(1)).update(okazu);
    verify(okazuRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateOkazu_削除のみあり() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    // ID=2を削除
    targets.removeIf(okazu -> okazu.getId() == 2L);
    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, never()).create(anyObject());
    verify(okazuRepository, never()).update(anyObject());
    verify(okazuRepository, times(1)).delete(2L);
  }


  @Test
  public void createOrUpdateOkazu_登録更新削除あり() {
    List<Okazu> okazuList = demoOkazuData();
    doReturn(okazuList)
      .when(okazuRepository).list();

    List<Okazu> targets = demoOkazuData();
    // 1つめを更新
    Okazu updated = targets.get(0);
    updated.setLabel("updated");
    // ID=2を削除
    targets.removeIf(target -> target.getId().equals(2L));
    Okazu added = new Okazu() {{
      label = "追加";
      value = "tuika";
      price = 560L;
    }};
    targets.add(added);

    service.createOrUpdateOkazu(targets);

    verify(okazuRepository, times(1)).create(added);
    verify(okazuRepository, times(1)).update(updated);
    verify(okazuRepository, times(1)).delete(2L);
  }

  @Test
  public void createOrUpdateGohan_登録更新削除なし() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    // 追加・変更・削除なし

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, never()).create(anyObject());
    verify(gohanRepository, never()).update(anyObject());
    verify(gohanRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateGohan_重複のため登録なし() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    Gohan gohan = new Gohan() {{
      label = "追加";
      value = "higawari"; // 重複値
      price = 160L;
    }};
    targets.add(gohan);

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, never()).create(gohan);
    verify(gohanRepository, never()).update(anyObject());
    verify(gohanRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateGohan_登録のみあり() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    Gohan gohan = new Gohan() {{
      label = "追加";
      value = "tuika";
      price = 560L;
    }};
    targets.add(gohan);

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, times(1)).create(gohan);
    verify(gohanRepository, never()).update(anyObject());
    verify(gohanRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateGohan_更新のみあり() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    Gohan gohan = targets.get(0);
    gohan.setLabel("updated");

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, never()).create(anyObject());
    verify(gohanRepository, times(1)).update(gohan);
    verify(gohanRepository, never()).delete(anyLong());
  }

  @Test
  public void createOrUpdateGohan_削除のみあり() {
    List<Gohan> gohanList = demoGohanData();
    doReturn(gohanList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    // ID=2を削除
    targets.removeIf(gohan -> gohan.getId() == 2L);

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, never()).create(anyObject());
    verify(gohanRepository, never()).update(anyObject());
    verify(gohanRepository, times(1)).delete(2L);
  }


  @Test
  public void createOrUpdateGohan_登録更新削除あり() {
    List<Gohan> okazuList = demoGohanData();
    doReturn(okazuList)
      .when(gohanRepository).list();

    List<Gohan> targets = demoGohanData();
    // 1つめを更新
    Gohan updated = targets.get(0);
    updated.setLabel("updated");
    // ID=2を削除
    targets.removeIf(gohan -> gohan.getId() == 2L);
    Gohan added = new Gohan() {{
      label = "追加";
      value = "tuika";
      price = 160L;
    }};
    targets.add(added);

    service.createOrUpdateGohan(targets);

    verify(gohanRepository, times(1)).create(added);
    verify(gohanRepository, times(1)).update(updated);
    verify(gohanRepository, times(1)).delete(2L);
  }

  /**
   * テストデータ作成
   * @return
   */
  private List<Okazu> demoOkazuData()
  {
    List<Okazu> results = new ArrayList<>();
    // 1件目データ投入（曜日あり）
    Okazu okazu = new Okazu();
    okazu.setId(1L);
    okazu.setLabel("オムライス");
    okazu.setPrice(450L);
    okazu.setValue("higawari4");
    okazu.setDayofweek(4L);
    results.add(okazu);

    // 2件目データ投入
    okazu = new Okazu();
    okazu.setId(2L);
    okazu.setLabel("ゆうき");
    okazu.setPrice(411L);
    okazu.setValue("yuuki");
    results.add(okazu);

    // 3件目データ投入
    okazu = new Okazu();
    okazu.setId(3L);
    okazu.setLabel("愛");
    okazu.setPrice(360L);
    okazu.setValue("ai");
    results.add(okazu);

    return results;
  }

  private List<Gohan> demoGohanData() {
    List<Gohan> results = new ArrayList<>();

    // 1件目データ投入
    Gohan gohan = new Gohan();
    gohan.setId(1L);
    gohan.setLabel("日替わり健康米");
    gohan.setPrice(185L);
    gohan.setValue("higawari");
    results.add(gohan);

    // 2件目データ投入
    gohan = new Gohan();
    gohan.setId(2L);
    gohan.setLabel("白米");
    gohan.setPrice(154L);
    gohan.setValue("hakumai");
    results.add(gohan);

    return results;
  }
}
