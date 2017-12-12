<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
<style type="text/css">
	#courseHeading {
		height: 45px;
	}
	
	#courseHeading .panel-title {
		display: inline-block;
		margin-top: 5px;
	}
</style>

<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>

<script>
	$(document).ready(function() {
		
		$(".tablesorter").tablesorter({
			widthFixed:true,
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
			widgets: ["uitheme"],
			headers: {4:{sorter:false}}
		});
		
		//sort table only in case there is a data inside (it's a tablesorter bug)
		if ($(".tablesorter tbody tr").length > 0) {
			$(".tablesorter").tablesorterPager({
				container: jQuery(".ts-pager"),
	            output: '{startRow} to {endRow} ({totalRows})'
			});
			$(".tablesorter").trigger("sorton", [[[0, 0]]]);
		}
	});
	//-->
</script>

<p>
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
	<logic:equal name="orgType" value="2">
		: <a href="orgmanage.do?org=<bean:write name="UserManageForm" property="orgId" />" class="btn btn-default"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
	<logic:equal name="orgType" value="3">
		: <a href="orgmanage.do?org=<bean:write name="pOrgId" />" class="btn btn-default"><bean:write name="pOrgName"/></a>
		: <a href="orgmanage.do?org=<bean:write name="UserManageForm" property="orgId" />" class="btn btn-default"><bean:write name="UserManageForm" property="orgName"/></a>
	</logic:equal>
</p>

<div class="panel panel-default voffset5" >
	<div id="courseHeading" class="panel-heading">
		<span class="panel-title">
			<logic:equal name="orgType" value="1">
				<fmt:message key="admin.global.roles.manage" />
			</logic:equal>
			<logic:notEqual name="orgType" value="1">
				<c:out value="${heading}" />
			</logic:notEqual>
		</span>
		<div class="pull-right btn-group btn-group-sm">
			<input id="addRemoveUsers" class="btn btn-default" type="button" value="<fmt:message key="admin.user.add"/>" onclick=javascript:document.location='userorg.do?orgId=<bean:write name="UserManageForm" property="orgId"/>' />
			<logic:equal name="UserManageForm" property="courseAdminCanAddNewUsers" value="true">
				<input class="btn btn-default" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit&orgId=<bean:write name="UserManageForm" property="orgId"/>' />
			</logic:equal>
		</div>
	</div>

	<logic:notEqual name="orgType" value="1">
	<table class="table table-condensed table-striped">
		<tr>
			<td width="30%"><fmt:message key="label.learners"/>:</td>
			<td><c:out value="${LEARNER}"/></td>
		</tr>
		<tr>
			<td><fmt:message key="label.group.managers"/>:</td>
			<td><c:out value="${GROUP_MANAGER}"/></td>
		</tr>
		<tr>
			<td><fmt:message key="label.monitors"/>:</td>
			<td><c:out value="${MONITOR}"/></td>
		</tr>
		<tr>
			<td ><fmt:message key="label.group.admins"/>:</td>
			<td><c:out value="${GROUP_ADMIN}"/></td>
		</tr>
		<tr>
			<td><fmt:message key="label.authors"/>:</td>
			<td><c:out value="${AUTHOR}"/></td>
		</tr>
		<tr>
			<td colspan="2">
				<c:out value="${numUsers}"/>
			</td>
		</tr>
	</table>
	</logic:notEqual>
		
	<logic:equal name="orgType" value="1">
	<table class="table table-condensed table-striped">
		<tr>
			<td width="30%"><fmt:message key="label.sysadmins"/>:</td>
			<td><c:out value="${SYSADMIN}"/></td>
		</tr>
		<tr>
			<td width="30%"><fmt:message key="label.group.admins"/>:</td>
			<td><c:out value="${GROUP_ADMIN}"/></td>
		</tr>
		<tr>
			<td colspan="2">
				<c:out value="${numUsers}"/>
			</td>
		</tr>
	</table>
	</logic:equal>
</div>

<table class="tablesorter">
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
	<tfoot>
		<tr>
	    	<th colspan="5" class="ts-pager form-horizontal">
	    	<form onsubmit="return false;">	    	
		        <button type="button" class="btn btn-xs first"><i class="fa fa-step-backward"></i></button>
		        <button type="button" class="btn btn-xs prev"><i class="fa fa-backward"></i></button>
		        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
		        <button type="button" class="btn btn-xs next"><i class="fa fa-forward"></i></button>
		        <button type="button" class="btn btn-xs last"><i class="fa fa-step-forward"></i></button>
		        <select class="pagesize" title="Select page size">
		      		<option selected="selected" value="10">10</option>
		      		<option value="20">20</option>
		      		<option value="30">30</option>
		      		<option value="40">40</option>
		      		<option value="50">50</option>
		      		<option value="100">100</option>
		        </select>
		    </form>
	        </th>
	    </tr>
	</tfoot>
	<tbody>
	</tbody>
</table> 

