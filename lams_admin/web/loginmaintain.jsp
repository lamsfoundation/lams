<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain.loginpage"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}" formID="loginMaintainForm">
		
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="sysadmin.maintain.loginpage"/></li>
		  </ol>
		</nav>
		<div class="col-xl">
		<form:form action="./loginsave.do" modelAttribute="loginMaintainForm" id="loginMaintainForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<c:set var="language"><lams:user property="localeLanguage"/></c:set>
			<p><fmt:message key="sysadmin.login.text"/></p>
			
			<lams:CKEditor id="news" value="${loginMaintainForm.news}" contentFolderID="../public" height="600px"></lams:CKEditor>
			
			<div class="pull-right voffset5">
				<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
					<fmt:message key="admin.cancel"/>
				</a>
				<input type="submit" id="saveButton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="admin.save" />" />
			</div>
		</form:form>
		</div>
		
	</lams:Page>
</body>
</lams:html>
