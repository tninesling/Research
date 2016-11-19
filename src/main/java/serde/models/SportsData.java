package serde.models;

public class SportsData {
  protected String content;
  protected int parserAssigned;
  protected int index;
  protected String timeCode;
  protected int votes;
  protected int reply;
  protected String type;
  protected String user;
  protected int novelty;
  protected String contentSimp;
  protected int sessionID;

  public SportsData() {}

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getParserAssigned() {
    return parserAssigned;
  }

  public void setParserAssigned(int parserAssigned) {
    this.parserAssigned = parserAssigned;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }

  public int getReply() {
    return reply;
  }

  public void setReply(int reply) {
    this.reply = reply;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public int getNovelty() {
    return novelty;
  }

  public void setNovelty(int novelty) {
    this.novelty = novelty;
  }

  public String getContentSimp() {
    return contentSimp;
  }

  public void setContentSimp(String contentSimp) {
    this.contentSimp = contentSimp;
  }

  public int getSessionID() {
    return sessionID;
  }

  public void setSessionID(int sessionID) {
    this.sessionID = sessionID;
  }
}
