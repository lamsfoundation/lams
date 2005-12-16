<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
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
