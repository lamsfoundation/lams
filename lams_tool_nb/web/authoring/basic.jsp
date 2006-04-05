<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

<!---------------------------Basic Tab Content ------------------------>
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center" >
				<tr>
					<td class="formlabel"><fmt:message key="basic.title" /></td>
					<td class="formcontrol">
							<FCK:editor id="title" basePath="/lams/fckeditor/"
								height="150"
								width="85%">
								<c:out value="${NbAuthoringForm.title}" escapeXml="false" />
							</FCK:editor>
							
					</td>
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="basic.content" /></td>
					<td class="formcontrol">
						<FCK:editor id="content" basePath="/lams/fckeditor/"
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
