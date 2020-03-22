<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
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
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">

		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page">${title}</li>
		  </ol>
		</nav>		

				
			<p>
				<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
				<c:if test="${orgType == 2}">
					: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${userManageForm.orgId}" />" class="btn btn-default"><c:out value="${userManageForm.orgName}" /></a>
				</c:if>
				<c:if test="${orgType == 3}">
					: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${pOrgName}"/></a>
					: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${userManageForm.orgId}" />" class="btn btn-default"><c:out value="${userManageForm.orgName}"/></a>
				</c:if>
			</p>
			
			<div class="panel panel-default voffset5" >
				<div id="courseHeading" class="panel-heading">
					<span class="panel-title">
						<c:if test="${orgType == 1}">
							<fmt:message key="admin.global.roles.manage" />
						</c:if>
						<c:if test="${orgType != 1}">
							<c:out value="${heading}" />
						</c:if>
					</span>
					<div class="pull-right btn-group btn-group-sm">
						<input id="addRemoveUsers" class="btn btn-default" type="button" value="<fmt:message key="admin.user.add"/>" onclick="javascript:document.location='<lams:LAMSURL/>admin/userorg.do?orgId=<c:out value="${userManageForm.orgId}"/>'" />
						<c:if test="${userManageForm.canResetOrgPassword == true}">
							<a class="btn btn-default" href="<lams:LAMSURL/>admin/orgPasswordChange/start.do?organisationID=${userManageForm.orgId}"><fmt:message key='admin.org.password.change.button'/></a>
						</c:if>
						<c:if test="${userManageForm.courseAdminCanAddNewUsers == true}">
							<input class="btn btn-default" type="button" value="<fmt:message key="admin.user.create"/>" onclick="javascript:document.location='<lams:LAMSURL/>admin/user/edit.do?orgId=<c:out value="${userManageForm.orgId}"/>'" />
						</c:if>
					</div>
				</div>
			
				<c:if test="${orgType != 1}">
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
						<td><fmt:message key="label.authors"/>:</td>
						<td><c:out value="${AUTHOR}"/></td>
					</tr>
					<tr>
						<td colspan="2">
							<c:out value="${numUsers}"/>
						</td>
					</tr>
				</table>
				</c:if>
					
				<c:if test="${orgType == 1}">
				<table class="table table-condensed table-striped">
					<tr>
						<td width="30%"><fmt:message key="label.sysadmins"/>:</td>
						<td><c:out value="${SYSADMIN}"/></td>
					</tr>
					<tr>
						<td colspan="2">
							<c:out value="${numUsers}"/>
						</td>
					</tr>
				</table>
				</c:if>
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
				<c:forEach var="userManageBean" items="${userManageForm.userManageBeans}">
					<tr>
						<td>
							<c:out value="${userManageBean.login}" />
						</td>
						<td>
							<c:out value="${userManageBean.firstName}" />
						</td>
						<td>
							<c:out value="${userManageBean.lastName}" />
						</td>
						<td>
						    <small>
						    <c:forEach var="role" items="${userManageBean.roles}">
						        <fmt:message>role.<lams:role role="${role.name}" /></fmt:message>&nbsp;
						    </c:forEach>
						    </small>
						</td>
						<td>
							<c:if test="${userManageForm.canEditRole}">
                            	<a title="<fmt:message key="admin.user.assign.roles"/>" href="<lams:LAMSURL/>admin/userroles.do?userId=<c:out value="${userManageBean.userId}" />&orgId=<c:out value="${userManageForm.orgId}"/>"><button type="button" class="btn btn-primary btn-xs"><i class="fa fa-users"></i> <span class="hidden-xs hidden-sm"><fmt:message key="admin.user.assign.roles"/></span></button></a>
								&nbsp;
							</c:if>
							<c:if test="${userManageForm.courseAdminCanAddNewUsers}">
                            	<a title="<fmt:message key="admin.edit" />" href="<lams:LAMSURL/>admin/user/edit.do?userId=<c:out value="${userManageBean.userId}" />&orgId=<c:out value="${userManageForm.orgId}"/>"><button type="button" class="btn btn-primary btn-xs"><i class="fa fa-pencil"></i> <span class="hidden-xs hidden-sm"><fmt:message key="admin.edit" /></span></button></a>
								&nbsp;
							</c:if>
							<c:if test="${canDeleteUser}">
                            	<a title="<fmt:message key="admin.user.delete"/>" href="<lams:LAMSURL/>admin/user/remove.do?userId=<c:out value="${userManageBean.userId}" />&orgId=<c:out value="${userManageForm.orgId}"/>"><button type="button" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> <span class="hidden-xs hidden-sm"><fmt:message key="admin.user.delete"/></span></button></a>
							</c:if>
							<br/>
						</td>		
					</tr>
				</c:forEach>
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
		<hr>
		<div class="pull-right">
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>		
	</lams:Page>
</body>
</lams:html>
