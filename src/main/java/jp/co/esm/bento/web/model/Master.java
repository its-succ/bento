package jp.co.esm.bento.web.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * すべてのマスタを保有するクラスです。
 */
@Data
public class Master implements Serializable {

  // シリアルUID
  private static final long serialVersionUID = -7861999337865096546L;

  // おかずを格納したリスト
  private List<Okazu> okazu;
  
  // ごはんを格納したリスト
  private List<Gohan> gohan;
}
