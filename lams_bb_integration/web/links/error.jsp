<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page isErrorPage = "true" %>
<%
	String strException = exception.getMessage();
	response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
%>		
<bbUI:receipt type="FAIL" title="Error">
<%=strException%>
<p>
<pre>
<%
	// now display a stack trace of the exception
  PrintWriter pw = new PrintWriter( out );
  exception.printStackTrace( pw );
%>
</pre>
</bbUI:receipt><br>

