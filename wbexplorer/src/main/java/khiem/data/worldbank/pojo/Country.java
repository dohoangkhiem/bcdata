package khiem.data.worldbank.pojo;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Country {
  @PrimaryKey
  @Persistent
  String id;
  @Persistent
  String iso2Code;
  @Persistent
  String name;
  @Persistent
  String regionId;
  @Persistent
  String adminRegionId;
  @Persistent
  String incomeLevelId;
  @Persistent
  String lendingTypeId;
  @Persistent
  String capitalCity;
  @Persistent
  String longitude;
  @Persistent
  String latitude;
  
  /**
   * All these fields will not be persisted to database
   */
  @NotPersistent
  Region region;
  @NotPersistent
  IncomeLevel incomeLevel;
  @NotPersistent
  LendingType lendingType;
  @NotPersistent 
  Region adminregion;
  
  public static class Region {
    String id;
    String value;
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public String getValue() {
      return value;
    }
    public void setValue(String value) {
      this.value = value;
    }
    
  }
  
  @Override
  public String toString() {
    return "(id: " + id + ", name: " + name + ")";
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getIso2Code() {
    return iso2Code;
  }
  public void setIso2Code(String iso2Code) {
    this.iso2Code = iso2Code;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getRegionId() {
    return regionId;
  }
  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }
  public String getAdminRegionId() {
    return adminRegionId;
  }
  public void setAdminRegionId(String adminRegionId) {
    this.adminRegionId = adminRegionId;
  }
  public String getIncomeLevelId() {
    return incomeLevelId;
  }
  public void setIncomeLevelId(String incomeLevelId) {
    this.incomeLevelId = incomeLevelId;
  }
  public String getCapitalCity() {
    return capitalCity;
  }
  public void setCapitalCity(String capitalCity) {
    this.capitalCity = capitalCity;
  }
  public String getLongitude() {
    return longitude;
  }
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }
  public String getLatitude() {
    return latitude;
  }
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }
  public String getLendingTypeId() {
    return lendingTypeId;
  }
  public void setLendingTypeId(String lendingTypeId) {
    this.lendingTypeId = lendingTypeId;
  }
  public Region getRegion() {
    return region;
  }
  public void setRegion(Region region) {
    this.region = region;
  }
  public IncomeLevel getIncomeLevel() {
    return incomeLevel;
  }
  public void setIncomeLevel(IncomeLevel incomeLevel) {
    this.incomeLevel = incomeLevel;
  }
  public LendingType getLendingType() {
    return lendingType;
  }
  public void setLendingType(LendingType lendingType) {
    this.lendingType = lendingType;
  }
  public Region getAdminRegion() {
    return adminregion;
  }

  public void setAdminRegion(Region adminRegion) {
    this.adminregion = adminRegion;
  }
}
