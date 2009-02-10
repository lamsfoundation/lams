<table>
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">		
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.question"></fmt:message>
				${status.index+1}
			</span>
		</td>
	</tr>
	<tr>			
		<td style="padding-left:10px; border-bottom:0px; background:none;">
			<lams:FCKEditor id="optionQuestion${status.index}" value="${option.question}"
				contentFolderID="${contentFolderID}" width="622">
			</lams:FCKEditor>				
		</td>									
	</tr>						
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
			</span>
		</td>
	</tr>
	<tr>			
		<td style="padding-left:10px; border-bottom:0px; background:none;" width="622px">	
			<input type="text" name="optionAnswer${status.index}"
				id="optionAnswer${status.index}" size="99" value="${option.answerString}">
		</td>									
	</tr>
</table>