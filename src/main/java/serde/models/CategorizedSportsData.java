package serde.models;

public class CategorizedSportsData extends SportsData {
  protected String category;

  public CategorizedSportsData() {
    super();
  }

  public CategorizedSportsData(SportsData existingData) {
    this.content = existingData.getContent();
    this.parserAssigned = existingData.getParserAssigned();
    this.index = existingData.getIndex();
    this.timeCode = existingData.getTimeCode();
    this.votes = existingData.getVotes();
    this.reply = existingData.getReply();
    this.type = existingData.getType();
    this.user = existingData.getUser();
    this.novelty = existingData.getNovelty();
    this.contentSimp = existingData.getContentSimp();
    this.sessionID = existingData.getSessionID();
    this.category = "";
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
