package khiem.data.worldbank.pojo;

import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Indicator {
  @PrimaryKey
  String id;
  
  String name;
  String sourceId;
  String sourceNote;
  String sourceOrganization;
  List<String> topicIds;
  @NotPersistent
  List<Topic> topics;
  
  @NotPersistent
  KeyValue source;
  /*@NotPersistent
  List<KeyValue> topics;*/
  
  public static class KeyValue {
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
    return "Indicator {id: " + id + ", name: " + name + "}"; 
  }
  
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
  public String getSourceId() {
    return sourceId;
  }
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }
  public String getSourceNote() {
    return sourceNote;
  }
  public void setSourceNote(String sourceNote) {
    this.sourceNote = sourceNote;
  }
  public List<Topic> getTopics() {
    return topics;
  }
  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }  
  public KeyValue getSource() {
    return source;
  }
  public void setSource(KeyValue source) {
    this.source = source;
  }
  /*public List<KeyValue> getTopics() {
    return topics;
  }
  public void setTopics(List<KeyValue> topics) {
    this.topics = topics;
  }*/

  public List<String> getTopicIds() {
    return topicIds;
  }

  public void setTopicIds(List<String> topicIds) {
    this.topicIds = topicIds;
  }
  
}
