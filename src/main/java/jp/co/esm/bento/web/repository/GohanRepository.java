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

import jp.co.esm.bento.web.model.Gohan;

/**
 * おかずマスタのリポジトリクラスです。
 */
@Repository
public class GohanRepository implements DatastoreRepository<Gohan> {

  // エンティティの種類
  public final static String KIND = "Gohan";
  
  @Autowired
  DatastoreService datastore;

  @Override
  public Entity create(Gohan model) {
    Entity entity = new Entity(KIND);
    model.convert(entity);
    datastore.put(entity);
    return entity;
  }

  @Override
  public Gohan read(Long id) {
    Entity entity = null;
    try {
      entity = datastore.get(KeyFactory.createKey(KIND, id));
    } catch (EntityNotFoundException e) {
        return null;
    }
    return entityToModel(entity);  
  }

  @Override
  public void update(Gohan model) {
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

  /**
   * 全てのデータを取得します。
   */
  @Override
  public List<Gohan> list() {
    Query query = new Query(KIND);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> entities = preparedQuery.asQueryResultIterator();
    return entitiesToModels(entities);
  }

  private Gohan entityToModel(Entity entity)
  {
    Gohan gohan = new Gohan();
    gohan.setProperties(entity);
    return gohan;
  }
  
  private List<Gohan> entitiesToModels(Iterator<Entity> results) {
    List<Gohan> gohans = new ArrayList<>();
    while (results.hasNext()) {
      gohans.add(entityToModel(results.next()));
    }
    return gohans;  
  }
}
