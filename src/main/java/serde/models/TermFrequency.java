package serde.models;

import java.util.List;

public interface TermFrequency {
  public List<CountWord> getTermFrequencyList();

  public int alpha(String term);
}
