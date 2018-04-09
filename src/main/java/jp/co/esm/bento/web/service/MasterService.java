package jp.co.esm.bento.web.service;

import java.time.DayOfWeek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.bento.web.model.Gohan;
import jp.co.esm.bento.web.model.Master;
import jp.co.esm.bento.web.model.Okazu;
import jp.co.esm.bento.web.repository.GohanRepository;
import jp.co.esm.bento.web.repository.OkazuRepository;
import lombok.extern.slf4j.Slf4j;

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

  /**
   * 全てのマスタの内容を取得します。
   * 
   * @return　取得したマスタの内容
   */
  public Master getAllMaster()
  {
    Master master = new Master();
    master.setOkazu(okazuRepository.list());
    master.setGohan(gohanRepository.list());
    return master;
  }
  
  /**
   * ローカルテスト用
   * マスタを登録します。
   */
  public void loadData()
  {
    // TODO トランザクション
    CreateOkazu();
    CreateGohan();
  }
  
  /**
   * ローカルデータストア投入用データ
   */
  private void CreateOkazu()
  {
    {
      Okazu model = new Okazu();
      model.setLabel("愛");
      model.setValue("ai");
      model.setPrice(new Long(360));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("ゆうき");
      model.setValue("yuuki");
      model.setPrice(new Long(411));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("げんき");
      model.setValue("genki");
      model.setPrice(new Long(515));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("応援");
      model.setValue("ouen");
      model.setPrice(new Long(360));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("豚生姜弁");
      model.setValue("higawari1");
      model.setPrice(new Long(400));
      model.setDayofweek(new Long(DayOfWeek.MONDAY.getValue()));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("麻婆丼");
      model.setValue("higawari2");
      model.setPrice(new Long(400));
      model.setDayofweek(new Long(DayOfWeek.TUESDAY.getValue()));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("牛カルビ弁");
      model.setValue("higawari3");
      model.setPrice(new Long(500));
      model.setDayofweek(new Long(DayOfWeek.WEDNESDAY.getValue()));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("オムライス");
      model.setValue("higawari4");
      model.setPrice(new Long(450));
      model.setDayofweek(new Long(DayOfWeek.THURSDAY.getValue()));
      okazuRepository.create(model);
    }
    {
      Okazu model = new Okazu();
      model.setLabel("豚バラ弁");
      model.setValue("higawari5");
      model.setPrice(new Long(450));
      model.setDayofweek(new Long(DayOfWeek.FRIDAY.getValue()));
      okazuRepository.create(model);
    }
  }
  
  private void CreateGohan()
  {
    {
      Gohan model = new Gohan();
      model.setLabel("白米");
      model.setValue("hakumai");
      model.setPrice(new Long(154));
      gohanRepository.create(model);
    }
    {
      Gohan model = new Gohan();
      model.setLabel("日替わり健康米");
      model.setValue("higawari");
      model.setPrice(new Long(185));
      gohanRepository.create(model);
    }
  }
 }
