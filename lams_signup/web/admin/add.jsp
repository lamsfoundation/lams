<%@ include file="/taglibs.jsp"%>

<h1>Add/edit new signup page</h1>

<html:form action="/admin.do" method="post">
	<html:hidden property="method" value="add" />
	<html:hidden property="signupOrganisationId" />
	
	<table border="0" cellspacing="1" cellpadding="1" align="left">
		<tr>
			<td align="right">Group:	</td>
			<td align="left">
				<html:select property="organisationId">
					<c:forEach items="${organisations}" var="organisation">
						<html:option value="${organisation.organisationId}"><c:out value="${organisation.name}" /></html:option>
					</c:forEach>
				</html:select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Add to lessons?:	</td>
			<td align="left"><html:checkbox property="addToLessons" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Add as staff?:	</td>
			<td align="left"><html:checkbox property="addAsStaff" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Course key:	</td>
			<td align="left"><html:text property="courseKey" size="40" maxlength="255" /></td>
			<td align="left"><html:errors property="courseKey" /></td>
		</tr>
		<tr>
			<td align="right">Confirm course key:	</td>
			<td align="left"><html:text property="confirmCourseKey" size="40" maxlength="255" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Blurb:	</td>
			<td align="left"><html:textarea property="blurb" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Disabled?:	</td>
			<td align="left"><html:checkbox property="disabled" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">Context:	</td>
			<td align="left"><html:text property="context" /></td>
			<td align="left"><html:errors property="context" /></td>
		</tr>
		<tr>
			<td colspan="" align="right"><html:submit styleClass="button">Submit</html:submit></td>
			<td></td>
		</tr>
	</table>

</html:form>