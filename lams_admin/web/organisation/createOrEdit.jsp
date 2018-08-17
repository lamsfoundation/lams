<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><tiles:getAsString name="title"/></c:set>
	<c:set var="title"><fmt:message key="${title}"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
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
						document.location.href = 'organisation/deleteAllLessonsInit.do?orgId=${courseToDeleteLessons}';
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
	<c:set var="subtitle"><tiles:getAsString name="subtitle" ignore="true"/></c:set>	
	<c:if test="${not empty subtitle}">
		<c:set var="title">${title}: <fmt:message key="${subtitle}"/></c:set>
	</c:if>
	
	<c:set var="help"><tiles:getAsString name='help'  ignore="true"/></c:set>
	<c:choose>
		<c:when test="${not empty help}">
			<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
			<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
				<form:form action="orgsave.do" method="post" modelAttribute="organisationForm" onsubmit="return warnIfRemoved()">
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				
				<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					<c:if test="${OrganisationForm.typeId == 3}">
						: <a href="orgmanage.do?org=<c:out value="${OrganisationForm.parentId}" />" class="btn btn-default"><c:out value="${OrganisationForm.parentName}"/></a>
					</c:if>
					
					<c:if test="${not empty OrganisationForm.orgId}">
						: <a href="orgmanage.do?org=<c:out value="${OrganisationForm.orgId}" />" class="btn btn-default"><c:out value="${OrganisationForm.name}"/></a>
					</c:if>
				</p>
				
				<h4>
					<c:if test="${not empty OrganisationForm.orgId}">
						<fmt:message key="admin.edit"/>&nbsp;<c:out value="${OrganisationForm.name}"/>&nbsp;
					</c:if>
					<<c:if test="${empty OrganisationForm.orgId}">
						<c:if test="${OrganisationForm.typeId == 2}">
							<fmt:message key="admin.course.add"/>
						</c:if>
						<c:if test="${OrganisationForm.typeId == 3}">
							<fmt:message key="admin.class.add"/>
						</c:if>
					</c:if>
				</h4>
				
				<div align="center"><html-el:errors/></div>
				
				<div id="deleteAllLessonsBox" class="alert alert-info" style="display: none">
					<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount"></span> / <span id="totalLessonCount"></span>
					<fmt:message key="label.delete.all.lesson.progress" />
				</div>
				
				
				<table class="table table-condensed table-no-border">
					<tr>
						<td width="15%"><fmt:message key="admin.organisation.name"/> *</td>
						<td><input type="text" name="name" size="40" class="form-control" maxlength="240"/></td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/></td>
						<td><input type="text" name="code" size="20" class="form-control" maxlength="20"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/></td>
						<td><input type="text" name="description" cols="50" rows="3" class="form-control" id="description" /></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.locale"/></td>
						<td>
							<form:select path="localeId" cssClass="form-control" >
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>	
							</form:select>
						</td>
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
					
					<c:if test="${OrganisationForm.typeId == 2}">
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
							<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
							<input type="hidden" name="enableLiveEdit" value="false">
								<fmt:message key="config.live.edit"/>
							</label>
						</div>
						
						<div class="checkbox">
							<label for="enable-kumalive">
							<form:checkbox path="enableKumalive" id="enable-kumalive"/>
								<fmt:message key="config.kumalive.enable"/>
							</label>
						</div>
					
					<c:if test="${not empty OrganisationForm.orgId}">
						<div class="voffset10">
							<c:if test="${OrganisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
							<c:if test="${OrganisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
							&nbsp;<c:out value="${OrganisationForm.orgId}" />.
						</div>
					</c:if>
					
					<div class="pull-right">
						<html-el:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
						<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
					</div>
				
				</form:form>

			</lams:Page>
		</c:when>
		<c:otherwise>
			<lams:Page type="admin" title="${title}" >
				<form:form action="orgsave.do" method="post" modelAttribute="organisationForm" onsubmit="return warnIfRemoved()">
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				
				<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					<c:if test="${OrganisationForm.typeId == 3}">
						: <a href="orgmanage.do?org=<c:out value="${OrganisationForm.parentId}" />" class="btn btn-default"><c:out value="${OrganisationForm.parentName}"/></a>
					</c:if>
					
					<c:if test="${not empty OrganisationForm.orgId}">
						: <a href="orgmanage.do?org=<c:out value="${OrganisationForm.orgId}" />" class="btn btn-default"><c:out value="${OrganisationForm.name}"/></a>
					</c:if>
				</p>
				
				<h4>
					<c:if test="${not empty OrganisationForm.orgId}">
						<fmt:message key="admin.edit"/>&nbsp;<c:out value="${OrganisationForm.name}"/>&nbsp;
					</c:if>
					<<c:if test="${empty OrganisationForm.orgId}">
						<c:if test="${OrganisationForm.typeId == 2}">
							<fmt:message key="admin.course.add"/>
						</c:if>
						<c:if test="${OrganisationForm.typeId == 3}">
							<fmt:message key="admin.class.add"/>
						</c:if>
					</c:if>
				</h4>
				
				<div align="center"><html-el:errors/></div>
				
				<div id="deleteAllLessonsBox" class="alert alert-info" style="display: none">
					<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount"></span> / <span id="totalLessonCount"></span>
					<fmt:message key="label.delete.all.lesson.progress" />
				</div>
				
				
				<table class="table table-condensed table-no-border">
					<tr>
						<td width="15%"><fmt:message key="admin.organisation.name"/> *</td>
						<td><input type="text" name="name" size="40" class="form-control" maxlength="240"/></td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/></td>
						<td><input type="text" name="code" size="20" class="form-control" maxlength="20"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/></td>
						<td><input type="text" name="description" cols="50" rows="3" class="form-control" id="description" /></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.locale"/></td>
						<td>
							<form:select path="localeId" cssClass="form-control" >
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>	
							</form:select>
						</td>
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
					
					<c:if test="${OrganisationForm.typeId == 2}">
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
							<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
							<input type="hidden" name="enableLiveEdit" value="false">
								<fmt:message key="config.live.edit"/>
							</label>
						</div>
						
						<div class="checkbox">
							<label for="enable-kumalive">
							<form:checkbox path="enableKumalive" id="enable-kumalive"/>
								<fmt:message key="config.kumalive.enable"/>
							</label>
						</div>
					
					<c:if test="${not empty OrganisationForm.orgId}">
						<div class="voffset10">
							<c:if test="${OrganisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
							<c:if test="${OrganisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
							&nbsp;<c:out value="${OrganisationForm.orgId}" />.
						</div>
					</c:if>
					
					<div class="pull-right">
						<html-el:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
						<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
					</div>
				
				</form:form>
				
			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>



