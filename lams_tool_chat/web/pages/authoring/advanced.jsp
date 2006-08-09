<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td align="right">
				<html:checkbox property="lockOnFinished" value="1" styleClass="noBorder"></html:checkbox>
			</td>
			<td>
				<fmt:message key="advanced.lockOnFinished" />
			</td>
		</tr>
		<tr>
			<td align="right">
				<html:checkbox property="reflectOnActivity" value="1" styleClass="noBorder"></html:checkbox>
			</td>
			<td>
				<fmt:message key="advanced.reflectOnActivity" />
			</td>
		</tr>
				<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:textarea property="reflectInstructions" cols="30" rows="3" />
			</td>
		</tr>
		<tr>
			<td align="right">
				<html:checkbox property="filteringEnabled" value="1" styleClass="noBorder"></html:checkbox>
			</td>
			<td>
				<fmt:message key="advanced.filteringEnabled" />
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:textarea property="filterKeywords" cols="30" rows="3" />
			</td>
		</tr>
	</tbody>
</table>

