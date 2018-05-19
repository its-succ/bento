package jp.co.esm.bento.web.model;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.esm.bento.web.util.DateUtil;
import lombok.Data;

/**
 * Google Calendar API が戻すレスポンスのうち、Key="Item"の内容を格納するモデルです。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarItem {

  // Key="start.date"のJson内容
  private LocalDate startDate;

  // Key="summary"の値
  private String summary;

  /**
   * Key="start"より"date"を設定する
   */
  @JsonProperty("start")
  private void unpackNested(Map<String, String> start) {
    this.startDate = DateUtil.stringToLocalDate((String)start.get("date"));
  }
}

