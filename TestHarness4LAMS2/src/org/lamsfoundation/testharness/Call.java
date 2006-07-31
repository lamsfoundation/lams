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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


/**
 * @version
 *
 * <p>
 * <a href="Call.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class Call {
    
	private static final Logger log = Logger.getLogger(Call.class);
	
	private static final String HTTP = "http://";
	
	private static final String GET = "GET ";
	
	private static final String POST = "POST ";

	private static final char COLON = ':';
	
	private static final char SPACE = ' ';
	
	private static final String NON_HTTP_ERROR = "Check the log for the error message";
	
	private WebConversation wc; //for WEB
	
    private AbstractTest test;
    
    private String description;
    
    @SuppressWarnings("unused")
	private String method; //for RMI and WS
    
    @SuppressWarnings("unused")
	private Map<String, Object> parameters; //for RMI or WS
    
    private String url; //for WEB
    
    private WebForm form; //for WEB
    
    private InputStream is; //for WEB POST method
    
    private String contentType; //for WEB POST method

	public Call(WebConversation wc, AbstractTest test, String description, WebForm form) {
		this.wc = wc;
		this.test = test;
		this.description = description;
		this.form = form;
	}

	public Call(AbstractTest test, String description, String method, Map<String, Object> parameters) {
		this.test = test;
		this.description = description;
		this.method = method;
		this.parameters = parameters;
	}

	public Call(WebConversation wc, AbstractTest test, String description, String url, InputStream is, String contentType) {
		this.wc = wc;
		this.test = test;
		this.description = description;
		this.url = url;
		this.is = is;
		this.contentType = contentType;
	}

	public Call(WebConversation wc, AbstractTest test, String description, String url) {
		this.wc = wc;
		this.test = test;
        this.description = description;
        this.url = url;
    }

    public AbstractTest getTest() {
        return test;
    }
    
    /**
     *  need add CallRecord to TestReport
     */
    public Object execute(){
    	switch(test.callType){
    		case RMI: return executeRMI();
    		case WS:  return executeWS();
    		case WEB: return executeHttpRequest();
    		default: return null;
    	}
    }

	private Object executeHttpRequest(){
		String message = null;
		String callee = null;
		Integer httpStatusCode = null;
		long start =0,  end = 0;
		try{
			WebResponse resp;
			if(form!=null){
				SubmitButton[] submitButtons = filterCancelButton(form.getSubmitButtons());
				log.debug(submitButtons.length+" non-cancel submit button(s) in the form");
				WebRequest req;
				if(submitButtons.length <= 1){
					req = form.getRequest();
				}else{
					req = form.getRequest(submitButtons[TestUtil.generateRandomIndex(submitButtons.length)]);
				}
				callee = form.getMethod().toUpperCase() + SPACE + form.getAction();
				log.debug(callee);
				start = System.currentTimeMillis();
				resp = wc.getResponse(req);
				end = System.currentTimeMillis();
			}else{
				String absoluteURL = getAbsoluteURL(url);
				WebRequest  req;
				if(is==null){
					callee = GET+url;
					req = new GetMethodWebRequest(absoluteURL);
				}else{
					callee = POST+url;
					req = new PostMethodWebRequest(absoluteURL,is,contentType);
				}
				log.debug(callee);
				start = System.currentTimeMillis();
			    resp = wc.getResponse( req );
			    end = System.currentTimeMillis();
			}
			httpStatusCode = resp.getResponseCode();
			message = resp.getResponseMessage();
			
			/*if(callee.indexOf("passon.swf")==-1)
				log.debug(resp.getText());*/

			if(resp.getResponseCode() >= 400){
				log.debug(resp.getText());
				throw new TestHarnessException(test.testName + " Got http error code "+httpStatusCode);
			}
			return resp;
		}catch(IOException e){
			message = NON_HTTP_ERROR;
			log.debug(e.getMessage(),e);
			throw new RuntimeException(e);
		} catch (SAXException e) {
			message = NON_HTTP_ERROR;
			log.debug(e.getMessage(),e);
			throw new RuntimeException(e);
		}finally{
			TestReporter.addCallRecord(new CallRecord(test.getTestSuite().getSuiteIndex(),test.testName,test.callType,callee,description,end,end-start,httpStatusCode,message));
		}
	}

	
	private SubmitButton[] filterCancelButton(SubmitButton[] sbmtBtns){
		boolean found = false;
		int i = 0;
		for(; i < sbmtBtns.length; i++){
			if(isCancelButton(sbmtBtns[i])){
				found = true;
				break;
			}
		}
		if(found){
			SubmitButton[] btns = new SubmitButton[sbmtBtns.length-1];
			int j = 0;
			for(int k = 0; k < sbmtBtns.length; k++){
				if( k != i ){
					btns[j] = sbmtBtns[k];
					j++;
				}
			}
			return btns;
		}else{
			return sbmtBtns;
		}
	}
	
	private boolean isCancelButton(SubmitButton button) {
		if(button.getName().contains("CANCEL")||button.getName().contains("Cancel")||button.getName().contains("cancel")
				||button.getValue().contains("cancel")||button.getValue().contains("Cancel")||button.getValue().contains("CANCEL"))
			return true;
		return false;
	}

	/**
	 * TODO implement me
	 * @param
	 * @return
	 */
	private Object executeWS() {
		return null;
	}

	/**
	 * TODO implement me
	 * @param
	 * @return
	 */
	private Object executeRMI() {
		return null;
	}

	private String getAbsoluteURL(String url){
		if(test.getTestSuite().getHttpPort()!=80)
			return HTTP+test.getTestSuite().getTargetServer()+COLON+test.getTestSuite().getHttpPort()+test.getTestSuite().getContextRoot()+url;
		else
			return HTTP+test.getTestSuite().getTargetServer()+test.getTestSuite().getContextRoot()+url;
	}

	protected static class CallRecord {
	    
		private int suiteIndex;
		private String testName;
		private CallType type;
		private String callee;
		private String description;
	    private long snapShotTime;
	    private long timeInMillis;
	    private Integer httpStatusCode;//for Web Call only
	    private String message;


	    public CallRecord(){
	    	//empty constructor
	    }
	    
	    public CallRecord(int suiteIndex, String testName, CallType type, String callee, String description, long snapShotTime, long timeInMillis, Integer httpStatusCode, String message) {
	    	this.suiteIndex = suiteIndex;
	    	this.testName = testName;
	    	this.type = type;
	    	this.callee = callee;
	    	this.description = description;
	        this.snapShotTime = snapShotTime;
	        this.timeInMillis = timeInMillis;
	        this.httpStatusCode = httpStatusCode;
	        this.message = message;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public long getSnapShotTime() {
	        return snapShotTime;
	    }

	    public long getTimeInMillis() {
	        return timeInMillis;
	    }

		public String getCallee() {
			return callee;
		}

		public CallType getType() {
			return type;
		}

		public String getTestName() {
			return testName;
		}

		public int getSuiteIndex() {
			return suiteIndex;
		}

		public String getDescription() {
			return description;
		}

		public final Integer getHttpStatusCode() {
			return httpStatusCode;
		}
	}

	public enum CallType {
		
		RMI,WS,WEB,UNKNOWN;
		
		public static CallType get(String value){
			if(value.equals("RMI"))
				return CallType.RMI;
			else if(value.equals("WS"))
				return CallType.WS;
			else if(value.equals("WEB"))
				return CallType.WEB;
			else return CallType.UNKNOWN;
		}
		
		public String getName(){
			switch(this){
				case RMI: return "RMI";
				case WS:  return "WS";
				case WEB: return "WEB";
				default:  return "Unknown";
			}
		}
	}

}
