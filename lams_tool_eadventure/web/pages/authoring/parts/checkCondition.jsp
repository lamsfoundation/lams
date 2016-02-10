<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />
	</lams:head>
	<body class="tabpart">
	
	<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />	
	<table>
	<c:forEach var="expressionInfo" items="${sessionMap.exprsInfo}"
			varStatus="status">
			
	</c:forEach>		
	</table>
	
	
	</body>
</lams:html>