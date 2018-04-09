package jp.co.esm.bento.web.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.QueryResultIterator;

import jp.co.esm.bento.web.model.Order;
import jp.co.esm.bento.web.util.DateUtil;

/**
 * Orderエンティティのリポジトリクラスです。
 */
@Repository
public class OrderRepository implements DatastoreRepository<Order> {

  // エンティティの種類
  public final static String KIND = "Order";

  @Autowired
  DatastoreService datastore;

@Override
  public Entity create(Order model) {
  Entity entity = new Entity(KIND);
  model.convert(entity);
  datastore.put(entity);
  return entity;
  }

  @Override
  public Order read(Long id) {
    Entity entity = null;
    try {
      entity = datastore.get(KeyFactory.createKey(KIND, id));
    } catch (EntityNotFoundException e) {
        return null;
    }
    return entityToModel(entity);  
  }

  @Override
  public void update(Order model) {
    Key key = KeyFactory.createKey(KIND, model.getId());
    Entity entity = new Entity(key);
    model.convert(entity);
    datastore.put(entity);  
  }

  @Override
  public void delete(Long id) {
    Key key = KeyFactory.createKey(KIND, id);
    datastore.delete(key);
  }

  @Override
  public List<Order> list() {
    Query query = new Query(KIND);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> entities = preparedQuery.asQueryResultIterator();
    return entitiesToModels(entities);
  }
  
  /**
   * 指定の条件で注文を最大5件取得します。
   * 
   * @param userId ユーザID
   * @param week 週の始めの日付
   * @return 取得した結果
   */
  public List<Order> listByUserIdAndWeek(String userId, LocalDate week) {
    // 週の初めの日付と終わりの日付を取得
    Date fromDate = DateUtil.localDateToDate(week);
    Date toDate = DateUtil.localDateToDate(week.plusDays(4));
    
    Filter userFilter = new FilterPredicate(Order.USER_ID, FilterOperator.EQUAL, userId);
    Filter dateFilter = CompositeFilterOperator.and(new FilterPredicate(Order.DATE, FilterOperator.GREATER_THAN_OR_EQUAL, fromDate),
                                                    new FilterPredicate(Order.DATE, FilterOperator.LESS_THAN_OR_EQUAL, toDate));
    Query query = new Query(KIND)
        .setFilter(CompositeFilterOperator.and(userFilter, dateFilter));
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> entities = preparedQuery.asQueryResultIterator();
    return entitiesToModels(entities);
  }
  
  /**
   * 指定のユーザIDと日付を持つエンティティを取得します。
   * 
   * @param userId ユーザID
   * @param date 注文日
   * @return 取得した結果
   */
  public Order readByUserIdAndDate(String userId, LocalDate date) {
    Filter userFilter = new FilterPredicate(Order.USER_ID, FilterOperator.EQUAL, userId);
    Filter dateFilter = new FilterPredicate(Order.DATE, FilterOperator.EQUAL, DateUtil.localDateToDate(date));
    Query query = new Query(KIND)
        .setFilter(CompositeFilterOperator.and(userFilter, dateFilter));
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> entities = preparedQuery.asQueryResultIterator();
    List<Order> orders = entitiesToModels(entities);
    if (orders.isEmpty()) {
      // 注文なし
      return null;
    }
    // TODO 2件以上はないはず
    return orders.get(0);
  }

  /**
   * エンティティの内容をOrderに変換します。
   * @param entity エンティティ
   * @return 作成したOrder
   */
  private Order entityToModel(Entity entity)
  {
    Order order = new Order();
    order.setProperties(entity);
    return order;
  }
  
  /**
   * エンティティの集まりをOrderのリストに変換します。
   * @param entities エンティティの集まり
   * @return 作成したOrderを格納したリスト
   */
  private List<Order> entitiesToModels(Iterator<Entity> entities) {
    List<Order> results = new ArrayList<>();
    while (entities.hasNext()) {
      results.add(entityToModel(entities.next()));
    }
    return results;  
  }
}
