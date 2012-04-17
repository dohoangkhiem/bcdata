package khiem.data.worldbank.pojo;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class IndicatorTopic {
  String indicatorId;
  String topicId;
  
  public IndicatorTopic(String ind, String top) {
    indicatorId = ind;
    topicId = top;
  }
}

