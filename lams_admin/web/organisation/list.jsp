<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<%@ page import=" org.lamsfoundation.lams.util.FileUtil" %>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="datePattern"><%= FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.toPattern() %></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.course.manage"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
	<style >
		table.infoDisplay {
			margin-left:5px; 
			padding-top:10px; 
			width:100%;
		}
		
		.panel-heading {
			height: 45px;
		}
		
		.panel-heading .panel-title > span {
			display: inline-block;
			margin-top: 5px;
		}
		
		.course-admin-property {
			padding-left: 20px !important;
		}
	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script>
	  	$(document).ready(function(){
		    
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
				headerTemplate : '{content} {icon}',
				widgets: ["uitheme","zebra", "filter"],
				headers: { 0: { filter: false }, 2: { filter: false }, 3: { filter: false } },
			    widgetOptions : {
			        // include column filters
			        filter_columnFilters: true,
			        filter_placeholder: { search : 'Search...' },
			      },
			    widthFixed: true,
			    sortInitialOrder: 'desc',
	            sortList: [[1]] 
			});
			
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					savePages: false,
	                container: $(this).find(".ts-pager"),
	                output: '{startRow} to {endRow} ({totalRows})',
	                cssPageDisplay: '.pagedisplay',
	                cssPageSize: '.pagesize',
	                cssDisabled: 'disabled',
	                ajaxUrl : "<c:url value='/orgmanage/getOrgs.do'/>?type=${orgManageForm.type}&page={page}&size={size}&{sortList:column}&parentOrgId=${orgManageForm.parentId}&{filterList:fcol}",
					ajaxProcessing: function (data, table) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {};
				    		
							for (i = 0; i < data.rows.length; i++){
								var orgData = data.rows[i];
								
								rows += '<tr>';
								
								rows += '<td>';
								rows += 	orgData["id"];
								rows += '</td>';
								
								rows += '<td>';
								rows += 	'<a id="' + orgData["id"] + '" href="orgmanage.do?<csrf:token/>&org=' + orgData["id"] + '">';
								rows += 		orgData["name"];
								rows += 	'</a>';
								rows += '</td>';
								
								rows += '<td>';
								rows += 	orgData["code"];
								rows += '</td>';
	
								rows += '<td>';
								rows += 	orgData["createDate"];
								rows += '</td>';
								
								rows += '</tr>';
							}
				            
							json.total = data.total_rows;
							json.rows = $(rows);
							return json;
				            
				    	}
					},
					// modify the url after all processing has been applied
					customAjaxUrl: function(table, url) {
					    // get current selection & add it to the url
					    return url += '&stateId=' + $("#org-state-id").val();
					}
				})
			});
			
			$( "#org-state-id" ).change(function() {
				$('.tablesorter').trigger('pagerUpdate');
			});
	  	})
	</script>
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}" formID="orgManageForm">
		<c:if test="${orgManageForm.type == 1}">
				<form>
				
				<div class="btn-group btn-group-sm">
					<c:if test="${createGroup == true}">
					<a id="userCreate" class="btn btn-default" href="user/edit.do?orgId=1"><i class="fa fa-user-plus"></i> <fmt:message key="admin.user.create"/></a>
					<a id="findUsers" class="btn btn-default" href="usersearch.do"><i class="fa fa-search"></i> <fmt:message key="admin.user.find"/></a>
					</c:if>
					
					<c:if test="${manageGlobalRoles == true}">
						<a id="manageGlobalRoles" class="btn btn-default" href="usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>"><i class="fa fa-globe"></i> <fmt:message key="admin.global.roles.manage" /></a>
					</c:if>
					<c:if test="${createGroup == true}">
						<c:url var="editaction" value="organisation/create.do">
							<c:param name="typeId" value="2" />
							<c:param name="parentId" value="${orgManageForm.parentId}" />
						</c:url>
						<a id="createCourse" class="btn btn-default" href="<c:out value="${editaction}"/>"><i class="fa fa-graduation-cap"></i> <fmt:message key="admin.course.add"/></a>
					</c:if>
				</div>
				<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
				</form>
			
				<form:form cssClass="indentPad" action="orgmanage.do" modelAttribute="orgManageForm" id="orgManageForm" method="post">
					<input type="hidden" name="org" value="${orgManageForm.parentId}" />
					<fmt:message key="label.show"/>&nbsp;
					<form:select path="stateId" id="org-state-id" cssClass="form-control form-control-inline input-sm">
						<form:option value="1"><fmt:message key="organisation.state.ACTIVE"/></form:option>
						<form:option value="2"><fmt:message key="organisation.state.HIDDEN"/></form:option>
						<form:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></form:option>
					</form:select> &nbsp;
					<fmt:message key="label.groups"/>:
				</form:form>
		</c:if>
			
		<c:if test="${orgManageForm.type == 2}">
				<p><a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a></p>
			
				<div class="panel panel-default voffset5" >
					<div class="panel-heading">
						<div class="panel-title">
							<span id="courseName"><c:out value="${orgManageForm.parentName}"/></span>
			
							<div class="btn-group btn-group-sm  pull-right">
								<c:if test="${editGroup == true}">
									<a href="organisation/edit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" id="editCourse" class="btn btn-default"><i class="fa fa-edit"></i><span class="hidden-xs"> <fmt:message key="admin.edit" /></span></a> 
								</c:if>
								<a href="usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>" id="manageUsers" class="btn btn-default"><i class="fa fa-users"></i> <span class="hidden-xs"><fmt:message key="admin.user.manage" /></span></a>
								
								<c:if test="${pageContext.request.isUserInRole('SYSADMIN')}">
								<a href="clone/start.do?groupId=<c:out value="${orgManageForm.parentId}"/>" class="btn btn-default"><i class="fa fa-clone"></i><span class="hidden-xs"> <fmt:message key="title.clone.lessons" /></span></a>
									<a href="organisation/deleteAllLessonsInit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" class="btn btn-default"><i class="fa fa-bomb"></i><span class="hidden-xs"> <fmt:message key="admin.delete.lessons" /></span></a>
								</c:if>
							</div>
						</div>
					</div>
					<div class="panel-body">
			
					<table class="table table-striped table-bordered">
						<tr>
							<td width="50%"><fmt:message key="admin.organisation.code"/>:</td>
							<td><c:out value="${org.code}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.description"/>:</td>
							<td><c:out value="${org.description}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.status"/>:</td>
							<td id="courseStatus"><c:out value="${org.organisationState.description}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.create.date"/>:</td>
							<td><fmt:formatDate value="${org.createDate}" pattern="${datePattern}" /></td>
						</tr>
						<tr>
							<td colspan="2"><fmt:message key="role.GROUP.MANAGER"/>:</td>
						</tr>
						<tr>
							<td class="course-admin-property"><fmt:message key="admin.can.add.user"/>:</td>
							<td><c:out value="${org.courseAdminCanAddNewUsers}" /></td>
						</tr>
						<tr>
							<td class="course-admin-property"><fmt:message key="admin.can.browse.user"/>:</td>
							<td><c:out value="${org.courseAdminCanBrowseAllUsers}" /></td>
						</tr>
						<tr>
							<td class="course-admin-property"><fmt:message key="admin.can.change.status"/>:</td>
							<td><c:out value="${org.courseAdminCanChangeStatusOfCourse}" /></td>
						</tr>
						<tr>
							<td colspan="2"><c:out value="${numUsers}"/></td>
						</tr>
					</table>
					</div>		
				</div>
				
				<form>
				<div class="pull-right">
					<c:url var="createSubgroupLink" value="organisation/create.do">
						<c:param name="typeId" value="3" />
						<c:param name="parentId" value="${orgManageForm.parentId}" />
					</c:url>
					<input id="createNewSubcourse" class="btn btn-default" type="button" value="<fmt:message key="admin.class.add"/>" onclick=javascript:document.location='<c:out value="${createSubgroupLink}"/>' />
				</div>
				</form>
			
				<form:form cssClass="indentPad" action="orgmanage.do" modelAttribute="orgManageForm" id="orgManageForm" method="post">
					<input type="hidden" name="org" value="<c:out value="${orgManageForm.parentId}"/>" />
					<fmt:message key="label.show"/>&nbsp;
					<form:select path="stateId" id="org-state-id" cssClass="form-control form-control-inline input-sm">
						<form:option value="1"><fmt:message key="organisation.state.ACTIVE"/></form:option>
						<form:option value="2"><fmt:message key="organisation.state.HIDDEN"/></form:option>
						<form:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></form:option>
					</form:select> &nbsp;
					<fmt:message key="label.subgroups"/>:
				</form:form>
		</c:if>
			
		<c:if test="${orgManageForm.type == 3}">
				<p>
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${parentGroupId}"/>" class="btn btn-default"><c:out value="${parentGroupName}"/></a>
				</p>
				
				<div class="panel panel-default voffset5" >
					<div class="panel-heading">
						<div class="panel-title">
							<span><c:out value="${orgManageForm.parentName}"/></span>
							<div class="btn-group btn-group-sm  pull-right">
								<c:if test="${editGroup == true}">
									<input class="btn btn-default" type="button" value="<fmt:message key="admin.edit" />&nbsp;<c:out value="${orgManageForm.parentName}"/>" onclick=javascript:document.location='organisation/edit.do?orgId=<c:out value="${orgManageForm.parentId}"/>' />
								</c:if>
								<input class="btn btn-default" type="button" value="<fmt:message key="admin.user.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>' />
								<c:if test="${pageContext.request.isUserInRole('SYSADMIN')}">
								<input class="btn btn-default" type="button" value="<fmt:message key="title.clone.lessons" />" onclick="javascript:document.location='clone/start.do?groupId=<c:out value="${orgManageForm.parentId}"/>';">
										<a href="organisation/deleteAllLessonsInit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" class="btn btn-default"><i class="fa fa-bomb"></i><span class="hidden-xs"> <fmt:message key="admin.delete.lessons" /></span></a>
								</c:if>
						</div>
						</div>
					</div>
			
					<table class="table table-striped">
						<tr>
							<td width="50%"><fmt:message key="admin.organisation.name"/>:</td>
							<td><c:out value="${org.name}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.code"/>:</td>
							<td><c:out value="${org.code}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.description"/>:</td>
							<td><c:out value="${org.description}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.status"/>:</td>
							<td><c:out value="${org.organisationState.description}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.organisation.create.date"/>:</td>
							<td><fmt:formatDate value="${org.createDate}" pattern="${datePattern}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.can.add.user"/>:</td>
							<td><c:out value="${org.courseAdminCanAddNewUsers}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.can.browse.user"/>:</td>
							<td><c:out value="${org.courseAdminCanBrowseAllUsers}" /></td>
						</tr>
						<tr>
							<td><fmt:message key="admin.can.change.status"/>:</td>
							<td><c:out value="${org.courseAdminCanChangeStatusOfCourse}" /></td>
						</tr>
						<tr>
							<td colspan="2"><c:out value="${numUsers}"/></td>
						</tr>
					</table>
				
					</div>	
		</c:if>
			
		<c:if test="${orgManageForm.type != 3}">
			
				<div class="voffset10">
				<lams:TSTable numColumns="4"> 
		<th width="3%" id="idsorter" class="filter-false">
						Id
					</th>
					<th width="10%" align="center"> 
						<fmt:message key="admin.organisation.name"/>
					</th>
					<th width="10%" align="center">
						<fmt:message key="admin.organisation.code"/>
					</th>
					<th width="20%" align="center">
						<fmt:message key="admin.organisation.create.date"/>
					</th>
				</lams:TSTable>
				</div>
		</c:if>
	</lams:Page>
</body>
</lams:html>
