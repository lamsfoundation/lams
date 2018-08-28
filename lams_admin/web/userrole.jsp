<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.entry"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.user.assign.roles"/></c:set>
	<lams:Page type="admin" title="${title}" formID="userRolesForm">
	
		<form:form action="./userrolessave.do" modelAttribute="userRolesForm" id="userRolesForm" method="post">
		<form:hidden path="userId" />
		<form:hidden path="orgId" />
		<p>
			<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
			<c:if test="${not empty pOrgId}">
				: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${parentName}" /></a>
				: <a href="usermanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><c:out value="${orgName}" /></a>
			</c:if>
			<c:if test="${empty pOrgId}">
				<c:if test="${userRolesForm.orgId != 1}">
					: <a href="orgmanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><c:out value="${orgName}" /></a>
				</c:if>
				<c:if test="${userRolesForm.orgId == 1}">
					: <a href="usermanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
				</c:if>
			</c:if>
		</p>
		
		<p><fmt:message key="msg.roles.mandatory"/></p>
		
		<div align="center">
		<c:set var="errorKey" value="roles" /> 
			<c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
				 <lams:Alert id="error" type="danger" close="false"> 
				 <c:forEach var="error" items="${errorMap[errorKey]}"> 
					 <c:out value="${error}" /><br /> 
				 </c:forEach> 
				</lams:Alert> 
			</c:if>
		</div>
		
		<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2"><fmt:message key="admin.user.login"/>:</div>
		  <div class="col-xs-10"><c:out value="${login}" /></div>
		</div>
		
		<div class="row">
		  <div class="col-xs-2"><fmt:message key="admin.user.name"/>:</div>
		  <div class="col-xs-10"><c:out value="${fullName}" /></div>
		</div>
		
		<div class="row">
		  <div class="col-xs-2"><fmt:message key="admin.user.roles"/>:</div>
		  <div class="col-xs-10">            
		  	<c:forEach items="${rolelist}" var="role">
		    	<form:checkbox path="${userRolesForm.roles}" value="${role.roleId}"/>
		        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message><br/>
		    </c:forEach>
		  </div>
		</div>
		</div>
		
		<div class="pull-right">
			<a href="usermanage.do?org=1" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
			<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
			<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
		</div>
		
		</form:form>
	</lams:Page>


</body>
</lams:html>

