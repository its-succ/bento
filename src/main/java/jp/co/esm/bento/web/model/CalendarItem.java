package jp.co.esm.bento.web.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jp.co.esm.bento.web.util.DateUtil;
import lombok.Data;

/**
 * Google Calendar API が戻すレスポンスのうち、Key="Item"の内容を格納するモデルです。 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarItem {
  
  // Key="start"のJson内容
  private CalendarStartDate start;

  // Key="summary"の値
  private String summary;
  
  /**
   * 祝日の日付を返します。
   * 
   * @return 祝日
   */
  public LocalDate getData() {
    return DateUtil.stringToLocalDate(start.getDate());
  }
}

