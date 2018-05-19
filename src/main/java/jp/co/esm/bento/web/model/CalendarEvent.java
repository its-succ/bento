package jp.co.esm.bento.web.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

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
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyList();
    }

    return items.stream().map(i -> i.getStartDate()).collect(Collectors.toList());
  }
}

