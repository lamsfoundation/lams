<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.UserOrganisation" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Organisation" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.User" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/user" method="post">
	<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" class="body">
		<tr> 
			<td valign="top">
				<span class="mainHeader">User edit</span> 
				<p class="body">
					Required fields are marked with an asterisk.<BR>
					<html:errors/>
				</p>
				<table width="100%" align="center" class="body">
					<tr bgcolor="#668A80">
						<td colspan="2" class="tableHeader" >Membership:</td>
					</tr>				
					<logic:present name="UserActionForm" property="otherMemberships" >
						<logic:iterate id="membershipItem" name="UserActionForm" property="otherMemberships" >
							<tr align="left">
								<bean:define id="memberItem" name="membershipItem" type="UserOrganisation"/>
									<td colspan="2" valign="top">
										<span class="subHeader"><%=memberItem.getOrganisation().getName()%></span><br>
										<span class="body"><%=memberItem.getOrganisation().getDescription()%></span>
									</td>
							</tr>								 
						</logic:iterate>
					</logic:present>
 					
					<logic:present name="UserActionForm" property="newMembershipOrganisationId">
						<tr valign="top">
							<td width="19%" align="right" class="body">
								Adding to organisation: 
							</td>
							<td width="81%" align="left" class="body">
								<bean:write name="UserActionForm" property="newMembershipOrgName"/>
								<html:hidden name="UserActionForm" property="newMembershipOrganisationId"/>
								<html:hidden name="UserActionForm" property="newMembershipOrgName"/>
							</td>
						</tr>
					</logic:present> 

					<tr>
						<td class="body" align="right">
							<html:hidden name="UserActionForm" property="userId"/>
							<html:hidden name="UserActionForm" property="createNew"/>
							Login: *
						</td>
						<td class="body" align="left">
							<logic:equal name="UserActionForm" property="createNew" value="true">
								<html:text name="UserActionForm" property="login" size="20" maxlength="20" styleClass="textField" /> 
							</logic:equal>
							<logic:notEqual name="UserActionForm" property="createNew" value="true">
								<html:hidden name="UserActionForm"  property="login"/>
								<bean:write name="UserActionForm" property="login"/>
							</logic:notEqual>
						</td>
					</tr>

					<tr>
						<td class="body" align="right">
							Authentication Method: *
						</td>
						<td class="body" align="left">
							<logic:iterate id="authMethod" name="UserActionForm" property="allAuthMethods">
								<html:radio property="authMethodName" name="UserActionForm" idName="authMethod" value="authenticationMethodName">
									<bean:write name="authMethod" property="authenticationMethodName"/>
								</html:radio>
							</logic:iterate>
						</td>
					</tr>

					
					<tr>
						<td class="body" align="right">Title:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="title" size="32" maxlength="32"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">First Name:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="firstName" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Last Name:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm"  styleClass="textField" property="lastName" size="64" maxlength="128"/> 
						</td>
					</tr>
					<tr bgcolor="#668A80">
						<td colspan="2" class="tableHeader" >Password:</td>
					</tr>				
					<tr>
						<td class="body" colspan="2">
							Only enter a password if you are creating a new user or wish to change a current password. 
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Password:</td>
						<td class="body" align="left">
							<html:password name="UserActionForm" styleClass="textField" property="password" size="50" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Confirm Password:</td>
						<td class="body" align="left">
							<html:password name="UserActionForm"  styleClass="textField" property="passwordConfirm" size="50" maxlength="50"/>
						</td>
					</tr>
					<tr bgcolor="#668A80">
						<td colspan="2" class="tableHeader" >Details:</td>
					</tr>				
					<tr>
						<td class="body" align="right">
							Email:
						</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="email" size="64" maxlength="128"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Address Line 1:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="addressLine1" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Address Line 2:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="addressLine2" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Address Line 3:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="addressLine3" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">City:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="city" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">State:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="state" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Country:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="country" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Day Phone:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="dayPhone" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Evening Phone:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="evePhone" size="64" maxlength="64"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Mobile Phone:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="mobPhone" size="64" maxlength="64"/>
						</td>
					</tr>
				
					<tr>
						<td class="body" align="right">Fax:</td>
						<td class="body" align="left">
							<html:text name="UserActionForm" styleClass="textField" property="fax" size="32" maxlength="32"/>
						</td>
					</tr>
					<tr>
						<td class="body" align="right">Roles:</td>
						<td class="body" align="left">					
							<logic:iterate id="item" name="UserActionForm" property="allRoleNames">
								<html:multibox name="UserActionForm" property="roleNames">
									<bean:write name="item"/>
								</html:multibox>
								<bean:write name="item"/>
							</logic:iterate>
						</td>
					</tr>
					<tr>
						<td class="body">&nbsp;</td>
						<td class="body" align="left">
						</td>
					</tr>
				</table>
				<p align="right"> 
					<html:submit>Save</html:submit> &nbsp; 	
					<html:cancel>Cancel</html:cancel>
				</p>
			</td>
		</tr>
	</table>
</html:form>
