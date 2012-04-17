package khiem.data.worldbank;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * @author khiem
 *
 * @param <T>
 */
public class SimpleQuery<T> {
  
  private static final String sourcesURL = "http://api.worldbank.org/sources?format=json";
  private static final String topicsURL = "http://api.worldbank.org/topics?format=json";
  private static final String countriesURL = "http://api.worldbank.org/countries?format=json";
  
  public Collection<T> query(String requestURL, TypeToken<Collection<T>> tt, int time) throws Exception {
    if (time==3) {
      System.out.println("Failed to connect to " + requestURL + " 3 times. Skip");
      return null;
    }
    StringBuilder result = new StringBuilder();
    Reader reader = null;
    try {
      URL url = new URL(requestURL);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      InputStream is = connection.getInputStream();
      int code = connection.getResponseCode();
      if (code != 200) {
        // re-try after 2s
        System.err.println("Trying to reconnect to " + requestURL + "..");
        Thread.sleep(2000);
        return query(requestURL, tt, time+1);
      }
      reader = new InputStreamReader(is);
      int c;
      while ((c=reader.read()) != -1) {
        char ch = (char) c;
        result.append(ch);
      }
      reader.close();
    } catch (Exception e) {
      // retry
      System.err.println("Exception occurs, retry after 2s...\n" + requestURL);
      Thread.sleep(2000);
      return query(requestURL, tt, time+1);
    } finally {
      if (reader != null) reader.close();
    }
    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    JsonElement element = parser.parse(result.toString());
    JsonArray array = element.getAsJsonArray();
    JsonObject info = array.get(0).getAsJsonObject();
    JsonInfo inf = gson.fromJson(info, JsonInfo.class);
    
    int page = Integer.parseInt(inf.getPage());
    int pages = Integer.parseInt(inf.getPages());
    
    if (pages > page) {
      int total = Integer.parseInt(inf.getTotal());
      String newURL = requestURL + "&page=1&per_page=" + total;
      System.out.println("Make new request to get all data..");
      System.out.println(newURL);
      return query(newURL, tt, 1);
    }
    
    JsonElement jsonEle = array.get(1);
    Collection<T> output = gson.fromJson(jsonEle, tt.getType());
    /*for (T s : output) {
      System.out.println(s.toString());
    }*/
    if (output != null) 
      System.out.println("Parsed " + output.size() + " items.");
    return output;
  }
  
  public static void main(String[] args) throws Exception {
    /*System.out.println("SOURCES: ");
    new SimpleQuery<Source>().query(sourcesURL, new TypeToken<Collection<Source>>(){});
    System.out.println("COUNTRIES: ");
    new SimpleQuery<Country>().query(countriesURL, new TypeToken<Collection<Country>>(){});*/
    /*String requestURL = "http://api.worldbank.org/countries/all/indicators/DT.ODA.DACD.GVCS.GEN.CD?format=json";
    Collection<DataItem> result = new SimpleQuery<DataItem>().query(requestURL, new TypeToken<Collection<DataItem>>(){}, 1);
    int count = 0;
    for (DataItem d : result) {
      if (d.getValue() != null && !d.getValue().equals("")) {
        count++;
      }
    }
    System.out.println("Total " + count + " data items");*/
    /*String indicatorsURL = "http://api.worldbank.org/indicators?format=json";
    Collection<Indicator> indicators = new SimpleQuery<Indicator>().query(indicatorsURL, new TypeToken<Collection<Indicator>>(){});
    Object[] inds = indicators.toArray();
    for (int i = 450; i <= 474; i++)
      System.out.println((Indicator)inds[i]);
      
*/  
    Collection<String> test = null;
    for (String s : test) {
      
    }
  }

}
