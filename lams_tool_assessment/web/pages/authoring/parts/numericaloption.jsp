<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<table class="table-row-spacing" style="margin-left: -4px;">
	<tr>
		<td style="width: 100px;" class="greyed-out-label">
			<fmt:message key="label.authoring.basic.option.answer"/>&nbsp;${status.index+1}
		</td>
		<td>
			<div class="form-inline">    	
				<input type="text" name="numericalOption${status.index}" value="${option.numericalOption}" 
					id="numericalOption${status.index}" class="number form-control short-input-text input-xs" 
					title="<fmt:message key='label.authoring.choice.enter.float'/>">
			</div>
		</td>
	</tr>

	<tr class="option-settings-hidden" style="display: none;">
		<td class="greyed-out-label">
			<fmt:message key="label.authoring.basic.option.accepted.error"></fmt:message>
		</td>
		<td>
			<div class="form-inline">	
				<input type="text" name="optionAcceptedError${status.index}" value="${option.acceptedError}"
					id="optionAcceptedError${status.index}" class="number form-control short-input-text input-xs" 
					title="<fmt:message key='label.authoring.choice.enter.float'/>">
			</div>
		</td>
	</tr>
</table>

<div class="option-settings-hidden" style="display: none;">
	<%@ include file="gradeselector.jsp"%>
	
	<div class="voffset5-bottom">
	   	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
	   	<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" 
	     	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>
