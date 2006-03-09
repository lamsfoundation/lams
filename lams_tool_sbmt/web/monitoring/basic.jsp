	<div id="basic">
		<h1><bean:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${authoring.contentID}'/>"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
				<td NOWRAP width="700">
					<lams:SetEditor id="Title" text="${authoring.title}" small="true"/>
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><bean:message key="label.authoring.basic.instruction" />:</td>
				<td NOWRAP width="700">
					<lams:SetEditor id="Instructions" text="${authoring.instruction}"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:link href="javascript:doSubmit('updateActivity');" property="Update" styleClass="button">
								<bean:message key="label.monitoring.edit.activity.update"/>
					</html:link>
				</td>
			</tr>
		</table>
	</div>