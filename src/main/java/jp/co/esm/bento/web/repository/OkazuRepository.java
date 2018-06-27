package jp.co.esm.bento.web.repository;

import java.util.ArrayList;
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
import com.google.appengine.api.datastore.QueryResultIterator;

import jp.co.esm.bento.web.model.Okazu;

/**
 * おかずマスタのリポジトリクラスです。
 */
@Repository
public class OkazuRepository implements DatastoreRepository<Okazu> {

  // エンティティの種類
  public final static String KIND = "Okazu";

  @Autowired
  DatastoreService datastore;

  /**
   * 指定の内容でOkazuエンティティを追加します。
   *
   * @param model 作成内容
   * @return 作成したエンティティ
   */
  @Override
  public Entity create(Okazu model) {
    Entity entity = new Entity(KIND);
    model.convert(entity);
    datastore.put(entity);
    return entity;
  }

  @Override
  public Okazu read(Long id) {
    Entity entity = null;
    try {
      entity = datastore.get(KeyFactory.createKey(KIND, id));
    } catch (EntityNotFoundException e) {
        return null;
    }
    return entityToModel(entity);
  }

  @Override
  public void update(Okazu model) {
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
  public List<Okazu> list() {
    Query query = new Query(KIND);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> entities = preparedQuery.asQueryResultIterator();
    return entitiesToModels(entities);
  }

  private Okazu entityToModel(Entity entity)
  {
    Okazu okazu = new Okazu();
    okazu.setProperties(entity);
    return okazu;
  }

  private List<Okazu> entitiesToModels(Iterator<Entity> entities) {
    List<Okazu> results = new ArrayList<>();
    while (entities.hasNext()) {
      results.add(entityToModel(entities.next()));
    }
    return results;
  }
}
