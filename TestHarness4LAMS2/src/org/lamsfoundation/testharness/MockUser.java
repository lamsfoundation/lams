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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.protocol.UploadFileSpec;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class MockUser {

    private static final Logger log = Logger.getLogger(MockUser.class);

    private static final String[] DELAY_MESSAGES = { " is deserting ", " is napping ", " is pondering ",
	    " is in a daze ", " will have a cup of coffee for ", " is away for toilet " };

    private static final String LOGIN_PAGE_FLAG = "j_security_check";
    private static final String INDEX_PAGE_FLAG = "Index page flag";

    private static final String USERNAME = "j_username";
    private static final String PASSWORD = "j_password";

    protected AbstractTest test;
    protected String userId;
    protected String username;
    protected String password;
    protected String role;
    protected WebConversation wc;

    public MockUser(AbstractTest test, String username, String password, String role, String userId) {
	this.test = test;
	this.username = username;
	this.role = role;
	this.password = password;
	this.userId = userId;
    }

    protected static boolean checkPageContains(WebResponse resp, String flag) throws IOException {
	return resp.getText().indexOf(flag) != -1;
    }

    public final String getPassword() {
	return password;
    }

    public final String getUserId() {
	return userId;
    }

    public final String getUsername() {
	return username;
    }

    /**
     * Login to the system.
     *
     * @exception TestHarnessException
     *                :failure
     */
    public void login() {
	try {
	    wc = new WebConversation();
	    WebResponse resp = (WebResponse) new Call(wc, test, username + " fetch index page", "").execute();
	    if (!MockUser.checkPageContains(resp, MockUser.LOGIN_PAGE_FLAG)) {
		MockUser.log.debug(resp.getText());
		throw new TestHarnessException(username + " didn't get login page when hitting LAMS the first time");
	    }
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(MockUser.USERNAME, username);
	    params.put(MockUser.PASSWORD, password);
	    resp = (WebResponse) new Call(wc, test, username + " login", fillForm(resp, 0, params)).execute();
	    if (!MockUser.checkPageContains(resp, MockUser.INDEX_PAGE_FLAG)) {
		MockUser.log.debug(resp.getText());
		Thread.sleep(1000);
		resp = (WebResponse) new Call(wc, test, username + " ping again", "").execute();
		MockUser.log.debug(resp.getText());
		throw new TestHarnessException(username + " failed to login with password " + password);
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public final void setUserId(String userId) {
	this.userId = userId;
    }

    protected void delay() {
	try {
	    int seconds = 0;
	    if (test.getMaxDelay() <= test.getMinDelay()) {// to avoid IllegalArgumentException in nextInt method on
							   // Random object
		seconds = test.getMinDelay();
	    } else {
		seconds = test.getMinDelay()
			+ TestUtil.generateRandomNumber((test.getMaxDelay() - test.getMinDelay()) + 1);
	    }
	    if (seconds > 0) {
		MockUser.log.info(username
			+ MockUser.DELAY_MESSAGES[TestUtil.generateRandomNumber(MockUser.DELAY_MESSAGES.length)]
			+ seconds + " seconds");
		Thread.sleep(seconds * 1000);
	    }
	} catch (InterruptedException e) {
	    MockUser.log.error("Interrupted exception");
	}
    }

    protected WebForm fillForm(WebResponse resp, int formIndex, Map<String, Object> params)
	    throws SAXException, IOException {
	WebForm[] forms = resp.getForms();
	if ((forms == null) || (forms.length <= formIndex)) {
	    MockUser.log.debug(resp.getText());
	    throw new TestHarnessException(username + " cannot find a form with index " + formIndex);
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
		    throw new TestHarnessException(
			    "Unsupported parameter value type:" + entry.getValue().getClass().getName());
		}
	    }
	}
	return form;
    }

    @Override
    public boolean equals(Object o) {
	return username.equals(((MockUser) o).username);
    }

    @Override
    public int hashCode() {
	return username.hashCode();
    }

    public String getRole() {
	return role;
    }
}