<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Error Page for LAMS Integration Errors (Communication timeouts etc.)
--%>
<%@ page import="java.io.PrintWriter"%>
<%@ page isErrorPage = "true" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbNG:genericPage title="LAMS Server Timeout">	
    <bbNG:receipt type="FAIL" 
        	iconUrl="/images/ci/icons/receiptfail_u.gif" 
        	title="LAMS Server Timeout">
		<h3>Did not get a response from the LAMS server. Please contact your systems administrator.</h3>
    </bbNG:receipt>
</bbNG:genericPage>