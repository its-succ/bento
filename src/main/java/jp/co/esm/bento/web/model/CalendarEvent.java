package jp.co.esm.bento.web.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Google Calendar API が戻すEventの内容を格納するモデルです。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarEvent {

  // Key="Item"の内容
  private List<CalendarItem> items;
  
  /**
   * CalendarのItemsのうち、dateのみを返します。
   * 
   * @return 日付を格納したリスト
   */
  public List<LocalDate> getDateList() {
    if (items == null || items.isEmpty()) {
      return new ArrayList<LocalDate>();
    }
    
    return items.stream().map(i -> i.getData()).collect(Collectors.toList());
  }
}

