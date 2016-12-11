package serde.models;

public class RankedCountWord extends CountWord {
  int rank;

  public RankedCountWord() {}

  public RankedCountWord(CountWord existingCountWord) {
    this.setWord(existingCountWord.getWord());
    this.setCount(existingCountWord.getCount());
    rank = -1;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }
}
