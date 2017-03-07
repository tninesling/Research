package serde;

import serde.models.CountWord;
import serde.models.PostCountRankedCountWord;
import serde.models.RankedCountWord;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class CountWordSerDe {
  private static ObjectMapper mapper = new ObjectMapper();

  public static CountWord[] parseCountWordJson(String jsonFileLocation, CountWord[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }

  public static RankedCountWord[] parseCountWordJson(String jsonFileLocation, RankedCountWord[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }

  public static PostCountRankedCountWord[] parseCountWordJson(String jsonFileLocation, PostCountRankedCountWord[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }


  public static String countWordsToJson(CountWord[] countWordArray) {
    String mappedJsonString = "";
    try {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      // Write array as indented JSON string value
      mappedJsonString = mapper.writeValueAsString(countWordArray);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return mappedJsonString;
  }
}
