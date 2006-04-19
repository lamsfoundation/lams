<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Advanced Tab ========== -->
<div id="authoring_advanced">
	<table class="forms">
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="lockOnFinished" value="1">
					<fmt:message key="advanced.lockOnFinished" />
				</html:checkbox>
			</td>
		</tr>
	</table>
</div>
