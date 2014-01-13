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
package org.lamsfoundation.testharness;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class Call {

    static class CallRecord {
	private int suiteIndex;
	private String testName;
	private String callee;
	private String description;
	private String snapShotTime;
	private long timeInMillis;
	private Integer httpStatusCode;
	private String message;

	public CallRecord() {
	}

	public CallRecord(int suiteIndex, String testName, String callee, String description, String snapShotTime,
		long timeInMillis, Integer httpStatusCode, String message) {
	    this.suiteIndex = suiteIndex;
	    this.testName = testName;
	    this.callee = callee;
	    this.description = description;
	    this.snapShotTime = snapShotTime;
	    this.timeInMillis = timeInMillis;
	    this.httpStatusCode = httpStatusCode;
	    this.message = message;
	}

	public String getCallee() {
	    return callee;
	}

	public String getDescription() {
	    return description;
	}

	public final Integer getHttpStatusCode() {
	    return httpStatusCode;
	}

	public String getMessage() {
	    return message;
	}

	public String getSnapShotTime() {
	    return snapShotTime;
	}

	public int getSuiteIndex() {
	    return suiteIndex;
	}

	public String getTestName() {
	    return testName;
	}

	public long getTimeInMillis() {
	    return timeInMillis;
	}
    }

    private static final Logger log = Logger.getLogger(Call.class);
    private WebConversation wc;
    private AbstractTest test;
    private String description;
    private String url;
    private WebForm form;
    private InputStream is;

    private String contentType; // for WEB POST method

    public Call(WebConversation wc, AbstractTest test, String description, String url) {
	this.wc = wc;
	this.test = test;
	this.description = description;
	this.url = url;
    }

    public Call(WebConversation wc, AbstractTest test, String description, String url, InputStream is,
	    String contentType) {
	this.wc = wc;
	this.test = test;
	this.description = description;
	this.url = url;
	this.is = is;
	this.contentType = contentType;
    }

    public Call(WebConversation wc, AbstractTest test, String description, WebForm form) {
	this.wc = wc;
	this.test = test;
	this.description = description;
	this.form = form;
    }

    private static boolean isCancelButton(SubmitButton button) {
	return button.getName().contains("CANCEL") || button.getName().contains("Cancel")
		|| button.getName().contains("cancel") || button.getValue().contains("cancel")
		|| button.getValue().contains("Cancel") || button.getValue().contains("CANCEL");
    }

    public Object execute() {
	String message = null;
	String callee = null;
	Integer httpStatusCode = null;
	long start = 0;
	long end = 0;
	try {
	    WebResponse resp = null;
	    if (form != null) {
		SubmitButton[] submitButtons = filterCancelButton(form.getSubmitButtons());
		Call.log.debug(submitButtons.length + " non-cancel submit buttons in the form");
		WebRequest req = null;
		if (submitButtons.length <= 1) {
		    req = form.getRequest();
		} else {
		    req = form.getRequest(submitButtons[TestUtil.generateRandomNumber(submitButtons.length)]);
		}
		callee = form.getMethod().toUpperCase() + " " + form.getAction();
		Call.log.debug(callee);
		start = System.currentTimeMillis();
		resp = wc.getResponse(req);
		end = System.currentTimeMillis();
	    } else {
		String absoluteURL = getAbsoluteURL(url);
		WebRequest req;
		if (is == null) {
		    callee = "GET " + url;
		    req = new GetMethodWebRequest(absoluteURL);
		} else {
		    callee = "POST " + url;
		    req = new PostMethodWebRequest(absoluteURL, is, contentType);
		}
		Call.log.debug(callee);
		start = System.currentTimeMillis();
		resp = wc.getResponse(req);
		end = System.currentTimeMillis();
	    }
	    httpStatusCode = resp.getResponseCode();
	    message = resp.getResponseMessage();

	    /*if(callee.indexOf("passon.swf")==-1)
	    	log.debug(resp.getText());*/

	    if (resp.getResponseCode() >= 400) {
		Call.log.debug(resp.getText());
		throw new TestHarnessException(test.testName + " got http error code " + httpStatusCode);
	    }

	    for (String headerFieldName : resp.getHeaderFieldNames()) {
		if (headerFieldName.equalsIgnoreCase("SET-COOKIE")) {
		    for (String headerFieldValue : resp.getHeaderFields(headerFieldName)) {
			String[] headerFieldSplit = headerFieldValue.split("=|;");
			String cookieName = headerFieldSplit[0];
			if ("JSESSIONID".equalsIgnoreCase(cookieName) && (wc.getCookieValue(cookieName) == null)) {
			    String cookieValue = headerFieldSplit[1];
			    Call.log.debug("Manually setting cookie: " + cookieName + "=" + cookieValue);
			    wc.putCookie(cookieName, cookieValue);
			}
		    }
		}
	    }

	    return resp;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    TestReporter.addCallRecord(new CallRecord(test.getTestSuite().getSuiteIndex(), test.testName, callee,
		    description, new SimpleDateFormat("HH:mm:ss SSS").format(new Date(end)), end - start,
		    httpStatusCode, message));
	}
    }

    private SubmitButton[] filterCancelButton(SubmitButton[] sbmtBtns) {
	boolean found = false;
	int i = 0;
	for (; i < sbmtBtns.length; i++) {
	    if (Call.isCancelButton(sbmtBtns[i])) {
		found = true;
		break;
	    }
	}
	if (found) {
	    SubmitButton[] btns = new SubmitButton[sbmtBtns.length - 1];
	    int j = 0;
	    for (int k = 0; k < sbmtBtns.length; k++) {
		if (k != i) {
		    btns[j] = sbmtBtns[k];
		    j++;
		}
	    }
	    return btns;
	} else {
	    return sbmtBtns;
	}
    }

    private String getAbsoluteURL(String url) {
	if (url.startsWith("http://")) {
	    return url;
	}

	String withSlash = url.startsWith("/") ? url : "/" + url;
	String context = url.startsWith(test.getTestSuite().getContextRoot()) ? "" : test.getTestSuite()
		.getContextRoot();
	if (test.getTestSuite().getHttpPort() != 80) {
	    return "http://" + test.getTestSuite().getTargetServer() + ":" + test.getTestSuite().getHttpPort()
		    + context + withSlash;
	} else {
	    return "http://" + test.getTestSuite().getTargetServer() + context + withSlash;
	}
    }
}