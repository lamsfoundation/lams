<%@ include file="/taglibs.jsp"%>

<html-el:form action="/usersave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />

<logic:notEmpty name="UserForm" property="orgId">
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
	<logic:notEmpty name="pOrgId">
		: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default"><c:out value="${parentName}" /></a>
		: <a href="usermanage.do?org=<bean:write name="UserForm" property="orgId" />" class="btn btn-default"><c:out value="${orgName}" /></a>
	</logic:notEmpty>
	<logic:empty name="pOrgId">
		: <a href="orgmanage.do?org=<bean:write name="UserForm" property="orgId" />" class="btn btn-default"><c:out value="${orgName}" /></a>
	</logic:empty>
</logic:notEmpty>

<logic:empty name="UserForm" property="orgId">
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
</logic:empty>

<html-el:errors/>

<div class="panel panel-default voffset5" >
	<div class="panel-heading">
		<span class="panel-title">
			<logic:notEmpty name="UserForm" property="userId">
				<fmt:message key="admin.user.edit"/>
			</logic:notEmpty>
			<logic:empty name="UserForm" property="userId">
				<fmt:message key="admin.user.create"/>
			</logic:empty>
		</span>
	</div>

	<div class="panel-body">     
		<table class="table table-condensed table-no-border">
			<tr>
				<td class="align-right"><fmt:message key="admin.user.login"/> *:</td>
				<td><html-el:text property="login" size="50" maxlength="255" styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.password"/> *:</td>
				<td><html-el:password property="password" size="50" maxlength="50"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.password.confirm"/> *:</td>
				<td><html-el:password property="password2" size="50" maxlength="50"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.authentication.method"/>:</td>
				<td>
					<html-el:select property="authenticationMethodId"  styleClass="form-control">
						<c:forEach items="${authenticationMethods}" var="method">
							<html-el:option value="${method.authenticationMethodId}">
								<c:out value="${method.authenticationMethodName}" />
							</html-el:option>
						</c:forEach>	
					</html-el:select>
				</td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.title"/>:</td>
				<td><html-el:text property="title" size="32" maxlength="32"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.first_name"/> *:</td>
				<td><html-el:text property="firstName" size="50" maxlength="128"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.last_name"/> *:</td>
				<td><html-el:text property="lastName" size="50" maxlength="128"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.email"/> *:</td>
				<td><html-el:text property="email" size="50" maxlength="128"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.address_line_1"/>:</td>
				<td><html-el:text property="addressLine1" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.address_line_2"/>:</td>
				<td><html-el:text property="addressLine2" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.address_line_3"/>:</td>
				<td><html-el:text property="addressLine3" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.city"/>:</td>
				<td><html-el:text property="city" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.postcode"/>:</td>
				<td><html-el:text property="postcode" size="10" maxlength="10" styleClass="form-control" /></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.state"/>:</td>
				<td><html-el:text property="state" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.country"/>:</td>
				<td><html-el:text property="country" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.day_phone"/>:</td>
				<td><html-el:text property="dayPhone" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.evening_phone"/>:</td>
				<td><html-el:text property="eveningPhone" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.mobile_phone"/>:</td>
				<td><html-el:text property="mobilePhone" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>
			<tr>
				<td class="align-right"><fmt:message key="admin.user.fax"/>:</td>
				<td><html-el:text property="fax" size="50" maxlength="64"  styleClass="form-control"/></td>
			</tr>					
			
			<tr>
				<td class="align-right"><fmt:message key="admin.organisation.locale"/>:</td>
				<td>
					<html-el:select property="localeId"  styleClass="form-control">
						<c:forEach items="${locales}" var="locale">
							<html-el:option value="${locale.localeId}">
								<c:out value="${locale.description}" />
							</html-el:option>
						</c:forEach>	
					</html-el:select>
				</td>
			</tr>
			
			<tr>
				<td class="align-right"><fmt:message key="admin.user.time.zone"/>:</td>
				<td>
					<html:select property="timeZone"  styleClass="form-control">
						<c:forEach items="${timezoneDtos}" var="timezoneDto">
							<html:option value="${timezoneDto.timeZoneId}">
								${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
							</html:option>
						</c:forEach>	
					</html:select>
				</td>
			</tr>				
			
			<tr>
				<td class="align-right"><fmt:message key="label.theme"/>:</td>
				<td>
				<html:select property="userTheme" styleClass="form-control">
					<c:forEach items="${themes}" var="theme">	
						<html:option value="${theme.themeId}">${theme.name}</html:option>
					</c:forEach>
				</html:select>
				</td>
			</tr>
		</table>
		
		<div class="pull-right">
			<html-el:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html-el:cancel>
			<html-el:submit styleId="saveButton" styleClass="btn btn-primary loffset5"><fmt:message key="admin.save"/></html-el:submit>
		</div>
		
	</div>
	</div>
	
	
	<c:if test="${not empty globalRoles || not empty userOrgRoles}">
	
		<div class="panel panel-default voffset5" >
		<div class="panel-heading">
			<span class="panel-title">
				<fmt:message key="admin.organisation"/>
			</span>
		</div>

		<table class="table table-no-border">
		
		<c:if test="${not empty globalRoles}">
			<tr><td>
				<table class="table table-striped table-condensed">
					<tr>
						<th><fmt:message key="label.global.roles"/>:</th>
					</tr>
					<tr>
						<td>
							<c:forEach var="role" items="${globalRoles.roles}">
								<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
							</c:forEach>
						</td>
					</tr>
				</table>
			</td></tr>
		</c:if>

		<c:if test="${not empty userOrgRoles}">
			<tr><td>
				<table id="tableRoles" class="table table-striped table-condensed">
					<tr>
						<th><fmt:message key="label.member.of"/>:</th>
						<th><fmt:message key="label.with.roles"/>:</th>
					</tr>
					<c:forEach var="userOrgRole" items="${userOrgRoles}">
						<tr>
							<td><c:out value="${userOrgRole.orgName}"/></td>
							<td>
								<c:forEach var="role" items="${userOrgRole.roles}">
									<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
								</c:forEach>
							</td>
						</tr>
						<c:if test="${not empty userOrgRole.childDTOs}">
							<c:forEach var="child" items="${userOrgRole.childDTOs}">
								<tr>
									<td><i class="fa fa-square"></i>&nbsp;<c:out value="${child.orgName}"/></td>
									<td>
										<c:forEach var="role" items="${child.roles}">
											<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
										</c:forEach>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</c:forEach>
				</table>
			</td></tr>
		</c:if>
		
		</table>
	</div>
	</c:if>

</html-el:form>
