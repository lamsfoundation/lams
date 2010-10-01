<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<html-el:form action="/usersave.do" method="post">
<html-el:hidden property="userId" />
<html-el:hidden property="orgId" />
<h4 class="align-left">
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
		<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
	</logic:empty>
</h4>

<h1>
	<logic:notEmpty name="UserForm" property="userId">
		<fmt:message key="admin.user.edit"/>
	</logic:notEmpty>
	<logic:empty name="UserForm" property="userId">
		<fmt:message key="admin.user.create"/>
	</logic:empty>
</h1>

<div align="center"><html-el:errors/></div>

<table>
	<tr>
		<td>
			<table>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.login"/> *:</td>
					<td><html-el:text property="login" size="50" maxlength="255" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.password"/> *:</td>
					<td><html-el:password property="password" size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.password.confirm"/> *:</td>
					<td><html-el:password property="password2" size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.authentication.method"/>:</td>
					<td>
						<html-el:select property="authenticationMethodId">
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
					<td><html-el:text property="title" size="32" maxlength="32" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.first_name"/> *:</td>
					<td><html-el:text property="firstName" size="50" maxlength="128" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.last_name"/> *:</td>
					<td><html-el:text property="lastName" size="50" maxlength="128" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.email"/> *:</td>
					<td><html-el:text property="email" size="50" maxlength="128" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.address_line_1"/>:</td>
					<td><html-el:text property="addressLine1" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.address_line_2"/>:</td>
					<td><html-el:text property="addressLine2" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.address_line_3"/>:</td>
					<td><html-el:text property="addressLine3" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.city"/>:</td>
					<td><html-el:text property="city" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.postcode"/>:</td>
					<td><html-el:text property="postcode" size="10" maxlength="10" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.state"/>:</td>
					<td><html-el:text property="state" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.country"/>:</td>
					<td><html-el:text property="country" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.day_phone"/>:</td>
					<td><html-el:text property="dayPhone" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.evening_phone"/>:</td>
					<td><html-el:text property="eveningPhone" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.mobile_phone"/>:</td>
					<td><html-el:text property="mobilePhone" size="50" maxlength="64" /></td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="admin.user.fax"/>:</td>
					<td><html-el:text property="fax" size="50" maxlength="64" /></td>
				</tr>
				
				<c:set var="serverFlashEnabled"><%=Configuration.get(ConfigurationKeys.FLASH_ENABLE)%></c:set>
				<c:choose>
					<c:when test="${serverFlashEnabled}"> 
						<tr>
							<td class="align-right"><fmt:message key="admin.user.enable.flash.for.learner.window"/>:</td>
							<td><html:select property="enableFlash">
								<html:option value="true"><fmt:message key="label.yes"/></html:option>
								<html:option value="false"><fmt:message key="label.no"/></html:option>
								</html:select>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<html:hidden property="enableFlash" />
					</c:otherwise>
				</c:choose>				
				
				<tr>
					<td class="align-right"><fmt:message key="admin.organisation.locale"/>:</td>
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
					<td class="align-right"><fmt:message key="admin.user.time.zone"/>:</td>
					<td>
						<html:select property="timeZone" style="width:328px;">
							<c:forEach items="${timezoneDtos}" var="timezoneDto">
								<html:option value="${timezoneDto.timeZoneId}">
									${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
								</html:option>
							</c:forEach>	
						</html:select>
					</td>
				</tr>				
				
				<tr>
					<td class="align-right"><fmt:message key="label.html.htmlTheme"/>:</td>
					<td>
					<html:select property="userCSSTheme">
						<c:forEach items="${cssThemes}" var="theme">	
							<html:option value="${theme.themeId}">${theme.name}</html:option>
						</c:forEach>
					</html:select>
					</td>
				</tr>
				<tr>
					<td class="align-right"><fmt:message key="label.html.flashTheme"/>:</td>
					<td>
					<html:select property="userFlashTheme">
						<c:forEach items="${flashThemes}" var="theme">	
							<html:option value="${theme.themeId}">${theme.name}</html:option>
						</c:forEach>
					</html:select>
					</td>
				</tr>
				<tr>
					<td colspan=2 class="align-right">
						<html-el:submit styleClass="button"><fmt:message key="admin.save"/></html-el:submit>
						<html-el:reset styleClass="button"><fmt:message key="admin.reset"/></html-el:reset>
						<html-el:cancel styleClass="button"><fmt:message key="admin.cancel"/></html-el:cancel>
					</td>
				</tr>
			</table>
			</td>

			<td>
				<table>
				
				<c:if test="${not empty globalRoles}">
					<tr><td>
						<table class="alternative-color" cellspacing="0">
							<tr>
								<th><fmt:message key="label.global.roles"/>:</th>
							</tr>
							<tr>
								<td>
									<small>
									<c:forEach var="role" items="${globalRoles.roles}">
										<fmt:message>role.<lams:role role="${role}" /></fmt:message>&nbsp;
									</c:forEach>
									</small>
								</td>
							</tr>
						</table>
					</td></tr>
				</c:if>

				<c:if test="${not empty userOrgRoles}">
					<tr><td>
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
					</td></tr>
				</c:if>
				
				</table>
			</td>
	</tr>

</table>

</html-el:form>
