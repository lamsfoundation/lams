<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ page import="java.util.Map"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.web.UsersAddActionForm"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.User"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.web.UsersAddAction"%>


<html>

<head>

<script language="JavaScript" type="text/JavaScript">
<!--

	var names = new Array();
	<c:if test="${ not empty UsersAddActionForm.availableOrgs }">
		<c:forEach var="org" items="${UsersAddActionForm.availableOrgs}">
			names[<c:out value="${org.organisationId}" escapeXml="false"/>]="<c:out value="${org.name}" escapeXml="false"/>";
		</c:forEach>
	</c:if>
	
	function showDescription(listBox){
		document.forms[0].name.value = names[listBox.value];
	
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
				<H2>Add Existing Users To <bean:write name="UsersAddActionForm" property="name"/></H2>
				<html:hidden name="UsersAddActionForm" property="organisationId"/>
				<html:errors/>							
				<p class="body"><strong>Select an organisation from which to pick new users: </strong></p>
				<select name="selectedOrgId" onchange="showName(this)">
					<option value="-1" selected="selected">-</option>
					<c:forEach var="org" items="${UsersAddActionForm.availableOrgs}">
						<option value="<c:out value="${org.organisationId}"/>">
							<c:out value="${org.name}"/>
						</option>									
					</c:forEach>
				</select> 
				<p>
					&nbsp; &nbsp; &nbsp;
					<input name="Show Users" onClick="return submitForm();"	type="button" value="Show Users">
				</p>
				<p>
					<input name="name" type="text" style="{border: 0}" onFocus="javascript:this.blur();" value="" size="60">
				</p>
				<hr>
				<logic:notPresent name="UsersAddActionForm" property="potentialUsers">
					<p class="body" >Please select an organisation from the list above.</p>
				</logic:notPresent>
				<logic:present name="UsersAddActionForm" property="potentialUsers">
					<p class="body"><strong>Select Users and Assign Roles </strong></p>
					<table>
						<c:set var="me" value="${remoteUser}"/>
						<logic:iterate id="user" indexId="index" name="UsersAddActionForm" property="potentialUsers" type="User" >
							<logic:present name="user">
								<TR>
									<TD class="body" valign="top">
										<bean:write name="user" property="login"/>: 
										<bean:write name="user" property="firstName"/>
										<bean:write name="user" property="lastName"/>
									</TD>
									<TD class="body" >							  
										<c:if test="${not ( user.login eq me)}">
											<c:forEach var="item" items="${UsersAddActionForm.allRoleNames}">
												<!--<input type="checkbox" name="potentialRoleNames" value="<c:out value="${user.login}">_<c:out value="${item}"/>">-->
												<c:out value="${item}"/>
											</c:forEach>	
										</c:if>
									</TD>
								</TR>
							</logic:present>
						</logic:iterate> 
					</table>
				</logic:present>
				<p><html:cancel>Cancel</html:cancel>&nbsp; 	
				<html:submit><%=UsersAddAction.SUBMIT_ADD_USERS%></html:submit> &nbsp; 	
			</td>
		</tr>
	</table>
</html:form>

</html>