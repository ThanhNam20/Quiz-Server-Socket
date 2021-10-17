package model;
import java.io.Serializable;

public class Topic implements Serializable {
  private int topicId;
  private String topicName;
  private int topicQuestionCount;

  public Topic(int topicId, String topicName, int topicQuestionCount) {
    this.topicId = topicId;
    this.topicName = topicName;
    this.topicQuestionCount = topicQuestionCount;
  }

  public int getTopicId() {
    return topicId;
  }

  public void setTopicId(int topicId) {
    this.topicId = topicId;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public int getTopicQuestionCount() {
    return topicQuestionCount;
  }

  public void setTopicQuestionCount(int topicQuestionCount) {
    this.topicQuestionCount = topicQuestionCount;
  }

}
