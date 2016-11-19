# Sports Data Package #

Author: Taylor Ninesling

### Package overview: ###
This package was created as part of an ongoing research project in the Hofstra
Computer Science department involving NLP and text summarization/comparison
techniques.

This package contains classes to:
1) Serialize and Deserialize Sports Data to and from JSON
2) Parse the Category_Coding.xlsx spreadsheet and add categories to existing
Sports Data JSON
3) Spell Check the content of Sports Data
4) Tokenize, Lemmatize, and POS Tag the content for Sports Data
These are meant to be used in this order.

This package uses sbt for dependency management.

### Compiling and Running the package: ###
To compile the package, from the root directory use the command: sbt compile

To run the package you must run the Main class and pass the name of the class
you would like to run. Options for class names are:
categorizer,
spellChecker,
nlp

From the root directory, use the command: sbt "run-main Main <class name>"
with the name of the desired class

### Specific Package Descriptions ###
#### 1) SerDe Package ####
This package contains a JSON Serializer/Deserializer for the Sports Data.
It contains model classes for the uncategorized Sports Data and Categorized Sports
Data. The SerDe uses the Jackson ObjectMapper class to map between JSON and the
model classes.

The ObjectMapper is configured to escape non-ASCII characters. For example,
smart quotes are escaped to UTF code \u201C and \u201D

#### 2) Categorizer Package ####
This package contains a parser for the Category_Coding.xlsx spreadsheet containing
the category information for each post. Each category datum is mapped to the
PostUserTimeAndCategory class. The category is matched to the existing Sports
Data by Username and TimeStamp. The final categorized data is mapped to the
CategorizedSportsData class, and then serialized using the SerDe package. The
Apache POI API is used to read from the Excel spreadsheets.

New posts have category "n", and replies have category "r".
For new posts and replies, missing categories are corrected to category "0".
Votes do not have categories.

#### 3) Spell Check ####
This package uses the SerDe package to read the Sports Data from JSON, and
corrects spelling errors in the Sports Data's content section using the
LanguageTool Java API.

Words are corrected by taking the first suggestion in a list of suggestions
provided for each misspelled word.

#### 4) NLP Package ####
This package uses the Stanford CoreNLP Java API for tokenizing the content section
of each instance of Sports Data, as well as lemmatizing each token, and tagging
each token with a part of speech. Tokens are arranged into unigrams and
multigrams (bigrams or trigrams). Only certain parts of speech are kept for each.
Note: Gerunds/Present Participles (code VBG) are considered Nouns

unigrams allowed: Adjectives, Adverbs, Nouns, Verbs

bigram/trigram patterns allowed: Number/Noun, Adjective/Noun, Noun/Noun, Number/Verb,
Number/Noun/Noun, Number/Adjective/Noun, Adjective/Adjective/Noun,
Adjective/Noun/Noun, Noun/Adjective/Noun, Noun/Noun/Noun, Noun/Pronoun/Noun

Parts of speech are encoded with Penn Treebank Project Part of Speech Tags. The
tag references can be found here: https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html

#### 5) Final Sport Parser Package ####
This package reads the Final Sport Templates in the form of Word documents.
The line in Main.java where the parse converts the documents from .docx to .txt
is commented out because the data was altered by hand after the conversion. Some
documents did not follow any naming convention and either had to be renamed by
hand or deleted along with duplicate entries. The text file versions of the
templates are then parsed and converted to JSON. Each resulting JSON file contains
a JSON array of FinalTemplate objects (one for each section of the group). Some
of the group sections may have empty templates that were not manually deleted
beforehand.
