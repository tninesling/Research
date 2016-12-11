package serde.models;

import java.util.List;

public class LemmatizedTokenData extends CategorizedSportsData implements LemmatizedData {
  protected List<List<String>> taggedUnigramTokens;
  protected List<List<String>> taggedMultigramTokens;

  public LemmatizedTokenData() {}

  public LemmatizedTokenData(CategorizedSportsData existingData) {
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
    this.category = existingData.getCategory();
    this.taggedUnigramTokens = null;
    this.taggedMultigramTokens = null;
  }

  public List<List<String>> getTaggedUnigramTokens() {
    return taggedUnigramTokens;
  }

  public void setTaggedUnigramTokens(List<List<String>> taggedUnigramTokens) {
    this.taggedUnigramTokens = taggedUnigramTokens;
  }

  public List<List<String>> getTaggedMultigramTokens() {
    return taggedMultigramTokens;
  }

  public void setTaggedMultigramTokens(List<List<String>> taggedMultigramTokens) {
    this.taggedMultigramTokens = taggedMultigramTokens;
  }
}
