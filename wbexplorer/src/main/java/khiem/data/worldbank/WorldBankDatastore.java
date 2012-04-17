package khiem.data.worldbank;

import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import khiem.data.worldbank.pojo.*;
import java.util.List;
import java.util.Iterator;
import com.google.gson.reflect.TypeToken;

public class WorldBankDatastore {
  
  static PersistenceManagerFactory pmf;
  
  public static void main(String args[]) throws Exception {
        
    pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    
    Query q = pmf.getPersistenceManager().newQuery(Indicator.class);
    q.compile();
    List<Indicator> indicators = (List) q.execute();
    int start = 0;
    int end = 0;
    if (args == null || args.length == 0) {
      start = 0;
      end = indicators.size() - 1;
    } else if (args.length == 1) {
      try {
        start = Integer.parseInt(args[0]);
        end = indicators.size() - 1;
      } catch (NumberFormatException e) {
        System.err.println("Invalid arguments.");
        return;
      }
    } else if (args.length >= 2) {
      try {
        start = Integer.parseInt(args[0]);
        end = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        System.err.println("Invalid arguments.");
        return;
      }
    }
    if (start < 0 || end < 0 || start > end || start >= indicators.size()) {
      System.err.println("Invalid arguments.");
      return;
    }
    /*int year = 1960;
    while (year <= 1980) {*/
      for (int counter = start; counter <= end; counter++) {
        if (counter >= indicators.size()) {
          System.out.println("Reach end of indicator list.");
          return;
        }
        Indicator i = indicators.get(counter);
        System.out.println(counter + ": Getting data for indicator " + i.getId());
        SimpleQuery<DataItem> dataQuery = new SimpleQuery<DataItem>();
        String requestURL = "http://api.worldbank.org/countries/all/indicators/" + i.getId().trim() + "?format=json";
        Collection<DataItem> data = dataQuery.query(requestURL, new TypeToken<Collection<DataItem>>(){}, 1);
        if (data == null || data.size() == 0) {
          System.out.println("Null data for " + i.getId() + " (" + counter + ")");
          continue;
        }
        for (DataItem d : data) {
          d.setIndicatorId(d.getIndicator().getId());
          d.setCountryId(d.getCountry().getId());
        }
        Iterator<DataItem> iter = data.iterator();
        while (iter.hasNext()) {
          DataItem item = iter.next();
          if (item.getValue() == null || item.getValue().equals("null")) {
            iter.remove();
            continue;
          }
          item.setIndicatorId(item.getIndicator().getId());
          item.setCountryId(item.getCountry().getId());
        }
        persistData(data);
        System.out.println("Successfully persisted " + data.size() + " items for indicator " + i.getId());
        if (counter == end) {
          System.out.println("Completed for " + (end-start+1) + " indicators from index " + start 
              + " to index " + end + "! Break.");
          break;
        }
      }
      /*year++;*/
    //}
    
    /*
    // all topics
    SimpleQuery<Topic> topicsQuery = new SimpleQuery<Topic>();
    Collection<Topic> topics = topicsQuery.query(WorldBankURLs.TOPICS_URL, new TypeToken<Collection<Topic>>() {}); 
    persistData(topics);
    
    // 
    SimpleQuery<Country> countriesQuery = new SimpleQuery<Country>();
    Collection<Country> countries = countriesQuery.query(WorldBankURLs.COUNTRIES_URL, new TypeToken<Collection<Country>>() {});
    // process for countries
    for (Country c : countries) {
      c.setIncomeLevelId(c.getIncomeLevel().getId());
      c.setAdminRegionId(c.getAdminRegion()!=null?c.getAdminRegion().getId():null);
      c.setRegionId(c.getRegion().getId());
      c.setLendingTypeId(c.getLendingType().getId());
    }
    //persistData(countries);
    
    SimpleQuery<Region> regionsQuery = new SimpleQuery<Region>();
    Collection<Region> regions = regionsQuery.query(WorldBankURLs.REGIONS_URL, new TypeToken<Collection<Region>>() {});
    //persistData(regions);
    
    SimpleQuery<IncomeLevel> incomeLevelsQuery = new SimpleQuery<IncomeLevel>();
    Collection<IncomeLevel> incomeLevels = incomeLevelsQuery.query(WorldBankURLs.INCOME_LEVELS_URL, new TypeToken<Collection<IncomeLevel>>() {});
    //persistData(incomeLevels);
    
    
    SimpleQuery<LendingType> lendingTypesQuery = new SimpleQuery<LendingType>();
    Collection<LendingType> lendingTypes = lendingTypesQuery.query(WorldBankURLs.LENDING_TYPES_URL, new TypeToken<Collection<LendingType>>() {});
    //persistData(lendingTypes);
*/    
    //SimpleQuery<Source> sourcesQuery = new SimpleQuery<Source>();
    //Collection<Source> sources = sourcesQuery.query(WorldBankURLs.SOURCES_URL, new TypeToken<Collection<Source>>() {});
    //persistData(sources);
    /*
    try {
      SimpleQuery<Indicator> indicatorsQuery = new SimpleQuery<Indicator>();
      Collection<Indicator> indicators = indicatorsQuery.query(WorldBankURLs.INDICATORS_URL, new TypeToken<Collection<Indicator>>() {});
      for (Indicator i : indicators) {
        i.setSourceId(i.getSource().getId());
        if (i.getTopics() != null && i.getTopics().size() > 0) {
          i.setTopicIds(new ArrayList<String>());
          for (Topic t : i.getTopics()) {
            i.getTopicIds().add(t.getId());
          }
        }
      }
      persistData(indicators);
    } catch (org.datanucleus.exceptions.NucleusDataStoreException e) {
      e.printStackTrace();
      return;
    }*/
    
    /*int year = 1960;
    while (year <= 1980) {
      for (Indicator i : indicators) {
        SimpleQuery<DataItem> dataQuery = new SimpleQuery<DataItem>();
        String requestURL = "http://api.worldbank.org/countries/all/indicators/" + i.getId() + "?date=" + String.valueOf(year) + ":" + String.valueOf(year) + "&format=json";
        Collection<DataItem> data = dataQuery.query(requestURL, new TypeToken<Collection<DataItem>>(){});
        for (DataItem d : data) {
          d.setIndicatorId(d.getIndicator().getId());
          d.setCountryId(d.getCountry().getId());
        }
        Iterator<DataItem> iter = data.iterator();
        while (iter.hasNext()) {
          DataItem item = iter.next();
          if (item.getValue() == null || item.getValue().equals("null")) {
            iter.remove();
            continue;
          }
          item.setIndicatorId(item.getIndicator().getId());
          item.setCountryId(item.getCountry().getId());
        }
        persistData(data);
      }
      year++;
    }*/
    //Map<Source,Collection<Indicator>> indicatorMap = new HashMap<Source, Collection<Indicator>>();
    
    /*
    for (Source s : sources) {
      // find all indicators in this source
      SimpleQuery<Indicator> indicatorQuery = new SimpleQuery<Indicator>();
      String sourceIndicatorURL = "http://api.worldbank.org/source/" + s.getId() + "/indicators?format=json";
      Collection<Indicator> indicators = indicatorQuery.query(sourceIndicatorURL, new TypeToken<Collection<Indicator>>(){});
      for (Indicator i : indicators) {
        i.setSourceId(i.getSource().getId());
        List<KeyValue> topics = i.getTopics();
        List<Topic> topicIds = 
        for (KeyValue kv : topics) {
          
        }
        if (i.getId().equals("SL.EMP.TOTL.SP.ZS")) {
          System.out.println("Source: " + s.getName() + ", source from indicator: " + i.getSourceId());
        }
      }
      //persistData(indicators);
      SimpleQuery<DataItem> dataItemsQuery = new SimpleQuery<DataItem>();
      
      
      String requestURL = "http://api.worldbank.org/countries/all/";
      Collection<DataItem> dataItems = dataItemsQuery.query(requestURL, new TypeToken<Collection<DataItem>>() {});
      // persist indicators
    }*/
    
    //
    
  }
  
  public static <T> void persistData(Collection<T> collection) {
    if (collection != null && collection.size() > 0) {
      PersistenceManager pm = pmf.getPersistenceManager();
      Transaction tx = pm.currentTransaction();
      try {
        tx.begin();
        pm.makePersistentAll(collection);
        tx.commit();
      } finally {
        if (tx.isActive()) tx.rollback();
      }
      
    }
  }
}
