<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="com.lamsinternational.lams.usermanagement.UserOrganisation" %>
<%@ page import="com.lamsinternational.lams.usermanagement.Organisation" %>
<%@ page import="com.lamsinternational.lams.usermanagement.Role" %>
<%@ page import="com.lamsinternational.lams.usermanagement.User" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<form name="form1" method="post">
	<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
		<tr> 
			<td valign="top">
				<H2>User Details</H2>
				<logic:notPresent name="user">
					<p class="body">User details not found</p>
				</logic:notPresent>
				<logic:present name="user">
					<table>
						<tr>
							<td class="body" align="right">Login name:</td>
							<td class="body" align="left">
								<bean:write name="user" property="login"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Authentication Method:</td>
							<td class="body" align="left">
								<c:out value="${user.authenticationMethod.authenticationMethodName}"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Roles:</td>
							<td class="body" align="left">
								<logic:iterate id="role" name="roles" type="Role">
									<logic:present name="role">
										<bean:write name="role" property="name"/>&nbsp; &nbsp; &nbsp;
									</logic:present>
								</logic:iterate>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Title:</td>
							<td class="body" align="left">
								<bean:write name="user" property="title"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">First Name:</td>
							<td class="body" align="left">
								<bean:write name="user" property="firstName"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Last Name:</td>
							<td class="body" align="left">
								<bean:write name="user" property="lastName"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Email:</td>
							<td class="body" align="left">
								<bean:write name="user" property="email"/>
							</td>
						</tr>
						<tr>
							<td colspan="2"><hr></td>
						</tr>				
						<tr>
							<td class="body" align="right">Address Line 1:</td>
							<td class="body" align="left">
								<bean:write name="user" property="addressLine1"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Address Line 2:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="addressLine2"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Address Line3:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="addressLine3"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">City:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="city"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">State:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="state"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Country:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="country"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Day Phone:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="dayPhone"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Evening Phone:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="evePhone"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Mobile Phone:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="mobPhone"/>
							</td>
						</tr>
						<tr>
							<td class="body" align="right">Fax:</td>
							<td class="body" align="left">
								<bean:write name="user"  property="fax"/>
							</td>
						</tr>
						<tr>
							<td colspan="2"><hr></td>
						</tr>				
						<tr>
							<td colspan="2" class="body" >LAMS User Organisations</td>
						</tr>				
						<logic:iterate id="membership" name="memberships">
							<tr>
								<bean:define id="member" name="membership" type="UserOrganisation"/>
								<td class="body" align="right" valign="top"><%=member.getOrganisation().getName()%> </td>
								<td class="body" align="left">
									Description: <%=member.getOrganisation().getDescription()%><BR>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
				</table>
				<p> 					
					<input name="ReturnOrganisation" type="button" class="longButton" id="ReturnOrganisation" 
						onClick="javascript:document.location='admin.do?method=getOrganisationEdit&orgId=<c:out value="${organisation.organisationId}">';"
						onMouseOver="changeStyle(this,'longButtonover')"
						onMouseOut="changeStyle(this,'longButton')" 
						value="Return to Organisation" />
				</p>
			</td>
		</tr>
	</table>
</form>
