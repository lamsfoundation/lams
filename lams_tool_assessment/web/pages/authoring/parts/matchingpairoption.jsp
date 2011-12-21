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
			<lams:CKEditor id="optionQuestion${status.index}" value="${option.question}"
				contentFolderID="${contentFolderID}" width="690">
			</lams:CKEditor>				
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
		<td style="padding-left:10px; border-bottom:0px; background:none;" width="612px">	
			<input type="text" name="optionString${status.index}"
				id="optionString${status.index}" size="111" value="${option.optionString}">
		</td>									
	</tr>
</table>