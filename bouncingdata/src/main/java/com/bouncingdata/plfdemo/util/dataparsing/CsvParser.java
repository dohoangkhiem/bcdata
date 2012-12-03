package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvParser implements DataParser {
  
  private Logger logger = LoggerFactory.getLogger(CsvParser.class);
  
  CsvParser() {}

  @Override
  public List<String[]> parse(InputStream is) throws Exception {
    CSVParser parser = new CSVParser(new InputStreamReader(is), CSVFormat.DEFAULT);
    List<CSVRecord> records = parser.getRecords();
    long lineNumber = parser.getLineNumber();
    logger.info("Reading CSV file: {} rows found.", lineNumber);
    List<String[]> results = new ArrayList<String[]>();
    boolean isFirst = true;
    int columnNum = 0;
    for (CSVRecord record : records) {
      if(isFirst) columnNum = record.size();
      
      String[] row = new String[columnNum];
      Iterator<String> iter = record.iterator();
      int i = 0;
      while (iter.hasNext()) {
        if (i < columnNum) {
          row[i++] = iter.next();
        } else break;
      }
         
      results.add(row);
      isFirst = false;
    }
    return results;
  }
  
  public static void main(String[] args) throws Exception {
    File file = new File("/home/khiem/Downloads/COMPS.csv");
    CSVParser parser = new CSVParser(new FileReader(file));
    //Map<String, Integer> headerMap = parser.getHeaderMap();
    List<CSVRecord> records = parser.getRecords();
    long lineNumber = parser.getLineNumber();
    System.out.println("Number of line " + lineNumber);
  }
}
