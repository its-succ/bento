package jp.co.esm.bento.web.model;

import com.google.appengine.api.datastore.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Okazuエンティティの内容を保有するモデルクラスです。
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Okazu extends AbstractMaster {

  // シリアルUID
  private static final long serialVersionUID = -6263238718583758680L;

  // 曜日
  protected Long dayofweek;
  
  // プロパティ名：dayofweek
  public static final String DAYOFWEEK = "dayofweek";

  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  @Override
  public void setProperties(Entity entity) {
    super.setProperties(entity);
    if (entity.hasProperty(DAYOFWEEK))
    {
      this.dayofweek = (Long)entity.getProperty(DAYOFWEEK);
    }
  }
}
