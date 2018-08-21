<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.organisation.entry"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}" formID="organisationForm">
	
		<form:form action="../orgsave.do" modelAttribute="organisationForm" id="organisationForm" method="post">
				<form:hidden path="orgId" />
				<form:hidden path="parentId" />
				<form:hidden path="typeId" />
				<form:hidden path="courseAdminCanAddNewUsers" />
				<form:hidden path="courseAdminCanBrowseAllUsers" />
				<form:hidden path="courseAdminCanChangeStatusOfCourse" />
				<c:if test="${organisationForm.courseAdminCanChangeStatusOfCourse == false}">
					<form:hidden path="stateId" />
				</c:if>
				<p>
					<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> : 
					<a href="orgmanage.do?org=<c:out value="${organisationForm.orgId}" />" class="btn btn-default">
						<c:out value="${organisationForm.name}"/>
					</a>
				</p>
				
				<h1>
					<fmt:message key="admin.edit"/> <c:out value="${organisationForm.name}"/>
				</h1>
				
				<div align="center"><html-el:errors/></div>
				<table  class="table table-no-border">
					<tr>
						<td><fmt:message key="admin.organisation.name"/>:</td>
						<td><input type="text" name="name" size="40" class="form-control" value="${organisationForm.name}" /> *</td>
					</tr>
					<tr>
					<td><fmt:message key="admin.organisation.code"/>:</td>
						<td><input type="text" name="code" size="20"  class="form-control" value="${organisationForm.code}" /></td>
					</tr>
					<tr>
						<td><fmt:message key="admin.organisation.description"/>:</td>
						<td><textarea name="description" cols="50" rows="3"  class="form-control">${organisationForm.description}</textarea></td>
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
					<c:if test=${organisationForm.courseAdminCanChangeStatusOfCourse == true}>
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
						<td><c:if test="${organisationForm.typeId == 2}"><fmt:message key="msg.group.organisation_id"/></c:if>
							<c:if test="${organisationForm.typeId == 3}"><fmt:message key="msg.subgroup.organisation_id"/></c:if>
							<c:out value="${organisationForm.orgId}" />.
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				
				<div class="pull-right">
					<input type="submit" name="CANCEL" value="<fmt:message key="admin.cancel"/>" onclick="bCancel=true;" class="btn btn-default">
					<input type="reset" class="btn btn-default loffset5" value="<fmt:message key="admin.reset"/>" />
					<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save"/>" />
				</div>
				
		</form:form>
	</lams:Page>

</body>
</lams:html>




		