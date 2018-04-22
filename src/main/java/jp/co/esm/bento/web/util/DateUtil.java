package jp.co.esm.bento.web.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 日付関連のユーティリティクラスです。 
 */
public class DateUtil {

  static final private String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
  
  /**
   * Date型からLocalDate型へ変換します。
   * @param target Date型の日付
   * @return LocalDate型の日付（変換失敗した場合はnull）
   */
  public static LocalDate dateToLocalDate(Date target) {
    if (target == null) {
      return null;
    }
    return target.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }
  
  /**
   * yyyy-MM-ddでフォーマットされた日付をLocalDate型へ変換します。
   * 
   * @param target yyyy-MM-ddでフォーマットされた日付
   * @return LocalDate型の日付（変換失敗した場合はnull）
   */
  public static LocalDate stringToLocalDate(String target) {
    return stringToLocalDate(target, DEFAULT_DATE_FORMAT);    
  }
  
  /**
   * 指定の書式の文字列型日付をLocalDate型へ変換します。
   * 
   * @param target　フォーマットされた日付
   * @param format 書式
   * @return LocalDate型の日付（変換失敗した場合はnull）
   */
  public static LocalDate stringToLocalDate(String target, String format) {
    if (target == null || target.isEmpty()) {
      return null;
    }
    return LocalDate.parse(target, DateTimeFormatter.ofPattern(format)); 
  }
  
  /**
   * LocalDate型からDate型へ変換します。
   * @param target LocalDate型の日付
   * @return Date型の日付（変換失敗した場合はnull）
   */
  public static Date localDateToDate(LocalDate target) {
    if (target == null) {
      return null;
    }
    return Date.from(target.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
  
  /**
   * 指定の日付を起点として5日間分の日付を返します。
   * @param week 週の初めの日付
   * @return　変換後の日付（変換失敗した場合は空のリスト）
   */
  public static List<LocalDate> weekDate(LocalDate week)
  {
    List<LocalDate> results = new ArrayList<LocalDate>();
    if (week == null) {
      return results;
    }
    
    results.add(week);
    IntStream.range(1, 5).forEach(i -> 
    {
      results.add( week.plusDays(i));
    });
    return results;
    
  }
}