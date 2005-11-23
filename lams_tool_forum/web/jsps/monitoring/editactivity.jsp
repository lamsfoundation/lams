	<html:form action="monitoring" method="post" >
		<html:errors/>
		<div id="basic">
		<h1><fmt:message key="label.authoring.heading.basic" /></h1>
		<h1><fmt:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${contentID}'/>"/>
			<input type="hidden" name="method" value="updateActivity"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
				<td class="formcontrol">
					<input type="text" name="title" value="<c:out value='${title}' escapeXml='false'/>" />
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
				<td class="formcontrol">
					<FCK:editor id="instructions"
						basePath="/lams/fckeditor/" height="150" width="85%">
						<c:out value="${instruction}" escapeXml="false"/>
					</FCK:editor>
				</td>
			</tr>
			<tr><td colspan="2">
				<html:cancel>
					<fmt:message key="label.monitoring.edit.activity.cancel"/>
				</html:cancel>
				<html:submit>
					<fmt:message key="label.monitoring.edit.activity.update"/>
				</html:submit>
			</td></tr>
		</table>
		</div>
	</html:form>
</body>
</html:html>