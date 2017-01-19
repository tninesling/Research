import categorizer.*;
import finalSportTemplate.*;
import nlp.*;
import serde.*;
import serde.models.*;
import spellChecker.SpellChecker;
import termcounter.TermCounter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
  private final static Logger LOGGER = Logger.getLogger(SportsJsonCategorizer.class.getName());
  private static FileHandler fileHandler = null;
  private static String logLocation = "./logs/log.log";

  // private variables containing local directories of files
  private static String sessionDataDirectoryLocation = "C:\\Users\\Taylor\\Research\\data\\SportsSessionData\\";
  private static String categoryCodingSheetLocation = "C:\\Users\\Taylor\\Research\\data\\Category_Coding.xlsx";
  private static String categorizedDataDirectoryLocation = "C:\\Users\\Taylor\\Research\\data\\CategorizedData\\";
  private static String spellCheckedDataDirectoryLocation = "C:\\Users\\Taylor\\Research\\data\\SpellCheckedData\\";
  private static String lemmatizedDataDirectoryLocation = "C:\\Users\\Taylor\\Research\\data\\LemmatizedData\\";
  private static String finalSportTemplateLocation = "C:\\Users\\Taylor\\Research\\data\\FinalTemplates\\";
  private static String finalSportTextLocation = "C:\\Users\\Taylor\\Research\\data\\FinalSportText\\";
  private static String finalSportJsonLocation = "C:\\Users\\Taylor\\Research\\data\\FinalSportJson\\";
  private static String termCountLocation = "C:\\Users\\Taylor\\Research\\data\\TermFrequencyData\\";
  private static String termCountTemplateLocation = "C:\\Users\\Taylor\\Research\\data\\TermFrequencyTemplates\\";
  private static String rankedVocabLocation = "C:\\Users\\Taylor\\Research\\data\\RankedVocab\\";

  public static String[] groupNames = {"A", "AA", "AB", "AC", "AD", "AE", "AF",
          "AG", "AH", "AI", "AK", "AM", "AN", "AR", "AT", "AV", "AX", "AY", "B",
          "C", "D", "EA", "EB", "EC", "EF", "EG", "EH", "EJ", "EK", "EM", "EP",
          "ER", "ES", "ET", "EX", "EZ", "F", "G", "H", "J", "K", "P", "R", "S",
          "T", "U", "W", "X", "Y", "Z"};

  public static void main(String[] args) {
    setLogger(logLocation);

    if (args.length < 1) {
      return;
    }
    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
    switch (args[0]) {
      case "categorizer": {
        newArgs = new String[] { sessionDataDirectoryLocation,
                                 categoryCodingSheetLocation,
                                 categorizedDataDirectoryLocation };
        categorizerMain(newArgs);
        break;
      }
      case "spellChecker": {
        newArgs = new String[] { categorizedDataDirectoryLocation,
                                 spellCheckedDataDirectoryLocation };
        spellCheckerMain(newArgs);
        break;
      }
      case "nlp": {
        newArgs = new String[] { spellCheckedDataDirectoryLocation,
                                 lemmatizedDataDirectoryLocation };
        nlpMain(newArgs);
        break;
      }
      case "finalSportParser": {
        newArgs = new String[] { finalSportTemplateLocation,
                                 finalSportTextLocation,
                                 finalSportJsonLocation };
        finalSportMain(newArgs);
        break;
      }
      case "finalSportLemmatizeCount": {
        newArgs = new String[] { finalSportJsonLocation,
                                 termCountTemplateLocation };
        finalSportLemmatizeAndCount(newArgs);
        break;
      }
      case "termCounter": {
        newArgs = new String[] { lemmatizedDataDirectoryLocation,
                                 termCountLocation,
                                 rankedVocabLocation };
        termCounterMain(newArgs);
        break;
      }
      case "matrixBuilder": {
        newArgs = new String[] { termCountLocation,
                                 rankedVocabLocation };
        buildMatrix(newArgs);
        break;
      }
      case "updateVocab": {
        newArgs = new String[] { rankedVocabLocation, rankedVocabLocation };
        addNumberOfPostsToVocabulary(newArgs);
        break;
      }
      default: {
        System.out.println("Invalid class name specified");
      }
    }
  }

  public static void categorizerMain(String[] args) {
    SportsJsonSerDe jsonHandler = new SportsJsonSerDe();
    SportsExcelSheetParser categoryParser = new SportsExcelSheetParser();
    SportsJsonCategorizer categorizer = new SportsJsonCategorizer();
    String sessionDataDirectoryLocation = args[0];
    String categoryExcelSheetLocation = args[1];
    String categorizedDataDirectoryLocation = args[2];

    for (String groupName: groupNames) {
      String jsonLocation = sessionDataDirectoryLocation + groupName + "_session.json";
      SportsData[] existingSportsJsonArray = jsonHandler.parseSportsJson(jsonLocation, new SportsData[0]);

      // Read the Category_Coding spreadsheet for group
      XSSFWorkbook workbook = categoryParser.getExcelWorkbookFromFileLocation(categoryExcelSheetLocation);
      XSSFSheet groupSheet = categoryParser.getSheetForGroup(workbook, "group " + groupName);
      Iterator<PostUserTimeAndCategory> categorizedPostIterator =
              categoryParser.categorizedPostIterator(groupSheet);

      // Merge the existing data with the categories
      CategorizedSportsData[] categorizedSportsDataArray =
              categorizer.mergeExistingSportsDataWithCategories(existingSportsJsonArray,
                                                                categorizedPostIterator);

      int corrections = categorizer.correctUncategorizedPostsAndReplies(categorizedSportsDataArray);
      LOGGER.info(corrections + " corrections made for group " + groupName);

      // Output the JSON formatted data to *_categorized.json
      String jsonOutputPath = categorizedDataDirectoryLocation + groupName + "_categorized.json";

      String finalJson = jsonHandler.sportsDataToJson(categorizedSportsDataArray);

      writeJsonToLocation(finalJson, jsonOutputPath);
    }
  }

  public static void spellCheckerMain(String[] args) {
    SpellChecker spellChecker = new SpellChecker();
    SportsJsonSerDe jsonHandler = new SportsJsonSerDe();
    String categorizedDataDirectoryLocation = args[0];
    String spellCheckedDataDirectoryLocation = args[1];

    for (String groupName : groupNames) {
      String categorizedGroupLocation = categorizedDataDirectoryLocation + groupName + "_categorized.json";

      // Read categorized json data
      CategorizedSportsData[] categorizedSportsDataArray =
            (CategorizedSportsData[]) jsonHandler.parseSportsJson(categorizedGroupLocation, new CategorizedSportsData[0]);
      // Correct spelling for content of each item
      for (CategorizedSportsData datum : categorizedSportsDataArray) {
        if (!datum.getType().equals("v")) {
          String correctedContent = spellChecker.correctString(datum.getContent());
          datum.setContent(correctedContent);
        }
      }
      // Write the data back to json format
      String correctedDataAsJson = jsonHandler.sportsDataToJson(categorizedSportsDataArray);

      // Write json to file at spellCheckedGroupLocation
      String spellCheckedGroupLocation = spellCheckedDataDirectoryLocation + groupName + "_spell_checked.json";
      writeJsonToLocation(correctedDataAsJson, spellCheckedGroupLocation);
    }
  }

  public static void nlpMain(String[] args) {
    SportsJsonSerDe jsonHandler = new SportsJsonSerDe();
    ContentProcessor cp = new ContentProcessor();
    String inputJsonLocation = args[0];
    String lemmatizedDataDirectoryLocation = args[1];

    for (String groupName : groupNames) {
      String spellCheckedGroupLocation = inputJsonLocation + groupName + "_spell_checked.json";

      // Read categorized json data
      CategorizedSportsData[] categorizedSportsDataArray =
            (CategorizedSportsData[]) jsonHandler.parseSportsJson(spellCheckedGroupLocation, new CategorizedSportsData[0]);

      // Build array of LemmatizedTokenData
      LemmatizedTokenData[] lemmatizedDataArray = cp.lemmatizeAndTagCategorizedData(categorizedSportsDataArray);
      // Write the data back to json format
      String lemmatizedDataAsJson = jsonHandler.sportsDataToJson(lemmatizedDataArray);

      // Write json to file at lemmatizedGroupLocation
      String lemmatizedGroupLocation = lemmatizedDataDirectoryLocation + groupName + "_lemmed.json";
      writeJsonToLocation(lemmatizedDataAsJson, lemmatizedGroupLocation);
    }
  }

  public static void finalSportMain(String[] args) {
    FinalSportTemplateParser parser = new FinalSportTemplateParser();
    FinalTemplateSerDe serde = new FinalTemplateSerDe();
    String docxTemplateLocation = args[0];
    String textTemplateLocation = args[1];
    String jsonTemplateLocation = args[2];
    //parser.convertAllDocumentsToText(docxTemplateLocation, textTemplateLocation);

    for (String groupName : groupNames) {
      System.out.println("Parsing " + groupName);
      String templateJson = serde.finalTemplateArrayToJson(
            serde.parseFinalTemplatesFromDirectory(textTemplateLocation, groupName));
      writeJsonToLocation(templateJson, jsonTemplateLocation + groupName + "_template.json");
    }
  }

  public static void finalSportLemmatizeAndCount(String[] args) {
    String finalSportLocation = args[0];
    String termCountTemplateLocation = args[1];

    FinalTemplateSerDe serde = new FinalTemplateSerDe();

    for (String groupName : groupNames) {
      LemmatizedFinalTemplate[] lemmedTemplates = FinalSportTemplateLemmatizer.lemmatizeTemplatesFromFile(finalSportLocation + groupName + "_template.json");
      TermFrequencyTemplate[] countedTemplates = TermCounter.getTermFrequencyTemplates(lemmedTemplates);
      String templateJson = serde.finalTemplateArrayToJson(countedTemplates);
      writeJsonToLocation(templateJson, termCountTemplateLocation + groupName + "_frequency.json");
    }
  }

  public static void termCounterMain(String[] args) {
    String lemmatizedDataLocation = args[0];
    String termCountLocation = args[1];
    String rankedVocabLocation = args[2];

    HashMap<String,Integer> globalHash = new HashMap<String,Integer>();

    for (String groupName : groupNames) {
      String termCountJson = TermCounter.getTermFrequencyData(lemmatizedDataLocation + groupName + "_lemmed.json", globalHash);
      writeJsonToLocation(termCountJson, termCountLocation + groupName + "_frequency.json");
    }

    List<RankedCountWord> rankedList = TermCounter.rankCountWords(TermCounter.buildCountWordListFromHashMap(globalHash));
    writeJsonToLocation(TermCounter.rankedListToJson(rankedList), rankedVocabLocation + "ranked_vocab.json");
  }

  public static void buildMatrix(String[] args) {
    String termCountLocation = args[0];
    String rankedVocabLocation = args[1];

    TermFrequencyMatrix matrix = new TermFrequencyMatrix();
    SportsJsonSerDe serde = new SportsJsonSerDe();
    CountWordSerDe cserde = new CountWordSerDe();
    TermFrequencyMatrixSerDe mserde = new TermFrequencyMatrixSerDe();

    List<RankedCountWord> vocab = Arrays.asList(cserde.parseCountWordJson(rankedVocabLocation + "ranked_vocab.json", new RankedCountWord[0]));

    for (String groupName : groupNames) {
      TermFrequencyData[] data = serde.parseSportsJson(termCountLocation + groupName + "_frequency.json", new TermFrequencyData[0]);
      matrix.addData(vocab, groupName, data);
    }

    writeJsonToLocation(mserde.termFrequencyMatrixToJson(matrix), rankedVocabLocation + "term_matrix.json");
  }

  public static void addNumberOfPostsToVocabulary(String[] args) {
    String rankedVocabLocation = args[0];
    String termMatrixLocation = args[1];

    CountWordSerDe cserde = new CountWordSerDe();
    TermFrequencyMatrixSerDe mserde = new TermFrequencyMatrixSerDe();

    List<RankedCountWord> vocab = Arrays.asList(cserde.parseCountWordJson(rankedVocabLocation + "ranked_vocab.json", new RankedCountWord[0]));
    TermFrequencyMatrix matrix = mserde.parseTermFrequencyMatrixJson(termMatrixLocation + "term_matrix.json");

    List<PostCountRankedCountWord> updatedVocab = TermCounter.updateRankedVocab(vocab, matrix);

    writeJsonToLocation(cserde.countWordsToJson(updatedVocab.toArray(new PostCountRankedCountWord[0])), rankedVocabLocation + "num_posts_vocab.json");
  }

  public static void setLogger(String logLocation) {
    try {
      fileHandler = new FileHandler(logLocation, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    fileHandler.setFormatter(new SimpleFormatter());
    LOGGER.addHandler(fileHandler);
  }

  public static void writeJsonToLocation(String json, String location) {
    try {
      PrintWriter jsonOut = new PrintWriter(new BufferedWriter(new FileWriter(location)));
      jsonOut.write(json);
      jsonOut.close();
    } catch (IOException ioe) {
      LOGGER.severe("Error writing json to file at " + location + ":\n" + ioe.toString());
    }
  }
}
