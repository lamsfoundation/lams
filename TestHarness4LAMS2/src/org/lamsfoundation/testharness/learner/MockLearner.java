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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.lamsfoundation.testharness.admin.MockAdmin;
import org.xml.sax.SAXException;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.FormControl;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class MockLearner extends MockUser implements Runnable {

    private static final Logger log = Logger.getLogger(MockLearner.class);

    public static final String DEFAULT_NAME = "Learner";
    private static final String ARBITRARY_TEXT_ALPHABET = "`1234567890-=qwertyuiop[]\\asdfghjkl;'\tzxcvbnm,./ ~!@#$%^&*()_+}{POIUYTREWQASDFGHJKL:\"?><MNBVCXZ";
    private static final String LESSON_ID_PATTERN = "%lsId%";

    private static final Pattern NEXT_URL_PATTERN = Pattern.compile("var url = \"(.*)\"");
    private static final String OPTIONAL_ACTIVITY_FLAG = "selectActivity";
    private static final String ACTIVITY_FINISHED_FLAG = "The next task is loading.";
    private static final String LESSON_FINISHED_FLAG = "LessonComplete.do";
    private static final String LOAD_TOOL_ACTIVITY_FLAG = "Load Tool Activity";
    private static final Pattern SESSION_MAP_ID_PATTERN = Pattern.compile("sessionMapID=(.+)\\&");
    private static final Pattern TOOL_SESSION_ID_PATTERN = Pattern.compile("var TOOL_SESSION_ID = '(\\d+)'");
    private static final String FINISH_SUBSTRING = "finish.do";

    private static final String FORUM_FINISH_SUBSTRING = "lafrum11/learning/finish.do";
    private static final String FORUM_VIEW_TOPIC_SUBSTRING = "lafrum11/learning/viewTopic.do";
    private static final String FORUM_REPLY_SUBSTRING = "lafrum11/learning/newReplyTopic.do";
    private static final String FORUM_VIEW_FORUM_SUBSTRING = "lafrum11/learning/viewForum.do";

    private static final String LEADER_FINISH_SUBSTRING = "/lams/tool/lalead11/learning.do";
    private static final Pattern LEADER_BECOME_PATTERN = Pattern.compile("dispatch=becomeLeader&toolSessionID=\\d+");

    private static final String SCRATCHIE_FINISH_SUBSTRING = "/lams/tool/lascrt11/learning/finish.do";
    private static final String SCRATCHIE_RESULTS_SUBSTRING = "/lams/tool/lascrt11/learning/showResults.do";
    private static final String SCRATCHIE_REFLECTION_SUBSTRING = "/lams/tool/lascrt11/learning/newReflection.do";
    private static final Pattern SCRATCHIE_SCRATCH_PATTERN = Pattern.compile("scratchItem\\((\\d+), (\\d+)\\)");
    private static final String SCRATCHIE_FINISH_AVAILABLE = "return finish()";
    private static final String SCRATCHIE_REFLECTION_AVAILABLE = "return continueReflect()";
    private static final String SCRATCHIE_IS_LEADER_SUBSTRING = "isUserLeader=true";

    private static final String CHAT_FINISH_SUBSTRING = "/lams/tool/lachat11/learning.do";
    private static final int CHAT_REPLIES = 3;

    private static final String KNOCK_GATE_SUBSTRING = "/lams/learning/gate.do?method=knockGate";

    private static final Pattern ASSESSMENT_FINISH_PATTERN = Pattern
	    .compile("'(/lams/tool/laasse10/learning/finish\\.do\\?.*)'");

    private static final Pattern IMAGE_GALLERY_FINISH_PATTERN = Pattern
	    .compile("'(/lams/tool/laimag10/learning/finish\\.do\\?.*)'");

    private static final String QA_TOOL_SUBSTRING = "laqa11";

    private static final String VIDEORECORDER_TOOL_STRING = "lavidr10";

    private static final String TASK_FINISH_SUBSTRING = "/lams/tool/latask10/learning/finish.do";

    private static final String VOTE_VIEW_ALL_RESULTS_BUTTON_STRING = "submitMethod('viewAllResults')";
    private static final String VOTE_LEARNER_FINISHED_BUTTON_STRING = "submitMethod('learnerFinished')";

    private static final String WIKI_EDIT_BUTTON_STRING = "doEditOrAdd('editPage')";

    private static final Pattern SHARE_RESOURCES_REDIRECT_PATTERN = Pattern
	    .compile("\"(/lams/tool/larsrc11/pages/learning/learning\\.jsp.*)\"");
    private static final Pattern SHARE_RESOURCES_VIEW_ITEM_URL_PATTERH = Pattern
	    .compile("\"(/lams/tool/larsrc11/reviewItem\\.do.*)\"");
    private static final Pattern SHARE_RESOURCES_VIEW_ITEM_PATTERN = Pattern.compile("viewItem\\((\\d+)\\)");

    private static final Pattern SUBMIT_FILES_FINISH_PATTERN = Pattern
	    .compile("/lams/tool/lasbmt11/learner\\.do\\?method=finish\\&sessionMapID=sessionMapID-\\d+");

    private static int joinLessonUserCount = 0;
    private static int topJoinLessonUserCount = 0;
    private boolean finished = false;

    /**
     * MockLearner Constructor
     */
    public MockLearner(LearnerTest test, String username, String password, String userId) {
	super(test, username, password, MockAdmin.LEARNER_ROLE, userId);
    }

    private static String[] chooseArbitraryValues(String[] values) {
	int length = 1 + TestUtil.generateRandomNumber(values.length);
	String[] answers = new String[length];
	for (int i = 0; i < length; i++) {
	    answers[i] = values[TestUtil.generateRandomNumber(values.length)];
	}
	return answers;
    }

    private static String composeArbitraryText() {
	int length = 1 + TestUtil.generateRandomNumber(MockLearner.ARBITRARY_TEXT_ALPHABET.length());
	StringBuilder text = new StringBuilder(length);
	for (int i = 0; i < length; i++) {
	    text.append(MockLearner.ARBITRARY_TEXT_ALPHABET.charAt(TestUtil
		    .generateRandomNumber(MockLearner.ARBITRARY_TEXT_ALPHABET.length())));
	}
	return text.toString();
    }

    private static String findURLInAHREF(WebResponse resp, String linkSubstring) throws SAXException {
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

    private static String findURLInLocationHref(WebResponse resp, String linkSubstring) throws IOException {
	// an example of matched string:
	// location.href ='http://localhost/lams/mylinksubstring/learner.do'
	Pattern linkPattern = Pattern.compile("location\\.href\\s*=\\s*['\"](.*" + Pattern.quote(linkSubstring)
		+ ".*)['\"]");
	Matcher m = linkPattern.matcher(resp.getText());
	if (m.find()) {
	    String url = m.group(1);
	    MockLearner.log.debug("Found location.href = " + url);
	    return url;
	}
	return null;
    }

    private static Map<String, List<Button>> groupButtonsByName(Button[] btns, String buttonType) {
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

    private static String parseOutNextURL(WebResponse resp) throws SAXException, IOException {
	String text = resp.getText();
	String toolURL = null;
	Matcher m = MockLearner.NEXT_URL_PATTERN.matcher(text);
	if (m.find()) {
	    toolURL = m.group(1);
	}

	if ((toolURL != null) && !toolURL.startsWith("/")) {
	    toolURL = '/' + toolURL;
	}

	MockLearner.log.debug("Tool URL: " + toolURL);
	return toolURL;
    }

    private static File selectArbitraryFile(String[] filesToUpload) {
	return new File(filesToUpload[TestUtil.generateRandomNumber(filesToUpload.length)]);
    }

    public final boolean isFinished() {
	return finished;
    }

    @Override
    public void run() {
	MockLearner.log.info(username + " is studying the lesson");
	try {
	    login();
	    LearnerTest learnerTest = (LearnerTest) test;
	    String lessonId = test.getTestSuite().getMonitorTest().getLsId();
	    joinLesson(learnerTest.joinLessonURL, lessonId);
	    progressThroughActivities(learnerTest.lessonEntryURL, lessonId);
	    finished = true;
	    MockLearner.log.info(username + " finished the lesson");
	} catch (TestHarnessException e) {
	    MockLearner.log.error(username + " aborted on the lesson", e);
	    // We don't propagate the TestHarnessException so that other
	    // learners are not affected.
	    // other RuntimeException will still get propagated so that the
	    // application will halt, but that's expected, since those exceptions
	    // should be serious system errors
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	} finally {
	    ((LearnerTest) test).allDoneSignal.countDown();
	}
    }

    private WebForm fillFormArbitrarily(WebForm form) throws IOException, SAXException {
	String[] params = form.getParameterNames();
	if (params != null) {
	    for (String param : params) {
		if (!form.isDisabledParameter(param) && !form.isHiddenParameter(param)
			&& !form.isReadOnlyParameter(param)) {
		    if (form.isTextParameter(param)) {
			String text = MockLearner.composeArbitraryText();
			form.setParameter(param, text);
			MockLearner.log.debug(username + " input " + text + " for form field " + param);

		    } else if (form.isFileParameter(param)) {
			File file = MockLearner.selectArbitraryFile(((LearnerTest) test).getFilesToUpload());
			form.setParameter(param, file);
			MockLearner.log.debug(username + " uploaded file " + file.getName() + " for form field "
				+ param);
		    } else if (form.isMultiValuedParameter(param)) {
			String[] values = MockLearner.chooseArbitraryValues(form.getOptionValues(param));
			form.setParameter(param, values);
			MockLearner.log.debug(username + " set " + values.length + " values for form field " + param);
			Arrays.toString(values);
		    } else {
			MockLearner.log.debug(param + " may be a radio button. Current value is "
				+ form.getParameterValue(param));
			if (form.getParameterValue(param) == null) {
			    String[] candidateValues = form.getOptionValues(param);
			    if ((candidateValues != null) && (candidateValues.length > 0)) {
				String value = candidateValues[TestUtil.generateRandomNumber(candidateValues.length)];
				MockLearner.log.debug(username + " set " + value + " for form field " + param);
				form.setParameter(param, value);
			    }
			}
		    }
		} else {
		    MockLearner.log.debug("Disabled or hidden or readonly parameter " + param + " with value "
			    + form.getParameterValue(param));
		}
	    }
	}

	Map<String, List<Button>> buttonGroups = MockLearner.groupButtonsByName(form.getButtons(),
		FormControl.RADIO_BUTTON_TYPE);
	for (Map.Entry<String, List<Button>> entry : buttonGroups.entrySet()) {
	    entry.getValue().get(TestUtil.generateRandomNumber(entry.getValue().size())).click();
	    MockLearner.log.debug(username + " clicked a radio button " + entry.getKey());
	}
	return form;
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
		    return (WebResponse) new Call(wc, test, username + " forwarded to absolute URL", httpString)
			    .execute();
		}
	    }
	    index = index > 0 ? respAsText.lastIndexOf("\"http", index - 1) : -1;
	}
	return null;
    }

    private WebResponse handleActivity(WebResponse resp) throws SAXException, IOException {
	// MockLearner.log.debug(resp.getText());

	WebResponse nextResp = null;
	WebForm[] forms = resp.getForms();
	if ((forms != null) && (forms.length > 0)) {
	    MockLearner.log.debug("There " + (forms.length == 1 ? "is " : "are ") + forms.length
		    + (forms.length == 1 ? " form in the page " : " forms in the page"));
	    nextResp = handlePageWithForms(resp);
	} else {
	    nextResp = handlePageWithoutForms(resp);
	}

	boolean isActivityFinished = (resp != null)
		&& ((resp.getText().indexOf(MockLearner.ACTIVITY_FINISHED_FLAG) != -1) || (resp.getText().indexOf(
			MockLearner.LESSON_FINISHED_FLAG) != -1));
	return isActivityFinished ? nextResp : handleActivity(nextResp);
    }

    private WebResponse handleLoadToolActivity(String asText) throws SAXException, IOException {
	Matcher m = MockLearner.NEXT_URL_PATTERN.matcher(asText);
	if (m.find()) {
	    String toolURL = m.group(1);
	    return (WebResponse) new Call(wc, test, username + " forwarded to tool content page", toolURL).execute();
	}
	return null;
    }

    private WebResponse handlePageWithForms(WebResponse resp) throws IOException, SAXException {
	int index = -1;
	WebForm[] forms = resp.getForms();
	WebForm form = null;
	String action = null;
	String asText = resp.getText();
	do {
	    index++;
	    if (index >= forms.length) {
		throw new TestHarnessException(username
			+ " checked all forms on the page and does not know how to finish the activity");
	    }
	    form = forms[index];
	    action = form.getAction();
	} while ((action == null) || (action.trim().length() == 0));

	// special behaviour for different flavours of activities
	WebResponse nextResp = null;
	while (action.startsWith(MockLearner.KNOCK_GATE_SUBSTRING)) {
	    delay();
	    MockLearner.log.debug(username + " knocking gate");
	    nextResp = (WebResponse) new Call(wc, test, username + " knocks gate", form).execute();
	    if (nextResp.getText().indexOf(MockLearner.KNOCK_GATE_SUBSTRING) == -1) {
		return nextResp;
	    }
	}
	if (asText.contains(MockLearner.TASK_FINISH_SUBSTRING)) {
	    return handleToolTaskList(resp);
	}
	if (action.startsWith(MockLearner.CHAT_FINISH_SUBSTRING)) {
	    handleToolChat(resp);
	} else if (action.contains(MockLearner.QA_TOOL_SUBSTRING)) {
	    // make QA look at "answerX__textarea" form fields rather than "answerX"
	    form.setAttribute("action", action + "&testHarness=true");
	} else if (action.contains(MockLearner.VIDEORECORDER_TOOL_STRING)) {
	    // the second form should be taken into consideration
	    form = forms[1];
	} else if (asText.contains(MockLearner.VOTE_LEARNER_FINISHED_BUTTON_STRING)) {
	    // this is normally done by Javascript in browser
	    form.setAttribute("action", action + "&dispatch=learnerFinished");
	} else if (asText.contains(MockLearner.VOTE_VIEW_ALL_RESULTS_BUTTON_STRING)) {
	    form.setAttribute("action", action + "&dispatch=viewAllResults");
	} else if (asText.contains(MockLearner.WIKI_EDIT_BUTTON_STRING)) {
	    form = handleToolWiki(form, action);
	}

	nextResp = (WebResponse) new Call(wc, test, username + " submits tool form", fillFormArbitrarily(form))
		.execute();

	// check if it is assessment activity
	asText = nextResp.getText();
	Matcher m = MockLearner.ASSESSMENT_FINISH_PATTERN.matcher(asText);
	if (m.find()) {
	    nextResp = (WebResponse) new Call(wc, test, username + " finishes Assessment", m.group(1)).execute();
	} else {
	    m = MockLearner.SUBMIT_FILES_FINISH_PATTERN.matcher(asText);
	    if (m.find()) {
		nextResp = (WebResponse) new Call(wc, test, username + " finishes Submit Files", m.group()).execute();
	    }
	}

	return nextResp;
    }

    private WebResponse handlePageWithoutForms(WebResponse resp) throws SAXException, IOException {
	String asText = resp.getText();

	if (asText.contains(MockLearner.LOAD_TOOL_ACTIVITY_FLAG)) {
	    return handleLoadToolActivity(asText);
	}

	// special behaviour for different flavours of activities
	if (asText.contains(MockLearner.FORUM_FINISH_SUBSTRING)) {
	    return handleToolForum(resp);
	}
	if (asText.contains(MockLearner.LEADER_FINISH_SUBSTRING)) {
	    return handleToolLeader(resp);
	}
	if (asText.contains(MockLearner.SCRATCHIE_FINISH_SUBSTRING)
		|| asText.contains(MockLearner.SCRATCHIE_RESULTS_SUBSTRING)) {
	    return handleToolScratchie(resp);
	}

	Matcher m = MockLearner.SHARE_RESOURCES_REDIRECT_PATTERN.matcher(asText);
	if (m.find()) {
	    return handleToolShareResources(resp, m.group(1));
	}
	m = MockLearner.IMAGE_GALLERY_FINISH_PATTERN.matcher(asText);
	if (m.find()) {
	    return (WebResponse) new Call(wc, test, username + " finishes Image Gallery", m.group(1)).execute();
	}

	// fallback to some URL in page
	WebResponse nextResp = findAnAbsoluteURLOnPage(asText);
	if (nextResp == null) {
	    String url = MockLearner.findURLInLocationHref(resp, MockLearner.FINISH_SUBSTRING);
	    if (url == null) {
		throw new TestHarnessException("Unable to find a link to go to on page" + asText);
	    }
	    nextResp = (WebResponse) new Call(wc, test, username + " forwarded to tool finish URL", url).execute();
	}
	return nextResp;
    }

    private void handleToolChat(WebResponse resp) throws IOException {
	String asText = resp.getText();
	Matcher m = MockLearner.TOOL_SESSION_ID_PATTERN.matcher(asText);
	if (!m.find()) {
	    MockLearner.log.debug("No TOOL_SESSION_ID found in Chat page, it's probably reflection page, carry on");
	    return;
	}

	String url = MockLearner.CHAT_FINISH_SUBSTRING + "?dispatch=sendMessage&toolSessionID=" + m.group(1)
		+ "&message=";
	// send few messages
	for (int replyIndex = 0; replyIndex < MockLearner.CHAT_REPLIES; replyIndex++) {
	    String message = MockLearner.composeArbitraryText();
	    message = URLEncoder.encode(message, "UTF-8");
	    new Call(wc, test, username + " sends Chat message", url + message).execute();
	    delay();
	}
    }

    private WebResponse handleToolForum(WebResponse resp) throws SAXException, IOException {
	WebResponse replyResponse = null;

	String replyURL = MockLearner.findURLInAHREF(resp, MockLearner.FORUM_VIEW_TOPIC_SUBSTRING);
	if (replyURL != null) {
	    MockLearner.log.debug("Accessing the forum view topic screen using " + replyURL);
	    replyResponse = handleToolForumReply(replyURL);
	}

	if (replyResponse == null) {
	    MockLearner.log.debug(resp.getText());
	    throw new TestHarnessException("No links found on the main forum page, or unable to do reply");
	}

	String finishURL = MockLearner.findURLInLocationHref(replyResponse, MockLearner.FORUM_FINISH_SUBSTRING);
	if (finishURL == null) {
	    MockLearner.log.debug(replyResponse.getText());
	    throw new TestHarnessException("Unable to finish the forum. No finish link found");
	}

	MockLearner.log.debug("Ending forum using url " + finishURL);
	return (WebResponse) new Call(wc, test, "Finish Forum", finishURL).execute();
    }

    private WebResponse handleToolForumReply(String url) throws SAXException, IOException {
	WebResponse resp = (WebResponse) new Call(wc, test, username + " views Forum topic", url).execute();
	String replyURL = MockLearner.findURLInAHREF(resp, MockLearner.FORUM_REPLY_SUBSTRING);
	if (replyURL == null) {
	    MockLearner.log.debug(resp.getText());
	    throw new TestHarnessException("No reply URL found - unable to do reply. ");
	}

	resp = (WebResponse) new Call(wc, test, username + " replies to Forum topic", replyURL).execute();
	WebForm[] forms = resp.getForms();
	if ((forms == null) || (forms.length == 0)) {
	    MockLearner.log.debug(resp.getText());
	    throw new TestHarnessException("No form found on the reply topic page - unable to do reply");
	}
	// skip validation and make Forum read "message.body__textarea" instead of Javascript controlled "message.bod"
	WebForm form = resp.getForms()[0];
	String action = form.getAction() + "?testHarness=true";
	form.setAttribute("action", action);

	resp = handlePageWithForms(resp);

	// now we are back on the topic page, so go back to the main forum page.
	String returnToForumURL = MockLearner.findURLInAHREF(resp, MockLearner.FORUM_VIEW_FORUM_SUBSTRING);
	if (returnToForumURL == null) {
	    MockLearner.log.debug(resp.getText());
	    throw new TestHarnessException("No button back to forum page found - stuck while doing reply");
	}

	MockLearner.log.debug("Returning to forum page " + returnToForumURL);
	return (WebResponse) new Call(wc, test, username + " returns to Forum", returnToForumURL).execute();
    }

    private WebResponse handleToolLeader(WebResponse resp) throws SAXException, IOException {
	String asText = resp.getText();
	Matcher m = MockLearner.LEADER_BECOME_PATTERN.matcher(asText);
	if (m.find()) {
	    String becomeLeaderQueryOptions = m.group();
	    String url = "/lams/tool/lalead11/learning.do?" + becomeLeaderQueryOptions;
	    MockLearner.log.debug("Becoming a leader using link: " + url);
	    new Call(wc, test, username + " becomes Leader", url).execute();
	}

	String finishURL = MockLearner.findURLInLocationHref(resp, MockLearner.LEADER_FINISH_SUBSTRING);
	if (finishURL == null) {
	    throw new TestHarnessException("Unable to finish the leader, no finish link found. " + asText);
	}

	MockLearner.log.debug("Ending leader using url " + finishURL);
	return (WebResponse) new Call(wc, test, username + " finishes Leader", finishURL).execute();
    }

    private WebResponse handleToolScratchie(WebResponse resp) throws SAXException, IOException {
	String asText = resp.getText();
	// check if current user is the leader
	boolean isLeader = asText.contains(MockLearner.SCRATCHIE_IS_LEADER_SUBSTRING);
	String sessionMapID = null;
	String recordScratchedURL = null;
	String refreshQuestionsURL = null;
	Matcher m = MockLearner.SESSION_MAP_ID_PATTERN.matcher(asText);
	if (m.find()) {
	    sessionMapID = m.group(1);
	    recordScratchedURL = "/lams/tool/lascrt11/learning/recordItemScratched.do?sessionMapID=" + sessionMapID
		    + "&answerUid=";
	    refreshQuestionsURL = "/lams/tool/lascrt11/learning/refreshQuestionList.do?sessionMapID=" + sessionMapID;
	} else {
	    MockLearner.log.warn("Session map ID was not found in Scratchie Tool");
	}

	if (isLeader) {
	    // find all answers on the page
	    m = MockLearner.SCRATCHIE_SCRATCH_PATTERN.matcher(asText);
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
	    Random generator = new Random();

	    // start scratching random answers
	    while (!uids.isEmpty()) {
		Long questionID = uids.keySet().iterator().next();
		List<Long> answerUids = uids.get(questionID);

		int index = generator.nextInt(answerUids.size());
		Long answerUid = answerUids.get(index);
		answerUids.remove(index);
		MockLearner.log.debug("Scratching answer UID " + answerUid + " for question " + questionID);
		WebResponse scratchResponse = (WebResponse) new Call(wc, test,
			username + " checks answer in Scratchie", scratchURL + answerUid).execute();
		boolean answerCorrect = scratchResponse.getText().indexOf("true") != -1;
		MockLearner.log.debug("Scratched answer UID " + answerUid + " for question " + questionID
			+ " and it was " + (answerCorrect ? "correct" : "incorrect"));

		if (recordScratchedURL != null) {
		    MockLearner.log.debug("Recording scratched answer UID " + answerUid);
		    new Call(wc, test, username + " records answer in Scratchie", recordScratchedURL + answerUid)
			    .execute();
		}

		if (answerCorrect) {
		    uids.remove(questionID);
		}
		if (!answerUids.isEmpty()) {
		    delay();
		}
	    }
	} else {
	    while ((refreshQuestionsURL != null)
		    && !(asText.contains(MockLearner.SCRATCHIE_FINISH_AVAILABLE) || asText
			    .contains(MockLearner.SCRATCHIE_REFLECTION_AVAILABLE))) {
		MockLearner.log.debug("Waiting for leader to finish scratchie");
		try {
		    Thread.sleep(3000);
		} catch (InterruptedException e) {
		    MockLearner.log.error("Interrupted waiting between question list refresh in scratchie");
		}
		String url = resp.getURL().toString() + "&reqId=" + System.currentTimeMillis();
		WebResponse questionRefreshResp = (WebResponse) new Call(wc, test, username
			+ " refreshes Scratchie question list", refreshQuestionsURL).execute();
		asText = questionRefreshResp.getText();
	    }
	}

	String reflectionURL = MockLearner.findURLInLocationHref(resp, MockLearner.SCRATCHIE_REFLECTION_SUBSTRING);
	if (reflectionURL != null) {
	    MockLearner.log.debug("Showing reflection of scratchie using url " + reflectionURL);
	    resp = (WebResponse) new Call(wc, test, username + " gets Scratchie reflection", reflectionURL).execute();
	    resp = handlePageWithForms(resp);
	}

	String resultsURL = MockLearner.findURLInLocationHref(resp, MockLearner.SCRATCHIE_RESULTS_SUBSTRING);
	if (resultsURL != null) {
	    MockLearner.log.debug("Showing results of scratchie using url " + resultsURL);
	    resp = (WebResponse) new Call(wc, test, username + " gets Scratchie results", resultsURL).execute();
	}

	String finishURL = MockLearner.findURLInLocationHref(resp, MockLearner.SCRATCHIE_FINISH_SUBSTRING);
	if (finishURL != null) {
	    MockLearner.log.debug("Ending scratchie using url " + finishURL);
	    return (WebResponse) new Call(wc, test, username + " finishes Scratchie", finishURL).execute();
	}

	throw new TestHarnessException("Unable to finish the scratchie, no finish link found. " + asText);
    }

    private WebResponse handleToolShareResources(WebResponse resp, String initialRedirectLink) throws IOException,
	    SAXException {
	WebResponse nextResponse = (WebResponse) new Call(wc, test, username + " forwarded to Share Resources",
		initialRedirectLink).execute();
	String asText = nextResponse.getText();
	Matcher m = MockLearner.SHARE_RESOURCES_VIEW_ITEM_URL_PATTERH.matcher(asText);
	if (!m.find()) {
	    throw new TestHarnessException("View Item URL in Share Resources tool not found");
	}
	String viewItemURL = m.group(1);
	m = MockLearner.SHARE_RESOURCES_VIEW_ITEM_PATTERN.matcher(asText);
	while (m.find()) {
	    new Call(wc, test, username + " views Share Resources item", viewItemURL + m.group(1)).execute();
	    delay();
	}

	String finishURL = MockLearner.findURLInLocationHref(nextResponse, MockLearner.FINISH_SUBSTRING);
	return (WebResponse) new Call(wc, test, username + " finishes Share Resources", finishURL).execute();
    }

    private WebResponse handleToolTaskList(WebResponse resp) throws IOException, SAXException {
	String finishURL = MockLearner.findURLInLocationHref(resp, MockLearner.TASK_FINISH_SUBSTRING);
	WebForm[] forms = resp.getForms();
	// send a comment or a file, if available
	if (forms.length > 0) {
	    WebForm form = forms[TestUtil.generateRandomNumber(forms.length)];
	    resp = (WebResponse) new Call(wc, test, username + " adds a comment or file in Task List",
		    fillFormArbitrarily(form)).execute();
	    delay();
	}

	return (WebResponse) new Call(wc, test, username + " finishes Task List", finishURL).execute();
    }

    private WebForm handleToolWiki(WebForm form, String action) throws IOException, SAXException {
	// add one random page...
	form.setAttribute("action", action + "?dispatch=addPage");
	WebResponse nextResp = (WebResponse) new Call(wc, test, username + " adds Wiki page", fillFormArbitrarily(form))
		.execute();
	form = nextResp.getForms()[1];
	// ...and mark the activity to be finished
	form.setAttribute("action", action + "?dispatch=finishActivity");
	return form;
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
	new Call(wc, test, username + " joins lesson", url).execute();
	MockLearner.joinLessonUserCount--;
    }

    /**
     * Steps:
     * <ol>
     * <li>fetch entry url</li>
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
	String nextURL = MockLearner.parseOutNextURL(resp);
	boolean lessonFinished = false;
	while (!lessonFinished) {
	    if (nextURL == null) {
		boolean isOptionalActivity = resp.getText().contains(MockLearner.OPTIONAL_ACTIVITY_FLAG);
		if (isOptionalActivity) {
		    resp = handleActivity(resp);
		    nextURL = MockLearner.parseOutNextURL(resp);
		} else {
		    lessonFinished = true;
		}
	    } else {
		try {
		    resp = (WebResponse) new Call(wc, test, username + " forwarded to next activity", nextURL)
			    .execute();
		    delay();
		    resp = handleActivity(resp);
		} catch (Exception e) {
		    throw new TestHarnessException(e);
		}
		nextURL = MockLearner.parseOutNextURL(resp);
	    }
	}
    }
}