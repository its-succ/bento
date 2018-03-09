package jp.co.esm.bento.web.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;

import lombok.Data;

/**
 * Orderエンティティの内容を保有するモデルクラスです。
 *
 */
@Data
public class Order implements Serializable {

  /**
   * シリアルUID
   */
  private static final long serialVersionUID = -7771442788859375301L;

  // ID
  protected Long id;
  
  // 名称
  protected Date date;
  
  // ごはんマスタの値
  protected String gohan;
  
  // 味噌汁有無
  protected Boolean miso;
  
  // おかずマスタの値
  protected String okazu;
  
  // ユーザID
  protected String userId;
  
  // プロパティ名：ID
  public static final String ID = "id";
  // プロパティ名：date
  public static final String DATE = "date";
  // プロパティ名：gohan
  public static final String GOHAN = "gohan";
  // プロパティ名：miso
  public static final String MISO = "miso";
  // プロパティ名：okazu
  public static final String OKAZU = "okazu";
  // プロパティ名：userId
  public static final String USER_ID = "userId";
  
  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  public void setProperties(Entity entity)
  {
    this.id = entity.getKey().getId();
    this.date = (Date)entity.getProperty(DATE);
    this.gohan = (String)entity.getProperty(GOHAN);
    this.miso = (Boolean)entity.getProperty(MISO);
    this.okazu = (String)entity.getProperty(OKAZU);
    this.userId = (String)entity.getProperty(USER_ID);
  }
  
  /**
   * モデルの内容を指定のエンティティに設定します。
   * @param entity エンティティ
   */
  public void convert(Entity entity)
  {
    entity.setProperty(ID, id);
    entity.setProperty(DATE, date);
    entity.setProperty(GOHAN, gohan);
    entity.setProperty(MISO, miso);
    entity.setProperty(OKAZU, okazu);
    entity.setProperty(USER_ID, userId);
  }
}
