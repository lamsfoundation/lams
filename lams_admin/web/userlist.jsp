<%@ include file="/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-1.1.4.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pager.js"></script>
<script>
	<!--
	jQuery(document).ready(function() {
		jQuery("table.tablesorter-admin").tablesorter({widthFixed:true, sortList:[[0,0]], headers:{4:{sorter:false}}})
			.tablesorterPager({container: jQuery("#pager")});
	});
	//-->
</script>

<style>
table.infoDisplay {
	margin-left:5px; padding-top:10px; width:60%;
}
p {
	margin-left:5px;
}
.floatRight {
	float:right;
}
</style>

<h4>
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	<logic:equal name="orgType" value="2">
		: <a href="orgmanage.do?org=<bean:write name="UserManageForm" property="orgId" />"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
	<logic:equal name="orgType" value="3">
		: <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a>
		: <a href="orgmanage.do?org=<bean:write name="UserManageForm" property="orgId" />"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
</h4>

<h1>
	<logic:equal name="orgType" value="1">
		<fmt:message key="admin.global.roles.manage" />
	</logic:equal>
	<logic:notEqual name="orgType" value="1">
		<c:out value="${heading}" />
	</logic:notEqual>
</h1>

<logic:notEqual name="orgType" value="1">
<table cellspacing="7" class="infoDisplay">
	<tr>
		<td align="right"><fmt:message key="label.learners"/>:</td>
		<td><c:out value="${LEARNER}"/></td>
		<td align="right"><fmt:message key="label.group.managers"/>:</td>
		<td width="10%"><c:out value="${GROUP_MANAGER}"/></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.monitors"/>:</td>
		<td><c:out value="${MONITOR}"/></td>
		<td align="right"><fmt:message key="label.group.admins"/>:</td>
		<td><c:out value="${GROUP_ADMIN}"/></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.authors"/>:</td>
		<td><c:out value="${AUTHOR}"/></td>
		<td align="right"></td>
		<td></td>
	</tr>
</table>
</logic:notEqual>
	
<logic:equal name="orgType" value="1">
<table cellspacing="7" class="infoDisplay">
	<tr>
		<td align="right"><fmt:message key="label.sysadmins"/>:</td>
		<td><c:out value="${SYSADMIN}"/></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.group.admins"/>:</td>
		<td><c:out value="${GROUP_ADMIN}"/></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.author.admins"/>:</td>
		<td><c:out value="${AUTHOR_ADMIN}"/></td>
	</tr>
</table>
</logic:equal>

<form>
	
<p>
	<input class="button" type="button" value="<fmt:message key="admin.user.add"/>" onclick=javascript:document.location='userorg.do?orgId=<bean:write name="UserManageForm" property="orgId"/>' />
</p>

<div class="floatRight">
	<logic:equal name="UserManageForm" property="courseAdminCanAddNewUsers" value="true">
		<input class="button" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit&orgId=<bean:write name="UserManageForm" property="orgId"/>' />
	</logic:equal>
</div>
<p style="padding-top:10px;">
	<c:out value="${numUsers}"/>
</p>

<table class="tablesorter-admin" width=100% cellspacing="0" >
<thead>
<tr>
	<th><fmt:message key="admin.user.login"/></th>
	<th><fmt:message key="admin.user.first_name"/></th>
	<th><fmt:message key="admin.user.last_name"/></th>
	<th><fmt:message key="admin.user.roles"/></th>
	<th><fmt:message key="admin.user.actions"/></th>
</tr>
</thead>
<tbody>
<logic:iterate id="userManageBean" name="UserManageForm" property="userManageBeans">
	<tr>
		<td>
			<bean:write name="userManageBean" property="login" />
		</td>
		<td>
			<bean:write name="userManageBean" property="firstName" />
		</td>
		<td>
			<bean:write name="userManageBean" property="lastName" />
		</td>
		<td>
		    <small>
		    <logic:iterate id="role" name="userManageBean" property="roles">
		        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message>&nbsp;
		    </logic:iterate>
		    </small>
		</td>
		<td>
				<a href="userroles.do?userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.user.assign.roles"/></a>
				&nbsp;
				<logic:equal name="UserManageForm" property="courseAdminCanAddNewUsers" value="true">
					<a href="user.do?method=edit&userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.edit" /></a>
					&nbsp;
				</logic:equal>
				<logic:equal name="canDeleteUser" value="true">
					<a href="user.do?method=remove&userId=<bean:write name='userManageBean' property='userId' />&orgId=<bean:write name='UserManageForm' property='orgId'/>"><fmt:message key="admin.user.delete"/></a>
				</logic:equal>
				<br/>
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
