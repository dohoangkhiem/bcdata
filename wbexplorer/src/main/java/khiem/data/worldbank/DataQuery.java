package khiem.data.worldbank;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import khiem.data.worldbank.pojo.DataItem;
import khiem.data.worldbank.pojo.Source;

public class DataQuery {
  Source source;
  
  public List<DataItem> doQuery(String indicatorId, int year) throws IOException {
    String requestURL = "http://api.worldbank.org/countries/all/indicators/" + indicatorId + "?date=" + year;
    URL url = new URL(requestURL);
    URLConnection connection = url.openConnection();
    InputStream is = connection.getInputStream();

    StringBuilder result = new StringBuilder();
    Reader reader = new InputStreamReader(is);
    int c;
    while ((c=reader.read()) != -1) {
      char ch = (char) c;
      result.append(ch);
    }
    reader.close();
    
    
    return null;
  }
}
