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
	
	<script type="text/JavaScript">
		function toggleCheckboxes(roleIndex, object){
			<c:forEach var="userBean" items="${userOrgRoleForm.userBeans}" varStatus="beanIndex" >
			document.forms.userOrgRoleForm.elements[roleIndex+2+<c:out value="${numroles}"/>*(<c:out value="${beanIndex.index}"/>+1)].checked=object.checked;
			</c:forEach>
		}
	</script>
</lams:head>
    
<body class="stripes">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1" | <fmt:message key="admin.course.manage" /></c:set>
    <c:if test="${not empty pOrgId}">
    	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${pOrgId} | <c:out value="${pOrgCode}" escapeXml="true"/> : <c:out value="${pOrgName}" escapeXml="true"/></c:set>
    </c:if>
    <c:if test="${userOrgForm.orgId != 1}">
    	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/<c:if test="${orgType == 3}">user</c:if><c:if test="${orgType != 3}">org</c:if>manage.do?org=${userOrgForm.orgId} | <c:out value="${orgCode}" escapeXml="true"/> : <c:out value="${orgName}" escapeXml="true"/></c:set>
    	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/userorg.do?orgId=${userOrgForm.orgId} | <fmt:message key="admin.user.management"/></c:set>
    	
	</c:if>
	<c:if test="${userOrgForm.orgId == 1}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usermanage.do?org=${userOrgForm.orgId} | <fmt:message key="admin.global.roles.manage" /></c:set>
	</c:if>	
	<c:set var="breadcrumbItems">${breadcrumbItems}, . |<fmt:message key="admin.user.assign.roles"/></c:set>

	<c:set var="title">${title}: <fmt:message key="admin.user.assign.roles"/></c:set>
	<lams:Page type="admin" breadcrumbItems="${breadcrumbItems}" formID="userOrgRoleForm">
		<div class="row">
			<div class="col bg-light ml-3 mr-3 p-4">
	
				<h1><fmt:message key="admin.user.assign.roles"/></h1>
				
				<p><fmt:message key="msg.roles.mandatory.users"/></p>
				
				<form:form action="userorgrolesave.do" modelAttribute="userOrgRoleForm" id="userOrgRoleForm" method="post">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="orgId" />
				
				<table class="table table-striped table-hover">
				<thead class="thead-light">
				<tr>
					<th scope="col" style="width: 20%"><fmt:message key="admin.user.login"/></th>
					<c:forEach var="role" items="${roles}" varStatus="roleIndex">
						<th scope="col" style="width: 20%"><input aria-label="set role ${role.name} for all users" type="checkbox" 
									name="<c:out value="${roleIndex.index}" />" 
									onclick="toggleCheckboxes(<c:out value="${roleIndex.index}" />, this);" 
									onkeyup="toggleCheckboxes(<c:out value="${roleIndex.index}" />, this);" />&nbsp;
							<fmt:message>role.<lams:role role="${role.name}" /></fmt:message></th>
					</c:forEach>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="userBean" items="${userOrgRoleForm.userBeans}" varStatus="beanIndex">
					<tr scope="row">
						<td>
							<c:out value="${userBean.login}" /><c:if test="${!userBean.memberOfParent}"> *<c:set var="parentFlag" value="true" /></c:if>
						</td>
						<c:forEach var="role" items="${roles}">
							<td>
								<input type="checkbox" aria-label="${userBean.login} ${role.name}" id="${userBean.login}Role${role.roleId}" name="userBeans[${beanIndex.index}].roleIds" value="${role.roleId}" />&nbsp;
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
				</tbody>
				</table>
				<c:if test="${parentFlag}">
				<p><fmt:message key="msg.user.add.to.parent.group" /></p>
				</c:if>
				
				<div class="pull-right">
					<a href="<lams:LAMSURL/>admin/userorg.do?orgId=${userOrgForm.orgId}" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
					<input type="submit" id="saveButton" class="btn btn-primary pl-2" value="<fmt:message key="admin.save"/>" />
				</div>
				</form:form>
			</div>
		</div>	
	</lams:Page>
</body>
</lams:html>
