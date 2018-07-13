package jp.co.esm.bento.web.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.appengine.api.datastore.Entity;

import jp.co.esm.bento.web.util.DateUtil;
import lombok.Data;

/**
 * Orderエンティティの内容を保有するモデルクラスです。
 */
@Data
public class Order {

  // ID
  private Long id;

  // 日付(yyyy-mm-dd)
  private LocalDate date;

  // ごはんマスタの値
  private String gohan;

  // ごはんマスタの名前
  private String gohanLabel;

  // ごはんマスタの値段
  private Long gohanPrice;

  // 味噌汁有無
  private Boolean miso;

  // おかずマスタの値
  private String okazu;

  // おかずマスタの名前
  private String okazuLabel;

  // おかずマスタの値段
  private Long okazuPrice;

  // ユーザID
  private String userId;

  // 値段
  private Long price;

  // 祝日かどうか
  private Boolean holiday;

  // プロパティ名：ID
  public static final String ID = "id";
  // プロパティ名：date
  public static final String DATE = "date";
  // プロパティ名：gohan
  public static final String GOHAN = "gohan";
  // プロパティ名：gohanLabel
  public static final String GOHAN_LABEL = "gohanLabel";
  // プロパティ名：gohanPrice
  public static final String GOHAN_PRICE = "gohanPrice";
  // プロパティ名：miso
  public static final String MISO = "miso";
  // プロパティ名：okazu
  public static final String OKAZU = "okazu";
  // プロパティ名：okazuLabel
  public static final String OKAZU_LABEL = "okazuLabel";
  // プロパティ名：okazuPrice
  public static final String OKAZU_PRICE = "okazuPrice";
  // プロパティ名：userId
  public static final String USER_ID = "userId";
  // プロパティ名：price
  public static final String PRICE = "price";
  // プロパティ名：holiday
  public static final String HOLIDAY = "holiday";

  /**
   * 指定のエンティティの内容をモデルに設定します。
   * @param entity エンティティ
   */
  public void setProperties(Entity entity)
  {
    this.id = entity.getKey().getId();
    this.date = DateUtil.dateToLocalDate((Date)entity.getProperty(DATE));
    this.gohan = (String)entity.getProperty(GOHAN);
    this.gohanLabel = (String)entity.getProperty(GOHAN_LABEL);
    this.gohanPrice = (Long) entity.getProperty(GOHAN_PRICE);
    this.miso = (Boolean)entity.getProperty(MISO);
    this.okazu = (String)entity.getProperty(OKAZU);
    this.okazuLabel = (String)entity.getProperty(OKAZU_LABEL);
    this.okazuPrice = (Long)entity.getProperty(OKAZU_PRICE);
    this.userId = (String)entity.getProperty(USER_ID);
    Object p = entity.getProperty(PRICE);
    if (Objects.nonNull(p)) {
      this.price = Long.valueOf(p.toString());
    }
    this.holiday = Boolean.FALSE;
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
    entity.setProperty(GOHAN_LABEL, gohanLabel);
    entity.setProperty(GOHAN_PRICE, gohanPrice);
    entity.setProperty(MISO, miso);
    entity.setProperty(OKAZU, okazu);
    entity.setProperty(OKAZU_LABEL, okazuLabel);
    entity.setProperty(OKAZU_PRICE, okazuPrice);
    entity.setProperty(USER_ID, userId);
    entity.setProperty(PRICE, price);
  }

  /**
   * 注文が空かどうかチェックします。
   * @return 注文なしの場合はtrue、ありの場合はfalse
   */
  public boolean noOrder() {
    // おかずチェック
    if (okazu != null &&
      !okazu.isEmpty()) {
      return false;
    }
    // ごはんチェック
    if (gohan != null &&
      !gohan.isEmpty()) {
      return false;
    }
    // おかず・ごはんなしで味噌汁ありはないのでチェックしない
    return true;
  }}
