package jp.co.esm.bento.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.esm.bento.web.model.Master;
import jp.co.esm.bento.web.service.MasterService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/masters")
public class MasterController {

  // マスタサービス
  @Autowired
  private MasterService masterService;
  
  /**
   * すべてのマスタを取得します。
   * @return
   */
  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public Master getAll() {
    log.info("getMasterAll");
    return masterService.getAllMaster();
  }
 
  @GetMapping(value = "/load")
  public String setData()
  {
    log.info("setData");
    masterService.loadData();
    return "OK.";
  }
}
