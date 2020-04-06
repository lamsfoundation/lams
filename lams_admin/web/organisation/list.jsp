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
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.theme.bootstrap4.css">	
	<link type="text/css" href="<lams:LAMSURL/>css/jquery.tablesorter.pager.css" rel="stylesheet">
	<style >
		table.infoDisplay {
			margin-left:5px; 
			padding-top:10px; 
			width:100%;
		}
		

	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/popper.js"></script>	
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script>
	  	$(document).ready(function(){
		    
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
				headerTemplate : '{content} {icon}',
				widgets: ["uitheme","zebra", "filter"],
				headers: { 0: { filter: false }, 2: { filter: false }, 3: { filter: false} },
			    widgetOptions : {
			        // include column filters
			        filter_columnFilters: true,
			        filter_placeholder: { search : 'Search...' },
			        filter_cssFilter: [
						'form-control form-control-sm',
						'form-control form-control-sm',
						'form-control form-control-sm',
						'form-control form-control-sm'
						]
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
							
								var formattedDate = new Date(orgData["createDate"]);
								
								rows += '<tr>';
								
								rows += '<td>';
								rows += 	orgData["id"];
								rows += '</td>';
								
								rows += '<td>';
								rows += 	'<a id="' + orgData["id"] + '" href="orgmanage.do?org=' + orgData["id"] + '">';
								rows += 		orgData["name"];
								rows += 	'</a>';
								rows += '</td>';
								
								rows += '<td>';
								rows += 	orgData["code"];
								rows += '</td>';
	
								rows += '<td class="text-right">';
								rows += 	formattedDate
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

	<%-- Build the breadcrumb --%>
	<c:if test="${orgManageForm.type == 1}">
		<c:set var="breadcrumbItems">.| <fmt:message key="admin.course.manage" /> </c:set>
	</c:if>
	<c:if test="${orgManageForm.type == 2}">
		<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /> </c:set>
		<c:set var="breadcrumbItems">${breadcrumbItems}, . | <c:out value="${org.code}" escapeXml="true"/> : <c:out value="${org.name}" escapeXml="true"/></c:set>
	</c:if>
	<c:if test="${orgManageForm.type == 3}">
		<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /> </c:set>
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${parentGroupId}"/> | <c:out value="${parentCode}" escapeXml="true"/> : <c:out value="${parentGroupName}" escapeXml="true"/></c:set>
		<c:set var="breadcrumbItems">${breadcrumbItems}, . | <c:out value="${org.code}" escapeXml="true" /> : <c:out value="${org.name}" escapeXml="true"/></c:set>
	</c:if>	

	<lams:Page type="admin" title="${title}" formID="orgManageForm" breadcrumbItems="${breadcrumbItems}">
		<c:if test="${orgManageForm.type == 1}">
				<form>
				
				<div class="btn-group" role="group" >
					<c:if test="${createGroup == true}">
					<a id="userCreate" class="btn btn-sm btn-outline-secondary" href="user/edit.do?orgId=1"><i class="fa fa-user-plus"></i> <fmt:message key="admin.user.create"/></a>
					<a id="findUsers" class="btn btn-sm btn-outline-secondary" href="usersearch.do"><i class="fa fa-search"></i> <fmt:message key="admin.user.find"/></a>
					</c:if>
					
					<c:if test="${manageGlobalRoles == true}">
						<a id="manageGlobalRoles" class="btn btn-sm btn-outline-secondary" href="usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>"><i class="fa fa-globe"></i> <fmt:message key="admin.global.roles.manage" /></a>
					</c:if>
					<c:if test="${createGroup == true}">
						<c:url var="editaction" value="organisation/create.do">
							<c:param name="typeId" value="2" />
							<c:param name="parentId" value="${orgManageForm.parentId}" />
						</c:url>
						<a id="createCourse" class="btn  btn-sm btn-outline-secondary" href="<c:out value="${editaction}"/>"><i class="fa fa-graduation-cap"></i> <fmt:message key="admin.course.add"/></a>
					</c:if>
				</div>
				<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
				</form>
			
				<form:form cssClass="indentPad" action="orgmanage.do" modelAttribute="orgManageForm" id="orgManageForm" method="post">
					<input type="hidden" name="org" value="${orgManageForm.parentId}" />
					<fmt:message key="label.show"/>&nbsp;
					<form:select path="stateId" id="org-state-id" cssClass="form-control form-control-inline form-control-sm">
						<form:option value="1"><fmt:message key="organisation.state.ACTIVE"/></form:option>
						<form:option value="2"><fmt:message key="organisation.state.HIDDEN"/></form:option>
						<form:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></form:option>
					</form:select> &nbsp;
					<fmt:message key="label.groups"/>:
				</form:form>
		</c:if>
			
		<c:if test="${orgManageForm.type == 2}">
		
		
		<section id="courseDetails" class="pl-3 pr-3">	
			<div class="row bg-light p-2">
			
				<div class="col-lg-9">

					<div class="dropdown ml-4 mr-1 pull-right">
						<a class="btn btn-sm btn-outline-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-label="configure" aria-expanded="false">
						  <i class="fa fa-lg fa-cog" aria-hidden="true"></i>
						</a>
						<div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuLink">
						<c:if test="${editGroup == true}">
						<a href="organisation/edit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" id="editCourse" class="dropdown-item"><i class="fa fa-edit" aria-hidden="true"></i> <fmt:message key="admin.edit" /></a>
						</c:if>	
						
						<a href="usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>" id="manageUsers" class="dropdown-item"><i class="fa fa-users" aria-hidden="true"></i> <fmt:message key="admin.user.manage" /></a>
						
						<a onclick="javascript:document.location='organisation/create.do?typeId=3&parentId=${orgManageForm.parentId}'"   class="dropdown-item" id="createNewSubcourse"><i class="fa fa-plus" aria-hidden="true"></i> <fmt:message key="admin.class.add"/></a>
						
						<c:if test="${pageContext.request.isUserInRole('SYSADMIN')}">
							<a href="clone/start.do?groupId=<c:out value="${orgManageForm.parentId}"/>" id="cloneLesson" class="dropdown-item"><i class="fa fa-clone" aria-hidden="true"></i> <fmt:message key="title.clone.lessons" /></a>
								<a href="organisation/deleteAllLessonsInit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" id="deleteAllLessons" class="dropdown-item"><i class="fa fa-bomb" aria-hidden="true"></i> <fmt:message key="admin.delete.lessons" /></a>
						</c:if>
						
						</div>
					</div>
				
					<span title="<fmt:message key="admin.organisation.status"/>" class="badge badge-success py-2 px-2 pull-right text-uppercase">Active</span>
					
					<h2 id="courseName" class="font-weight-bolder"><c:out value="${orgManageForm.parentName}" escapeXml="true"/></h2>
					
					<label title="<fmt:message key="admin.organisation.create.date"/>" style="position:relative;top:-1rem;font-size: x-small;"><lams:Date value="${org.createDate}"/></label>
					
				    <div class="row mt-1">
				        <div class="col-lg-4">

			                <c:set var="title"><fmt:message key="heading.users"/></c:set>
							<lams:Widget type="123" style="warning" shadow="shadow" icon="fa-3x fa-users text-white" title="${title}" titleAlignment="text-right" bodyText="${totalUsers}" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>
			                

				        </div>
				        <div class="col-lg-4">

			                <c:set var="title"><fmt:message key='admin.statistics.lessons'/></c:set>
			                <lams:Widget type="123" style="danger" shadow="shadow" icon="fa-3x fa-book text-white" title="${title}" titleAlignment="text-right" bodyText="#" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>

				        </div>
				        <div class="col-lg-4">
				        
				        	<c:set var="title"><fmt:message key="label.subgroups"/></c:set>
				        	<lams:Widget type="123" shadow="shadow" icon="fa-3x fa-graduation-cap text-white" title="${title}" titleAlignment="text-right text-capitalize" bodyText="#" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>
				        
				        </div>
				    </div>		
    			
				
				
				</div>
				<div class="col-lg-3">
					<div class="card shadow-sm"  >
						<div class="card-body">
							<p class="border-bottom"">
								<i class="fa fa-sliders">&nbsp;</i> <strong>Course settings</strong>
							</p>
							<p>
							Course managers can:
							</p>
							<p>
							Add new users
							<c:choose>
								<c:when test="${org.courseAdminCanAddNewUsers}">
									<i class="fa text-success fa-toggle-on pull-right  mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							
							<br/>
							Browse all users
							<c:choose>
								<c:when test="${org.courseAdminCanBrowseAllUsers}">
									<i class="fa text-success fa-toggle-on pull-right  mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							<br/>
							Change course status
							<c:choose>
								<c:when test="${org.courseAdminCanChangeStatusOfCourse}">
									<i class="fa text-success fa-toggle-on pull-right mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							</p>

						</div>
					</div>
				</div>
				
			</div>

			<c:if test="${not empty org.description}">
	
				<!--  Description if any -->
				<div class="row bg-light pl-3 pr-3">
					<div class="col-sm-12">
							<hr>
					
							<dl class="row">
								<dt class="col-sm-2"><fmt:message key="admin.organisation.description"/></dt>
								<dd class="col-sm-10"><c:out value="${org.description}" /></dd>
							</dl>	

						<hr>
					</div>
				</div>

			</c:if>		

	</section>

			<h3 class="mt-2"><span class="text-capitalize"><fmt:message key="label.subgroups"/></span></h3>
			<form:form cssClass="indentPad" action="orgmanage.do" modelAttribute="orgManageForm" id="orgManageForm" method="post">
				<input type="hidden" name="org" value="<c:out value="${orgManageForm.parentId}"/>" />
				<fmt:message key="label.show"/>&nbsp;
				<form:select path="stateId" id="org-state-id" cssClass="form-control form-control-inline form-control-sm">
					<form:option value="1"><fmt:message key="organisation.state.ACTIVE"/></form:option>
					<form:option value="2"><fmt:message key="organisation.state.HIDDEN"/></form:option>
					<form:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></form:option>
				</form:select> &nbsp;
				
			</form:form>
		</c:if>
			
		<c:if test="${orgManageForm.type == 3}">
		
		<section id="courseDetails">	
		<div class="container">
			<div class="row p-3">
				<div class="col-lg-9 bg-light">

					<div class="dropdown ml-4 mr-1 pull-right">
						<a class="btn btn-sm btn-outline-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-label="configure" aria-expanded="false">
						  <i class="fa fa-lg fa-cog" aria-hidden="true"></i>
						</a>
						<div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuLink">
						<c:if test="${editGroup == true}">
						<a href="organisation/edit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" id="editCourse" class="dropdown-item"><i class="fa fa-edit" aria-hidden="true"></i> <fmt:message key="admin.edit" /></a>
						</c:if>	
						
						<a href="usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>" id="manageUsers" class="dropdown-item"><i class="fa fa-users" aria-hidden="true"></i> <fmt:message key="admin.user.manage" /></a>
						
						<c:if test="${pageContext.request.isUserInRole('SYSADMIN')}">
							<a href="clone/start.do?groupId=<c:out value="${orgManageForm.parentId}"/>" id="cloneLesson" class="dropdown-item"><i class="fa fa-clone" aria-hidden="true"></i> <fmt:message key="title.clone.lessons" /></a>
								<a href="organisation/deleteAllLessonsInit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" id="deleteAllLessons" class="dropdown-item"><i class="fa fa-bomb" aria-hidden="true"></i> <fmt:message key="admin.delete.lessons" /></a>
						</c:if>
						
						</div>
					</div>
				
					<span title="<fmt:message key="admin.organisation.status"/>" class="badge badge-success py-2 px-2 pull-right text-uppercase">Active</span>
					
					<h2 id="courseName" class="font-weight-bolder"><c:out value="${orgManageForm.parentName}" escapeXml="true"/></h2>
					
					<label title="<fmt:message key="admin.organisation.create.date"/>" style="position:relative;top:-1rem;font-size: x-small;"><lams:Date value="${org.createDate}"/></label>
					
				    <div class="row mt-4">
				        <div class="col-lg-4">

			                <c:set var="title"><fmt:message key="heading.users"/></c:set>
							<lams:Widget type="123" style="warning" shadow="shadow" icon="fa-3x fa-users text-white" title="${title}" titleAlignment="text-right" bodyText="${totalUsers}" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>
			                

				        </div>
				        <div class="col-lg-4">

			                <c:set var="title"><fmt:message key='admin.statistics.lessons'/></c:set>
			                <lams:Widget type="123" style="danger" shadow="shadow" icon="fa-3x fa-book text-white" title="${title}" titleAlignment="text-right" bodyText="#" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>

				        </div>
				        <div class="col-lg-4">
				        
				        	<c:set var="title"><fmt:message key="label.subgroups"/></c:set>
				        	<lams:Widget type="123" shadow="shadow" icon="fa-3x fa-graduation-cap text-white" title="${title}" titleAlignment="text-right text-capitalize" bodyText="#" bodyTextFontSize="x-large" bodyTextAlignment="text-right"/>
				        
				        </div>
				    </div>		
    			
				
				
				</div>
				<div class="col-lg-3">
					<div class="card "  style="background-color: #f5f5f5;">
						<div class="card-body">
							<p class="border-bottom"">
								<i class="fa fa-sliders">&nbsp;</i> <strong>Course settings</strong>
							</p>
							<p>
							Course managers can:
							</p>
							<p>
							Add new users
							<c:choose>
								<c:when test="${org.courseAdminCanAddNewUsers}">
									<i class="fa text-success fa-toggle-on pull-right  mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							
							<br/>
							Browse all users
							<c:choose>
								<c:when test="${org.courseAdminCanBrowseAllUsers}">
									<i class="fa text-success fa-toggle-on pull-right  mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							<br/>
							Change course status
							<c:choose>
								<c:when test="${org.courseAdminCanChangeStatusOfCourse}">
									<i class="fa text-success fa-toggle-on pull-right mt-1" aria-label="On"></i>
								</c:when>
								<c:otherwise>
									<i class="fa  pull-right fa-toggle-off  mt-1" aria-label="Off"></i>
								</c:otherwise>
							</c:choose> 
							</p>

						</div>
					</div>
				</div>
				
			</div>

			<c:if test="${not empty org.description}">
	
				<!--  Description if any -->
				<div class="row bg-light pl-3 pr-3">
					<div class="col-sm-12">
							<hr>
					
							<dl class="row">
								<dt class="col-sm-2"><fmt:message key="admin.organisation.description"/></dt>
								<dd class="col-sm-10"><c:out value="${org.description}" /></dd>
							</dl>	

						<hr>
					</div>
				</div>

			</c:if>		
		</div>
	</section>				
				<div class="card panel-default" >
					<div class="panel-heading">
						<div class="panel-title">
							<span><c:out value="${orgManageForm.parentName}"/></span>
							<div class="btn-group btn-group-sm  pull-right">
								<c:if test="${editGroup == true}">
									<input class="btn btn-outline-secondary btn-sm" type="button" value="<fmt:message key="admin.edit" />" onclick=javascript:document.location='organisation/edit.do?orgId=<c:out value="${orgManageForm.parentId}"/>' />
								</c:if>
								<input class="btn btn-outline-secondary btn-sm" type="button" value="<fmt:message key="admin.user.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${orgManageForm.parentId}"/>' />
								<c:if test="${pageContext.request.isUserInRole('SYSADMIN')}">
								<input class="btn btn-outline-secondary btn-sm" type="button" value="<fmt:message key="title.clone.lessons" />" onclick="javascript:document.location='clone/start.do?groupId=<c:out value="${orgManageForm.parentId}"/>';">
										<a href="organisation/deleteAllLessonsInit.do?orgId=<c:out value="${orgManageForm.parentId}"/>" class="btn btn-outline-secondary btn-sm"><i class="fa fa-bomb"></i><span class="hidden-xs"> <fmt:message key="admin.delete.lessons" /></span></a>
								</c:if>
						</div>
						</div>
					</div>
			
					<table class="table table-sm">
						<thead class="thead-light">
						<tr>
							<th width="50%"><fmt:message key="admin.organisation.name"/>:</td>
							<th><c:out value="${org.name}" /></td>
						</tr>
						</thead>
						<tbody>
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
							<td class="text-right"><lams:Date value="${org.createDate}"/></td>
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
							<td><c:if test="${org.courseAdminCanChangeStatusOfCourse}" >HH</c:if></td>
						</tr>
						<tr>
							<td colspan="2"><c:out value="${numUsers}"/></td>
						</tr>
						</tbody>
					</table>
				
					</div>	
		</c:if>
			
		<c:if test="${orgManageForm.type != 3}">
			
		<div class="mt-2">
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
					<th width="20%" align="center" class="sorter-shortDate dateFormat-ddmmyyyy">
						<fmt:message key="admin.organisation.create.date"/>
					</th>
				</lams:TSTable>
				</div>
		</c:if>
	</lams:Page>
</body>
</lams:html>
