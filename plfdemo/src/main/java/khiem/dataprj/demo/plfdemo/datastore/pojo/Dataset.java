package khiem.dataprj.demo.plfdemo.datastore.pojo;

import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PrimaryKey;

public class Dataset {
  
  @PrimaryKey
  private int id;
  private String name;
  private String description;
  @NotPersistent private List<Table> tables;

  public Dataset(String name, String description) {
    this.name = name;
    this.description = description;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public List<Table> getTables() {
    return tables;
  }
  public void setTables(List<Table> tables) {
    this.tables = tables;
  }
}
