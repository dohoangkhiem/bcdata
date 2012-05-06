package khiem.dataprj.demo.plfdemo.utils;

public enum ApplicationLanguage {
  PYTHON("python"), RUBY("ruby"), R("r") ;
  
  private String language;
  
  private ApplicationLanguage(String lang) {
    language = lang;
  }

  public String getLanguage() {
    return language;
  }
}
