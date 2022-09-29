<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.maintain.loginpage"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4">

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="appadmin.maintain.loginpage"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>


	<lams:Page5 type="admin" title="${title}" formID="loginMaintainForm" breadcrumbItems="${breadcrumbItems}">
	
		<form:form action="./loginsave.do" modelAttribute="loginMaintainForm" id="loginMaintainForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<c:set var="language"><lams:user property="localeLanguage"/></c:set>
			<h4 class="text-center"><fmt:message key="appadmin.login.text"/></h4>
			
			<lams:CKEditor id="news" value="${loginMaintainForm.news}" contentFolderID="../public" height="600px"></lams:CKEditor>
			
			<div class="mt-3 text-end">
				<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary"><fmt:message key="admin.cancel"/></a>
				<button class="btn btn-primary">
					<fmt:message key="admin.save" /> 
				</button>
			</div>	

		</form:form>
		
	</lams:Page5>
</body>
</lams:html>
