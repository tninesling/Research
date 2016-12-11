package serde.models;

import java.util.List;

public interface LemmatizedData {
  List<List<String>> getTaggedUnigramTokens();
  List<List<String>> getTaggedMultigramTokens();

  void setTaggedUnigramTokens(List<List<String>> unigrams);
  void setTaggedMultigramTokens(List<List<String>> multigrams);
}
