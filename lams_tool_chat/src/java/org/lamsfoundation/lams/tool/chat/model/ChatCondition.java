package org.lamsfoundation.lams.tool.chat.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputFormatException;
import org.lamsfoundation.lams.tool.ToolOutputValue;
import org.lamsfoundation.lams.tool.chat.dto.ChatConditionDTO;

/**
 * A text search condition with a set of messages on which the search should be performed.
 *
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "tl_lachat11_conditions")
public class ChatCondition extends TextSearchCondition {

    public ChatCondition() {
	super();
    }

    public ChatCondition(TextSearchConditionDTO conditionDTO) {
	super(conditionDTO);
    }

    public ChatCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String allWords, String phrase, String anyWords, String excludedWords) {
	super(conditionId, conditionUIID, orderId, name, displayName, BranchCondition.OUTPUT_TYPE_COMPLEX, null, null,
		null, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public Object clone() {
	return new ChatCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public ChatCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	return new ChatCondition(null, newConditionUIID, orderId, name, displayName, allWords, phrase, anyWords,
		excludedWords);
    }

    @Override
    public boolean isMet(ToolOutput output) throws ToolOutputFormatException {
	boolean result = false;
	if (output != null) {
	    ToolOutputValue value = output.getValue();
	    if (value != null) {
		if (OutputType.OUTPUT_COMPLEX.equals(value.getType())) {
		    // the condition "knows" it's an array of strings - user's messages
		    String[] messages = (String[]) value.getValue();
		    parseConditionStrings();

		    if (messages == null) {
			result = getAllWordsCondition() == null && getAnyWordsCondition() == null
				&& getPhraseCondition() == null;
		    } else {
			/*
			 * We want to check if each messages doesn't contain excluded words and at least one message
			 * contains the desired (all, any, phrase) words. That is why we check all, any and phrase
			 * conditions using the "matches" method, but we check excluded words manually.
			 */
			List<String> excludedWordsCopy = getExcludedWordsCondition();
			setExcludedWordsCondition(null);
			for (String message : messages) {
			    if (!result) {
				result = matches(message);
			    }
			    if (matchExcludedWordsOnly(excludedWordsCopy, message)) {
				result = false;
				break;
			    }
			}
		    }

		} else {
		    throw new ToolOutputFormatException("Chat tool produced a non-complex tool output.");
		}
	    }
	}
	return result;
    }

    /**
     * It filters the given text in order to find any of the unwanted words.
     *
     * @param excludedWords
     *            words to search for
     * @param textToMatch
     *            string to be filtered
     * @return <code>true</code> if at least one of the words from the list is found in the text
     */
    private boolean matchExcludedWordsOnly(List<String> excludedWords, String textToMatch) {
	if (textToMatch == null || excludedWords == null) {
	    return false;
	}
	StringBuilder stringPattern = new StringBuilder();
	for (String excludedWord : excludedWords) {
	    stringPattern.append("(?:").append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
		    .append(Pattern.quote(excludedWord)).append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
		    .append(")|");
	}
	stringPattern.deleteCharAt(stringPattern.length() - 1);
	Pattern regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
	Matcher matcher = regexPattern.matcher(textToMatch);
	return matcher.find();
    }

    @Override
    public ChatConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new ChatConditionDTO(this, toolActivityUIID);
    }
}