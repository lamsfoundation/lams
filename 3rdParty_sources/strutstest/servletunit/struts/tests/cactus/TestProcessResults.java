package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;
import org.apache.cactus.WebResponse;

public class TestProcessResults extends CactusStrutsTestCase {

    public void testSuccessfulLogin() {
        processRequest(true);
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
	    verifyForwardPath("/main/success.jsp");
        assertEquals("deryl", getSession().getAttribute("authentication"));
        verifyNoActionErrors();
    }

    public void endSuccessfulLogin(WebResponse response) {
        assertEquals("unexpected response code",200,response.getStatusCode());
        String[] text = response.getTextAsArray();
        for (int i = 0; i < text.length; i++) {
            String line = text[i];
            if (line != null && 
                    !line.equals("<h2>You have successfully logged in!</h2>") &&
                    !line.equals("<a href=\"/test/login/login.jsp\">Go back</a>") &&
                    !line.equals(""))
                fail("unexpected text: " + line);

        }
    }
}
