package jp.co.esm.bento.web.repository;

import java.util.List;
//import com.google.cloud.datastore.Entity;

import com.google.appengine.api.datastore.Entity;

/**
 * Datastoreのリポジトリインターフェースです。
 *
 * @param <T> モデル
 */
public interface DatastoreRepository<T> {

  /**
   * 指定の内容でエンティティを追加します。
   * 
   * @param model 作成内容
   * @return 作成したエンティティ
   */
  Entity create(T model);
  
  /**
   * 指定のIDのエンティティを1件取得します。
   * @param id ID
   * @return 取得したエンティティ
   */
  T read(Long id);
  
  /**
   * 指定の内容でエンティティを更新します、
   * 
   * @param model 更新内容
   */
  void update(T model);

  /**
   * 指定のIDのエンティティを削除します。
   * 
   * @param id ID
   */
  void delete(Long id);
  
  /**
   * 全てのデータを取得します。
   * 
   * @return 全てのエンティティ
   */
  List<T> list();
}
