<table>
	<tr>
		<td style="padding-left:0px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.question"></fmt:message>
			</span>
		</td>
		<td style="padding-left:0px; border-bottom:0px; background:none;">
			<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">	
			<lams:STRUTS-textarea rows="5" cols="43" tabindex="2" property="optionQuestion${status.index}" styleId="optionQuestion${status.index}" value="${option.question}"/>
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