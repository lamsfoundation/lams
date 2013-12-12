/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.testharness.learner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.TestUtil;
import org.xml.sax.SAXException;

import com.allaire.wddx.WddxDeserializationException;
import com.meterware.httpunit.Button;
import com.meterware.httpunit.FormControl;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

/**
 * @version
 * 
 *          <p>
 *          <a href="MockLearner.java.html"><i>View Source</i></a>
 *          </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockLearner extends MockUser implements Runnable {

    private static final Logger log = Logger.getLogger(MockLearner.class);

    public static final String DEFAULT_NAME = "Learner";

    private static final String ARBITRARY_TEXT_ALPHABET = "`1234567890-=qwertyuiop[]\\asdfghjkl;'\tzxcvbnm,./ ~!@#$%^&*()_+}{POIUYTREWQASDFGHJKL:\"?><MNBVCXZ";

    private static final String LESSON_ID_PATTERN = "%lsId%";

    private static final String LD_ID_PATTERN = "%ldId%";

    private static final String MESSAGE_VALUE_KEY = "messageValue";

    private static final String LD_ID_KEY = "learningDesignID";

    private static final String SWF_URL_START_FLAG = "<param name=\"movie\" value=\"";

    private static final char SWF_URL_END_FLAG = '"';

    private static final String NEXT_URL_START_FLAG = "var url = \"";

    private static final char NEXT_URL_END_FLAG = '"';

    private static final String OPTIONAL_ACTIVITY_FLAG = "selectActivity";

    private static final String ACTIVITY_FINISHED_FLAG = "The next task is loading.";
    private static final String LESSON_FINISHED_FLAG = "LessonComplete.do";

    private static final String FORUM_FINISH_SUBSTRING = "lafrum11/learning/finish.do";
    private static final String FORUM_VIEW_TOPIC_SUBSTRING = "lafrum11/learning/viewTopic.do";
    private static final String FORUM_REPLY_SUBSTRING = "lafrum11/learning/newReplyTopic.do";
    private static final String FORUM_VIEW_FORUM_SUBSTRING = "lafrum11/learning/viewForum.do";

    private static final String LEADER_FINISH_SUBSTRING = "/lams/tool/lalead11/learning.do";
    private static final Pattern LEADER_BECOME_PATTERN = Pattern.compile("dispatch=becomeLeader&toolSessionID=\\d+");

    private static final String SCRATCHIE_FINISH_SUBSTRING = "/lams/tool/lascrt11/learning/finish.do";
    private static final String SCRATCHIE_RESULTS_SUBSTRING = "/lams/tool/lascrt11/learning/showResults.do";
    private static final Pattern SCRATCHIE_SCRATCH_PATTERN = Pattern.compile("scratchItem\\((\\d+), (\\d+)\\)");
    private static final String SCRATCHIE_FINISH_AVAILABLE = "return finish()";

    private static final String KNOCK_GATE_SUBSTRING = "/lams/learning/gate.do?method=knockGate";

    private static final Pattern ASSESSMENT_FINISH_PATTERN = Pattern
	    .compile("'(/lams/tool/laasse10/learning/finish\\.do\\?.*)'");

    private static final Pattern SESSION_MAP_ID_PATTERN = Pattern.compile("sessionMapID=(.+)\\&");

    private static final String LOAD_TOOL_ACTIVITY_SUBSTRING = "Load Tool Activity";

    private static int joinLessonUserCount = 0;

    private static int topJoinLessonUserCount = 0;

    private boolean finished = false;

    /**
     * MockLearner Constructor
     */
    public MockLearner(LearnerTest test, String username, String password, String userId) {
	super(test, username, password, userId);

    }

    @Override
    public void run() {
	study();
    }

    public void study() {
	MockLearner.log.info(username + " is studying the lesson...");
	try {
	    login();
	    LearnerTest learnerTest = (LearnerTest) test;
	    String lsId = test.getTestSuite().getMonitorTest().getLsId();
	    String ldId = getLesson(learnerTest.getLessonURL, lsId);
	    getLearningDesign(learnerTest.getLearningDesignURL, ldId);
	    joinLesson(learnerTest.joinLessonURL, lsId);
	    getFlashProgessData(learnerTest.getFlashProgressDataURL, lsId);
	    progressThroughActivities(learnerTest.lessonEntryURL, lsId);
	    finished = true;
	    MockLearner.log.info(username + " finished the lesson");
	} catch (TestHarnessException e) {
	    MockLearner.log.info(username + " aborted on the lesson");
	    MockLearner.log.error(e.getMessage(), e);
	    // We don't propagate the TestHarnessException so that other
	    // learners are not affected.
	    // other RuntimeException will still get propagated so that the
	    // application will halt, but that's expected, since those exceptions
	    // should be serious system errors
	} catch (WddxDeserializationException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	} finally {
	    ((LearnerTest) test).allDoneSignal.countDown();
	}
    }

    /**
     * Steps:
     * <ol>
     * <li>fetch entry url</li>
     * <li>get passon.swf url and the next activity url</li>
     * <li>fetch the next activity url and get activity page with form</li>
     * <li>fill the form and submit it
     * <li>
     * <li>repeat step 2-4 until lesson completed or error happened</li>
     * </ol>
     * 
     * Note: httpunit will automatically redirect url if there is a redirect http header in response. In this case, the
     * redirect url won't be recorded in access log or testharness log.
     * 
     * @param lessonEntryURL
     * @param lsId
     * @return void
     * @throws IOException
     * @throws SAXException
     */
    private void progressThroughActivities(String lessonEntryURL, String lsId) throws SAXException, IOException {
	delay();
	WebResponse resp = (WebResponse) new Call(wc, test, username + " enters lesson", lessonEntryURL.replace(
		MockLearner.LESSON_ID_PATTERN, lsId)).execute();
	String nextURL = parseOutNextURL(resp);
	boolean lessonFinished = false;
	while (!lessonFinished) {
	    if (nextURL != null) {
		resp = takeActivity(nextURL);
		nextURL = parseOutNextURL(resp);
	    } else {
		if (isOptionalActivity(resp)) {
		    resp = handleActivity(resp);
		    nextURL = parseOutNextURL(resp);
		} else {
		    lessonFinished = true;
		}
	    }

	}
    }

    private boolean isOptionalActivity(WebResponse resp) throws IOException {
	return resp.getText().indexOf(MockLearner.OPTIONAL_ACTIVITY_FLAG) != -1;
    }

    /**
     * Retrieve the toolURL and play with it
     * 
     * @param toolURL
     * @return WebResponse
     */
    private WebResponse takeActivity(String toolURL) {
	try {
	    WebResponse resp = (WebResponse) new Call(wc, test, "", toolURL).execute();
	    delay();
	    return handleActivity(resp);
	} catch (Exception e) {
	    throw new TestHarnessException(e.getMessage(), e);
	}
    }

    private WebResponse handleActivity(WebResponse resp) throws SAXException, IOException {
	if (resp.getFrameNames().length == 2) {
	    return handleParallelActivity(resp);
	}

	if (resp.getText().indexOf("/lams/tool/laqa11/learning.do") != -1) {
	    MockLearner.log.debug(resp.getText());
	}

	WebResponse nextResp;
	WebForm[] forms = resp.getForms();
	if ((forms != null) && (forms.length > 0)) {
	    MockLearner.log.debug("There " + (forms.length == 1 ? "is " : "are ") + forms.length
		    + (forms.length == 1 ? " form in the page " : " forms in the page"));
	    nextResp = handlePageWithForms(forms);
	} else {
	    nextResp = handlePageWithoutForms(resp);
	}
	if (isActivityFinished(nextResp)) {
	    return nextResp;
	} else {
	    return handleActivity(nextResp);
	}

    }

    private boolean isActivityFinished(WebResponse resp) throws IOException {
	return (resp != null)
		&& ((resp.getText().indexOf(MockLearner.ACTIVITY_FINISHED_FLAG) != -1) || (resp.getText().indexOf(
			MockLearner.LESSON_FINISHED_FLAG) != -1));
    }

    private WebResponse handlePageWithoutForms(WebResponse resp) throws SAXException, IOException {

	String asText = resp.getText();

	// Is this a Forum activity?
	if (asText.indexOf(MockLearner.FORUM_FINISH_SUBSTRING) != -1) {
	    return handleForum(resp);
	} else if (asText.indexOf(MockLearner.LEADER_FINISH_SUBSTRING) != -1) {
	    return handleLeader(resp);
	} else if (asText.indexOf(MockLearner.SCRATCHIE_RESULTS_SUBSTRING) != -1) {
	    return handleScratchie(resp);
	} else if (asText.indexOf(MockLearner.LOAD_TOOL_ACTIVITY_SUBSTRING) != -1) {
	    return handleLoadToolActivity(asText);
	} else {
	    return findAnAbsoluteURLOnPage(asText);
	}
    }

    private WebResponse handleLoadToolActivity(String asText) throws SAXException, IOException {

	String toolURL = TestUtil.extractString(asText, MockLearner.NEXT_URL_START_FLAG, MockLearner.NEXT_URL_END_FLAG);
	return (WebResponse) new Call(wc, test, "Redirect to tool page", toolURL).execute();
    }

    private WebResponse findAnAbsoluteURLOnPage(String respAsText) throws SAXException, IOException {
	// Probably safest to get the last http address on the page, make sure we don't accidently
	// go to http://www.w3c.org/
	int index = respAsText.lastIndexOf("\"http");
	while (index != -1) {
	    int indexEnd = respAsText.indexOf("\"", index + 1);
	    if (indexEnd != -1) {
		String httpString = respAsText.substring(index + 1, indexEnd);
		if ((httpString.indexOf("www.w3.org") == -1) && !httpString.endsWith(".js")
			&& !httpString.endsWith(".css")) {
		    MockLearner.log.debug("Forwarding user " + username + " to discovered link " + httpString);
		    return (WebResponse) new Call(wc, test, "", httpString).execute();
		}
	    }
	    index = index > 0 ? respAsText.lastIndexOf("\"http", index - 1) : -1;
	}
	throw new TestHarnessException("Unable to find a link to go to on page" + respAsText);
    }

    private WebResponse handleForum(WebResponse resp) throws SAXException, IOException {

	WebResponse replyResponse = null;

	String replyURL = findURLInAHREF(resp, MockLearner.FORUM_VIEW_TOPIC_SUBSTRING);
	if (replyURL != null) {
	    MockLearner.log.debug("Accessing the forum view topic screen using " + replyURL);
	    replyResponse = handleForumReply(replyURL);
	}

	if (replyResponse == null) {
	    throw new TestHarnessException("No links found on the main forum page, or unable to do reply. "
		    + resp.getText());
	}

	String finishURL = findURLInLocationHref(replyResponse, MockLearner.FORUM_FINISH_SUBSTRING);
	if (finishURL != null) {
	    MockLearner.log.debug("Ending forum using url " + finishURL);
	    return (WebResponse) new Call(wc, test, "Finish Forum", finishURL).execute();
	}

	throw new TestHarnessException("Unable to finish the forum. No finish link found. " + replyResponse.getText());
    }

    private WebResponse handleLeader(WebResponse resp) throws SAXException, IOException {
	String asText = resp.getText();
	Matcher m = MockLearner.LEADER_BECOME_PATTERN.matcher(asText);
	if (m.find()) {
	    String becomeLeaderQueryOptions = m.group();
	    String url = "/lams/tool/lalead11/learning.do?" + becomeLeaderQueryOptions;
	    MockLearner.log.debug("Becoming a leader using link: " + url);
	    new Call(wc, test, "Become Leader", url).execute();
	}

	String finishURL = findURLInLocationHref(resp, MockLearner.LEADER_FINISH_SUBSTRING);
	if (finishURL != null) {
	    MockLearner.log.debug("Ending leader using url " + finishURL);
	    return (WebResponse) new Call(wc, test, "Finish Leader", finishURL).execute();
	}

	throw new TestHarnessException("Unable to finish the leader, no finish link found. " + asText);
    }

    private WebResponse handleScratchie(WebResponse resp) throws SAXException, IOException {
	String asText = resp.getText();
	Matcher m = MockLearner.SCRATCHIE_SCRATCH_PATTERN.matcher(asText);
	Map<Long, List<Long>> uids = new TreeMap<Long, List<Long>>();
	while (m.find()) {
	    Long questionID = Long.valueOf(m.group(1));
	    List<Long> answerUids = uids.get(questionID);
	    if (answerUids == null) {
		answerUids = new ArrayList<Long>();
		uids.put(questionID, answerUids);
	    }
	    answerUids.add(Long.valueOf(m.group(2)));
	}

	String scratchURL = "/lams/tool/lascrt11/learning/isAnswerCorrect.do?answerUid=";
	m = MockLearner.SESSION_MAP_ID_PATTERN.matcher(asText);

	String sessionMapID = null;
	String recordScratchedURL = null;
	String refreshQuestionsURL = null;
	if (m.find()) {
	    sessionMapID = m.group(1);
	    recordScratchedURL = "/lams/tool/lascrt11/learning/recordItemScratched.do?sessionMapID=" + sessionMapID
		    + "&answerUid=";
	    refreshQuestionsURL = "/lams/tool/lascrt11/learning/refreshQuestionList.do?sessionMapID=" + sessionMapID;
	} else {
	    MockLearner.log.warn("Session map ID was not found in Scratchie Tool");
	}
	Random generator = new Random();

	while (!uids.isEmpty()) {
	    Long questionID = uids.keySet().iterator().next();
	    List<Long> answerUids = uids.get(questionID);

	    int index = generator.nextInt(answerUids.size());
	    Long answerUid = answerUids.get(index);
	    answerUids.remove(index);
	    MockLearner.log.debug("Scratching answer UID " + answerUid + " for question " + questionID);
	    WebResponse scratchResponse = (WebResponse) new Call(wc, test, "Scratch response", scratchURL + answerUid)
		    .execute();
	    boolean answerCorrect = scratchResponse.getText().indexOf("true") != -1;
	    MockLearner.log.debug("Scratched answer UID " + answerUid + " for question " + questionID + " and it was "
		    + (answerCorrect ? "correct" : "incorrect"));

	    if (answerCorrect) {
		uids.remove(questionID);
	    }

	    if (recordScratchedURL != null) {
		MockLearner.log.debug("Recording scratched answer UID " + answerUid);
		new Call(wc, test, "Record answer scratched", recordScratchedURL + answerUid).execute();
	    }

	    if (refreshQuestionsURL != null) {
		MockLearner.log.debug("Refreshing scratchie question list");
		new Call(wc, test, "Refresh question list", refreshQuestionsURL).execute();
	    }

	    if (!answerCorrect && !answerUids.isEmpty()) {
		try {
		    Thread.sleep(3000);
		} catch (InterruptedException e) {
		    MockLearner.log.warn("Waiting to scratch was interuppted");
		}
	    }
	}

	while (refreshQuestionsURL != null && !asText.contains(MockLearner.SCRATCHIE_FINISH_AVAILABLE)) {
	    MockLearner.log.debug("Waiting for leader to finish scratchie");
	    delay();
	    String url = resp.getURL().toString() + "&reqId=" + System.currentTimeMillis();
	    WebResponse questionRefreshResp = (WebResponse) new Call(wc, test, "Scratchie refresh", refreshQuestionsURL).execute();
	    asText = questionRefreshResp.getText();
	}

	String resultsURL = findURLInLocationHref(resp, MockLearner.SCRATCHIE_RESULTS_SUBSTRING);
	if (resultsURL != null) {
	    MockLearner.log.debug("Showing results of scratchie using url " + resultsURL);
	    resp = (WebResponse) new Call(wc, test, "Scratchie show results", resultsURL).execute();
	}

	String finishURL = findURLInLocationHref(resp, MockLearner.SCRATCHIE_FINISH_SUBSTRING);
	if (finishURL != null) {
	    MockLearner.log.debug("Ending scratchie using url " + finishURL);
	    return (WebResponse) new Call(wc, test, "Finish scratchie", finishURL).execute();
	}

	throw new TestHarnessException("Unable to finish the scratchie, no finish link found. " + asText);
    }

    /**
     * @param link
     * @return
     * @throws SAXException
     * @throws IOException
     */
    private WebResponse handleForumReply(String url) throws SAXException, IOException {
	WebResponse resp = (WebResponse) new Call(wc, test, "View Topic Forum", url).execute();
	String replyURL = findURLInAHREF(resp, MockLearner.FORUM_REPLY_SUBSTRING);
	if (replyURL != null) {
	    resp = (WebResponse) new Call(wc, test, "Reply Topic Forum", replyURL).execute();
	    WebForm[] forms = resp.getForms();
	    if ((forms != null) && (forms.length > 0)) {
		resp = handlePageWithForms(forms);
	    } else {
		throw new TestHarnessException("No form found on the reply topic page - unable to do reply. "
			+ resp.getText());
	    }
	} else {
	    throw new TestHarnessException("No reply URL found - unable to do reply. " + resp.getText());
	    // log.info("No reply URL found - unable to do reply. "+resp.getText());
	}

	// now we are back on the topic page, so go back to the main forum page.
	String returnToForumURL = findURLInAHREF(resp, MockLearner.FORUM_VIEW_FORUM_SUBSTRING);
	if (returnToForumURL != null) {
	    MockLearner.log.debug("Returning to forum page " + returnToForumURL);
	    return (WebResponse) new Call(wc, test, "Return to Forum", returnToForumURL).execute();
	}
	throw new TestHarnessException("No button back to forum page found - stuck while doing reply. "
		+ resp.getText());
    }

    private String findURLInAHREF(WebResponse resp, String linkSubstring) throws SAXException {
	WebLink[] links = resp.getLinks();
	if (links != null) {
	    for (WebLink link : links) {
		MockLearner.log.debug("Checking link " + link.getURLString());
		if (link.getURLString().indexOf(linkSubstring) != -1) {
		    return link.getURLString();
		}
	    }
	}
	return null;
    }

    private String findURLInLocationHref(WebResponse resp, String linkSubstring) throws SAXException, IOException {
	String respAsText = resp.getText();
	String lowercaseRespAsText = respAsText.toLowerCase();
	int stringLength = lowercaseRespAsText.length();

	int index = lowercaseRespAsText.indexOf("location.href");
	while (index != -1) {
	    index++;
	    while ((index < stringLength)
		    && !((lowercaseRespAsText.charAt(index) == '\'') || (lowercaseRespAsText.charAt(index) == '\"'))) {
		index++;
	    }
	    if (index < (stringLength - 1)) {
		char quote = lowercaseRespAsText.charAt(index);
		int indexEnd = lowercaseRespAsText.indexOf(quote, index + 1);
		String httpString = respAsText.substring(index + 1, indexEnd);
		MockLearner.log.debug("Discovered link " + httpString);
		if (httpString.indexOf(linkSubstring) != -1) {
		    MockLearner.log.debug("Matched to " + linkSubstring);
		    return httpString;
		}
	    }
	    MockLearner.log.debug("Index was " + index);
	    index = (index < stringLength) && (index > 0) ? lowercaseRespAsText.indexOf("location.href", index + 1)
		    : -1;
	    MockLearner.log.debug("New index is " + index);
	}
	return null;
    }

    private WebResponse handleParallelActivity(WebResponse resp) {
	// TODO implement me
	throw new TestHarnessException("Unable to handle parallel activities.");
    }

    private WebResponse handlePageWithForms(WebForm[] forms) throws IOException, SAXException {
	int index = 0;
	WebForm form = forms[index];
	while ((form.getAction() == null) || (form.getAction().trim().length() == 0)) {
	    index++;
	    if (index >= forms.length) {
		throw new TestHarnessException(username + " don't know how to finish the activity now");
	    }
	    form = forms[index];
	}

	WebResponse resp = null;
	while (form.getAction().startsWith(MockLearner.KNOCK_GATE_SUBSTRING)) {
	    delay();
	    MockLearner.log.debug("Knocking gate");
	    resp = (WebResponse) new Call(wc, test, "Knock Gate", form).execute();
	    if (resp.getText().indexOf(MockLearner.KNOCK_GATE_SUBSTRING) == -1) {
		return resp;
	    }
	}

	resp = (WebResponse) new Call(wc, test, "", fillFormArbitrarily(form)).execute();

	// check if it is assessment activity
	String asText = resp.getText();
	Matcher m = MockLearner.ASSESSMENT_FINISH_PATTERN.matcher(asText);
	if (m.find()) {
	    resp = (WebResponse) new Call(wc, test, "", m.group(1)).execute();
	}

	return resp;
    }

    private String getLesson(String getLessonURL, String lsId) throws WddxDeserializationException, IOException {
	delay();
	String url = getLessonURL.replace(MockLearner.LESSON_ID_PATTERN, lsId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get lesson", url).execute();
	Hashtable hashtable = (Hashtable) TestUtil.deserialize(resp.getText());
	hashtable = (Hashtable) hashtable.get(MockLearner.MESSAGE_VALUE_KEY);
	return new Integer(((Double) hashtable.get(MockLearner.LD_ID_KEY)).intValue()).toString();
    }

    private void getLearningDesign(String getLearningDesignURL, String ldId) {
	delay();
	String url = getLearningDesignURL.replace(MockLearner.LD_ID_PATTERN, ldId);
	new Call(wc, test, username + " get learning design", url).execute();
    }

    private void joinLesson(String joinLessonURL, String lsId) {
	delay();
	String url = joinLessonURL.replace(MockLearner.LESSON_ID_PATTERN, lsId);
	MockLearner.joinLessonUserCount++;
	if (MockLearner.joinLessonUserCount > MockLearner.topJoinLessonUserCount) {
	    MockLearner.topJoinLessonUserCount = MockLearner.joinLessonUserCount;
	}
	MockLearner.log.info("Users joining lesson: " + MockLearner.joinLessonUserCount + ". Top: "
		+ MockLearner.topJoinLessonUserCount);
	new Call(wc, test, username + " join lesson", url).execute();
	MockLearner.joinLessonUserCount--;
    }

    private void getFlashProgessData(String getFlashProgressDataURL, String lsId) {
	delay();
	String url = getFlashProgressDataURL.replace(MockLearner.LESSON_ID_PATTERN, lsId);
	new Call(wc, test, username + " get flash progress data", url).execute();
    }

    private String parseOutNextURL(WebResponse resp) throws SAXException, IOException {
	String text = resp.getText();
	String toolURL = TestUtil.extractString(text, MockLearner.NEXT_URL_START_FLAG, MockLearner.NEXT_URL_END_FLAG);

	if ((toolURL != null) && !toolURL.startsWith("/")) {
	    toolURL = '/' + toolURL;
	}

	MockLearner.log.debug("toolURL:" + toolURL);
	return toolURL;
    }

    private WebForm fillFormArbitrarily(WebForm form) throws IOException, SAXException {
	String[] params = form.getParameterNames();
	if ((params != null) && (params.length > 0)) {
	    for (String param : params) {
		if (!form.isDisabledParameter(param) && !form.isHiddenParameter(param)
			&& !form.isReadOnlyParameter(param)) {
		    if (form.isTextParameter(param)) {
			String text = composeArbitraryText();
			form.setParameter(param, text);
			MockLearner.log.debug(username + " input " + text + " for form field " + param);
		    } else if (form.isFileParameter(param)) {
			File file = selectArbitraryFile(((LearnerTest) test).getFilesToUpload());
			form.setParameter(param, file);
			MockLearner.log.debug(username + " uploaded file " + file.getName() + " for form field "
				+ param);
		    } else if (form.isMultiValuedParameter(param)) {
			String[] values = chooseArbitraryValues(form.getOptionValues(param));
			form.setParameter(param, values);
			MockLearner.log.debug(username + " set " + values.length + " value(s) for form field " + param);
			MockLearner.log.debug(values);
		    } else {
			MockLearner.log.debug(param + " may be a radio button. Current value is "
				+ form.getParameterValue(param));
			if (form.getParameterValue(param) == null) {
			    String[] candidateValues = form.getOptionValues(param);
			    if ((candidateValues != null) && (candidateValues.length > 0)) {
				String value = candidateValues[TestUtil.generateRandomIndex(candidateValues.length)];
				MockLearner.log.debug("Setting param to " + value);
				form.setParameter(param, value);
			    }
			}
		    }
		} else {
		    MockLearner.log.debug("disabled or hidden or readonly parameter " + param);
		}
	    }
	}

	Map<String, List<Button>> buttonGroups = groupButtonsByName(form.getButtons(), FormControl.RADIO_BUTTON_TYPE);
	for (Map.Entry<String, List<Button>> entry : buttonGroups.entrySet()) {
	    entry.getValue().get(TestUtil.generateRandomIndex(entry.getValue().size())).click();
	    MockLearner.log.debug(username + " clicked a radio button " + entry.getKey());
	}
	return form;
    }

    private String processButtons(Button[] buttons) {
	StringBuffer buf = new StringBuffer(100);
	for (Button button : buttons) {
	    buf.append(button.toString());
	    buf.append("; ");
	}
	return buf.toString();
    }

    private Map<String, List<Button>> groupButtonsByName(Button[] btns, String buttonType) {
	MockLearner.log.debug(btns.length);
	Map<String, List<Button>> buttonGroups = new HashMap<String, List<Button>>();
	if (btns != null) {
	    for (Button btn : btns) {
		if (buttonType.equals(btn.getType())) {
		    String name = btn.getName();
		    MockLearner.log.debug("Got " + buttonType + " " + name + " and its value is " + btn.getValue());
		    if (!buttonGroups.containsKey(name)) {
			buttonGroups.get(name).add(btn);
		    } else {
			List<Button> buttons = new ArrayList<Button>();
			buttons.add(btn);
			buttonGroups.put(name, buttons);
		    }
		}
	    }
	}
	return buttonGroups;
    }

    private File selectArbitraryFile(String[] filesToUpload) {
	return new File(filesToUpload[TestUtil.generateRandomIndex(filesToUpload.length)]);
    }

    private String[] chooseArbitraryValues(String[] values) {
	int length = 1; // + TestUtil.generateRandomIndex(values.length);
	String[] answers = new String[length];
	for (int i = 0; i < length; i++) {
	    answers[i] = values[TestUtil.generateRandomIndex(values.length)];
	}
	return answers;
    }

    private String composeArbitraryText() {
	int length = 1 + TestUtil.generateRandomIndex(MockLearner.ARBITRARY_TEXT_ALPHABET.length());
	StringBuilder text = new StringBuilder(length);
	for (int i = 0; i < length; i++) {
	    text.append(MockLearner.ARBITRARY_TEXT_ALPHABET.charAt(TestUtil
		    .generateRandomIndex(MockLearner.ARBITRARY_TEXT_ALPHABET.length())));
	}
	return text.toString();
    }

    public final boolean isFinished() {
	return finished;
    }

}
