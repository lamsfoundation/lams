<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Advanced Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td >
				<html:checkbox property="lockOnFinished" value="1" styleClass="noBorder"><fmt:message key="advanced.lockOnFinished" /></html:checkbox>
			</td>
		</tr>
	</tbody>
</table>

