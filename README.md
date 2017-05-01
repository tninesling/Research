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
5) Parse the Final Sport Templates to JSON
6) Lemmatize and Count Term Frequencies for Final Sport Templates
7) Count Term Frequencies for Sports Data
8) Algorithmically Select Relevant Posts
These are meant to be used in this order.

This package uses sbt for dependency management.

### Compiling and Running the package: ###
To compile the package, from the root directory use the command: sbt compile

To run the package you must run the Main class and pass the name of the class
you would like to run. Options for class names are:
categorizer,
spellChecker,
nlp,
finalSportParser,
finalSportLemmatizeCount,
termCounter.
The arguments required for each command are outlined in each package description below

From the root directory, use the command: **sbt "run-main sportsdata.Main [_class name_] [_arguments_]"**
with the name of the desired class and the arguments required by the function

To transform the sports data from the base session JSON to the term-counted JSON ready for tf-idf, the classes must be run in the following order:
categorizer -> spellChecker -> nlp -> termCounter

Similarly, the final templates can be parsed and processed by running the following classes:
finalSportParser -> finalSportLemmatizeCount

To perform the tf-idf calculation and add the relevance to the processed data, run the following command:
**sbt "run-main tfIdfCalculator.TfIdfCalculator [_input posts directory_] [_input templates directory_] [_output directory_]"**
where the input posts are the Term Frequency Posts output by package 7 and the input templates are the Term Frequency Templates output by package 6.

### Specific Package Descriptions ###
#### 1) SerDe Package ####
This package contains a JSON Serializer/Deserializer for the Sports Data.
It contains model classes for the uncategorized Sports Data and Categorized Sports
Data. The SerDe uses the Jackson ObjectMapper class to map between JSON and the
model classes.

The ObjectMapper is configured to escape non-ASCII characters. For example,
smart quotes are escaped to UTF code \u201C and \u201D

#### 2) Categorizer Package ####

Run with: **sbt "run-main sportsdata.Main categorizer [_input directory_] [_category sheet directory_] [_output directory_]"**
Where the input directory contains the session sports data, and the category sheet directory contains the Category_Coding.xlsx spreadsheet

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

Run with: **sbt "run-main sportsdata.Main spellChecker [_input directory_] [_output directory_]"**

This package uses the SerDe package to read the Sports Data from JSON, and
corrects spelling errors in the Sports Data's content section using the
LanguageTool Java API.

Words are corrected by taking the first suggestion in a list of suggestions
provided for each misspelled word.

#### 4) NLP Package ####

Run with: **sbt "run-main sportsdata.Main nlp [_input directory_] [_output directory_]"**

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

#### 5) Final Template Parser Package ####

Run with: **sbt "run-main sportsdata.Main finalSportParser [_template docx directory_] [_output text directory_] [_output json directory_]"**

This package reads the Final Sport Templates in the form of Word documents.
The line in Main.java where the parse converts the documents from .docx to .txt
is commented out because the data was altered by hand after the conversion. Some
documents did not follow any naming convention and either had to be renamed by
hand or deleted along with duplicate entries. The text file versions of the
templates are then parsed and converted to JSON. Each resulting JSON file contains
a JSON array of FinalTemplate objects (one for each section of the group). Some
of the group sections may have empty templates that were not manually deleted
beforehand.

#### 6) Final Template Lemmatizer and Counter Package ####

Run with: **sbt "run-main sportsdata.Main finalSportLemmatizeCount [_final template json directory_] [_output directory_]"**

This package creates lists of lemmatized n-grams and their frequencies within each instance of the final template. These fields are added to the JSON structure.

#### 7) Sports Data Term Frequency Package ####

Run with: **sbt "run-main sportsdata.Main termCounter [_lemmatized data directory_] [_term count output directory_] [_ranked vocabulary output directory_]"**

This package reads in the Tokenized Sports Data produced by the NLP package (4) and adds a term frequency field where the frequencies of each of the n-grams are stored. The package also creates an overall ranked vocabulary for the post corpus. The ranked vocabulary is sorted in descending order with rank 1 having the highest frequency.

#### 8) Algorithmically Select Relevant Posts ####
Run with: **sbt "run-main tfIdfCalculator.TfIdfCalculator [_input posts directory_] [_input templates directory_] [_output directory_]"**

This package reads in Term Frequency Data posts and their corresponding Term Frequency Templates. The output is posts with a new relevance field added to the structure.

The relevance decision is made algorithmically by performing tf-idf using the existing term frequencies in the structure. Each post is compared to the final templates via a cosine similarity calculation. Each term in the term vectors for the post and final document are assigned a weight with the following formula:

alpha * (term frequency) / length(vector) * log(total number of posts / number of posts term appears in)

The cosine similarity is then calculated for two vectors, A and B, by the following:

(A dot_prod B) / (magnitude(A) * magnitude(B))

The posts are then sorted in decreasing order by cosine similarity to the final group template. Once the posts are sorted, the top 5% of posts are assigned a value of "relevant" which is coded as 1 = relevant, 0 = irrelevant.
