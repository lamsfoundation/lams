<%@ include file="/taglibs.jsp"%>

<html-el:form action="/usersave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h2 align="left">
	<logic:notEmpty name="UserForm" property="orgId">
		<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
		<logic:notEmpty name="pOrgId">
			: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />"><c:out value="${parentName}" /></a>
			: <a href="usermanage.do?org=<bean:write name="UserForm" property="orgId" />"><c:out value="${orgName}" /></a>
		</logic:notEmpty>
		<logic:empty name="pOrgId">
			: <a href="orgmanage.do?org=<bean:write name="UserForm" property="orgId" />"><c:out value="${orgName}" /></a>
		</logic:empty>
	</logic:notEmpty>
	<logic:empty name="UserForm" property="orgId">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		: <a href="usersearch.do"><fmt:message key="admin.user.find" /></a>
	</logic:empty>
	<logic:notEmpty name="UserForm" property="userId">
		: <fmt:message key="admin.user.edit"/>
	</logic:notEmpty>
	<logic:empty name="UserForm" property="userId">
		: <fmt:message key="admin.user.create"/>
	</logic:empty>
</h2>

<p>&nbsp;</p>

<div align="center"><html-el:errors/></div>

<table>
	<tr>
		<td>
			<table>
				<tr>
					<td align="right"><fmt:message key="admin.user.login"/> *:</td>
					<td><html-el:text property="login" size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.password"/> *:</td>
					<td><html-el:password property="password" size="20" maxlength="50" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.password.confirm"/> *:</td>
					<td><html-el:password property="password2" size="20" maxlength="50" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.title"/>:</td>
					<td><html-el:text property="title" size="20" maxlength="32" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.first_name"/> *:</td>
					<td><html-el:text property="firstName" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.last_name"/> *:</td>
					<td><html-el:text property="lastName" size="20" maxlength="128" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.email"/> *:</td>
					<td><html-el:text property="email" size="20" maxlength="128" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.address_line_1"/>:</td>
					<td><html-el:text property="addressLine1" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.address_line_2"/>:</td>
					<td><html-el:text property="addressLine2" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.address_line_3"/>:</td>
					<td><html-el:text property="addressLine3" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.city"/>:</td>
					<td><html-el:text property="city" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.postcode"/>:</td>
					<td><html-el:text property="postcode" size="20" maxlength="10" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.state"/>:</td>
					<td><html-el:text property="state" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.country"/>:</td>
					<td><html-el:text property="country" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.day_phone"/>:</td>
					<td><html-el:text property="dayPhone" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.evening_phone"/>:</td>
					<td><html-el:text property="eveningPhone" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.mobile_phone"/>:</td>
					<td><html-el:text property="mobilePhone" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.user.fax"/>:</td>
					<td><html-el:text property="fax" size="20" maxlength="64" /></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key="admin.organisation.locale"/>:</td>
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
					<td colspan=2 align="right">
						<html-el:submit><fmt:message key="admin.save"/></html-el:submit>
						<html-el:reset><fmt:message key="admin.reset"/></html-el:reset>
						<html-el:cancel><fmt:message key="admin.cancel"/></html-el:cancel>
					</td>
				</tr>
			</table>
			</td>

			<c:if test="${not empty userOrgRoles}">
				<td>
					<table class="alternative-color" cellspacing="0">
						<tr>
							<th><fmt:message key="label.member.of"/>:</th>
							<th><fmt:message key="label.with.roles"/>:</th>
						</tr>
						<c:forEach var="userOrgRole" items="${userOrgRoles}">
							<tr>
								<td><c:out value="${userOrgRole.orgName}"/></td>
								<td>
									<small>
									<c:forEach var="role" items="${userOrgRole.roles}">
										<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
									</c:forEach>
									</small>
								</td>
							</tr>
							<c:if test="${not empty userOrgRole.childDTOs}">
								<c:forEach var="child" items="${userOrgRole.childDTOs}">
									<tr>
										<td>-- <c:out value="${child.orgName}"/></td>
										<td>
											<small>
											<c:forEach var="role" items="${child.roles}">
												<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
											</c:forEach>
											</small>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</c:forEach>
					</table>
				</td>
			</c:if>
	</tr>

</table>

</html-el:form>
