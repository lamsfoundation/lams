<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- Advance Tab Content -->	
	<table class="forms">
		<!-- Instructions Row -->
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="survey.lockWhenFinished" styleClass="noBorder">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="survey.showOnePage" styleClass="noBorder">
					<fmt:message key="label.authoring.advance.show.on.one.page" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:checkbox property="survey.reflectOnActivity" styleClass="noBorder"  styleId="reflectOn">
					<fmt:message key="label.authoring.advanced.reflectOnActivity" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:textarea property="survey.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
			</td>
		</tr>		
	</table>
