<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div>
	<c:set var="OPTION_LABEL"><fmt:message key="label.authoring.basic.option.answer"/></c:set>
	<lams:CKEditor id="optionString${status.index}" classes="ckeditor-without-borders" value="${option.optionString}" 
		placeholder="${OPTION_LABEL}&thinsp; ${status.index+1}" contentFolderID="${contentFolderID}" height="50px"/>
</div>

<div class="option-settings">
	<div class="form-group form-inline">
		<div class="price-slider">
			<div class="col-sm-1" style="margin-top: -7px; padding: 0; opacity: 0.55;">
				<fmt:message key="label.authoring.basic.option.grade"/>
			</div>
            <div class="col-sm-5">
            	<div class="slider"></div>
            	<input type="hidden" name="optionGrade${status.index}" id="optionGrade${status.index}" value="${option.grade}">
            </div>
        </div>
	</div>

	<div style="margin-bottom: 5px;">
    	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.option.feedback"/></c:set>
     	<lams:CKEditor id="optionFeedback${status.index}" classes="ckeditor-without-borders" value="${option.feedback}" 
	     		placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
	</div>
</div>