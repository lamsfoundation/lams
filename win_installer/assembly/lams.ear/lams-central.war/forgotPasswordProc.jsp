<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.lamsfoundation.lams.web.ForgotPasswordServlet"%>
<%@page import="org.lamsfoundation.lams.util.MessageService"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%
	String languageKey = StringEscapeUtils.escapeHtml(request
			.getParameter("languageKey"));
	String stateStr = request.getParameter("state");
	String emailStr = request.getParameter("emailSent");
%>

<c:set var="languageKey" scope="request">
	<%=languageKey%>
</c:set>
<c:set var="stateStr" scope="request">
	<%=stateStr%>
</c:set>
<c:set var="emailStr" scope="request">
	<%=emailStr%>
</c:set>


<!DOCTYPE html>
<lams:html>

<lams:head>
	<lams:css />
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>

<script language="javascript" type="text/javascript">
	function toHome() {
		var isSuccess = "<c:out value='${stateStr}' />";
		if (isSuccess == "1") {
			window.location = "<lams:LAMSURL/>index.do";
		} else {
			window.location = "<lams:LAMSURL/>forgotPassword.jsp";
		}
	};
</script>

<c:set var="title">
	<fmt:message key="title.forgot.password" />
</c:set>

<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<h4>
			<fmt:message key="label.forgot.password.confirm" />
		</h4>


		<c:set var="type" value="info" />
		<c:if test="${stateStr == 0}">
			<c:set var="type" value="danger" />
		</c:if>
		
		<lams:Alert id="output" type="${type}" close="false">

			<fmt:message key="${languageKey}" />

		</lams:Alert>

		<html:button property="cancel" styleClass="btn btn-primary pull-right voffset10" onclick="javascript:toHome();">
			<fmt:message key="label.ok" />
		</html:button>


	</lams:Page>
</body>

</lams:html>
