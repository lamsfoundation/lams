<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    Error Page for access denied errors
--%>
<%@ page import="java.io.PrintWriter"%>
<%@ page isErrorPage = "true" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Access Denied">	
    <bbNG:receipt type="FAIL" 
        iconUrl="/images/ci/icons/receiptfail_u.gif" 
        title="Access Denied">
            <h3> Your current user privileges do not allow you to access this content. </h3>
    </bbNG:receipt>
</bbNG:genericPage>