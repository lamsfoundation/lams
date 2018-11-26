<input type="hidden" name="optionSequenceId${status.index}" value="${option.sequenceId}">
<input type="hidden" name="optionUid${status.index}" value="${option.uid}">

<div class="form-group">			
	<fmt:message key="label.authoring.basic.option.answer"/>&thinsp; ${status.index+1}
</div>

<div class="form-group">
	<lams:CKEditor id="optionString${status.index}" value="${option.optionString}" contentFolderID="${contentFolderID}" />
</div>

<div class="form-group form-inline">
	<label>
		<fmt:message key="label.authoring.basic.option.grade"></fmt:message>: 
	</label>
	<%@ include file="gradeselector.jsp"%>
</div>

<div class="form-group">
    <label for="optionFeedback${status.index}">
    	<a data-toggle="collapse" data-target="#feedback${status.index}" href="#fback${status.index}"><i class="fa fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.option.feedback"></fmt:message></a>
    </label>
    <div id="feedback${status.index}" class="collapse <c:if test="${not empty option.feedback}">in</c:if>">
     <lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" contentFolderID="${contentFolderID}" />
   </div> 
</div>
