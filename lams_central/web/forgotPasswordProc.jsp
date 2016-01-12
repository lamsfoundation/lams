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
String languageKey = StringEscapeUtils.escapeHtml(request.getParameter("languageKey"));
String stateStr = request.getParameter("state");
String emailStr = request.getParameter("emailSent");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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

<body class="stripes">
	<div id="page">
		<!--main box 'page'-->

		<div id="header-no-tabs"></div>
		<div id="content" align="center">
			<h1 class="no-tabs-below">
				<fmt:message key="label.forgot.password.confirm" />
			</h1>


			<%
	    	if (stateStr.equals("0")) {
	    %>
			<p class="warning">
				<%
	    	} else {
	    %>
				<p class="info">
					<% 
	    	}
	    %>
					<fmt:message key="<%=languageKey%>" />

					<%
	   		if (emailStr!=null && !emailStr.equals(""))
		    {
		    	out.print(emailStr);
		    }
		%>
				</p>

				<br>
				<html:button property="cancel" styleClass="button" onclick="javascript:toHome();">
					<fmt:message key="label.ok" />
				</html:button>
		</div>

		<div id="footer">
			<div id="footer"></div>
			<!--closes footer-->

		</div>
</body>

</lams:html>
