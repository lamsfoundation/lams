<table>
	<tr>
		<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.question"></fmt:message>
			</span>
		</td>
		<td style="padding-left:0px; border-bottom:0px; background:none;" width="630px">
			<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">
			<lams:FCKEditor id="optionQuestion${status.index}" value="${option.question}"
				contentFolderID="${contentFolderID}">
			</lams:FCKEditor>				
		</td>									
	</tr>						
	<tr>
		<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
			</span>
		</td>
		<td style="padding-left:0px; border-bottom:0px; background:none;">	
			<input type="text" name="optionAnswer${status.index}"
				id="optionAnswer${status.index}" size="56" value="${option.answerString}">
		</td>									
	</tr>
</table>