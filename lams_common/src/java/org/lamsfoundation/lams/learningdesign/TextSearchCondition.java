/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.web.TextSearchActionForm;

/**
 * Condition that is based on text search. Several properties set what needs to be found in a tool output. Based on the
 * result of the text scan, the condition is safisfied or not.
 * 
 * @author Marcin Cieslak
 * 
 */
public class TextSearchCondition extends BranchCondition implements Cloneable {

    // ---- persistent fields -------
    /**
     * All the words from this string should be found in the tool output.
     */
    protected String allWords;
    /**
     * The whole phrase from this string should be found in the tool output.
     */
    protected String phrase;
    /**
     * At least one of the words from this string should be found in the tool output.
     */
    protected String anyWords;
    /**
     * None of the words from this string should be found in the tool output.
     */
    protected String excludedWords;

    // ---- non-persistent fields ----------
    /**
     * Regular expression that divides a string into single words.
     */
    public static final String WORD_DELIMITER_REGEX = "\\s";
    private static Logger log = Logger.getLogger(TextSearchCondition.class);
    /**
     * Were the strings provided by user parsed into practical collections of words.
     */
    protected boolean conditionsParsed = false;
    /**
     * Property {@link #allWords} divided into words.
     */
    protected List<String> allWordsCondition = new ArrayList<String>();
    /**
     * Same as {@link #phrase}. Created for
     */
    protected String phraseCondition;
    /**
     * Property {@link #anyWords} divided into words.
     */
    protected List<String> anyWordsCondition = new ArrayList<String>();
    /**
     * Property {@link #excludedWords} divided into words.
     */
    protected List<String> excludedWordsCondition = new ArrayList<String>();

    public TextSearchCondition() {

    }

    /**
     * Creates the condition based on a DTO.
     * 
     * @param conditionDTO
     */
    public TextSearchCondition(BranchConditionDTO conditionDTO) {
	super(conditionDTO);
	allWords = conditionDTO.getAllWords();
	phrase = conditionDTO.getPhrase();
	anyWords = conditionDTO.getAnyWords();
	excludedWords = conditionDTO.getExcludedWords();
    }

    /**
     * Full constructor
     */
    public TextSearchCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name,
	    String displayName, String type, String startValue, String endValue, String exactMatchValue,
	    String allWords, String phrase, String anyWords, String excludedWords) {
	super(conditionId, conditionUIID, orderId, name, displayName, type, startValue, endValue, exactMatchValue);
	this.allWords = allWords;
	this.phrase = phrase;
	this.anyWords = anyWords;
	this.excludedWords = excludedWords;
    }

    @Override
    public BranchCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	return new TextSearchCondition(null, newConditionUIID, orderId, name, displayName, type, startValue, endValue,
		exactMatchValue, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public Object clone() {
	return new TextSearchCondition(null, null, orderId, name, displayName, type, startValue, endValue,
		exactMatchValue, allWords, phrase, anyWords, excludedWords);
    }

    public String getAllWords() {
	return allWords;
    }

    public List<String> getAllWordsCondition() {
	return allWordsCondition;
    }

    public String getAnyWords() {
	return anyWords;
    }

    public List<String> getAnyWordsCondition() {
	return anyWordsCondition;
    }

    public String getExcludedWords() {
	return excludedWords;
    }

    public List<String> getExcludedWordsCondition() {
	return excludedWordsCondition;
    }

    public String getPhrase() {
	return phrase;
    }

    public String getPhraseCondition() {
	return phraseCondition;
    }

    @Override
    public boolean isMet(ToolOutput output) {
	return output != null && matches(output.getValue().getString());
    }

    /**
     * Checks if the given text contain the words provided in the condition parameters. The search is done by using
     * lower case both text and paramaters.
     * 
     * @param text
     *                string to check
     * @return <code>true</code> if text satisfies this condition
     */
    public boolean matches(String text) {
	if (text == null) {
	    return false;
	}
	if (!conditionsParsed) {
	    parseConditionStrings();
	}
	String lowerCaseText = text.toLowerCase();
	if (getExcludedWordsCondition() != null) {
	    for (String excludedWord : getExcludedWordsCondition()) {
		if (lowerCaseText.contains(excludedWord)) {
		    return false;
		}
	    }
	}
	if (getAnyWordsCondition() != null) {
	    boolean wordFound = false;
	    for (String word : getAnyWordsCondition()) {
		if (lowerCaseText.contains(word)) {
		    wordFound = true;
		    break;
		}
	    }
	    if (!wordFound) {
		return false;
	    }
	}
	if (getPhraseCondition() != null && !lowerCaseText.contains(getPhraseCondition())) {
	    return false;
	}
	if (getAllWordsCondition() != null) {
	    for (String word : getAllWordsCondition()) {
		if (!lowerCaseText.contains(word)) {
		    return false;
		}
	    }
	}
	return true;
    }

    /**
     * Parser currently set condition string using {@link #parseConditionStrings(String, String, String, String)}.
     */
    public void parseConditionStrings() {
	parseConditionStrings(getAllWords(), getPhrase(), getAnyWords(), getExcludedWords());
    }

    /**
     * Splits condition parameters into lists of words for easier usage.
     * 
     * @param allWordsString
     * @param phraseString
     * @param anyWordsString
     * @param excludedWordsString
     */
    public void parseConditionStrings(String allWordsString, String phraseString, String anyWordsString,
	    String excludedWordsString) {

	conditionsParsed = true;
	String trimmed = null;
	String[] splited = null;
	if (allWordsString != null) {
	    trimmed = allWordsString.trim();
	    splited = trimmed.split(TextSearchCondition.WORD_DELIMITER_REGEX);
	    for (int index = 0; index < splited.length; index++) {
		splited[index] = splited[index].toLowerCase();
	    }

	    setAllWordsCondition(Arrays.asList(splited));
	} else {
	    setAllWordsCondition(null);
	}
	if (phraseString != null) {
	    trimmed = phraseString.trim();
	    setPhraseCondition(trimmed.toLowerCase());
	}

	if (anyWordsString != null) {
	    trimmed = anyWordsString.trim();
	    splited = trimmed.split(TextSearchCondition.WORD_DELIMITER_REGEX);
	    for (int index = 0; index < splited.length; index++) {
		splited[index] = splited[index].toLowerCase();
	    }
	    setAnyWordsCondition(Arrays.asList(splited));
	} else {
	    setAnyWordsCondition(null);
	}
	if (excludedWordsString != null) {
	    trimmed = excludedWordsString.trim();
	    splited = trimmed.split(TextSearchCondition.WORD_DELIMITER_REGEX);
	    for (int index = 0; index < splited.length; index++) {
		splited[index] = splited[index].toLowerCase();
	    }
	    setExcludedWordsCondition(Arrays.asList(splited));
	} else {
	    setExcludedWordsCondition(null);
	}
    }

    /**
     * Fills the condition parameters using strings provided by an user in the form.
     * 
     * @param textSearchActionForm
     *                form to parse
     */
    public void parseConditionStrings(TextSearchActionForm textSearchActionForm) {
	parseConditionStrings(textSearchActionForm.getAllWords(), textSearchActionForm.getPhrase(),
		textSearchActionForm.getAnyWords(), textSearchActionForm.getExcludedWords());
    }

    public void setAllWords(String allWords) {
	this.allWords = allWords;
	conditionsParsed = false;
    }

    public void setAnyWords(String anyWords) {
	this.anyWords = anyWords;
	conditionsParsed = false;
    }

    public void setExcludedWords(String excludedWords) {
	this.excludedWords = excludedWords;
	conditionsParsed = false;
    }

    public void setPhrase(String phrase) {
	this.phrase = phrase;
	conditionsParsed = false;
    }

    public void setPhraseCondition(String phraseCondition) {
	this.phraseCondition = phraseCondition;
    }

    /**
     * No condition parameters must be set in order to make the condition valid.
     */
    @Override
    protected boolean isValid() {
	return true;
    }

    protected void setAllWordsCondition(List<String> allWordsCondition) {
	this.allWordsCondition = allWordsCondition;
    }

    protected void setAnyWordsCondition(List<String> anyWordsCondition) {
	this.anyWordsCondition = anyWordsCondition;
    }

    protected void setExcludedWordsCondition(List<String> excludedWordsCondition) {
	this.excludedWordsCondition = excludedWordsCondition;
    }
}