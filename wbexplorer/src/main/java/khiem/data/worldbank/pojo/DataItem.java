package khiem.data.worldbank.pojo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import khiem.data.worldbank.pojo.Indicator.KeyValue;

@PersistenceCapable
public class DataItem {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
  long id;
  
  String indicatorId;
  String countryId;
  String date;
  String value;
  String decimal;
  
  @NotPersistent
  KeyValue indicator;
  @NotPersistent
  KeyValue country;
  
  public DataItem(String indicatorId, String countryId, String date, String value, String decimal) {
    this.indicatorId = indicatorId;
    this.countryId = countryId;
    this.date = date;
    this.value = value;
    this.decimal = decimal;
  }
  
  protected DataItem() {}

  public String toString() {
    return "{countryId: " + country.getId() + ", indicatorId: " + indicator.getId() + ", date: " + date;  
  }
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDecimal() {
    return decimal;
  }

  public void setDecimal(String decimal) {
    this.decimal = decimal;
  }

  public KeyValue getIndicator() {
    return indicator;
  }

  public void setIndicator(KeyValue indicator) {
    this.indicator = indicator;
  }

  public KeyValue getCountry() {
    return country;
  }

  public void setCountry(KeyValue country) {
    this.country = country;
  };
}
