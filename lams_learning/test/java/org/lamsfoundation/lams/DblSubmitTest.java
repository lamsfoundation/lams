/*
 * Created on 7/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams;

import junit.framework.TestCase;
import com.meterware.httpunit.*;
import com.meterware.servletunit.*;

/**
 * @author daveg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DblSubmitTest extends TestCase {
	
	private static String CONFIG_PATH = "/WEB-INF/";
	
	public DblSubmitTest() {
	}

    public void testDoubleSubmit() throws Exception {
        WebConversation wc = new WebConversation();
        WebRequest req = new GetMethodWebRequest("http://127.0.0.1:8080/lams_learning/test/DblSubmit.do");
        WebResponse displayResp = wc.getResponse(req);
        System.out.println(displayResp);
        WebForm[] forms = displayResp.getForms();
        if ((forms == null) || (forms.length == 0)) {
        	assertFalse(true);
        	return;
        }
        WebForm form = forms[0];
        WebResponse submit1Resp = form.submit();
        String text1 = submit1Resp.getText();
        int validPos = text1.indexOf("Valid=true");
        if (validPos == -1) {
        	assertFalse(true);
        	return;
        }
        WebResponse submit2Resp = form.submit();
        String text2 = submit1Resp.getText();
        validPos = text2.indexOf("Valid=false");
        if (validPos > -1) {
        	assertFalse(true);
        	return;
        }
    }

}
