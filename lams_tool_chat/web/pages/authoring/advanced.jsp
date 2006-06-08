<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Advanced Tab ========== -->
<div id="authoring_advanced">
	<table class="forms">
		<tr>
			<td class="formcontrol">
				<html:checkbox property="lockOnFinished" value="1">
					<fmt:message key="advanced.lockOnFinished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td class="formcontrol">
				<html:checkbox property="filteringEnabled" value="1">
					<fmt:message key="advanced.filteringEnabled" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td class="formcontrol">
				<html:textarea property="filterKeywords" cols="30" rows="3" />
			</td>
		</tr>
	</table>
</div>
