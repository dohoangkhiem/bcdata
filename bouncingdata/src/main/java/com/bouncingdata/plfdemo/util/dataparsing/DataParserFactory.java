package com.bouncingdata.plfdemo.util.dataparsing;


public class DataParserFactory {
  
  private DataParserFactory() {}
  
  public static DataParser getDataParser(int type) {
    switch(type) {
    case FileType.EXCEL:
      return new ExcelParser();
    case FileType.CSV:
      return new CsvParser();
    case FileType.TEXT:
      return new TextParser();
    default:
      return null;
    }
  }
  
  public static class FileType {
    public static final int EXCEL = 1;
    public static final int CSV = 2;
    public static final int TEXT = 3;
  }

}
