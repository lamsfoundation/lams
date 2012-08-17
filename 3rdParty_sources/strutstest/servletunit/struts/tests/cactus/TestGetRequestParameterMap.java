package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

import java.util.Map;

public class TestGetRequestParameterMap extends CactusStrutsTestCase {

    public void testGetParameterMap() {
        addRequestParameter("foo","bar");
        assertEquals("bar",getRequest().getParameter("foo"));
        Map parameterMap = getRequest().getParameterMap();
        assertNotNull(parameterMap);
        assertEquals("bar",parameterMap.get("foo"));
    }

}
