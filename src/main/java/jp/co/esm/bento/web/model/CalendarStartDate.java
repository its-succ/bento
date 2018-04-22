package jp.co.esm.bento.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Google Calendar API が戻すレスポンスのうち、Key="start"のJson内容を格納するモデルです。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarStartDate {
  
  // Key="date"の値
  private String date;
}
