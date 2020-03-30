<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.entry"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.user.assign.roles"/></c:set>
	
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /> </c:set>

	<c:if test="${not empty pOrgId}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${pOrgId} | <c:out value="${parentName}" escapeXml="true"/> </c:set>
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usermanage.do?org=${userRolesForm.orgId} |<c:out value="${orgName}" escapeXml="true"/></c:set>
	</c:if>
	<c:if test="${empty pOrgId}">
		<c:if test="${userRolesForm.orgId != 1}">
			<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${userRolesForm.orgId} | <c:out value="${orgName}" escapeXml="true"/></c:set>
		</c:if>
		<c:if test="${userRolesForm.orgId == 1}">
			<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usermanage.do?org=${userRolesForm.orgId} | <fmt:message key="admin.global.roles.manage" />  </c:set>
		</c:if>
		
	</c:if>
	
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.user.assign.roles"/></c:set>
	
	
	<lams:Page type="admin" title="${title}"  breadcrumbItems="${breadcrumbItems}"  formID="userRolesForm">
	
		<form:form action="/lams/admin/userrolessave.do" modelAttribute="userRolesForm" id="userRolesForm" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="orgId" />
		<form:hidden path="userId" />
		
		<p>
			<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
			<c:if test="${not empty pOrgId}">
				: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${parentName}" /></a>
				: <a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><c:out value="${orgName}" /></a>
			</c:if>
			<c:if test="${empty pOrgId}">
				<c:if test="${userRolesForm.orgId != 1}">
					: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><c:out value="${orgName}" /></a>
				</c:if>
				<c:if test="${userRolesForm.orgId == 1}">
					: <a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
				</c:if>
			</c:if>
		</p>
		
		<p><fmt:message key="msg.roles.mandatory"/></p>
		
		<lams:errors/>
		<lams:errors path="roles"/>
		
		<div class="container">
		<div class="row">
		  <div class="col-2"><fmt:message key="admin.user.login"/>:</div>
		  <div class="col-10"><c:out value="${login}" escapeXml="true"/></div>
		</div>
		
		<div class="row">
		  <div class="col-2"><fmt:message key="admin.user.name"/>:</div>
		  <div class="col-10"><c:out value="${fullName}" escapeXml="true"/></div>
		</div>
		
		<div class="row">
		  <div class="col-2"><fmt:message key="admin.user.roles"/>:</div>
		  <div class="col-10">            
		  	<c:forEach items="${rolelist}" var="role" varStatus="index">
		    	<input type="checkbox" name="roles" value="${role.roleId}" 
		    	<c:forEach items="${userRolesForm.roles}" var="userRole">
		    		<c:if test="${userRole == role.roleId}">
		    			checked="checked"
		    		</c:if>
		    	</c:forEach>
		    	/>
		        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message><br/>
		    </c:forEach>
		  </div>
		</div>
		</div>
		
		<div class="pull-right">
			<a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userRolesForm.orgId}" />" class="btn btn-outline-secondary"><fmt:message key="admin.cancel"/></a>
			<input type="submit" name="submitbutton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
		</div>
		
		</form:form>
	</lams:Page>
</body>
</lams:html>
