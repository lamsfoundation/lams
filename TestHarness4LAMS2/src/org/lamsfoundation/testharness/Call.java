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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 *          <p>
 *          <a href="Call.java.html"><i>View Source</i></a>
 *          </p>
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

    private WebConversation wc; // for WEB

    private AbstractTest test;

    private String description;

    @SuppressWarnings("unused")
    private String method; // for RMI and WS

    @SuppressWarnings("unused")
    private Map<String, Object> parameters; // for RMI or WS

    private String url; // for WEB

    private WebForm form; // for WEB

    private InputStream is; // for WEB POST method

    private String contentType; // for WEB POST method

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

    public Call(WebConversation wc, AbstractTest test, String description, String url, InputStream is,
	    String contentType) {
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
     * need add CallRecord to TestReporter
     */
    public Object execute() {
	switch (test.callType) {
	case RMI:
	    return executeRMI();
	case WS:
	    return executeWS();
	case WEB:
	    return executeHttpRequest();
	default:
	    return null;
	}
    }

    private Object executeHttpRequest() {
	String message = null;
	String callee = null;
	Integer httpStatusCode = null;
	long start = 0;
	long end = 0;
	try {
	    WebResponse resp = null;
	    if (form != null) {
		SubmitButton[] submitButtons = filterCancelButton(form.getSubmitButtons());
		Call.log.debug(submitButtons.length + " non-cancel submit button(s) in the form");
		WebRequest req = null;
		if (submitButtons.length <= 1) {
		    req = form.getRequest();
		} else {
		    req = form.getRequest(submitButtons[TestUtil.generateRandomIndex(submitButtons.length)]);
		}
		callee = form.getMethod().toUpperCase() + Call.SPACE + form.getAction();
		Call.log.debug(callee);
		start = System.currentTimeMillis();
		resp = wc.getResponse(req);
		end = System.currentTimeMillis();
	    } else {
		String absoluteURL = getAbsoluteURL(url);
		WebRequest req;
		if (is == null) {
		    callee = Call.GET + url;
		    req = new GetMethodWebRequest(absoluteURL);
		} else {
		    callee = Call.POST + url;
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
		throw new TestHarnessException(test.testName + " Got http error code " + httpStatusCode);
	    }

	    for (String headerFieldName : resp.getHeaderFieldNames()) {
		if (headerFieldName.equalsIgnoreCase("SET-COOKIE")) {
		    for (String headerFieldValue : resp.getHeaderFields(headerFieldName)) {
			String[] headerFieldSplit = headerFieldValue.split("=|;");
			String cookieName = headerFieldSplit[0];
			if ("JSESSIONID".equalsIgnoreCase(cookieName) && wc.getCookieValue(cookieName) == null) {
			    String cookieValue = headerFieldSplit[1];
			    Call.log.debug("Manually setting cookie: " + cookieName + "=" + cookieValue);
			    wc.putCookie(cookieName, cookieValue);
			}
		    }
		}
	    }

	    return resp;
	} catch (IOException e) {
	    message = Call.NON_HTTP_ERROR;
	    Call.log.debug(e.getMessage(), e);
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    message = Call.NON_HTTP_ERROR;
	    Call.log.debug(e.getMessage(), e);
	    throw new RuntimeException(e);
	} finally {
	    TestReporter.addCallRecord(new CallRecord(test.getTestSuite().getSuiteIndex(), test.testName, test.callType
		    .getName(), callee, description, new SimpleDateFormat("HH:mm:ss SSS").format(new Date(end)), end
		    - start, httpStatusCode, message));
	}
    }

    private SubmitButton[] filterCancelButton(SubmitButton[] sbmtBtns) {
	boolean found = false;
	int i = 0;
	for (; i < sbmtBtns.length; i++) {
	    if (isCancelButton(sbmtBtns[i])) {
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

    private boolean isCancelButton(SubmitButton button) {
	return button.getName().contains("CANCEL") || button.getName().contains("Cancel")
		|| button.getName().contains("cancel") || button.getValue().contains("cancel")
		|| button.getValue().contains("Cancel") || button.getValue().contains("CANCEL");
    }

    /**
     * TODO implement me
     * 
     * @param
     * @return
     */
    private Object executeWS() {
	return null;
    }

    /**
     * TODO implement me
     * 
     * @param
     * @return
     */
    private Object executeRMI() {
	return null;
    }

    private String getAbsoluteURL(String url) {
	if (url.startsWith(Call.HTTP)) {
	    return url;
	}

	String withSlash = url.startsWith("/") ? url : "/" + url;
	String context = url.startsWith(test.getTestSuite().getContextRoot()) ? "" : test.getTestSuite()
		.getContextRoot();
	if (test.getTestSuite().getHttpPort() != 80) {
	    return Call.HTTP + test.getTestSuite().getTargetServer() + Call.COLON + test.getTestSuite().getHttpPort()
		    + context + withSlash;
	} else {
	    return Call.HTTP + test.getTestSuite().getTargetServer() + context + withSlash;
	}
    }

    protected static class CallRecord {

	private int suiteIndex;
	private String testName;
	private String type;
	private String callee;
	private String description;
	private String snapShotTime;
	private long timeInMillis;
	private Integer httpStatusCode;// for Web Call only
	private String message;

	public CallRecord() {
	    // empty constructor
	}

	public CallRecord(int suiteIndex, String testName, String type, String callee, String description,
		String snapShotTime, long timeInMillis, Integer httpStatusCode, String message) {
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

	public String getSnapShotTime() {
	    return snapShotTime;
	}

	public long getTimeInMillis() {
	    return timeInMillis;
	}

	public String getCallee() {
	    return callee;
	}

	public String getType() {
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

	RMI, WS, WEB, UNKNOWN;

	public static CallType get(String value) {
	    if (value.equals("RMI")) {
		return CallType.RMI;
	    } else if (value.equals("WS")) {
		return CallType.WS;
	    } else if (value.equals("WEB")) {
		return CallType.WEB;
	    } else {
		return CallType.UNKNOWN;
	    }
	}

	public String getName() {
	    switch (this) {
	    case RMI:
		return "RMI";
	    case WS:
		return "WS";
	    case WEB:
		return "WEB";
	    default:
		return "Unknown";
	    }
	}
    }

}
