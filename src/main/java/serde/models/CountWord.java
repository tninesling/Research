package serde.models;

public class CountWord implements Comparable<CountWord> {
  String word;
  int count;

  public CountWord() {}

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int compareTo(CountWord other) {
    if (this.count < other.count) {
      return -1;
    } else if (this.count == other.count) {
      return 0;
    } else {
      return 1;
    }
  }
}
