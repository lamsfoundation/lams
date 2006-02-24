<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="org.lamsfoundation.lams.admin.web.OrganisationActionForm" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>

<html:form action="/admin/organisation" method="post">
	<table width="100%" height="177" border="0" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF">
		<tr> 
			<td valign="top">
				<span class="heading">Organisation Entry</span>
			
				<P class="body">
					An "organisation" is a grouping of users. It could be a high school, a particular year or a single class.
					If could be a set of people doing a particular course.
				</p>
				
				<p class="body">Required fields are marked with an asterisk.<BR><html:errors/></p>
				
				<table>
					<tr>
						<td class="body" align="right">
							<html:hidden name="<%=OrganisationActionForm.formName%>" property="orgId"/>
							Name:
						</td>
						<td class="body" align="left">				
							<html:text name="<%=OrganisationActionForm.formName%>"  property="name" size="50" maxlength="255" styleClass="textField"/>*
						</td>
					</tr>
					<tr>
						<td class="body" align="right" valign="top">
							Description:
						</td>
						<td class="body" align="left">
							<html:textarea name="<%=OrganisationActionForm.formName%>" property="description" cols="53" rows="5" styleClass="textField"/> 
							*
						</td>
					</tr>
					<c:if test="${not empty OrganisationActionForm.parentOrgName}">				
						<tr>
							<td class="body" align="right">
								Parent Organisation:
							</td>
							<td class="body" align="left">
								<c:out value="${OrganisationActionForm.parentOrgName}"/> 
							</td>
						</tr>
					</c:if>
				</table>
				<p class="body">					
					<html:hidden name="OrganisationActionForm" property="parentOrgId" />
					<html:hidden name="OrganisationActionForm" property="parentOrgName" />
					<html:submit styleClass="button" onmouseout="changeStyle(this,'button')" onmouseover="changeStyle(this,'buttonover')">Save</html:submit> &nbsp; 
					<html:cancel styleClass="button" onmouseout="changeStyle(this,'button')" onmouseover="changeStyle(this,'buttonover')">Cancel</html:cancel>
				</p>
			</td>
		</tr>
	</table>
</html:form>
