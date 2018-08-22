<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.loginpage"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}" formID="loginMaintainForm">
		
		<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>
		
		<c:set var="errorKey" value="GLOBAL" />
	 	  <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
	      <lams:Alert id="error" type="danger" close="false">
	        <c:forEach var="error" items="${errorMap[errorKey]}">
	           <c:out value="${error}" />
	        </c:forEach>
	      </lams:Alert>
		  </c:if>
		
		<form:form action="./loginsave.do" modelAttribute="loginMaintainForm" id="loginMaintainForm" enctype="multipart/form-data" method="post">
		
		<c:set var="language"><lams:user property="localeLanguage"/></c:set>
		<p class="help-block"><fmt:message key="sysadmin.login.text"/></p>
		
		<lams:CKEditor id="news" value="${loginMaintainForm.news}" contentFolderID="../public" height="600px"></lams:CKEditor>
		
		<div class="pull-right voffset5">
			<input type="submit" name="CANCEL" value="<fmt:message key="admin.cancel"/>" onclick="bCancel=true;" class="btn btn-default">
			<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
		</div>
		</form:form>
	</lams:Page>

</body>
</lams:html>


