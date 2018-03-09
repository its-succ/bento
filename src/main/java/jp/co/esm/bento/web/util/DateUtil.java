package jp.co.esm.bento.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class DateUtil {

  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  
  /**
   * 文字列型の日付（yyyy-MM-dd）をDate型に変換します。
   * @param target yyyyMMddの日付
   * @return 変換後の日付（変換失敗した場合はnull）
   */
  public static Date convert(String target) {
    if (target == null || target.isEmpty()) {
      return null;
    }
      
    try {
      return sdf.parse(target);
    } catch (ParseException e) {
      return null;
    }
  }
  
  /**
   * 文字列型の日付（yyyy-MM-dd）を起点として5日間分の日付をDate型で返します。
   * @param target yyyy-MM-ddの日付
   * @return　変換後の日付（変換失敗した場合は空のリスト）
   */
  public static List<Date> weekDate(String target)
  {
    List<Date> results = new ArrayList<Date>();
    Date date = convert(target);
    if (date == null) {
      return results;
    }
    
    // TODO LocalDateで処理したい。
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    
    results.add(date);
    IntStream.range(1, 5).forEach(i -> 
    {
      cal.add(Calendar.DATE, 1);
      results.add(cal.getTime());
    });
    return results;
    
  }
}
