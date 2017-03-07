package serde.models;

import java.util.List;

public class TermFrequencyData extends LemmatizedTokenData implements TermFrequency {
  protected List<CountWord> termFrequencyList;

  public TermFrequencyData() {}

  public TermFrequencyData(LemmatizedTokenData existingData) {
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
    this.taggedUnigramTokens = existingData.getTaggedUnigramTokens();
    this.taggedMultigramTokens = existingData.getTaggedMultigramTokens();
  }

  public List<CountWord> getTermFrequencyList() {
    return termFrequencyList;
  }

  public void setTermFrequencyList(List<CountWord> termFrequencyList) {
    this.termFrequencyList = termFrequencyList;
  }

  public int alpha(String term) {
    return 1;
  }
}
