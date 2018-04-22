package jp.co.esm.bento.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.esm.bento.web.model.CalendarEvent;

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

  // Json
  private static ObjectMapper objectMapper = new ObjectMapper();
  
  /**
   * 指定範囲の国民の休日をGoogleカレンダーより取得します。
   *
   * @param from 指定範囲from
   * @param to　指定範囲to
   * @param requestUrl リクエストURL
   * @return 休日が格納されたリスト（ない場合は空）
   */
  public List<LocalDate> GetHolidays(LocalDate from, LocalDate to, String requestUrl) {
    // 休日取得クエリー
    String timeMin = from.format(DateTimeFormatter.ofPattern("yyyy-M-d")) + "T00:00:00Z";
    String timeMax = to.format(DateTimeFormatter.ofPattern("yyyy-M-d")) + "T23:59:59Z";
    String query = String.format("key=%s&timeMin=%s&timeMax=%s&maxResults=%d&orderBy=startTime&singleEvents=true", apiKey, timeMin, timeMax, 30);

    HttpURLConnection con = null;
    InputStream in = null;
    BufferedReader reader = null;
    try {
      // 祝日カレンダーアクセス
      URL url = new URL(calendarUrl + calendarId + "/events?" + query);
      con = (HttpURLConnection)url.openConnection();
      // HTTPリファラー設定(アクセスサイトを制限しているため）
      con.setRequestProperty("Referer", requestUrl);
      con.connect();
      if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
        // レスポンス取得
        in = con.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line);
        }
        // Jsonデータより祝日のみ取得
        CalendarEvent event = objectMapper.readValue(output.toString(), CalendarEvent.class);
        return event.getDateList();
      }

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (con != null) {
        con.disconnect();
      }
      try {
        if (in != null) {
          in.close();
        }
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
