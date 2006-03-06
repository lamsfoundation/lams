	<div id="basic">
		<h1><bean:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${authoring.contentID}'/>"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
				<td class="formcontrol">
					<input type="text" name="title" value="<c:out value='${authoring.title}' escapeXml='false'/>" />
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><bean:message key="label.authoring.basic.instruction" />:</td>
				<td NOWRAP width="700">
					<lams:SetEditor id="Instructions" text="${authoring.instruction}"/>
				</td>
			</tr>
		</table>
	</div>