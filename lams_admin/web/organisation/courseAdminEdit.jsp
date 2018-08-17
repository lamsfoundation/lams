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
				<form:form action="orgsave.do" modelAttribute="organisationForm" method="post">
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				<form:hidden path="courseAdminCanAddNewUsers" />
				<form:hidden path="courseAdminCanBrowseAllUsers" />
				<form:hidden path="courseAdminCanChangeStatusOfCourse" />
				<c:if test="${OrganisationForm.courseAdminCanChangeStatusOfCourse == false}">
					<form:hidden path="stateId" />
				</c:if>
				<p>
					<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> : 
					<a href="orgmanage.do?org=<c:out value="${OrganisationForm.orgId}" />" class="btn btn-default">
						<c:out value="${OrganisationForm.name}"/>
					</a>
				</p>
				
				<h1>
					<fmt:message key="admin.edit"/> <c:out value="${OrganisationForm.name}"/>
				</h1>
				
				<div align="center"><html-el:errors/></div>
				<table  class="table table-no-border">
					<tr>
						<td><fmt:message key="admin.organisation.name"/>:</td>
						<td><input type="text" name="name" size="40" class="form-control"/> *</td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/>:</td>
						<td><input type="text" name="code" size="20"  class="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/>:</td>
						<td><form:textarea path="description" cols="50" rows="3"  cssClass="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.locale"/>:</td>
						<td>
							<form:select path="localeId"  cssClass="form-control">
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>	
							</form:select>
						</td>
					</tr>
					<c:if test=${OrganisationForm.courseAdminCanChangeStatusOfCourse == true}>
					<tr>
						<td><fmt:message key="admin.organisation.status"/>:</td>
						<td>
							<form:select path="stateId"  cssClass="form-control">
								<c:forEach items="${status}" var="state">
									<form:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></form:option>
								</c:forEach>
							</form:select>
						</td>
					</tr>
					</c:if>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><c:if test="${OrganisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
							<c:if test="${OrganisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
							<c:out value="${OrganisationForm.orgId}" />.
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				
				<div class="pull-right">
					<html-el:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
					<input type="reset" class="btn btn-default loffset5" value="<fmt:message key="admin.reset"/>" />
					<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
				</div>
				
				</form:form>
			</lams:Page>
			
		</c:when>
		<c:otherwise>
		
			<lams:Page type="admin" title="${title}" >
				<form:form action="orgsave.do" modelAttribute="organisationForm" method="post">
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				<form:hidden path="courseAdminCanAddNewUsers" />
				<form:hidden path="courseAdminCanBrowseAllUsers" />
				<form:hidden path="courseAdminCanChangeStatusOfCourse" />
				<c:if test="${OrganisationForm.courseAdminCanChangeStatusOfCourse == false}">
					<form:hidden path="stateId" />
				</c:if>
				<p>
					<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> : 
					<a href="orgmanage.do?org=<c:out value="${OrganisationForm.orgId}" />" class="btn btn-default">
						<c:out value="${OrganisationForm.name}"/>
					</a>
				</p>
				
				<h1>
					<fmt:message key="admin.edit"/> <c:out value="${OrganisationForm.name}"/>
				</h1>
				
				<div align="center"><html-el:errors/></div>
				<table  class="table table-no-border">
					<tr>
						<td><fmt:message key="admin.organisation.name"/>:</td>
						<td><input type="text" name="name" size="40" class="form-control"/> *</td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/>:</td>
						<td><input type="text" name="code" size="20"  class="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/>:</td>
						<td><form:textarea path="description" cols="50" rows="3"  cssClass="form-control"/></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.locale"/>:</td>
						<td>
							<form:select path="localeId"  cssClass="form-control">
								<c:forEach items="${locales}" var="locale">
									<form:option value="${locale.localeId}">
										<c:out value="${locale.description}" />
									</form:option>
								</c:forEach>	
							</form:select>
						</td>
					</tr>
					<c:if test=${OrganisationForm.courseAdminCanChangeStatusOfCourse == true}>
					<tr>
						<td><fmt:message key="admin.organisation.status"/>:</td>
						<td>
							<form:select path="stateId"  cssClass="form-control">
								<c:forEach items="${status}" var="state">
									<form:option value="${state.organisationStateId}"><fmt:message key="organisation.state.${state.description}"/></form:option>
								</c:forEach>
							</form:select>
						</td>
					</tr>
					</c:if>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><c:if test="${OrganisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
							<c:if test="${OrganisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
							<c:out value="${OrganisationForm.orgId}" />.
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				
				<div class="pull-right">
					<html-el:cancel styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
					<input type="reset" class="btn btn-default loffset5" value="<fmt:message key="admin.reset"/>" />
					<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
				</div>
				
				</form:form>
			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>




		