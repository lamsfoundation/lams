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
				contentFolderID="${contentFolderID}" >
			</lams:CKEditor>					
		</td>									
	</tr>

	<tr>
		<td style="border-bottom:0px; vertical-align:top; background:none;" class="right-buttons">
			<input type="radio" alt="${status.index}" name="optionCorrect" value="${option.sequenceId}" <c:if test="${option.correct}">checked="checked"</c:if> >
								
			<span class="field-name">
				<fmt:message key="label.option.correct" />
			</span>
		</td>
	</tr>

	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:top; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.feedback"></fmt:message>
			</span>
		</td>
	</tr>
	
	<tr>		
		<td style="padding-left:10px; padding-right:0px; border-bottom:0px; background:none;" width="622px">
			<lams:STRUTS-textarea property="optionFeedback${status.index}" rows="3" value="${option.feedback}"/>
		</td>
	</tr>
</table>
