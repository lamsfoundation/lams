<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="index.welcome" /></title>
	<lams:css/>
	<script language="JavaScript" type="text/javascript">
		function submitForm()
		{
			document.getElementById("lamsCommunityLogin").submit();
		}
		
		function init()
		{
			document.getElementById("lcPassword").value = "";
		}
	</script>
	
</lams:head>

<body style="text-align:center" onload="init()">

			<c:choose>
				<c:when test="${empty errorMessage}">
					<p class="info">
						<fmt:message key="label.lamscommunity.message"/>
					</p>
				</c:when>
				<c:otherwise>
					<p class="warning">
						<fmt:message key="${errorMessage}"/>
					</p>
				</c:otherwise>
			</c:choose>
		<html:form action="/lamsCommunityLogin" method="post" styleId="lamsCommunityLogin">
			
			<html:hidden property="dispatch" value="authenticate" />
			<html:hidden property="lcDest" />
			<p>
				<fmt:message key="label.lamscommunity.login"/>&nbsp;<html:text property="lcUserName" tabindex="1"></html:text>
			</p>
			
			<p>
				<fmt:message key="label.lamscommunity.password"/>&nbsp;<html:password property="lcPassword" styleId="lcPassword" tabindex="2"></html:password>
			</p>
			
			<p class="login-button">
				<a href="javascript:submitForm()" class="button" tabindex="3"/><fmt:message key="button.login"/></a>
			</p>
			
			<br />
			<br />
			<br />
		
		</html:form>
</body>
</lams:html>