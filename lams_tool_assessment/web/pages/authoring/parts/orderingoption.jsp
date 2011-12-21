<table>
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
				${status.index+1}
			</span>
		</td>
	</tr>
	<tr>		
		<td style="padding-left:10px; border-bottom:0px; background:none;">	
			<lams:CKEditor id="optionString${status.index}" value="${option.optionString}"
				contentFolderID="${contentFolderID}" width="690px">
			</lams:CKEditor>					
		</td>									
	</tr>
</table>
