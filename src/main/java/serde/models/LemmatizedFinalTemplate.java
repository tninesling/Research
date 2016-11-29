package serde.models;

import java.util.List;

public class LemmatizedFinalTemplate extends FinalTemplate {
  protected List<List<String>> taggedUnigramTokens;
  protected List<List<String>> taggedMultigramTokens;

  public LemmatizedFinalTemplate() {}

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
