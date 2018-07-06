package jp.co.esm.bento.web.model;

import com.google.appengine.api.datastore.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Okazuエンティティの内容を保有するモデルクラスです。
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
      Object week = entity.getProperty(DAYOFWEEK);
      if (Objects.nonNull(week)) {
        dayofweek = Long.valueOf(week.toString());
      }
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
}
