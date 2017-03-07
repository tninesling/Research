name := """sportsData-package"""

version := "0.0.1-SNAPSHOT"

lazy val root = (project in file("."))

mainClass in (Compile, run) := Some("tfIdfCalculator.TfIdfCalculator")

// Allows forking, and expands JVM heap size to 2GB
fork in run := true
javaOptions in run += "-Xmx2G"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.4",
  "com.google.protobuf" % "protobuf-java" % "3.1.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
  "org.apache.poi" % "poi-excelant" % "3.15",
  "org.languagetool" % "language-en" % "3.5",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
