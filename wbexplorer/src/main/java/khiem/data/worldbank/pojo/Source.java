package khiem.data.worldbank.pojo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Source {
  @PrimaryKey
  String id;
  String name;
  String description;
  String url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
  
  @Override
  public String toString() {
    return "id: '" + id + "', name: '" + name + "', description: '" + description + "', url: '" + url + "'";
  }
}
