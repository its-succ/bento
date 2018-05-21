package jp.co.esm.bento.web.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.esm.bento.web.model.CalendarEvent;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Googleカレンダーのサービスクラスです。
 */
@Service
public class CalendarService {

  // GoogleカレンダーURL
  @Value("${google.api.calendar.url}")
  private String calendarUrl;

  // 休日カレンダーID
  @Value("${google.api.calendar.id}")
  private String calendarId;

  // APIキー
  @Value("${google.api.calendar.key}")
  private String apiKey;

  // フォーマッタ
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-M-d");

  /**
   * 指定範囲の国民の休日をGoogleカレンダーより取得します。
   *
   * @param from 指定範囲from
   * @param to　指定範囲to
   * @param requestUrl リクエストURL
   * @return 休日が格納されたリスト（ない場合は空）
   */
  public List<LocalDate> getHolidays(LocalDate from, LocalDate to, String requestUrl) {
    // 休日カレンダーアクセスURL
    URI url = UriComponentsBuilder.newInstance()
              .fromUriString(calendarUrl)
              .path(calendarId)
              .path("/events")
              .queryParam("key", apiKey)
              .queryParam("timeMin", from.format(formatter) + "T00:00:00Z")
              .queryParam("timeMax", to.format(formatter) + "T23:59:59Z")
              .queryParam("maxResults", 30)
              .queryParam("orderBy", "startTime")
              .queryParam("singleEvents", "true")
              .build()
              .encode()
              .toUri();
      RequestEntity<?> requestEntity = RequestEntity.get(url).header("Referer", requestUrl).build();
      RestTemplate restTemplate = new RestTemplate();
      // 祝日カレンダーアクセス
      ResponseEntity<CalendarEvent> response = restTemplate.exchange(requestEntity, CalendarEvent.class);
      return response.getBody().getDateList();
  }
}
