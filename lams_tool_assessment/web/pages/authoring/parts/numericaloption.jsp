<table>
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;" width="70px;">
			<input type="hidden" name="optionSequenceId${status.index}" id="optionSequenceId${status.index}" value="${option.sequenceId}">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.answer"></fmt:message>
				${status.index+1}
			</span>
		</td>
		<td style="padding-left:10px; border-bottom:0px; background:none;">	
			<input type="text" name="optionFloat${status.index}"
				id="optionFloat${status.index}" size="25" value="${option.optionFloat}" class="number" title="<fmt:message key='label.authoring.choice.enter.float'/>">
		</td>									
	</tr>
	
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.accepted.error"></fmt:message>
			</span>
		</td>
		<td style="padding-left:10px; border-bottom:0px; background:none;">	
			<input type="text" name="optionAcceptedError${status.index}"
				id="optionAcceptedError${status.index}" size="25" value="${option.acceptedError}" class="number" title="<fmt:message key='label.authoring.choice.enter.float'/>">
		</td>									
	</tr>	
	
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
			</span>
		</td>
		<td style="padding-left:10px; border-bottom:0px; background:none;">
			<%@ include file="gradeselector.jsp"%>
		</td>
	</tr>
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:top; background:none;" colspan="2">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.feedback"></fmt:message>
			</span>
		</td>
	</tr>
	<tr>		
		<td style="padding-left:10px; border-bottom:0px; background:none;" colspan="2">
			<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}"
				contentFolderID="${contentFolderID}" width="642">
			</lams:CKEditor>		
		</td>
	</tr>	
</table>
