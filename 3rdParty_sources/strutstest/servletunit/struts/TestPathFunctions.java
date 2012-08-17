package servletunit.struts;

import junit.framework.TestCase;

public class TestPathFunctions extends TestCase {

    public void testStripSessionId() {
        String path = "/my/path;jsessionid=123456789";
        path = Common.stripJSessionID(path);
        assertEquals("/my/path",path);
    }

    public void testStripLongSessionId() {
        String path = "/my/path;jsessionid=99999999999999999999999999999999999999999999999999999999999";
        path = Common.stripJSessionID(path);
        assertEquals("/my/path",path);
    }

    public void testStripSessionIdWithQueryString() {
        String path = "/my/path;jsessionid=123456789?param=\"true\"";
        path = Common.stripJSessionID(path);
        assertEquals("/my/path?param=\"true\"",path);
    }
}
