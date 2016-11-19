package spellChecker;

import serde.SportsJsonSerDe;
import serde.models.CategorizedSportsData;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SpellChecker {
  public void spellCheckSummary(String candidate) {
    try {
      JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

      // Exclude words from being corrected
      List<String> wordsToIgnore = Arrays.asList("\"");
      for (Rule rule : langTool.getAllActiveRules()) {
        if (rule instanceof SpellingCheckRule) {
          ((SpellingCheckRule) rule).acceptPhrases(wordsToIgnore);
        }
      }
      List<RuleMatch> matches = langTool.check(candidate);
      for (RuleMatch match : matches) {
        System.out.println("Potential error at characters " +
                            match.getFromPos() + "-" + match.getToPos() + ": " +
                            match.getMessage());
        System.out.println("Suggested correction(s): " +
                            match.getSuggestedReplacements());
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public String correctString(String candidate) {
    String correctedCandidate = new String();
    try {
      JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

      List<RuleMatch> matches = langTool.check(candidate);

      correctedCandidate = new String();
      int lastCandidateIndex = 0;
      for (RuleMatch match : matches) {
        List<String> suggestedReplacements = match.getSuggestedReplacements();

        if (match.getFromPos() >= lastCandidateIndex) {
          if (suggestedReplacements.isEmpty()) {
            correctedCandidate += candidate.substring(lastCandidateIndex, match.getToPos());
          } else {
            correctedCandidate += candidate.substring(lastCandidateIndex, match.getFromPos());
            correctedCandidate += suggestedReplacements.get(0);
          }
          lastCandidateIndex = match.getToPos();
        }
      }
      correctedCandidate += candidate.substring(lastCandidateIndex, candidate.length());
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return correctedCandidate;
  }
}
