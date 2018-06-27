package jp.co.esm.bento.web.model;

import com.google.appengine.api.datastore.Entity;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * Okazuエンティティの内容を保有するモデルクラスです。
 */
@Data
@ToString(callSuper=true)
public class Okazu extends AbstractMaster {

  // 曜日
  private Long dayofweek;

  // プロパティ名：dayofweek
  public static final String DAYOFWEEK = "dayofweek";

  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  @Override
  public void setProperties(Entity entity) {
    super.setProperties(entity);
    dayofweek = null;
    if (entity.hasProperty(DAYOFWEEK)) {
      dayofweek = (Long) entity.getProperty(DAYOFWEEK);
    }
  }

  /**
   * モデルの内容を指定のエンティティに設定します。
   * @param entity エンティティ
   */
  @Override
  public void convert(Entity entity) {
    super.convert(entity);
    if (dayofweek != null) {
      entity.setProperty(DAYOFWEEK, dayofweek);
    }
  }

  /**
   * 指定のマスタの内容と差分があるかどうか返します。
   *
   * @param target 対象マスタ
   * @return true - 差分あり, false - 差分なし
   */
  public boolean isModified(Okazu target) {
    return super.isModified(target) ||
           !Objects.equals(dayofweek, target.getDayofweek());
  }
}
