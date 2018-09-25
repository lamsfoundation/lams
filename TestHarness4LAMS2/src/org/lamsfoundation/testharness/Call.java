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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
@SuppressWarnings("deprecation")
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

    static {

	TrustManager defaultTrustManager = new X509TrustManager() {
	    @Override
	    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
		return null;
	    }
	};

	try {
	    SSLContext ctx = SSLContext.getInstance("TLS");
	    ctx.init(new KeyManager[0], new TrustManager[] { defaultTrustManager }, new SecureRandom());
	    SSLContext.setDefault(ctx);

	    HostnameVerifier hv = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
		    return true;
		}
	    };
	    HttpsURLConnection.setDefaultHostnameVerifier(hv);
	} catch (NoSuchAlgorithmException e) {
	    Call.log.error("Error while setting Trust Manager", e);
	} catch (KeyManagementException e) {
	    Call.log.error("Error while setting Trust Manager", e);
	}
    }

    public Call(WebConversation wc, AbstractTest test, String description, String url) {
	this.wc = wc;
	this.test = test;
	this.description = description;
	this.url = url;
    }

    public Call(WebConversation wc, AbstractTest test, String description, String url, InputStream is) {
	this.wc = wc;
	this.test = test;
	this.description = description;
	this.url = url;
	this.is = is;
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
	    WebRequest req = null;

	    if (form != null) {
		SubmitButton[] submitButtons = filterCancelButton(form.getSubmitButtons());
		Call.log.debug(submitButtons.length + " non-cancel submit buttons in the form");
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
		if (is == null) {
		    callee = "GET " + url;
		    req = new GetMethodWebRequest(absoluteURL);
		} else {
		    callee = "POST " + url;
		    req = new PostMethodWebRequest(absoluteURL, is, "application/x-www-form-urlencoded;charset=utf-8");
		}
		Call.log.debug(callee);
		start = System.currentTimeMillis();
		resp = wc.getResponse(req);
		end = System.currentTimeMillis();
	    }

	    httpStatusCode = resp.getResponseCode();
	    if (httpStatusCode >= 400) {
		Call.log.debug("Got " + httpStatusCode + " HTTP code. Retrying call: " + callee);
		resp = wc.getResponse(req);
		end = System.currentTimeMillis();
		httpStatusCode = resp.getResponseCode();
		if (httpStatusCode >= 400) {
		    Call.log.debug(resp.getText());
		    throw new TestHarnessException(test.testName + " got HTTP code " + httpStatusCode);
		}
	    }

	    message = resp.getResponseMessage();
	    // set session cookie manually as the built-in mechanism can't really handle paths and domains
	    for (String headerFieldName : resp.getHeaderFieldNames()) {
		if (headerFieldName.equalsIgnoreCase("SET-COOKIE")) {
		    String cookieValue = null;
		    String path = null;
		    for (String headerFieldValue : resp.getHeaderFields(headerFieldName)) {
			String[] headerFieldSplit = headerFieldValue.split("=|;");
			for (int paramIndex = 0; paramIndex < headerFieldSplit.length; paramIndex++) {
			    String param = headerFieldSplit[paramIndex].trim();
			    if ("JSESSIONID".equalsIgnoreCase(param)) {
				cookieValue = headerFieldSplit[paramIndex + 1];
				paramIndex++;
			    } else if ("path".equalsIgnoreCase(param)) {
				path = headerFieldSplit[paramIndex + 1];
				paramIndex++;
			    }
			}
		    }
		    if (cookieValue != null) {
			Call.log.debug("Manually setting JSESSIONID = " + cookieValue);
			// "single use cookie" is misleading; it's just a cookie with a path and domain
			wc.getCookieJar().putSingleUseCookie("JSESSIONID", cookieValue,
				test.getTestSuite().getCookieDomain(), path);
		    }
		}
	    }

	    return resp;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    TestReporter.getCallRecords()
		    .add(new CallRecord(test.getTestSuite().getSuiteIndex(), test.testName, callee, description,
			    new SimpleDateFormat("HH:mm:ss SSS").format(new Date(end)), end - start, httpStatusCode,
			    message));
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
	if (url.startsWith("http")) {
	    return url;
	}

	String withSlash = url.startsWith("/") ? url : "/" + url;
	String context = url.startsWith(test.getTestSuite().getContextRoot()) ? ""
		: test.getTestSuite().getContextRoot();
	String targetServer = test.getTestSuite().getTargetServer();
	if (!targetServer.startsWith("http")) {
	    targetServer = "http://" + targetServer;
	}
	return targetServer + context + withSlash;
    }
}