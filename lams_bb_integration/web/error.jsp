<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--
    Original Version: 2007 LAMS Foundation
    Updated for Blackboard 9.1 SP6 (including new bbNG tag library) 2011
    Richard Stals (www.stals.com.au)
    Edith Cowan University, Western Australia
--%>
<%--
    General Error Page
    Print out the Exception Messsage and full stack trace
--%>
<%@ page import="java.io.PrintWriter"%>
<%@ page isErrorPage = "true" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%
	String strException = exception.getMessage();
	response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
%>		
<bbNG:genericPage title="Error">	

    <bbNG:receipt type="FAIL" 
        iconUrl="/images/ci/icons/receiptfail_u.gif" 
        title="Error">
            <%=strException%>
            <p>
                <pre>
                    <%  // Display a stack trace of the exception
                        PrintWriter pw = new PrintWriter( out );
                        exception.printStackTrace( pw );
                    %>
                </pre>
            </p>
    </bbNG:receipt>

</bbNG:genericPage>