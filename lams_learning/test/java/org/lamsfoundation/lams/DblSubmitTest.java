/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams;

import junit.framework.TestCase;
import com.meterware.httpunit.*;
import com.meterware.servletunit.*;

/**
 * @author daveg
 *
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
