package serde.models;

public class PostUserTimeAndCategory {
  private String user;
  private String timeStamp;
  private String category;

  public PostUserTimeAndCategory() {}

  public boolean hasCategory() {
    return (category != null && !category.equals(""));
  }

  @Override
  public String toString() {
    return new String("User: " + user + ", TimeStamp: " + timeStamp + ", Category: " + category);
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
