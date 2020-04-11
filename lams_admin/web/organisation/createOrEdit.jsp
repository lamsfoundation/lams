<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.organisation.entry"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">

	<script type="text/javascript">
		function warnIfRemoved(){
			// check if "Remove" state was selected
			var state = document.querySelector('select[name="stateId"]'),
				selected = state.options[state.options.selectedIndex].value;
			if (selected == 4) {
				// check if the course or one of its subcourses contain lessons
				if (${not empty courseToDeleteLessons}) {
					// confirm redirect to "delete all lessons" page
					if (confirm('<fmt:message key="msg.delete.organisation.delete.lessons.confirm"/>')) {
						document.location.href = '<lams:LAMSURL/>admin/organisation/deleteAllLessonsInit.do?orgId=${courseToDeleteLessons}';
					}
					return false;
				}
				// confirm removal of empty course
				if (!confirm('<fmt:message key="msg.delete.organisation.confirm"/>')) {
					return false;
				}
			}
			return true;
		}
	</script>
</lams:head>
    
<body class="stripes">

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1" | <fmt:message key="admin.course.manage" /></c:set>

	<c:if test="${organisationForm.typeId == 3}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${organisationForm.parentId}" | <c:out value="${organisationForm.parentName}" escapeXml="true"/></a></c:set>
	</c:if>
	
	<c:if test="${not empty organisationForm.orgId}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${organisationForm.orgId} | <c:out value="${organisationForm.name}" escapeXml="true"/></c:set>
	</c:if>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.organisation.entry"/></c:set>
	


	<lams:Page type="admin" title="${title}" formID="organisationForm" breadcrumbItems="${breadcrumbItems}">	
		<form:form action="../orgsave.do" method="post" modelAttribute="organisationForm" id="organisationForm" onsubmit="return warnIfRemoved()">
		    <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<form:hidden path="orgId" />
			<form:hidden path="parentId" />
			<form:hidden path="typeId" />
			
			<h2>
				<c:if test="${not empty organisationForm.orgId}">
					<fmt:message key="admin.edit"/>&nbsp;<c:out value="${organisationForm.name}"/>&nbsp;
				</c:if>
				<c:if test="${empty organisationForm.orgId}">
					<c:if test="${organisationForm.typeId == 2}">
						<fmt:message key="admin.course.add"/>
					</c:if>
					<c:if test="${organisationForm.typeId == 3}">
						<fmt:message key="admin.class.add"/>
					</c:if>
				</c:if>
			</h2>
			
			<lams:errors path="*"/>
			
			<div id="deleteAllLessonsBox" class="alert alert-info" style="display: none">
				<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount"></span> / <span id="totalLessonCount"></span>
				<fmt:message key="label.delete.all.lesson.progress" />
			</div>

			<div class="form-group">
				<label for="name">
					<fmt:message key="admin.organisation.name"/> 
				</label>&nbsp;<span class="text-danger">*</span>
				<input id="name" maxlength="40" name="name" value="${organisationForm.name}" class="form-control form-control-sm" maxlength="240" required/>			

				<label for="code">
					<fmt:message key="admin.organisation.code"/>
				</label>
				<form:input path="code" size="20" cssClass="form-control form-control-sm" maxlength="20"/>	
			</div>
			<div class="form-group">
				<label for="description">
					<fmt:message key="admin.organisation.description"/>
				</label>
				
				<form:textarea path="description" rows="3" cssClass="form-control form-control-sm" id="description"/>
			</div>
				
			<div class="form-group">
				<label for="stateId">
					<fmt:message key="admin.organisation.status"/>
				</label>
				
				<form:select name="stateId" path="stateId" cssClass="form-control" >
					<c:forEach items="${status}" var="state">
						<form:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></form:option>
					</c:forEach>
				</form:select>

			</div>
			<h3><fmt:message key="admin.course.settings"/></h3>				
			<div class="form-group">
			<c:if test="${organisationForm.typeId == 2}">

				<div class="form-group">
					<div class=""><fmt:message key="admin.course.managers.can"/>:</div>
					<div class="form-check ml-3">
						<form:checkbox path="courseAdminCanAddNewUsers" cssClass="form-check-input" id="course-admin-can-add-new-users" />
				    	<label  class="form-check-label" for="course-admin-can-add-new-users">
				    		<fmt:message key="admin.can.add.user"/>
				    	</label>
					</div>
					
					<div class="form-check ml-3">
						<form:checkbox path="courseAdminCanBrowseAllUsers" cssClass="form-check-input" id="course-admin-can-browse-all-users"/>
				    	<label  class="form-check-label" for="course-admin-can-browse-all-users">
				    		<fmt:message key="admin.can.browse.user"/>
				    	</label>
					</div>
					
					<div class="form-check ml-3">
						<form:checkbox path="courseAdminCanChangeStatusOfCourse" cssClass="form-check-input" id="course-admin-can-change-status-of-course"/>
				    	<label  class="form-check-label" for="course-admin-can-change-status-of-course">
				    		<fmt:message key="admin.can.change.status"/>
				    	</label>
					</div>
				</div>
				<div class="form-check">
					<form:checkbox path="enableCourseNotifications" id="enable-course-notifications" cssClass="form-check-input"/>
			    	<label  class="form-check-label" for="enable-course-notifications">
			    		<fmt:message key="admin.enable.course.notifications"/>
			    	</label>
				</div>

				<div class="form-check">
					<form:checkbox id="enableGradebookForLearners" path="enableGradebookForLearners" cssClass="form-check-input"/>
			    	<label  class="form-check-label" for="enableGradebookForLearners">
			    		<fmt:message key="admin.gradebook.learner.enable"/>
			    	</label>
				</div>

				<div class="form-check">
					<form:checkbox path="enableSingleActivityLessons" id="enable-single-activity-lessons" cssClass="form-check-input" />
			    	<label  class="form-check-label" for="enable-single-activity-lessons">
			    		<fmt:message key="config.authoring.single.activity"/>
			    	</label>
				</div>

			</c:if>	

			<div class="form-check">
				<form:checkbox path="enableLiveEdit" id="enable-live-edit" cssClass="form-check-input" />
		    	<label  class="form-check-label" for="enable-live-edit">
		    		<fmt:message key="config.live.edit"/>
		    	</label>
			</div>

			<div class="form-check">
				<form:checkbox path="enableKumalive" id="enable-kumalive" cssClass="form-check-input" />
		    	<label  class="form-check-label" for="enable-kumalive">
		    		<fmt:message key="config.kumalive.enable"/>
		    	</label>
			</div>

			<c:if test="${not empty organisationForm.orgId}">
					
				<div class="voffset10">
					<c:if test="${organisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
					<c:if test="${organisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
					&nbsp;<c:out value="${organisationForm.orgId}" />.
				</div>
						
			</c:if>
					
			<div class="pull-right">
				<a href="javascript:history.back();" class="btn btn-outline-secondary"><fmt:message key="admin.cancel"/></a>
				<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
			</div>
			
		</div>
				
		</form:form>

	</lams:Page>
</body>
</lams:html>
