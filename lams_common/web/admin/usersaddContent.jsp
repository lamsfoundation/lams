<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ page import="com.lamsinternational.lams.usermanagement.web.UsersAddActionForm"%>
<%@ page import="com.lamsinternational.lams.usermanagement.User"%>


<html>

<head>

<script language="JavaScript" type="text/JavaScript">
<!--

	var descriptions = new Array();
	<c:set var="actionform" value="${UsersAddActionForm}"/>
	<c:if test="${ not empty actionform.availableOrgs }">
		<c:forEach var="org" items="${actionform.availableOrgs}">
			descriptions[<c:out value="${org.sid}" escapeXml="false"/>]="<c:out value="${org.description}" escapeXml="false"/>";
		</c:forEach>
	</c:if>
	
	function showDescription(listBox){
		document.forms[0].description.value = descriptions[listBox.value];
	
	}

    function submitForm()
    {
		document.forms[0].action="usersadd.do?submit=Show Users";
	
		document.forms[0].submit();
		return true;
    }
//-->
</script>

</head>

<html:form action="/usersadd" method="post">
	<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
		<tr> 
			<td valign="top">
				<H2>Add Existing Users To <bean:write name="<%=UsersAddActionForm.formName%>" property="name"/></H2>
				<html:hidden name="<%=UsersAddActionForm.formName%>" property="organisationId"/>
				<html:errors/>							
				<p class="body"><strong>Select an organisation from which to pick new users: </strong></p>
				<c:set var="currOrgId" value="${UsersAddActionForm.organisationId}"/>
				<select name="selectedOrgId" onchange="showDescription(this)">
					<option value="-1" selected="selected">-</option>
					<c:forEach var="org" items="${actionform.availableOrgs}">
						<option value="<c:out value="${org.organisationId}"/>"><c:out value="${org.name}"/></option>									
					</c:forEach>
				</select> 
				<p>
					&nbsp; &nbsp; &nbsp;
					<input name="Show Users" onClick="return submitForm();"	type="button" value="Show Users" />
				</p>
				<p>
					<input name="description" type="text" style="{border: 0}" onFocus="javascript:this.blur();" value="" size="60">
				</p>
				<hr>
				<bean:define id="form" name="<%=UsersAddActionForm.formName%>" type="UsersAddActionForm"/>
				<logic:notPresent name="<%=UsersAddActionForm.formName%>" property="potentialUsers">
					<p class="body" >Please select an organisation from the list above.</p>
				</logic:notPresent>
				<logic:present name="<%=UsersAddActionForm.formName%>" property="potentialUsers">
					<p class="body"><strong>Select a role. To select a role for a user, choose one of the available roles from the drop-down menus. Leave all entries blank for users that you don't want to add to  the organisation</strong></p>
					<table>
						<c:set var="me" value="${pageContext.request.remoteUser}"/>
						<logic:iterate id="user" name="<%=UsersAddActionForm.formName%>" property="potentialUsers" type="User" >
							<logic:present name="user">
								<TR>
									<TD class="body" valign="top">
										<bean:write name="user" property="login"/>: 
										<bean:write name="user" property="firstName"/>
										<bean:write name="user" property="lastName"/>
									</TD>
									<TD class="body" >							  
										<c:choose>
											<c:when test="${not ( user.login eq me)}">
												<logic:iterate id="item" name="UsersAddActionForm" property="allRoleNames">
													<html:multibox name="UsersAddActionForm" property="roleNames">
														<bean:write name="item"/>
													</html:multibox>
													<bean:write name="item"/>
												</logic:iterate>	
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</TD>
								</TR>
							</logic:present>
						</logic:iterate> 
					</table>
				</logic:present>
				<p><html:cancel>Cancel</html:cancel>&nbsp; 	
				<html:submit><%=OrganisationAddUsersAction.SUBMIT_ADD_USERS%></html:submit> &nbsp; 	
			</td>
		</tr>
	</table>
</html:form>

</html>