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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.testharness;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

/**
 * @version
 *
 * <p>
 * <a href="MockUser.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockUser{

	private static final Logger log = Logger.getLogger(MockUser.class);
	
	private static final String[] DELAY_MESSAGES = {" is deserting ", " is napping ", " is pondering ", " is in a daze ", " will have a cup of coffee for ", " is away for toilet "};
	
	private static final String LOGIN_PAGE_FLAG = "j_security_check";
	private static final String INDEX_PAGE_FLAG = "Math.random()";
	
	private static final String USERNAME = "j_username";
	private static final String PASSWORD = "j_password";
	
	private static String indexPage;

	protected AbstractTest test;
	protected String userId;
	protected String username;
	protected String password;
	protected WebConversation wc;

	public MockUser(AbstractTest test, String username, String password, String userId) {
		this.test = test;
		this.username = username;
		this.password = password;
		this.userId = userId;
	}

	public static final void setIndexPage(String indexPageURL){
		indexPage = indexPageURL;
	}

	/** 
	 * Login to the system. 
	 * 
	 * @exception TestHarnessException:failure
	 */
	public final void login()
	{
		try{
			wc = new WebConversation();
			WebResponse resp = (WebResponse)new Call(wc,test,username+" fetch index page",indexPage).execute();
			if(!checkPageContains(resp,LOGIN_PAGE_FLAG)){
				log.debug(resp.getText());
				throw new TestHarnessException(username +" didn't get login page when hitting LAMS the first time!");
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(USERNAME,username);
			params.put(PASSWORD,HashUtil.sha1(password));
			resp = (WebResponse)new Call(wc, test,"User login",fillForm(resp,0,params)).execute();
			if(!checkPageContains(resp,INDEX_PAGE_FLAG)){
				log.debug(resp.getText());
				throw new TestHarnessException(username+" failed to login with password "+password);
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}catch(SAXException e){
			throw new RuntimeException(e);
		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
	}

	protected final WebForm fillForm(WebResponse resp, int formIndex, Map<String, Object> params) throws SAXException, IOException{
		WebForm[] forms = resp.getForms();
		if((forms==null)||(forms.length<=formIndex)){
			log.debug(resp.getText());
			throw new TestHarnessException(username + " cannot find the form whose index is "+formIndex+" in the page");
		}
		WebForm form = forms[formIndex];
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				Object value = entry.getValue();
				if (value instanceof String) {
					form.setParameter(entry.getKey(), (String) entry.getValue());
				} else if (value instanceof File) {
					form.setParameter(entry.getKey(), (File) entry.getValue());
				} else if (value instanceof String[]) {
					form.setParameter(entry.getKey(), (String[]) entry.getValue());
				} else if (value instanceof UploadFileSpec[]) {
					form.setParameter(entry.getKey(), (UploadFileSpec[]) entry.getValue());
				} else {
					throw new TestHarnessException("Unsupported parameter value type:" + entry.getValue().getClass().getName());
				}
			}
		}
		return form;
	}
	
	protected final boolean checkPageContains(WebResponse resp,String flag) throws IOException
	{
		return resp.getText().indexOf(flag)!=-1;
	}

    protected final void delay(){
    	try{
    		int seconds;
	    	if(test.getMaxDelay() <= test.getMinDelay()){//to avoid IllegalArgumentException in nextInt method on Random object
	    		seconds = test.getMinDelay();
	    	}else{
	    		seconds = test.getMinDelay() + TestUtil.generateRandomIndex(test.getMaxDelay() - test.getMinDelay() + 1 );
	    	}
	    	if(seconds>0){
	    		log.info(composeDelayInfo(seconds));
	    		Thread.sleep(seconds * 1000);
	    	}
    	}catch (InterruptedException e){
    		//ignore
    	}
    }
	
	private String composeDelayInfo(int seconds) {
		return username+DELAY_MESSAGES[TestUtil.generateRandomIndex(DELAY_MESSAGES.length)] + seconds + (seconds==1? " second" : " seconds");
	}

	private static class HashUtil {

		static String sha1(String plaintext) throws NoSuchAlgorithmException {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
		}

		static String md5(String plaintext) throws NoSuchAlgorithmException {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
		}

	}

	public final String getPassword() {
		return password;
	}

	public final String getUsername() {
		return username;
	}

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

}
