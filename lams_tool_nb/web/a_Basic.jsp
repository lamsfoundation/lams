<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center">
				<tr>
					<td class="formlabel"><fmt:message key="basic.title" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextTitle" basePath="/lams/fckEditor/"
								height="150"
								width="85%">
								<c:out value="${NbAuthoringForm.title}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="basic.content" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextContent" basePath="/lams/fckEditor/"
								width="85%"
								height="400">
							<c:out value="${NbAuthoringForm.content}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center"><html:button property="cancel" onclick="window.close()" styleClass="button"><fmt:message key="button.cancel"/></html:button>
	<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>
	</td>
</tr>
</table>
</div>

