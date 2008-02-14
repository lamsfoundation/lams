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

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.TestUtil;
import org.xml.sax.SAXException;

import com.allaire.wddx.WddxDeserializationException;
import com.meterware.httpunit.Button;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

/**
 * @version
 * 
 * <p>
 * <a href="MockLearner.java.html"><i>View Source</i></a>
 * </p>
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

	private static final String ACTIVITY_FINISHED_FLAG = "passon.swf";
	
	private static final String FORUM_FINISH_SUBSTRING = "lafrum11/learning/finish.do";
	private static final String FORUM_VIEW_TOPIC_SUBSTRING = "lafrum11/learning/viewTopic.do";
	private static final String FORUM_REPLY_SUBSTRING = "lafrum11/learning/newReplyTopic.do";
	private static final String FORUM_VIEW_FORUM_SUBSTRING = "lafrum11/learning/viewForum.do";
	
	private static final String LOAD_TOOL_ACTIVITY_SUBSTRING = "Load Tool Activity";
	
	
	private boolean finished = false;

	/**
	 * MockLearner Constructor
	 */
	public MockLearner(LearnerTest test, String username, String password, String userId) {
		super(test, username, password, userId);

	}

	public void run() {
		study();
	}

	public void study(){
		log.info(username+" is studying the lesson...");
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
			log.info(username + " finished the lesson");
		}catch(TestHarnessException e){
			log.info(username + " aborted on the lesson");
			log.error(e.getMessage(), e);
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
	 * <li> fetch entry url </li>
	 * <li> get passon.swf url and the next activity url </li>
	 * <li> fetch the next activity url and get activity page with form </li>
	 * <li> fill the form and submit it
	 * <li>
	 * <li> repeat step 2-4 until lesson completed or error happened </li>
	 * </ol>
	 * 
	 * Note: httpunit will automatically redirect url if there is a redirect
	 * http header in response. In this case, the redirect url won't be recorded
	 * in access log or testharness log.
	 * 
	 * @param lessonEntryURL
	 * @param lsId
	 * @return void
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private void progressThroughActivities(String lessonEntryURL, String lsId) throws SAXException, IOException {
		delay();
		WebResponse resp = (WebResponse) new Call(wc, test, username + " enters lesson", lessonEntryURL.replace(LESSON_ID_PATTERN, lsId)).execute();
		String[] nextURLs = parseOutNextURLs(resp);
		boolean lessonFinished = false;
		while (!lessonFinished) {
			if ((nextURLs[0] != null) && (nextURLs[1]!=null)) {
				new Call(wc, test, username + " requests flash", nextURLs[0]).execute();
				resp = takeActivity(nextURLs[1]);
				nextURLs = parseOutNextURLs(resp);
			}else if ((nextURLs[0]==null) && (nextURLs[1]!=null)){
				log.debug("It's a bit wierd! passon.swf url was not found while tool url found!");
				log.debug(resp.getText());
			}else if ((nextURLs[0]==null) && (nextURLs[1]==null)){
				log.debug("Neither passon.swf or tool url was found. "+username + " may have got an error!");
				log.debug(resp.getText());
			}else{//(nextURLs[0] != null) && (nextURLs[1]==null)
				if(isOptionalActivity(resp)){
					resp = handleActivity(resp);
					nextURLs = parseOutNextURLs(resp);
				}else{
					new Call(wc, test, username + " requests flash", nextURLs[0]).execute();
					lessonFinished = true;
				}
			}
			
		}
	}

	private boolean isOptionalActivity(WebResponse resp) throws IOException {
		return resp.getText().indexOf(OPTIONAL_ACTIVITY_FLAG)!=-1;
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
		
		if ( resp.getText().indexOf("/lams/tool/laqa11/learning.do") != -1 ) 
			log.debug(resp.getText());
		
		WebResponse nextResp;
		WebForm[] forms = resp.getForms();
		if ((forms != null) && (forms.length > 0)) {
			log.debug("There "+(forms.length==1? "is ":"are ")+forms.length+(forms.length==1? " form in the page ":" forms in the page"));
			nextResp = handlePageWithForms(forms);
		} else {
			nextResp = handlePageWithoutForms(resp);
		}
		if (isActivityFinished(nextResp))
			return nextResp;
		else
			return handleActivity(nextResp);

	}

	private boolean isActivityFinished(WebResponse resp) throws IOException {
		return resp != null && resp.getText().indexOf(ACTIVITY_FINISHED_FLAG) != -1;
	}

	private WebResponse handlePageWithoutForms(WebResponse resp) throws SAXException, IOException {

		String asText = resp.getText();

		// Is this a Forum activity? 
		if ( asText.indexOf(FORUM_FINISH_SUBSTRING) != -1 ) 
			return handleForum(resp);
		
		else if ( asText.indexOf(LOAD_TOOL_ACTIVITY_SUBSTRING) != -1 ) 
			return handleLoadToolActivity(asText);

		else
			return findAnAbsoluteURLOnPage(asText);
		
			
	}
	
	private WebResponse handleLoadToolActivity(String asText) throws SAXException, IOException {

		String toolURL = TestUtil.extractString(asText, NEXT_URL_START_FLAG, NEXT_URL_END_FLAG);
		return (WebResponse) new Call(wc, test, "Redirect to tool page", toolURL).execute();
	}
	
	private WebResponse findAnAbsoluteURLOnPage(String respAsText) throws SAXException, IOException {
		// Probably safest to get the last http address on the page, make sure we don't accidently
		// go to http://www.w3c.org/
		int index = respAsText.lastIndexOf("\"http");
		while ( index != -1) {
			int indexEnd = respAsText.indexOf("\"",index+1);
			if ( indexEnd != -1 ) {
				String httpString = respAsText.substring(index+1, indexEnd);
				if ( httpString.indexOf("www.w3.org") == -1 && ! httpString.endsWith(".js") && ! httpString.endsWith(".css")) {
					log.debug("Forwarding to discovered link "+httpString);
					return (WebResponse) new Call(wc, test, "", httpString).execute();
				}
			}
			index = index > 0 ? respAsText.lastIndexOf("\"http",index-1) : -1;
		}
		throw new TestHarnessException("Unable to find a link to go to on page"+respAsText);
	}

	private WebResponse handleForum(WebResponse resp) throws SAXException, IOException {

		WebResponse replyResponse = null;

		String replyURL = findURLInAHREF(resp,FORUM_VIEW_TOPIC_SUBSTRING);
		if ( replyURL != null ) {
			log.debug("Accessing the forum view topic screen using "+replyURL);
			replyResponse = handleForumReply(replyURL);
		}
		
		if ( replyResponse == null ) {
			throw new TestHarnessException("No links found on the main forum page, or unable to do reply. "+resp.getText());
		} 
		
		String finishURL = findURLInLocationHref(replyResponse, FORUM_FINISH_SUBSTRING);
		if ( finishURL != null ) {
			log.debug("Ending forum using url "+finishURL);
			return (WebResponse) new Call(wc, test, "Finish Forum", finishURL).execute();
		}
		
		throw new TestHarnessException("Unable to finish the forum. No finish link found. "+replyResponse.getText());
	}

	/**
	 * @param link
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	private WebResponse handleForumReply(String url) throws SAXException, IOException {
		WebResponse resp= (WebResponse) new Call(wc, test, "View Topic Forum", url).execute();
		String replyURL = findURLInAHREF(resp,FORUM_REPLY_SUBSTRING);
		if ( replyURL != null ) {
			resp = (WebResponse) new Call(wc, test, "Reply Topic Forum", replyURL).execute();
			WebForm[] forms = resp.getForms();
			if ((forms != null) && (forms.length > 0)) {
				resp = handlePageWithForms(forms);
			} else {
				throw new TestHarnessException("No form found on the reply topic page - unable to do reply. "+resp.getText());
			}
		} else {
			throw new TestHarnessException("No reply URL found - unable to do reply. "+resp.getText());
		}
		
		// now we are back on the topic page, so go back to the main forum page.
		String returnToForumURL = findURLInLocationHref(resp, FORUM_VIEW_FORUM_SUBSTRING);
		if ( returnToForumURL != null ) {
			log.debug("Returning to forum page "+returnToForumURL);
			return (WebResponse) new Call(wc, test, "Return to Forum", returnToForumURL).execute();
		}
		throw new TestHarnessException("No button back to forum page found - stuck while doing reply. "+resp.getText());
	}
	
	
	private String findURLInAHREF(WebResponse resp, String linkSubstring) throws SAXException {
		WebLink[] links = resp.getLinks();
		if ( links != null ) {
			for ( WebLink link: links) {
				log.debug("Checking link "+link.getURLString());
				if ( link.getURLString().indexOf(linkSubstring) != -1 ) 
					return link.getURLString();
			}
		}
		return null;
	}
	
	private String findURLInLocationHref(WebResponse resp, String linkSubstring) throws SAXException, IOException {
		String respAsText = resp.getText();
		String lowercaseRespAsText = respAsText.toLowerCase();
		int stringLength = lowercaseRespAsText.length();
		
		int index = lowercaseRespAsText.indexOf("location.href");
		while ( index != -1) {
			index++;
			while ( index < stringLength && ! ( lowercaseRespAsText.charAt(index) == '\'' || lowercaseRespAsText.charAt(index) == '\"') ) {
				index++;
			}
			if ( index < stringLength - 1 ) {
				char quote = lowercaseRespAsText.charAt(index);
				int indexEnd = lowercaseRespAsText.indexOf(quote,index+1);
				String httpString = respAsText.substring(index+1, indexEnd);
				log.debug("Discovered link "+httpString);
				if ( httpString.indexOf(linkSubstring) != -1 ) {
					log.debug("Matched to "+linkSubstring);
					return httpString;
				}
			}
			log.debug("Index was "+index);
			index = index < stringLength && index > 0 ? lowercaseRespAsText.indexOf("location.href",index+1) : -1;
			log.debug("New index is "+index);
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
		while((form.getAction() == null)||(form.getAction().trim().length()==0)){  
			index++;
			if(index >= forms.length){
				throw new TestHarnessException(username+" don't know how to finish the activity now");
			}
			form = forms[index];
		}
		return (WebResponse) new Call(wc, test, "", fillFormArbitrarily(form)).execute();
	}

	private String getLesson(String getLessonURL, String lsId) throws WddxDeserializationException, IOException {
		delay();
		String url = getLessonURL.replace(LESSON_ID_PATTERN, lsId);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " get lesson", url).execute();
		Hashtable hashtable = (Hashtable) TestUtil.deserialize(resp.getText());
		hashtable = (Hashtable) hashtable.get(MESSAGE_VALUE_KEY);
		return new Integer(((Double) hashtable.get(LD_ID_KEY)).intValue()).toString();
	}

	private void getLearningDesign(String getLearningDesignURL, String ldId) {
		delay();
		String url = getLearningDesignURL.replace(LD_ID_PATTERN, ldId);
		new Call(wc, test, username + " get learning design", url).execute();
	}

	private void joinLesson(String joinLessonURL, String lsId) {
		delay();
		String url = joinLessonURL.replace(LESSON_ID_PATTERN, lsId);
		new Call(wc, test, username + " join lesson", url).execute();
	}

	private void getFlashProgessData(String getFlashProgressDataURL, String lsId) {
		delay();
		String url = getFlashProgressDataURL.replace(LESSON_ID_PATTERN, lsId);
		new Call(wc, test, username + " get flash progress data", url).execute();
	}

	private String[] parseOutNextURLs(WebResponse resp) throws SAXException, IOException {
		String text = resp.getText();
		String passonSwfURL = TestUtil.extractString(text, SWF_URL_START_FLAG, SWF_URL_END_FLAG);
		String toolURL = TestUtil.extractString(text, NEXT_URL_START_FLAG, NEXT_URL_END_FLAG);
		
		if (passonSwfURL != null)
			passonSwfURL = ((LearnerTest) test).subContextRoot + passonSwfURL;
		
		if ((toolURL != null) && !toolURL.startsWith("/"))
			toolURL = '/' + toolURL;
		
		log.debug("passonSwfURL:"+passonSwfURL);
		log.debug("toolURL:"+toolURL);
		return new String[] { passonSwfURL, toolURL };
	}
	
	private WebForm fillFormArbitrarily(WebForm form) throws IOException, SAXException {
		String[] params = form.getParameterNames();
		if ((params != null) && (params.length > 0)) {
			for (String param : params) {
				if (!form.isDisabledParameter(param) && !form.isHiddenParameter(param) && !form.isReadOnlyParameter(param)) {
					if (form.isTextParameter(param)) {
						String text = composeArbitraryText();
						form.setParameter(param, text);
						log.debug(username+ " input " + text + " for form field " + param);
					} else if (form.isFileParameter(param)) {
						File file = selectArbitraryFile(((LearnerTest) test).getFilesToUpload());
						form.setParameter(param, file);
						log.debug(username + " uploaded file "+ file.getName()+" for form field "+param);
					} else if (form.isMultiValuedParameter(param)) {
						String[] values = chooseArbitraryValues(form.getOptionValues(param));
						form.setParameter(param, values);
						log.debug(username+" set " + values.length + " value(s) for form field " + param);
						log.debug(values);
					} else { 
						log.debug(param+" may be a radio button. Current value is "+form.getParameterValue(param));
						if ( form.getParameterValue(param) == null ) {
							String[] candidateValues = form.getOptionValues(param);
							if ( candidateValues != null && candidateValues.length > 0 ) {
									String value = candidateValues[TestUtil.generateRandomIndex(candidateValues.length)];
									log.debug("Setting param to "+value);
									form.setParameter(param, value);
							}
						} 
					}
				}else{
					log.debug("disabled or hidden or readonly parameter "+param);
				}
			}
		}
		
		Map<String, List<Button>> buttonGroups = groupButtonsByName(form.getButtons(), Button.RADIO_BUTTON_TYPE);
		for (Map.Entry<String, List<Button>> entry : buttonGroups.entrySet()){
			entry.getValue().get(TestUtil.generateRandomIndex(entry.getValue().size())).click();
			log.debug(username+" clicked a radio button "+entry.getKey());
		}
		return form;
	}
	
	private String processButtons(Button[] buttons) {
		StringBuffer buf = new StringBuffer(100);
		for ( Button button : buttons ) {
			buf.append(button.toString());
			buf.append("; ");
		}
		return buf.toString();
	}
	
	private Map<String, List<Button>> groupButtonsByName(Button[] btns, String buttonType ){
		log.debug(btns.length);
		Map<String, List<Button>> buttonGroups = new HashMap<String, List<Button>>();
		if (btns!=null) {
			for (Button btn : btns){
				if(buttonType.equals(btn.getType())){
					String name = btn.getName();
					log.debug("Got "+buttonType+" "+name+" and its value is "+btn.getValue());
					if(!buttonGroups.containsKey(name)){
						buttonGroups.get(name).add(btn);
					}else{
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
		int length = 1 + TestUtil.generateRandomIndex(ARBITRARY_TEXT_ALPHABET.length());
		StringBuilder text = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			text.append(ARBITRARY_TEXT_ALPHABET.charAt(TestUtil.generateRandomIndex(ARBITRARY_TEXT_ALPHABET.length())));
		}
		return text.toString();
	}

	public final boolean isFinished() {
		return finished;
	}

}
