package jp.co.esm.bento.web.model;

import java.util.List;

import lombok.Data;

/**
 * すべてのマスタを保有するクラスです。
 */
@Data
public class Master {

  // おかずを格納したリスト
  private List<Okazu> okazu;
  
  // ごはんを格納したリスト
  private List<Gohan> gohan;
}
