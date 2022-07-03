<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<c:set var="webAppUrl"><lams:WebAppURL /></c:set>

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
	<lams:Page type="admin" title="${title}" formID="organisationForm">
		<form:form action="${webAppUrl}orgsave.do" method="post" modelAttribute="organisationForm" id="organisationForm" onsubmit="return warnIfRemoved()">
			    <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				
				<p>
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					<c:if test="${organisationForm.typeId == 3}">
						: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${organisationForm.parentId}" />" class="btn btn-default"><c:out value="${organisationForm.parentName}"/></a>
					</c:if>
					
					<c:if test="${not empty organisationForm.orgId}">
						: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${organisationForm.orgId}" />" class="btn btn-default"><c:out value="${organisationForm.name}"/></a>
					</c:if>
				</p>
				
				<h4>
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
				</h4>
				
				<lams:errors path="*"/>
				
				<div id="deleteAllLessonsBox" class="alert alert-info" style="display: none">
					<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount"></span> / <span id="totalLessonCount"></span>
					<fmt:message key="label.delete.all.lesson.progress" />
				</div>
				
				<table class="table table-condensed table-no-border">
					<tr>
						<td width="15%"><fmt:message key="admin.organisation.name"/> *</td>
						<td><form:input path="name" size="40" cssClass="form-control" maxlength="240"/></td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/></td>
						<td><form:input path="code" size="20" cssClass="form-control" maxlength="20"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/></td>
						<td><form:input path="description" cols="50" rows="3" cssClass="form-control" id="description"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.status"/></td>
						<td>
							<form:select path="stateId" cssClass="form-control" >
								<c:forEach items="${status}" var="state">
									<form:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></form:option>
								</c:forEach>
							</form:select>
						</td>
					</tr>
				</table>
					
					<c:if test="${organisationForm.typeId == 2}">
						<div class="checkbox">
							<label for="course-admin-can-add-new-users">
							<form:checkbox path="courseAdminCanAddNewUsers" id="course-admin-can-add-new-users" />
								<fmt:message key="admin.can.add.user"/>
							</label>
						</div>
						<div class="checkbox">
							<label for="course-admin-can-browse-all-users">
							<form:checkbox path="courseAdminCanBrowseAllUsers" id="course-admin-can-browse-all-users"/>
								<fmt:message key="admin.can.browse.user"/>
							</label>
						</div>
						<div class="checkbox">
							<label for="course-admin-can-change-status-of-course">
							<form:checkbox path="courseAdminCanChangeStatusOfCourse" id="course-admin-can-change-status-of-course"/>
								<fmt:message key="admin.can.change.status"/>
							</label>
						</div>
						<div class="checkbox">
							<label for="enable-course-notifications">
							<form:checkbox path="enableCourseNotifications" id="enable-course-notifications"/>
								<fmt:message key="admin.enable.course.notifications"/>
							</label>
						</div>
						<div class="checkbox">
							<label for="enableGradebookForLearners">
							<form:checkbox id="enableGradebookForLearners" path="enableGradebookForLearners" />
								<fmt:message key="admin.gradebook.learner.enable"/>
							</label>
						</div>
						<div class="checkbox">
							<label for="enable-single-activity-lessons">
							<form:checkbox path="enableSingleActivityLessons" id="enable-single-activity-lessons"/>
								<fmt:message key="config.authoring.single.activity"/>
							</label>
						</div>
					</c:if>
					
						<div class="checkbox">
							<label for="enable-live-edit">
							<form:checkbox path="enableLiveEdit" id="enable-live-edit"/>
								<fmt:message key="config.live.edit"/>
							</label>
						</div>
						
						<div class="checkbox">
							<label for="enable-kumalive">
							<form:checkbox path="enableKumalive" id="enable-kumalive"/>
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
						<a href="javascript:history.back();" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
						<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
					</div>
				
		</form:form>
	</lams:Page>
</body>
</lams:html>
