<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!---------------------------Advance Tab Content ------------------------>	
	<table class="forms">
		<!-- Instructions Row -->
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.lockWhenFinished">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.runAuto">
					<fmt:message key="label.authoring.advance.run.content.auto" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td class="formcontrol">
				<html:select property="resource.minViewResourceNumber">
					<html:option value="1">1</html:option>
					<html:option value="2">2</html:option>
					<html:option value="3">3</html:option>
					<html:option value="4">4</html:option>
					<html:option value="5">5</html:option>
				</html:select>
			</td>
			<td class="formcontrol"  width="97%">
					<fmt:message key="label.authoring.advance.mini.number.resources.view" />
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.allowAddUrls">
					<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.allowAddFiles">
					<fmt:message key="label.authoring.advance.allow.learner.add.files" />
				</html:checkbox>
			</td>
		</tr>
	</table>