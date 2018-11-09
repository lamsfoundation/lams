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

package org.lamsfoundation.lams.learningdesign;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.ToolOutput;

/**
 * Condition that is based on text search. Several properties set what needs to be found in a tool output. Based on the
 * result of the text scan, the condition is safisfied or not.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "lams_text_search_condition")
public class TextSearchCondition extends BranchCondition implements Cloneable {

    // ---- persistent fields -------
    /**
     * All the words from this string should be found in the tool output.
     */
    @Column(name = "text_search_all_words")
    protected String allWords;
    /**
     * The whole phrase from this string should be found in the tool output.
     */
    @Column(name = "text_search_phrase")
    protected String phrase;
    /**
     * At least one of the words from this string should be found in the tool output.
     */
    @Column(name = "text_search_any_words")
    protected String anyWords;
    /**
     * None of the words from this string should be found in the tool output.
     */
    @Column(name = "text_search_excluded_words")
    protected String excludedWords;

    // ---- non-persistent fields ----------
    /**
     * Regular expression that divides a string into single words with optional punctuation. For example "lams" will be
     * considered a word according to this delimiter, but ":lams," will not. The meaning is "one or more non-word
     * characters or beginnings of a line or ends of a line".
     */
    protected static final String NON_WORD_DELIMITER_REGEX = "(?:\\W|$|^)+";
    /**
     * Regular expression that divides a string into single words without optional punctuation. For example "lams" will
     * be considered a word according to this delimiter as well as ":lams,". The meaning is "one or more whitespace
     * characters or beginnings of a line or ends of a line".
     */
    protected static final String WHITESPACE_DELIMITER_REGEX = "(?:\\s|$|^)+";
    /**
     * Integer that sets flags for regex pattern matching.
     */
    protected static final int PATTERN_MATCHING_OPTIONS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
	    | Pattern.MULTILINE;

    /**
     * Were the strings provided by user parsed into practical collections of words.
     */
    @Transient
    protected boolean conditionsParsed = false;
    /**
     * Property {@link #allWords} divided into words.
     */
    @Transient
    protected List<String> allWordsCondition = new ArrayList<>();
    /**
     * Property {@link #phrase} divided into words. Although we are looking for the whole phrase, spaces between words
     * should be divided into something more regex'y.
     */
    @Transient
    protected List<String> phraseCondition;
    /**
     * Property {@link #anyWords} divided into words.
     */
    @Transient
    protected List<String> anyWordsCondition = new ArrayList<>();
    /**
     * Property {@link #excludedWords} divided into words.
     */
    @Transient
    protected List<String> excludedWordsCondition = new ArrayList<>();

    public TextSearchCondition() {
	super();
    }

    /**
     * Creates the condition based on a DTO.
     *
     * @param conditionDTO
     */
    public TextSearchCondition(TextSearchConditionDTO conditionDTO) {
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
    public TextSearchCondition clone(int uiidOffset) {
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

    public List<String> getPhraseCondition() {
	return phraseCondition;
    }

    @Override
    public boolean isMet(ToolOutput output) {
	return output != null && matches(output.getValue().getString());
    }

    /**
     * Checks if the given text contain the words provided in the condition parameters (case insensitive).
     *
     * @param text
     *            string to check
     * @return <code>true</code> if text satisfies this condition
     */
    public boolean matches(String text) {

	// we parse the condition strings to more useful arrays of words
	if (!conditionsParsed) {
	    parseConditionStrings();
	}
	if (text == null) {
	    return getAllWordsCondition() == null && getAnyWordsCondition() == null && getPhraseCondition() == null;
	}
	Pattern regexPattern = null;
	StringBuilder stringPattern = null;
	Matcher matcher = null;
	// For each condition type we build a regular expression and try to find it in the text.
	if (getExcludedWordsCondition() != null) {
	    stringPattern = new StringBuilder();
	    for (String excludedWord : getExcludedWordsCondition()) {
		stringPattern.append("(?:").append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
			.append(Pattern.quote(excludedWord)).append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
			.append(")|");
	    }
	    stringPattern.deleteCharAt(stringPattern.length() - 1);
	    regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
	    matcher = regexPattern.matcher(text);
	    if (matcher.find()) {
		return false;
	    }
	}
	if (getAnyWordsCondition() != null) {
	    stringPattern = new StringBuilder();

	    for (String word : getAnyWordsCondition()) {
		stringPattern.append("(?:").append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
			.append(Pattern.quote(word)).append(TextSearchCondition.NON_WORD_DELIMITER_REGEX).append(")|");
	    }
	    stringPattern.deleteCharAt(stringPattern.length() - 1);
	    regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
	    matcher = regexPattern.matcher(text);
	    if (!matcher.find()) {
		return false;
	    }
	}
	if (getPhraseCondition() != null) {
	    stringPattern = new StringBuilder(TextSearchCondition.WHITESPACE_DELIMITER_REGEX);
	    for (String word : getPhraseCondition()) {
		stringPattern.append(Pattern.quote(word)).append(TextSearchCondition.WHITESPACE_DELIMITER_REGEX);
	    }
	    regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
	    matcher = regexPattern.matcher(text);
	    if (!matcher.find()) {
		return false;
	    }
	}

	if (getAllWordsCondition() != null) {
	    for (String word : getAllWordsCondition()) {
		stringPattern = new StringBuilder(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
			.append(Pattern.quote(word)).append(TextSearchCondition.NON_WORD_DELIMITER_REGEX);
		regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
		matcher = regexPattern.matcher(text);
		if (!matcher.find()) {
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
	setAllWordsCondition(splitSentence(allWordsString, TextSearchCondition.NON_WORD_DELIMITER_REGEX));
	setPhraseCondition(splitSentence(phraseString, TextSearchCondition.WHITESPACE_DELIMITER_REGEX));
	setAnyWordsCondition(splitSentence(anyWordsString, TextSearchCondition.NON_WORD_DELIMITER_REGEX));
	setExcludedWordsCondition(splitSentence(excludedWordsString, TextSearchCondition.NON_WORD_DELIMITER_REGEX));
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

    public void setPhraseCondition(List<String> phraseCondition) {
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

    /**
     * Splits the given string into words using configured delimiter.
     *
     * @param sentence
     *            string to split
     * @return list of non-empty words
     */
    private List<String> splitSentence(String sentence, String regex) {
	List<String> list = null;
	if (!StringUtils.isEmpty(sentence)) {
	    String[] splitted = sentence.trim().split(regex);
	    list = new ArrayList<>(splitted.length);
	    // we don't need empty words
	    for (String word : splitted) {
		if (!StringUtils.isEmpty(word)) {
		    list.add(word);
		}
	    }
	    if (list.isEmpty()) {
		list = null;
	    }
	}
	return list;
    }

    @Override
    public TextSearchConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new TextSearchConditionDTO(this, toolActivityUIID);
    }
}