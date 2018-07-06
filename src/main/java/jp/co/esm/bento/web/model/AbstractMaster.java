package jp.co.esm.bento.web.model;

import com.google.appengine.api.datastore.Entity;

import lombok.Data;

import java.util.Objects;

/**
 * マスタの基底クラス
 */
@Data
public abstract class AbstractMaster {

  // ID
  protected Long id;

  // 名称
  protected String label;

  // 値
  protected String value;

  // 値段
  protected Long price;

  // プロパティ名：label
  public static final String ID = "id";
  // プロパティ名：label
  public static final String LABEL = "label";
  // プロパティ名：value
  public static final String VALUE = "value";
  // プロパティ名：price
  public static final String PRICE = "price";

  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  public void setProperties(Entity entity)
  {
    this.id = entity.getKey().getId();
    this.label = (String)entity.getProperty(LABEL);
    this.value = (String)entity.getProperty(VALUE);
    Object price = entity.getProperty(PRICE);
    if (Objects.nonNull(price)) {
      this.price = Long.valueOf(price.toString());
    }
  }

  /**
   * モデルの内容を指定のエンティティに設定します。
   * @param entity エンティティ
   */
  public void convert(Entity entity)
  {
    entity.setProperty(ID, id);
    entity.setProperty(VALUE, value);
    entity.setProperty(LABEL, label);
    entity.setProperty(PRICE, price);
  }
}
