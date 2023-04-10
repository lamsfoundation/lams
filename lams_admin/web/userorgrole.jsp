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
	<c:set var="title">${title}: <fmt:message key="admin.user.assign.roles"/></c:set>
	<lams:Page type="admin" title="${title}" formID="userOrgRoleForm">
		
		<p><a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
		<c:set var="cancelUrl"><lams:LAMSURL/>admin/orgmanage.do?org=1</c:set>
	    <c:if test="${not empty pOrgId}">
	        : <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${pOrgName}"/></a>
	    </c:if>
	    <c:if test="${userOrgRoleForm.orgId != 1}">
	    	<c:set var="cancelUrl"><lams:LAMSURL/>admin/<c:if test="${orgType == 3}">user</c:if><c:if test="${orgType != 3}">org</c:if>manage.do?org=<c:out value="${userOrgRoleForm.orgId}" /></c:set>
			: <a href="${cancelUrl}" class="btn btn-default">
			<c:out value="${orgName}"/></a>
		</c:if>
		<c:if test="${userOrgRoleForm.orgId == 1}">
			<c:set var="cancelUrl"><lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userOrgRoleForm.orgId}" /></c:set>
			: <a href="${cancelUrl}" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
		</c:if>
		
		<h3><fmt:message key="admin.user.assign.roles" /></h3>
		
		<p><fmt:message key="msg.roles.mandatory.users"/></p>
		
		<form:form action="userorgrolesave.do" modelAttribute="userOrgRoleForm" id="userOrgRoleForm" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="orgId" />
		
		<label>
			<input type="checkbox" name="addToLessons" />&nbsp;<fmt:message key="admin.user.add.to.lessons"/>
		</label>
		
		<table class="table table-condensed table-striped table-hover voffset10">
		<tr>
			<th style="width: 20%"><fmt:message key="admin.user.login"/></th>
			<c:forEach var="role" items="${roles}" varStatus="roleIndex">
				<th style="width: 20%"><input type="checkbox" 
							name="<c:out value="${roleIndex.index}" />" 
							onclick="toggleCheckboxes(<c:out value="${roleIndex.index}" />, this);" 
							onkeyup="toggleCheckboxes(<c:out value="${roleIndex.index}" />, this);" />&nbsp;
					<fmt:message>role.<lams:role role="${role.name}" /></fmt:message></th>
			</c:forEach>
		</tr>
		<c:forEach var="userBean" items="${userOrgRoleForm.userBeans}" varStatus="beanIndex">
			<tr>
				<td>
					<c:out value="${userBean.login}" /><c:if test="${!userBean.memberOfParent}"> *<c:set var="parentFlag" value="true" /></c:if>
				</td>
				<c:forEach var="role" items="${roles}">
					<td>
						<input type="checkbox" id="${userBean.login}Role${role.roleId}" name="userBeans[${beanIndex.index}].roleIds" value="${role.roleId}" />&nbsp;
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
		</table>
		<c:if test="${parentFlag}">
		<p><fmt:message key="msg.user.add.to.parent.group" /></p>
		</c:if>
		
		<div class="pull-right">
			<a href="${cancelUrl}" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
			<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
		</div>
		</form:form>
	</lams:Page>
</body>
</lams:html>
