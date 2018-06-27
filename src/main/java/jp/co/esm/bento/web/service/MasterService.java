package jp.co.esm.bento.web.service;

import java.io.File;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jp.co.esm.bento.web.model.Gohan;
import jp.co.esm.bento.web.model.Master;
import jp.co.esm.bento.web.model.Okazu;
import jp.co.esm.bento.web.repository.GohanRepository;
import jp.co.esm.bento.web.repository.OkazuRepository;
import lombok.extern.slf4j.Slf4j;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * マスタ全般のサービスクラスです。
 */
@Slf4j
@Service
public class MasterService {

  @Autowired
  GohanRepository gohanRepository;

  @Autowired
  OkazuRepository okazuRepository;

  @Autowired
  private DatastoreService datastore;

  @Autowired
  ResourceLoader resourceLoader;

  /**
   * 全てのマスタの内容を取得します。
   *
   * @return 取得したマスタの内容
   */
  public Master getAllMaster()
  {
    Master master = new Master();
    master.setOkazu(getAllOkazus());
    master.setGohan(getAllGohans());
    return master;
  }

  /**
   * すべてのおかずマスタを取得します。
   * @return 取得したおかずマスタの内容
   */
  public List<Okazu> getAllOkazus() {
    List<Okazu> results = okazuRepository.list();
    // おかずは曜日と値段でソート
    // 曜日はnullがあるためクエリーでソートできない
    results.sort(comparing(Okazu::getDayofweek, nullsFirst(naturalOrder())).thenComparing(Okazu::getPrice));
    return results;
  }

  /**
   * 指定の内容でおかずマスタを登録・更新・削除します。
   * @param okazus
   */
  public void createOrUpdateOkazu(List<Okazu> okazus) {
    TransactionOptions options = TransactionOptions.Builder.withXG(true);
    Transaction transaction = datastore.beginTransaction(options);
    try {
      // 現在のマスタを取得
      List<Okazu> current = getAllOkazus();

      // 追加されたマスタを登録
      okazus.stream().filter(o -> o.getId() == null).forEach(target -> {
        // 値が重複していなければ登録
        Optional<Okazu> okazu = current.stream().filter(m -> Objects.equals(m.getValue(), target.getValue())).findAny();
        if (!okazu.isPresent()) {
          okazuRepository.create(target);
        }
      });

      // 変更されたマスタを更新
      okazus.stream().filter(o -> o.getId() != null).forEach(target -> {
        // 現在のマスタと差分があれば更新
        current.stream().filter(m -> Objects.equals(m.getValue(), target.getValue())).forEach(c -> {
          if (c.isModified(target)) {
            okazuRepository.update(target);
          }
        });
      });

      // 消されたマスタを削除
      current.stream().filter(c -> okazus.stream().noneMatch(o -> Objects.equals(o.getId(), c.getId()))).forEach((target -> {
        okazuRepository.delete(target.getId());
      }));

      transaction.commit();

    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }

  /**
   * すべてのごはんマスタを取得します。
   * @return 取得したごはんマスタの内容
   */
  public List<Gohan> getAllGohans() {
    List<Gohan> results = gohanRepository.list();
    // ごはんは値段でソート
    results.sort(comparing(Gohan::getPrice));
    return results;
  }

  /**
   * 指定の内容でごはんマスタを登録・更新・削除します。
   * @param gohans
   */
  public void createOrUpdateGohan(List<Gohan> gohans) {
    TransactionOptions options = TransactionOptions.Builder.withXG(true);
    Transaction transaction = datastore.beginTransaction(options);
    try {
      // 現在のマスタを取得
      List<Gohan> current = getAllGohans();

      // 追加されたマスタを登録
      gohans.stream().filter(o -> o.getId() == null).forEach(target -> {
        // 値が重複していなければ登録
        Optional<Gohan> gohan = current.stream().filter(m -> Objects.equals(m.getValue(), target.getValue())).findAny();
        if (!gohan.isPresent()) {
          gohanRepository.create(target);
        }
      });

      // 変更されたマスタを更新
      gohans.stream().filter(o -> o.getId() != null).forEach(target -> {
        // 現在のマスタと差分があれば更新
        current.stream().filter(m -> Objects.equals(m.getValue(), target.getValue())).forEach(c -> {
          if (c.isModified(target)) {
            gohanRepository.update(target);
          }
        });
      });

      // 消されたマスタを削除
      current.stream().filter(c -> gohans.stream().noneMatch(o -> Objects.equals(o.getId(), c.getId()))).forEach((target -> {
        gohanRepository.delete(target.getId());
      }));

      transaction.commit();

    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }

  /**
   * ローカルテスト用
   * Jsonファイルよりマスタを登録します。
   */
  public void loadData()
  {
    // リソースファイルを取得
    String filepath = "localdb/master.json";
    Resource resource = resourceLoader.getResource("classpath:" + filepath);
    try {
      File file = resource.getFile();
      if (!file.exists()) {
        log.warn("master.json not found.");
        return;
      }
      ObjectMapper mapper = new ObjectMapper();
      Master master = mapper.readValue(file, Master.class);
      for(Okazu okazu: master.getOkazu()){
        log.info("okazu: {}", okazu);
        okazuRepository.create(okazu);
      }
      for(Gohan gohan: master.getGohan()){
        log.info("gohan: {}", gohan);
        gohanRepository.create(gohan);
      }

    } catch (Exception e){

    }
  }
 }
