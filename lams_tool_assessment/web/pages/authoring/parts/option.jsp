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
			<lams:FCKEditor id="optionString${status.index}" value="${option.optionString}"
				contentFolderID="${contentFolderID}" width="642px">
			</lams:FCKEditor>					
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
			<lams:STRUTS-textarea property="optionFeedback${status.index}" rows="3" cols="77" value="${option.feedback}"/>
		</td>
	</tr>	
</table>
