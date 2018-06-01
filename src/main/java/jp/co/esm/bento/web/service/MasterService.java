package jp.co.esm.bento.web.service;

import java.io.File;
import java.time.DayOfWeek;

import com.fasterxml.jackson.databind.ObjectMapper;
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
  ResourceLoader resourceLoader;

  /**
   * 全てのマスタの内容を取得します。
   *
   * @return 取得したマスタの内容
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
