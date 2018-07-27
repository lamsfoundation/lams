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


package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.TextSearchCondition;

/**
 * Stores additional information from TextSearchCondition that should be serialized. All tools that have their own
 * TextSearchConditions must also have TextSearchConditionDTOs, otherwise learning design will not be properly exported.
 *
 * @author Marcin Cieslak
 *
 */
public class TextSearchConditionDTO extends BranchConditionDTO {
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

    public TextSearchConditionDTO(TextSearchCondition condition, Integer toolActivityUIID) {
	super(condition, toolActivityUIID);
	allWords = condition.getAllWords();
	phrase = condition.getPhrase();
	anyWords = condition.getAnyWords();
	excludedWords = condition.getExcludedWords();
    }

    public String getAllWords() {
	return allWords;
    }

    public void setAllWords(String allWords) {
	this.allWords = allWords;
    }

    public String getPhrase() {
	return phrase;
    }

    public void setPhrase(String phrase) {
	this.phrase = phrase;
    }

    public String getAnyWords() {
	return anyWords;
    }

    public void setAnyWords(String anyWords) {
	this.anyWords = anyWords;
    }

    public String getExcludedWords() {
	return excludedWords;
    }

    public void setExcludedWords(String excludedWords) {
	this.excludedWords = excludedWords;
    }

    @Override
    public TextSearchCondition getCondition() {
	return new TextSearchCondition(this);
    }
}