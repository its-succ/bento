package jp.co.esm.bento.web.controller;

import jp.co.esm.bento.web.model.Gohan;
import jp.co.esm.bento.web.model.Okazu;
import jp.co.esm.bento.web.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jp.co.esm.bento.web.model.Master;
import jp.co.esm.bento.web.service.MasterService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

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
  @GetMapping(value = "/all")
  public Master getAll() {
    return masterService.getAllMaster();
  }

  /**
   * 指定の内容でおかずマスタを更新します。
   * @param Okazus おかずマスタ
   * @return 更新後のおかずマスタ
   */
  @PostMapping(value="/okazu/update")
  public List<Okazu> UpdateOkazu(@RequestBody List<Okazu> Okazus) {
    // TODO 実装すること
    return Collections.emptyList();
  }

  /**
   * 指定の内容でごはんマスタを更新します。
   * @param gohans ごはんマスタ
   * @return 更新後のごはんマスタ
   */
  @PostMapping(value="/gohan/update")
  public List<Gohan> UpdateGohan(@RequestBody List<Gohan> gohans) {
    // TODO 実装すること
    return Collections.emptyList();
  }

  @GetMapping(value = "/load")
  public String setData()
  {
    masterService.loadData();
    return "OK.";
  }
}
