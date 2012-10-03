package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.InputStream;
import java.util.List;

public interface DataParser {  
  public List<String[]> parse(InputStream is) throws Exception;
}
