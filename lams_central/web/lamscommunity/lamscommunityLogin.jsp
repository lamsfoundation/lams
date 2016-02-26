<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="index.welcome" /></title>
	<lams:css/>
	
	<script src="<lams:LAMSURL/>/includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="<lams:LAMSURL/>/includes/javascript/jquery.validate.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
		function submitForm() {
			var valid = $("#lamsCommunityLoginForm").valid();
			
			if (valid) {
				document.getElementById("lamsCommunityLoginForm").submit();
			}
		}
		
		function init() {
			document.getElementById("lcPassword").value = "";
		}
		
		function onEnter(event){
			intKeyCode = event.keyCode;
			if (intKeyCode == 13) {
				submitForm();
			}
		}
		
		$().ready(function() {
			// validate
			$("#lamsCommunityLoginForm").validate({
				rules: {
					lcUserName: "required",
					lcPassword: "required",
				},
				messages: {
					lcUserName: ' <font color="red"><fmt:message key="label.required"/></font>',
					lcPassword: ' <font color="red"><fmt:message key="label.required"/></font>',
				}
			});
		});
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
		
		<br />
		<p>
			<fmt:message key="label.lamscommunity.info"/>
		</p>
		<br />
		
		<html:form action="/lamsCommunityLogin" method="post" styleId="lamsCommunityLoginForm">
			
			<html:hidden property="dispatch" value="authenticate" />
			<html:hidden property="dest" />
			<p>
				<fmt:message key="label.lamscommunity.login"/>&nbsp;<html:text property="lcUserName" tabindex="1" onkeypress="onEnter(event)"></html:text>
			</p>
			
			<p>
				<fmt:message key="label.lamscommunity.password"/>&nbsp;<html:password property="lcPassword" styleId="lcPassword" tabindex="2" onkeypress="onEnter(event)"></html:password>
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