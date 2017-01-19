package serde.models;

import java.util.ArrayList;
import java.util.List;

public class TermFrequencyMatrix {
  List<List<List<String>>> rows;

  public TermFrequencyMatrix() {
    rows = new ArrayList<List<List<String>>>();
  }

  public List<List<List<String>>> getRows() {
    return rows;
  }

  public void setRows(List<List<List<String>>> rows) {
    this.rows = rows;
  }

  public void addData(List<RankedCountWord> vocab, String groupName, TermFrequencyData[] data) {
    ArrayList<List<String>> newRow = new ArrayList<List<String>>();
    // Create a matrix entry for each term in each post
    for (TermFrequencyData post : data) {
      newRow.addAll(getPostEntries(vocab, groupName, post));
    }
    rows.add(newRow);
  }

  public List<List<String>> getPostEntries(List<RankedCountWord> vocab, String groupName, TermFrequencyData post) {
    List<List<String>> entriesForPost = new ArrayList<List<String>>();
    for (CountWord term : post.getTermFrequencyList()) {
      List<String> termEntry = new ArrayList<String>();
      // A term entry contains (group name, post index within group, global term index, term frequency)
      termEntry.add(groupName);
      termEntry.add(Integer.toString(post.getIndex()));

      int index = getTermIndex(vocab, term.getWord());
      termEntry.add(Integer.toString(index));
      termEntry.add(Integer.toString(term.getCount()));

      // Only add the term if it is found in the ranked vocabulary
      if (index >= 0) {
        entriesForPost.add(termEntry);
      }
    }
    return entriesForPost;
  }

  public int getTermIndex(List<RankedCountWord> vocab, String term) {
    int index = -1;
    for (int i = 0; i < vocab.size() && index < 0; i++) {
      if (vocab.get(i).getWord().equals(term)) {
        index = vocab.get(i).getRank();
      }
    }
    return index;
  }
}
