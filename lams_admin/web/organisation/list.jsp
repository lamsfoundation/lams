<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-1.1.4.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pager.js"></script>
<script>
	<!--
	jQuery(document).ready(function() {
		jQuery("table.tablesorter-admin").tablesorter({widthFixed:true, sortList:[[1,0]], textExtraction:'complex'})
			.tablesorterPager({container: jQuery("#pager")});
	});
	//-->
</script>

<style>
table.infoDisplay {
	margin-left:5px; padding-top:10px; width:100%;
}
p {
	margin-left:5px;
}
.floatRight {
	float:right;
}
.indentPad {
	margin-left:5px; padding-top:10px;
}
</style>

<logic:equal name="OrgManageForm" property="type" value="1">
	<h4>
		<a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><fmt:message key="admin.course.manage" /></a>
	</h4>
	<h1><fmt:message key="admin.course.manage" /></h1>
	
	<form>
	
	<div class="floatRight">
		<input class="button" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit' />
		<input class="button" type="button" value="<fmt:message key="admin.user.find"/>" onclick=javascript:document.location='usersearch.do' />
		<logic:equal name="manageGlobalRoles" value="true">
			<input class="button" type="button" value="<fmt:message key="admin.global.roles.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
		</logic:equal>
	</div>
	<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
	<div class="floatRight">
		<logic:equal name="createGroup" value="true">
			<c:url var="editaction" value="organisation.do">
				<c:param name="method" value="create" />
				<c:param name="typeId" value="2" />
				<c:param name="parentId" value="${OrgManageForm.parentId}" />
			</c:url>
			<input class="button" type="button" value="<fmt:message key="admin.course.add"/>" onclick=javascript:document.location='<c:out value="${editaction}"/>' />
		</logic:equal>
	</div>
	</form>
	<html:form styleClass="indentPad" action="orgmanage.do" method="post">
		<input type="hidden" name="org" value="<bean:write name="OrgManageForm" property="parentId"/>" />
		<fmt:message key="label.show"/> <html:select property="stateId" onchange="document.OrgManageForm.submit();">
			<html:option value="1"><fmt:message key="organisation.state.ACTIVE"/></html:option>
			<html:option value="2"><fmt:message key="organisation.state.HIDDEN"/></html:option>
			<html:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></html:option>
		</html:select> <fmt:message key="label.groups"/>:
	</html:form>
</logic:equal>

<logic:equal name="OrgManageForm" property="type" value="2">
	<h4>
		<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
		: <a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><bean:write name="OrgManageForm" property="parentName"/></a>
	</h4>
	<h1><bean:write name="OrgManageForm" property="parentName"/></h1>
	
	<table cellspacing="7" class="infoDisplay">
		<tr>
			<td align="right"><fmt:message key="admin.organisation.name"/>:</td>
			<td><c:out value="${org.name}" /></td>
			<td align="right"><fmt:message key="admin.organisation.status"/>:</td>
			<td width="10%"><c:out value="${org.organisationState.description}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.code"/>:</td>
			<td><c:out value="${org.code}" /></td>
			<td align="right"><fmt:message key="admin.can.add.user"/>:</td>
			<td><c:out value="${org.courseAdminCanAddNewUsers}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.description"/>:</td>
			<td><c:out value="${org.description}" /></td>
			<td align="right"><fmt:message key="admin.can.browse.user"/>:</td>
			<td><c:out value="${org.courseAdminCanBrowseAllUsers}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.locale"/>:</td>
			<td><c:out value="${org.locale.description}" /></td>
			<td align="right"><fmt:message key="admin.can.change.status"/>:</td>
			<td><c:out value="${org.courseAdminCanChangeStatusOfCourse}" /></td>
		</tr>
	</table>
	
	<form>
	
	<p>
	<logic:equal name="editGroup" value="true">
		<input class="button" type="button" value="<fmt:message key="admin.edit" /> <bean:write name="OrgManageForm" property="parentName"/>" onclick=javascript:document.location='organisation.do?method=edit&orgId=<c:out value="${OrgManageForm.parentId}"/>' />
	</logic:equal>
	</p>
	
	<div class="floatRight">
		<input class="button" type="button" value="<fmt:message key="admin.user.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
	</div>
	<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
	
	<% if (request.isUserInRole(Role.SYSADMIN)) { %>
	<div class="floatRight">
		<input class="button" type="button" value="<fmt:message key="title.clone.lessons" />" onclick="javascript:document.location='clone.do?groupId=<c:out value="${OrgManageForm.parentId}"/>';">
	</div>
	<p style="padding-top:10px;">&nbsp;</p>
	<% } %>
	
	<div class="floatRight">
		<c:url var="createSubgroupLink" value="organisation.do">
			<c:param name="method" value="create" />
			<c:param name="typeId" value="3" />
			<c:param name="parentId" value="${OrgManageForm.parentId}" />
		</c:url>
		<input class="button" type="button" value="<fmt:message key="admin.class.add"/>" onclick=javascript:document.location='<c:out value="${createSubgroupLink}"/>' />
	</div>
	</form>
	<html:form styleClass="indentPad" action="orgmanage.do" method="post">
		<input type="hidden" name="org" value="<bean:write name="OrgManageForm" property="parentId"/>" />
		<fmt:message key="label.show"/> <html:select property="stateId" onchange="document.OrgManageForm.submit();">
			<html:option value="1"><fmt:message key="organisation.state.ACTIVE"/></html:option>
			<html:option value="2"><fmt:message key="organisation.state.HIDDEN"/></html:option>
			<html:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></html:option>
		</html:select> <fmt:message key="label.subgroups"/>:
	</html:form>
</logic:equal>

<logic:equal name="OrgManageForm" property="type" value="3">
	<h4>
		<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
		: <a href="orgmanage.do?org=<c:out value="${parentGroupId}"/>"><c:out value="${parentGroupName}"/></a>
		: <a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><bean:write name="OrgManageForm" property="parentName"/></a>
	</h4>
	<h1><bean:write name="OrgManageForm" property="parentName"/></h1>
	
	<table cellspacing="7" class="infoDisplay">
		<tr>
			<td align="right"><fmt:message key="admin.organisation.name"/>:</td>
			<td><c:out value="${org.name}" /></td>
			<td align="right"><fmt:message key="admin.organisation.status"/>:</td>
			<td width="10%"><c:out value="${org.organisationState.description}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.code"/>:</td>
			<td><c:out value="${org.code}" /></td>
			<td align="right"><fmt:message key="admin.can.add.user"/>:</td>
			<td><c:out value="${org.courseAdminCanAddNewUsers}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.description"/>:</td>
			<td><c:out value="${org.description}" /></td>
			<td align="right"><fmt:message key="admin.can.browse.user"/>:</td>
			<td><c:out value="${org.courseAdminCanBrowseAllUsers}" /></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="admin.organisation.locale"/>:</td>
			<td><c:out value="${org.locale.description}" /></td>
			<td align="right"><fmt:message key="admin.can.change.status"/>:</td>
			<td><c:out value="${org.courseAdminCanChangeStatusOfCourse}" /></td>
		</tr>
	</table>
	
	<form>
	
	<p>
	<logic:equal name="editGroup" value="true">
		<input class="button" type="button" value="<fmt:message key="admin.edit" /> <bean:write name="OrgManageForm" property="parentName"/>" onclick=javascript:document.location='organisation.do?method=edit&orgId=<c:out value="${OrgManageForm.parentId}"/>' />
	</logic:equal>
	</p>
	
	<div class="floatRight">
		<input class="button" type="button" value="<fmt:message key="admin.user.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
	</div>
	<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
	
	<% if (request.isUserInRole(Role.SYSADMIN)) { %>
	<div class="floatRight">
		<input class="button" type="button" value="<fmt:message key="title.clone.lessons" />" onclick="javascript:document.location='clone.do?groupId=<c:out value="${OrgManageForm.parentId}"/>';">
	</div>
	<p style="padding-top:10px;">&nbsp;</p>
	<% } %>
	
	</form>
</logic:equal>

<c:if test="${not empty OrgManageForm.orgManageBeans}">
<form>
<table class=tablesorter-admin width=100%>
	<thead>
	<tr>
		<th>Id</th>
		<th><fmt:message key="admin.organisation.name"/></th>
		<th><fmt:message key="admin.organisation.code"/></th>
		<th><fmt:message key="admin.organisation.description"/></th>
	</tr>
	</thead>
	<tbody>
		<logic:iterate id="orgManageBean" name="OrgManageForm" property="orgManageBeans" indexId="idx">
		<tr>
			<td><bean:write name="orgManageBean" property="organisationId" /></td>
			<td>
				<a href="orgmanage.do?org=<bean:write name='orgManageBean' property='organisationId'/>"><bean:write name="orgManageBean" property="name" /></a>
			</td>
			<td>
				<bean:write name="orgManageBean" property="code" />
			</td>
			<td>
				<bean:write name="orgManageBean" property="description" />
			</td>
		</tr>
		</logic:iterate>
	</tbody>
</table>
</form>

<div id="pager" class="pager">
	<form onsubmit="return false;">
		<img src="<lams:LAMSURL/>/images/first.png" class="first"/>
		<img src="<lams:LAMSURL/>/images/prev.png" class="prev">
		<input type="text" class="pagedisplay"/>
		<img src="<lams:LAMSURL/>/images/next.png" class="next">
		<img src="<lams:LAMSURL/>/images/last.png" class="last">
		<select class="pagesize">
			<option selected="selected"  value="10">10&nbsp;&nbsp;</option>
			<option value="20">20</option>
			<option value="30">30</option>
			<option value="40">40</option>
			<option value="50">50</option>
			<option value="100">100</option>
		</select>
	</form>
</div>
</c:if>


