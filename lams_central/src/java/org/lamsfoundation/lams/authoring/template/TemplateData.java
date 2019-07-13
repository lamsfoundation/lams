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

package org.lamsfoundation.lams.authoring.template;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.WebUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Data class used as the basis for all template data objects. Contains common routines for data handling
 *
 */
public class TemplateData {

    /* I18N */
    protected Locale locale;
    protected ResourceBundle msgBundle;
    protected ResourceBundle appBundle;
    protected MessageFormat formatter;
    protected List<String> errorMessages; // Errors to be returned. Other error lists may be used temporarily for sorting purposes

    public TemplateData(HttpServletRequest request, String templateCode) {
	// get a new message source & formatter each time we process a template so
	// we pick up any changed text + we have the current users locale.
	// must do this first as local is used by the errorMessages code.
	locale = request.getLocale();
	formatter = TextUtil.getFormatter(locale);
	msgBundle = TextUtil.getBoilerplateBundle(locale, templateCode);
	appBundle = TextUtil.getBoilerplateBundle(locale, null);
	errorMessages = new ArrayList<String>();
    }

    public MessageFormat getFormatter() {
	return formatter;
    }

    public ResourceBundle getBoilerplateBundle() {
	return msgBundle;
    }

    public ResourceBundle getUIBundle() {
	return appBundle;
    }

    /** Gets a message from the template boilerplate file, not the UI file */
    public String getText(String key) {
	return TextUtil.getText(msgBundle, key);
    }

    /** Gets a message from the template boilerplate file, not the UI file */
    public String getText(String key, Object[] parameters) {
	return TextUtil.getText(msgBundle, formatter, key, parameters);
    }

    public List<String> getErrorMessages() {
	return errorMessages;
    }

    protected String getTrimmedString(HttpServletRequest request, String parameterName, boolean fromCKEditor) {
	String string = request.getParameter(parameterName);
	if (string != null) {
	    if (fromCKEditor) {
		if (WebUtil.removeHTMLtags(string).length() > 0) {
		    return string;
		}
	    } else {
		string = string.trim();
		if (string.length() > 0) {
		    return string;
		}
	    }
	}
	return null;
    }

    /** Create an error message from the ApplicationResources file into a particular set of errors  */
    protected void addValidationErrorMessage(String key, Object[] params, List<String> errorList) {
	errorList.add(params != null ? TextUtil.getText(appBundle, formatter, key, params)
		: TextUtil.getText(appBundle, key));
    }

    /**
     * Add all the errors in newErrors to the errorList 
     */
    protected void addValidationErrorMessages(List<String> newErrors, List<String> errorList) {
	if (newErrors != null) {
		errorList.addAll(newErrors);
	}
    }

    /** Create an error message from the ApplicationResources file */
    protected void addValidationErrorMessage(String key, Object[] params) {
	addValidationErrorMessage(key, params, errorMessages);
    }

    /**
     * Add all the errors in newErrors to the errorMessages set.
     *
     * @param newErrors
     *            may be null if there are no errors
     */
    protected void addValidationErrorMessages(List<String> newErrors) {
	addValidationErrorMessages(newErrors, errorMessages);
    }

    protected ArrayNode getForumTopics(HttpServletRequest request, Integer numTopics) {
	ArrayNode forumTopics = JsonNodeFactory.instance.arrayNode();
	for (int i = 1; i < numTopics + 1; i++) {
	    String subject = getTrimmedString(request, "forum" + i + "Subject", false);
	    String body = getTrimmedString(request, "forum" + i + "Body", false);
	    if (subject != null && subject.length() > 0 && body != null && body.length() > 0) {
		ObjectNode topic = JsonNodeFactory.instance.objectNode();
		topic.put("subject", subject);
		topic.put("body", body);
		forumTopics.add(topic);
	    }
	}
	return forumTopics;
    }

    /**
     * Converts the input fields to a series of types [ [title,url], [title,url] ]. Order is important.
     * assumes the number of urls is in "numURL", the titles are urltitle1, urltitle2, etc and the
     * urls are in url1, url2 etc.
     * Typical calls getUrls(request, "numURL", "url", "urltitle"); (e.g. Case) or
     * getUrls(request, "numURL1_", "url1_", "urltitle1_"); if there are multiple sets of urls (e.g. Jigsaw)
     */
    protected List<String[]> getUrls(HttpServletRequest request, String numUrlParameter, String urlParameterPrefix,
	    String urlTitleParameterPrefix) {

	Integer numUrls = WebUtil.readIntParam(request, numUrlParameter);
	List<String[]> returnVal = new ArrayList<String[]>();
	for (int i = 1; i < numUrls + 1; i++) {
	    String url = getTrimmedString(request, urlParameterPrefix + i, false);
	    if (url != null && url.equalsIgnoreCase("http://")) {
		url = null;
	    }
	    String urlTitle = getTrimmedString(request, urlTitleParameterPrefix + i, false);
	    if (url != null) {
		String[] urlTuple = new String[2];
		urlTuple[0] = urlTitle != null ? urlTitle : url;
		urlTuple[1] = url;
		returnVal.add(urlTuple);
	    }
	}
	return returnVal;
    }

    /**
     * Converts the question fields to a list of questions.
     * assumes the number of questions is in "numQuestions", the questions are question1, question2, etc.
     */
    protected List<String> getQuestions(HttpServletRequest request) {

	Integer numQuestions = WebUtil.readIntParam(request, "numQuestions");
	List<String> returnVal = new ArrayList<String>();
	for (int i = 1; i < numQuestions + 1; i++) {
	    String question = getTrimmedString(request, "question" + i, false);
	    if (question != null && question.length() > 0) {
		returnVal.add(question);
	    }
	}
	return returnVal;
    }

}
