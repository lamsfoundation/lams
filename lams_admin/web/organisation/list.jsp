<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<c:set var="lams"><lams:LAMSURL /></c:set>

<link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
<style>
	table.infoDisplay {
		margin-left:5px; 
		padding-top:10px; 
		width:100%;
	}
	p {
		margin-left:5px;
	}
	.floatRight {
		float:right;
	}
	.indentPad {
		margin-left:5px; 
		padding-top:10px;
	}
	#tablesorter thead .disabled {
		display: none
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
<script>
	
  	$(document).ready(function(){
	    
		$("#tablesorter").tablesorter({
			theme: 'blue',
			widgets: ["zebra", "filter"],
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
		
		$("#tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
				ajaxUrl : "<c:url value='/orgmanage.do'/>?dispatch=getOrgs&type=${OrgManageForm.type}&page={page}&size={size}&{sortList:column}&parentOrgId=${OrgManageForm.parentId}&{filterList:fcol}",
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
							rows += 	'<a id="' + orgData["id"] + '" href="orgmanage.do?org=' + orgData["id"] + '">';
							rows += 		orgData["name"];
							rows += 	'</a>';
							rows += '</td>';
							
							rows += '<td>';
							rows += 	orgData["code"];
							rows += '</td>';

							rows += '<td>';
							rows += 	orgData["description"];
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
				},
			    container: $(this).next("#pager"),
			    output: '{startRow} to {endRow} ({totalRows})',// possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
			    // css class names of pager arrows
			    cssNext: '.tablesorter-next', // next page arrow
				cssPrev: '.tablesorter-prev', // previous page arrow
				cssFirst: '.tablesorter-first', // go to first page arrow
				cssLast: '.tablesorter-last', // go to last page arrow
				cssGoto: '.gotoPage', // select dropdown to allow choosing a page
				cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
				cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option
				// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
				cssDisabled: 'disabled' // Note there is no period "." in front of this class name
			})
		});
		
		$( "#org-state-id" ).change(function() {
			$('#tablesorter').trigger('pagerUpdate');
		});
  	})
</script>

<logic:equal name="OrgManageForm" property="type" value="1">
	<h4>
		<a href="orgmanage.do?org=<bean:write name="OrgManageForm" property="parentId"/>"><fmt:message key="admin.course.manage" /></a>
	</h4>
	<h1><fmt:message key="admin.course.manage" /></h1>
	
	<form>
	
	<div class="floatRight">
		<input id="userCreate" class="button" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit' />
		<input id="findUsers" class="button" type="button" value="<fmt:message key="admin.user.find"/>" onclick=javascript:document.location='usersearch.do' />
		<logic:equal name="manageGlobalRoles" value="true">
			<input id="manageGlobalRoles" class="button" type="button" value="<fmt:message key="admin.global.roles.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
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
			<input id="createCourse" class="button" type="button" value="<fmt:message key="admin.course.add"/>" onclick=javascript:document.location='<c:out value="${editaction}"/>' />
		</logic:equal>
	</div>
	</form>
	<html:form styleClass="indentPad" action="orgmanage.do" method="post">
		<input type="hidden" name="org" value="<bean:write name="OrgManageForm" property="parentId"/>" />
		<fmt:message key="label.show"/> 
		<html:select property="stateId" styleId="org-state-id">
			<html:option value="1"><fmt:message key="organisation.state.ACTIVE"/></html:option>
			<html:option value="2"><fmt:message key="organisation.state.HIDDEN"/></html:option>
			<html:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></html:option>
		</html:select> 
		<fmt:message key="label.groups"/>:
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
			<td id="courseName" align="right"><fmt:message key="admin.organisation.name"/>:</td>
			<td><c:out value="${org.name}" /></td>
			<td align="right"><fmt:message key="admin.organisation.status"/>:</td>
			<td id="courseStatus" width="10%"><c:out value="${org.organisationState.description}" /></td>
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
		<input id="editCourse" class="button" type="button" value="<fmt:message key="admin.edit" /> <bean:write name="OrgManageForm" property="parentName"/>" onclick=javascript:document.location='organisation.do?method=edit&orgId=<c:out value="${OrgManageForm.parentId}"/>' />
	</logic:equal>
	</p>
	
	<div class="floatRight">
		<input id="manageUsers" class="button" type="button" value="<fmt:message key="admin.user.manage" />" onclick=javascript:document.location='usermanage.do?org=<c:out value="${OrgManageForm.parentId}"/>' />
	</div>
	<p style="padding-top:10px;"><c:out value="${numUsers}"/></p>
	
	<% if (request.isUserInRole(Role.SYSADMIN)) { %>
	<div class="floatRight">
		<input id="closeLessons" class="button" type="button" value="<fmt:message key="title.clone.lessons" />" onclick="javascript:document.location='clone.do?groupId=<c:out value="${OrgManageForm.parentId}"/>';">
	</div>
	<p style="padding-top:10px;">&nbsp;</p>
	<% } %>
	
	<div class="floatRight">
		<c:url var="createSubgroupLink" value="organisation.do">
			<c:param name="method" value="create" />
			<c:param name="typeId" value="3" />
			<c:param name="parentId" value="${OrgManageForm.parentId}" />
		</c:url>
		<input id="createNewSubcourse" class="button" type="button" value="<fmt:message key="admin.class.add"/>" onclick=javascript:document.location='<c:out value="${createSubgroupLink}"/>' />
	</div>
	</form>
	<html:form styleClass="indentPad" action="orgmanage.do" method="post">
		<input type="hidden" name="org" value="<bean:write name="OrgManageForm" property="parentId"/>" />
		<fmt:message key="label.show"/> 
		<html:select property="stateId" styleId="org-state-id">
			<html:option value="1"><fmt:message key="organisation.state.ACTIVE"/></html:option>
			<html:option value="2"><fmt:message key="organisation.state.HIDDEN"/></html:option>
			<html:option value="3"><fmt:message key="organisation.state.ARCHIVED"/></html:option>
		</html:select> 
		<fmt:message key="label.subgroups"/>:
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

<c:if test="${OrgManageForm.type != 3}">

	<table id="tablesorter">
		<thead>
			<tr>
				<th width="3%" clas="filter-false">
					Id
				</th>
				<th width="10%" align="center"> 
					<fmt:message key="admin.organisation.name"/>
				</th>
				<th width="10%" align="center">
					<fmt:message key="admin.organisation.code"/>
				</th>
				<th width="20%" align="center">
					<fmt:message key="admin.organisation.description"/>
				</th>
			</tr>
		</thead>
			
		<tbody>
		</tbody>
	</table>
		
	<!-- pager -->
	<div id="pager">
		<form>
			<img class="tablesorter-first"/>
			<img class="tablesorter-prev"/>
			<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
			<img class="tablesorter-next"/>
			<img class="tablesorter-last"/>
			<select class="pagesize">
				<option selected="selected" value="10">10</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
		</form>
	</div>
</c:if>
