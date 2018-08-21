<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<script language="javascript" type="text/JavaScript">
	function toggleCheckboxes(roleIndex, object){
		<c:forEach var="userBean" items="${userOrgRoleForm.userBeans}" indexId="beanIndex" >
		document.forms.UserOrgRoleForm.elements[roleIndex+1+<c:out value="${numroles}" />*(<c:out value="${beanIndex}" />+1)].checked=object.checked;
		</c:forEach>
	}
	</script>
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.user.assign.roles"/></c:set>
	<lams:Page type="admin" title="${title}" formID="userOrgRoleForm">
				<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
			    <c:if test=${not empty pOrgId}>
			        : <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${pOrgName}"/></a>
			    </c:if>
			    <c:if test="${UserOrgRoleForm.orgId != 1}">
					: <a href="<c:if test="${orgType == 3}">user</c:if><c:if test="${orgType != 3}">org</c:if>manage.do?org=<c:out value="${userOrgRoleForm.orgId}" />" class="btn btn-default">
					<c:out value="${orgName}"/></a>
				</c:if>
				<c:if test="${userOrgRoleForm.orgId == 1}">
					: <a href="usermanage.do?org=<c:out value="${userOrgRoleForm.orgId}"/>" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
				</c:if>
			</h4>
			
			<h1><fmt:message key="admin.user.assign.roles" /></h1>
			
			<p><fmt:message key="msg.roles.mandatory.users"/></p>
			
		<form:form action="userorgrolesave.do" method="post" modelAttribute="userOrgRoleForm" id="userOrgRoleForm">
			<form:hidden path="orgId" />
			
			<table class="table table-condensed table-no-border">
			<tr>
				<th><fmt:message key="admin.user.login"/></th>
				<c:forEach var="role" items="${roles}" indexId="roleIndex">
					<th><input type="checkbox" 
								name="<c:out value="${roleIndex}" />" 
								onclick="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" 
								onkeyup="toggleCheckboxes(<c:out value="${roleIndex}" />, this);" />&nbsp;
						<fmt:message>role.<lams:role role="${role.name}" /></fmt:message></th>
				</c:forEach>
			</tr>
			<c:forEach var="userBean" items="${userOrgRoleForm.userBeans}" indexId="beanIndex">
				<tr>
					<td>
						<c:out value="${userBean.login}" /><c:if test="${!userBean.memberOfParent}"> *<c:set var="parentFlag" value="true" /></c:if>
					</td>
					<c:forEach var="role" items="${roles}">
						<td>
							<form:checkbox id="${userBean.login}Role${role.roleId}" path="userBeans[${beanIndex}].roleIds" value="${role.roleId}" />&nbsp;
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
			</table>
			<c:if test="${parentFlag}">
			<p><fmt:message key="msg.user.add.to.parent.group" /></p>
			</c:if>
			
			<div class="pull-right">
				<input type="submit" name="CANCEL" value="<fmt:message key="admin.cancel"/>" onclick="bCancel=true;" class="btn btn-default">
				<input typye="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
			</div>
		</form:form>
	</lams:Page>

</body>
</lams:html>

