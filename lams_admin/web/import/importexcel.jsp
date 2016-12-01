<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<script language="javascript" type="text/JavaScript">
	function goToStatus() {
		document.location = '<lams:LAMSURL/>/admin/import/status.jsp';
	}
</script>

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do"
		class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
</p>

<p>
	<fmt:message key="msg.import.intro" />
</p>
<p>
<ul>
	<li><fmt:message key="msg.import.1" /></li>
	<li><fmt:message key="msg.import.2" />
		<ul>
			<li><p>
					<a href="file/lams_users_template.xls">lams_users_template.xls</a>
				</p></li>
		</ul></li>
	<li><fmt:message key="msg.import.3" />
		<ul>
			<li><p>
					<a href="file/lams_roles_template.xls">lams_roles_template.xls</a>
				</p></li>
		</ul></li>
</ul>
</p>
<div class ="pull-left">
	<lams:Alert type="info" close="false">
		<strong><fmt:message key='label.password.must.contain' />:</strong>
		<ul style="line-height: 1.2">
			<li><span class="fa fa-check"></span> <fmt:message
					key='label.password.min.length'>
					<fmt:param value='${minNumChars}' />
				</fmt:message></li>
			<c:if test="${mustHaveUppercase}">
				<li><span class="fa fa-check"></span> <fmt:message
						key='label.password.must.ucase' /></li>
			</c:if>
			<c:if test="${mustHaveLowercase}">
				<li><span class="fa fa-check"></span> <fmt:message
						key='label.password.must.lcase' /></li>
			</c:if>
			<c:if test="${mustHaveNumerics}">
				<li><span class="fa fa-check"></span> <fmt:message
						key='label.password.must.number' /></li>
			</c:if>
			<c:if test="${mustHaveSymbols}">
				<li><span class="fa fa-check"></span> <fmt:message
						key='label.password.must.symbol' /></li>
			</c:if>
		</ul>
	</lams:Alert>
</div>
<p>
	<fmt:message key="msg.import.conclusion" />
</p>

<html:form action="/importexcelsave.do" method="post"
	enctype="multipart/form-data" onsubmit="goToStatus();">
	<html:hidden property="orgId" />

	<table>
		<tr>
			<td align="right"><fmt:message key="label.excel.spreadsheet" />:&nbsp;</td>
			<td><html:file property="file" styleClass="form-control" /></td>
		</tr>
	</table>
	<div class="pull-right">
		<html:cancel styleId="cancelButton" styleClass="btn btn-default">
			<fmt:message key="admin.cancel" />
		</html:cancel>
		<html:submit styleId="importButton"
			styleClass="btn btn-primary loffset5">
			<fmt:message key="label.import" />
		</html:submit>
		&nbsp;
	</div>

</html:form>