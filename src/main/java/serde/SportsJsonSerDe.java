package serde;

import serde.models.CategorizedSportsData;
import serde.models.LemmatizedTokenData;
import serde.models.SportsData;
import serde.models.TermFrequencyData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SportsJsonSerDe {
  private static ObjectMapper mapper = new ObjectMapper();

  public SportsData[] parseSportsJson(String jsonFileLocation, SportsData[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      // Read JSON from the file location into an instance of the dynamic class of outArray
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }

  public LemmatizedTokenData[] parseSportsJson(String jsonFileLocation, LemmatizedTokenData[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      // Read JSON from the file location into an instance of the dynamic class of outArray
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }

  public TermFrequencyData[] parseSportsJson(String jsonFileLocation, TermFrequencyData[] outArray) {
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      // Read JSON from the file location into an instance of the dynamic class of outArray
      outArray = mapper.readValue(jsonFile, outArray.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outArray;
  }


  public String sportsDataToJson(SportsData[] sportsDataArray) {
    String mappedJsonString = "";
    try {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      // Write array as indented JSON string value
      mappedJsonString = mapper.writeValueAsString(sportsDataArray);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return mappedJsonString;
  }
}
