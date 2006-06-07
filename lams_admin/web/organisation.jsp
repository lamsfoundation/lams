<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>

<html:form action="orgsave.do" method="post">
<html:hidden property="orgId" />
<html:hidden property="parentId" />
<html:hidden property="typeId" />
<table>
	<tr>
		<td>Name:</td>
		<td><html:text property="name" size="20" /> *</td>
	</tr>
	<tr>
	<td>Code:</td>
		<td><html:text property="code" size="20" /></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td><html:textarea property="description" cols="50" rows="3" /></td>
	</tr>
	<tr>
		<td>Language:</td>
		<td>
			<html:select property="localeLanguage">
				<html:option key="locale.language.English" value="en" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td>Country:</td>
		<td>
			<html:select property="localeCountry">
				<html:option key="locale.country.English" value="AU" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td>State</td>
		<td>
			<html:select property="stateId">
				<html:option key="organisation.state.active" value="1" />
				<html:option key="organisation.state.hidden" value="2" />
				<html:option key="organisation.state.archived" value="3" />
				<html:option key="organisation.state.removed" value="4" />
			</html:select>
		</td>
	</tr>
<table>
<p>
<html:submit>Save</html:submit>
<html:cancel>Cancel</html:cancel>
</p>
</html:form>

