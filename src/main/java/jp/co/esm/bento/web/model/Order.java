package jp.co.esm.bento.web.model;

import java.time.LocalDate;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;

import jp.co.esm.bento.web.util.DateUtil;
import lombok.Data;

/**
 * Orderエンティティの内容を保有するモデルクラスです。
 *
 */
@Data
public class Order {

  // ID
  private Long id;
  
  // 日付(yyyy-mm-dd)
  private LocalDate date;
  
  // ごはんマスタの値
  private String gohan;
  
  // 味噌汁有無
  private Boolean miso;
  
  // おかずマスタの値
  private String okazu;
  
  // ユーザID
  private String userId;
  
  // 値段
  private Long price;
  
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
  // プロパティ名：price
  public static final String PRICE = "price";
  
  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  public void setProperties(Entity entity)
  {
    this.id = entity.getKey().getId();
    this.date = DateUtil.dateToLocalDate((Date)entity.getProperty(DATE));
    this.gohan = (String)entity.getProperty(GOHAN);
    this.miso = (Boolean)entity.getProperty(MISO);
    this.okazu = (String)entity.getProperty(OKAZU);
    this.userId = (String)entity.getProperty(USER_ID);
    this.price = (Long)entity.getProperty(PRICE);
  }
  
  /**
   * モデルの内容を指定のエンティティに設定します。
   * @param entity エンティティ
   */
  public void convert(Entity entity)
  {
    entity.setProperty(ID, id);
    entity.setProperty(DATE, DateUtil.localDateToDate(date));
    entity.setProperty(GOHAN, gohan);
    entity.setProperty(MISO, miso);
    entity.setProperty(OKAZU, okazu);
    entity.setProperty(USER_ID, userId);
    entity.setProperty(PRICE, price);
  }
}
