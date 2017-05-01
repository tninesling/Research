package serde.models;

public class RelevanceData extends TermFrequencyData {
  protected int relevance;

  public RelevanceData() {}

  public RelevanceData(TermFrequencyData existingData, int relevance) {
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
    this.termFrequencyList = existingData.getTermFrequencyList();
    setRelevance(relevance);
  }

  public int getRelevance() {
    return relevance;
  }

  public void setRelevance(int relevance) {
    this.relevance = relevance;
  }
}
