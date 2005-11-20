<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String root = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToLams = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title></title>
</head>
<body>

	<table align=center> 	  
	<tr>   
	<td class=error>
		<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
	</td>
	</tr> 
	</table>


<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
		<c:choose> 
		  <c:when test="${sessionScope.questionListingMode == sessionScope.questionListingModeSequential}" > 
				<jsp:include page="SingleQuestionAnswersContent.jsp" /> 
		  </c:when> 
		  <c:otherwise>
			  	<jsp:include page="CombinedAnswersContent.jsp" /> 
		  </c:otherwise>
		</c:choose> 
</html:form>

</body>
</html:html>
