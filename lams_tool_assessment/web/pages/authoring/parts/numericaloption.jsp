<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<table class="table-row-spacing">
	<tr>
		<td style="width: 120px;">
			<fmt:message key="label.authoring.basic.option.answer"/>&nbsp;${status.index+1}
		</td>
		<td>
			<div class="form-inline">    	
				<input type="text" name="optionFloat${status.index}" value="${option.optionFloat}" 
						id="optionFloat${status.index}" class="number form-control short-input-text input-sm" 
						title="<fmt:message key='label.authoring.choice.enter.float'/>">
			</div>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.option.accepted.error"></fmt:message>
		</td>
		<td>
			<div class="form-inline">	
				<input type="text" name="optionAcceptedError${status.index}" value="${option.acceptedError}"
						id="optionAcceptedError${status.index}" class="number form-control short-input-text input-sm" 
						title="<fmt:message key='label.authoring.choice.enter.float'/>">
			</div>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.option.grade"></fmt:message>
		</td>
		<td>
			<div class="form-inline">
				<%@ include file="gradeselector.jsp"%>
			</div>
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<div class="form-group">
				<label for="optionFeedback${status.index}">
					<a data-toggle="collapse" data-target="#feedback${status.index}" href="#fback${status.index}"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.option.feedback"></fmt:message></a>
				</label>
				<div id="feedback${status.index}" class="collapse <c:if test="${not empty option.feedback}">in</c:if>">
					<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" contentFolderID="${contentFolderID}"/>
				</div>	
			</div>
		</td>
	</tr>
</table>
