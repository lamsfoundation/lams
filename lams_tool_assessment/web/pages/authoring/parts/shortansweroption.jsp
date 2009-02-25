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
			<input type="text" name="optionString${status.index}" value="${option.optionString}" styleClass="noBorder" size="99"/>
		</td>									
	</tr>
	
	<tr>
		<td style="padding-left:10px; border-bottom:0px; vertical-align:middle; background:none;">
			<span class="field-name">
				<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
			</span>
			<span class="space-left">
				<%@ include file="gradeselector.jsp"%>
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
			<lams:FCKEditor id="optionFeedback${status.index}" value="${option.feedback}"
				contentFolderID="${contentFolderID}" width="622px">
			</lams:FCKEditor>		
		</td>
	</tr>	
</table>
