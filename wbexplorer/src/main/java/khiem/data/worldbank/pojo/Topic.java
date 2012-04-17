package khiem.data.worldbank.pojo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Topic {
  @PrimaryKey
  String id;
  String value;
  String sourceNote;
  
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
  public String getSourceNote() {
    return sourceNote;
  }
  public void setSourceNote(String sourceNote) {
    this.sourceNote = sourceNote;
  }
  @Override
  public String toString() {
    return "id: '" + id + ", value: '" + value + "', sourceNote: '" + sourceNote + "'";
  }
}
