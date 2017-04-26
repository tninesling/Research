package tfIdfCalculator

import serde._
import serde.models._

import java.io.File

import scala.collection.JavaConversions._

object TfIdfCalculator {
  def main(args: Array[String]) = {
    List("A", "AA", "AB", "AC", "AD", "AE", "AF",
         "AG", "AH", "AI", "AK", "AM", "AN", "AR", "AT", "AV", "AX", "AY", "B",
         "C", "D", "EA", "EB", "EC", /*"ED", "EE",*/ "EF", "EG", "EH", "EJ", "EK",
         "EM", /*"EN",*/ "EP", "ER", "ES", "ET", /*"EU",*/ "EX", "EY", "EZ", "F", "G",
         "H", "J", "K", "M", "N", "P", "R", "S", "T", "U", "W", "X", "Y", "Z")
         .foreach(name => topPosts(args(0), args(1), name))
  }

  def topPosts(groupLocation: String, docLocation: String, groupName: String) = {
    val finalDocs = readFinalDocs(docLocation, groupName)
    val posts = readPosts(groupLocation, groupName)
    val topNum = posts.size / 20

    println(groupName + ": " + topNum + " relevant posts:")

    posts.filter(!_.getType.equals("v")) // remove votes which will have no frequent terms
         .map(post => (post, cosineSimilarity(post, posts, finalDocs)))
         .sortWith((x, y) => x._2.compareTo(y._2) > 0) // sorts in descending order
         .take(topNum)
         .foreach { x => println(x._1.getIndex)}
  }

  def readFinalDocs(docLocation: String, groupName: String) = {
    val serde = new FinalTemplateSerDe
    serde.parseFrequencyTemplateJsonArray(docLocation + groupName + "_frequency.json").toList
  }

  def readPosts(groupLocation: String, groupName: String) = {
    val serde = new SportsJsonSerDe
    serde.parseSportsJson(groupLocation + groupName + "_frequency.json", new Array[TermFrequencyData](0)).toList
  }

  def cosineSimilarity(post: TermFrequencyData,
                       posts: List[TermFrequencyData],
                       finalDocs: List[TermFrequency]): Double = {
    val postVec = calculateTfIdfVector(post, posts)
    val finalDocVec = calculateTfIdfVector(finalDocs, posts)

    cosineSimilarity(postVec, finalDocVec)
  }

  def cosineSimilarity(vec1: List[(String, Double)], vec2: List[(String, Double)]): Double = {
    val vec2map: Map[String,Double] = vec2.toMap

    val dotProd: Double = vec1.map(tuple =>
      tuple._2 * vec2map.getOrElse(tuple._1, 0.0)
    ).foldLeft(0.0)(sum)

    val mag = magnitude(vec1.map(_._2)) * magnitude(vec2.map(_._2))

    if (mag == 0)
      0
    else
      dotProd / mag
  }

  def magnitude(vec: List[Double]): Double = {
    math.sqrt(vec.map(x => x*x)
                 .foldLeft(0.0)(sum))
  }

  def sum(x: Int, y: Int) = x + y
  def sum(x: Double, y: Double) = x + y

  def calculateTfIdfVector(termFrequencyInstance: TermFrequency,
                           posts: List[TermFrequencyData]): List[(String, Double)] = {
    calculateTfIdfVector(List(termFrequencyInstance), posts)
  }

  /**
    * Calculates the tf-idf vector for a given implementation of the TermFrequency
    * interface
    */
  def calculateTfIdfVector(termFrequencyInstances: List[TermFrequency],
                           posts: List[TermFrequencyData]): List[(String, Double)] = {
    val tfList = processTfList(termFrequencyInstances)
    val idfMap = processIdfMap(posts)

    tfList.map { tfTuple =>
      val term = tfTuple._1
      val alpha = termFrequencyInstances.map(_.alpha(term))
                                        .foldLeft(1)(math.max)

      val tfCalc = alpha * tfTuple._2.toDouble / termFrequencyInstances.size
      val idfCalc = idfMap.getOrElse(term, posts.size.toDouble)

      (term, tfCalc * idfCalc)
    }
  }

  def processTfList(tfInstance: List[TermFrequency]) =
    condenseFrequencies(tfInstance.flatMap(_.getTermFrequencyList))

  def processIdfMap(posts: List[TermFrequencyData]) =
    condenseFrequencies(posts.flatMap(_.getTermFrequencyList)).map(idfTuple =>
      (idfTuple._1, math.log(posts.size.toDouble / (idfTuple._2 + 1)))
    ).toMap

  /**
    * Condenses a List of CountWords by aggregating the counts of CountWords with the same word
    * @param termList - a List of CountWords
    * @return A List of tuples containing the term and the aggregated count
    */
  def condenseFrequencies(termList: List[CountWord]): List[(String, Int)] = {
    termList.map { x => (x.getWord, x.getCount) } // Map each CountWord to a 2-tuple of (word, count)
            .groupBy(_._1) // Creates a Map grouping tuples by their 1st entry
            .values // Takes the tuple Lists without the keys
            .map(_.reduce(
              (x,y) => (x._1, x._2 + y._2) // Reduces each tuple List to a single tuple by summing the counts of the tuples
            ))
            .toList
  }
}
