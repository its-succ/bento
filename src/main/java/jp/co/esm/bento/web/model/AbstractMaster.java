package jp.co.esm.bento.web.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Entity;

//import com.google.cloud.datastore.Entity;

import lombok.Data;

/**
 * マスタの基底クラス 
 */
@SuppressWarnings("serial")
@Data
public abstract class AbstractMaster implements Serializable {

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
    this.price = (Long)entity.getProperty(PRICE);
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
