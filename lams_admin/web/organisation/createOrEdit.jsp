<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">
	function selectMonitorGradebook() {
		document.getElementById("enableGradebookForMonitors").checked = true;
	}
	
	function unSelectLearnerGradebook() {
		var mon = document.getElementById("enableGradebookForMonitors");
		var lrn = document.getElementById("enableGradebookForLearners");
		
		if (lrn.checked) {
			lrn.checked = mon.checked;
		}
	}

</script>

<html-el:form action="/orgsave.do" method="post">
<html-el:hidden property="orgId" />
<html-el:hidden property="parentId" />
<html-el:hidden property="typeId" />
<h4>
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	
	<logic:equal name="OrganisationForm" property="typeId" value="3">
		: <a href="orgmanage.do?org=<bean:write name="OrganisationForm" property="parentId" />"><bean:write name="OrganisationForm" property="parentName"/></a>
	</logic:equal>
	
	<logic:notEmpty name="OrganisationForm" property="orgId">
		: <a href="orgmanage.do?org=<bean:write name="OrganisationForm" property="orgId" />"><bean:write name="OrganisationForm" property="name"/></a>
	</logic:notEmpty>
</h4>

<h1>
	<logic:notEmpty name="OrganisationForm" property="orgId">
		<fmt:message key="admin.edit"/> <bean:write name="OrganisationForm" property="name"/>
	</logic:notEmpty>
	<logic:empty name="OrganisationForm" property="orgId">
		<logic:equal name="OrganisationForm" property="typeId" value="2">
			<fmt:message key="admin.course.add"/>
		</logic:equal>
		<logic:equal name="OrganisationForm" property="typeId" value="3">
			<fmt:message key="admin.class.add"/>
		</logic:equal>
	</logic:empty>
</h1>

<div align="center"><html-el:errors/></div>
<table>
	<tr>
		<td><fmt:message key="admin.organisation.name"/>:</td>
		<td><html-el:text property="name" size="40" maxlength="240"/> *</td>
	</tr>
	<tr>
	<td><fmt:message key="admin.organisation.code"/>:</td>
		<td><html-el:text property="code" size="20" maxlength="20"/></td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.description"/>:</td>
		<td><html-el:textarea styleId="description" property="description" cols="50" rows="3" /></td>
		<script type="text/javascript">
			document.getElementById("description").setAttribute("maxlength", "240");
		</script>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.locale"/>:</td>
		<td>
			<html-el:select property="localeId">
				<c:forEach items="${locales}" var="locale">
					<html-el:option value="${locale.localeId}">
						<c:out value="${locale.description}" />
					</html-el:option>
				</c:forEach>	
			</html-el:select>
		</td>
	</tr>
	<tr>
		<td><fmt:message key="admin.organisation.status"/>:</td>
		<td>
			<html-el:select property="stateId">
				<c:forEach items="${status}" var="state">
					<html-el:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></html-el:option>
				</c:forEach>
			</html-el:select>
		</td>
	</tr>
	
	<logic:equal name="OrganisationForm" property="typeId" value="2">
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanAddNewUsers" styleId="course-admin-can-add-new-users" />
			<label for="course-admin-can-add-new-users">
				<fmt:message key="admin.can.add.user"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanBrowseAllUsers" styleId="course-admin-can-browse-all-users"/>
			<label for="course-admin-can-browse-all-users">
				<fmt:message key="admin.can.browse.user"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="courseAdminCanChangeStatusOfCourse" styleId="course-admin-can-change-status-of-course"/>
			<label for="course-admin-can-change-status-of-course">
				<fmt:message key="admin.can.change.status"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="enableCourseNotifications" styleId="enable-course-notifications"/>
			<label for="enable-course-notifications">
				<fmt:message key="admin.enable.course.notifications"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox onclick="unSelectLearnerGradebook();" styleId="enableGradebookForMonitors" property="enableGradebookForMonitors" />
			<label for="enableGradebookForMonitors">
				<fmt:message key="admin.gradebook.monitor.enable"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox onclick="selectMonitorGradebook();" styleId="enableGradebookForLearners" property="enableGradebookForLearners" />
			<label for="enableGradebookForLearners">
				<fmt:message key="admin.gradebook.learner.enable"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="enableSingleActivityLessons" styleId="enable-single-activity-lessons"/>
			<label for="enable-single-activity-lessons">
				<fmt:message key="config.authoring.single.activity"/>
			</label>
		</td>
	</tr>
	</logic:equal>
	
	<tr>
		<td colspan=2>
			<html-el:checkbox property="enableLiveEdit" styleId="enable-live-edit"/>
			<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
			<input type="hidden" name="enableLiveEdit" value="false">
			<label for="enable-live-edit">
				<fmt:message key="config.live.edit"/>
			</label>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<html-el:checkbox property="enableExportPortfolio" styleId="enable-export-portfolio"/>			
			<!-- to overcome nasty DynaActionForm bug (http://www.coderanch.com/t/46408/Struts/DynaValidatorActionForm-checkboxes) -->
			<input type="hidden" name="enableExportPortfolio" value="false">
			<label for="enable-export-portfolio">
				<fmt:message key="config.export.portfolio"/>
			</label>
		</td>
	</tr>
	
	<logic:notEmpty name="OrganisationForm" property="orgId">
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<logic:equal name="OrganisationForm" property="typeId" value="2"><fmt:message key="msg.group.organisation_id"/></logic:equal>
			<logic:equal name="OrganisationForm" property="typeId" value="3"><fmt:message key="msg.subgroup.organisation_id"/></logic:equal>
			<bean:write name="OrganisationForm" property="orgId" />.
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	</logic:notEmpty>
	
	<tr>
		<td colspan=2 class="align-right">
			<html-el:submit styleId="saveButton" styleClass="button"><fmt:message key="admin.save"/></html-el:submit>
			<html-el:cancel styleId="cancelButton" styleClass="button"><fmt:message key="admin.cancel"/></html-el:cancel>
		</td>
	</tr>
</table>
</html-el:form>
