package khiem.dataprj.demo.plfdemo.datastore.pojo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Table {
  @PrimaryKey
  private int id;
  private String name;
  private String description;
  private String datasetName;
  private String fieldList;
  
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
  public String getDatasetName() {
    return datasetName;
  }
  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }
  public String getFieldList() {
    return fieldList;
  }
  public void setFieldList(String fieldList) {
    this.fieldList = fieldList;
  }
  
}
