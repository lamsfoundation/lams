<%@ include file="/common/taglibs.jsp"%>
<%-- Generic assessment tool page. Expects an input of questionNumber & contentFolderID, and creates a field named assessment${questionNumber} suitable for a essay entry.
  Question, and hence question.title and question.text are optional and are only populated if QTI is used to start the questions. ${containingDivName} is set if this is being called from 
         a page with multiple sets of assessments, like the Application Exercises for TBL. --%>

<%-- The title needs to look like an ordinary panel title, but be editable via the X-editable javascript. But that won't be returned to the server in the form data, so copy what appears in the displayed span to a hidden input field.  --%>
<c:choose>
<c:when test="${not empty question.title}"><c:set var="questionTitle">${question.title}</c:set></c:when>
<c:otherwise><c:set var="questionTitle"><fmt:message key="authoring.label.question.num"><fmt:param value="${questionNumber}"/></fmt:message></c:set></c:otherwise>
</c:choose>
<c:set var="questionTitleDisplay">${containingDivName}assessment${questionNumber}titleDisplay</c:set>
<c:set var="questionTitleField">${containingDivName}assessment${questionNumber}title</c:set>

<div class="panel panel-default">
	<div class="panel-heading" id="${containingDivName}assessmentQuestionPanelHeading${questionNumber}">
		<a href="#" onclick="javascript:deleteQuestionDiv('${containingDivName}divassess${questionNumber}', '${questionTitleField}');" class="btn btn-default btn-sm panel-title-button">
			<i class="fa fa-lg fa-trash-o"></i> <fmt:message key="authoring.fla.delete.button"/></a>
		<div class="panel-title collapsable-icon-left">
			<a role="button" data-toggle="collapse" href="#${containingDivName}assessmentQuestionPanelCollapse${questionNumber}" 
				aria-expanded="true" aria-controls="${containingDivName}assessmentQuestionPanelCollapse${questionNumber}" >&nbsp;&nbsp;
		  	</a>	
			<span class="hoverEdit" name="${questionTitleDisplay}" id="${questionTitleDisplay}" ><c:out value="${questionTitle}" /></span><span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
			<input name="${questionTitleField}" id="${questionTitleField}" type="hidden" value="${questionTitle}"/>
		</div>
	</div>

	<div class="panel-body panel-collapse collapse in" id="${containingDivName}assessmentQuestionPanelCollapse${questionNumber}"
		 role="tabpanel" aria-labelledby="${containingDivName}assessmentQuestionPanelHeading${questionNumber}">	
	
	<input type="hidden" name="${containingDivName}assessment${questionNumber}uuid" value="${question.uuid}"/>
	
	<input type="hidden" name="${containingDivName}assessment${questionNumber}type" id="${containingDivName}assessment${questionNumber}type" value="essay"/>
	
	<lams:CKEditor id="${containingDivName}assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>

	<label for="${containingDivName}assessment${questionNumber}mark" class="voffset5"><fmt:message key="label.marks"/></label>
	<input type="number" step="1" min="1" value="${not empty question.defaultGrade ? question.defaultGrade : 1}" name="${containingDivName}assessment${questionNumber}mark" id="${containingDivName}assessment${questionNumber}mark"  class="form-control form-control-inline voffset5"/>
	
	</div>
</div>

<script type="text/javascript">
   $('#${questionTitleDisplay}').editable({
   		mode: 'inline',
    	onblur: 'ignore',
       	type: 'text',
       	validate: validateXEditable,
       	success: function(response, newValue) {
	       	var trimmedValue = newValue.trim();
	       	$('#${questionTitleDisplay}').val(trimmedValue);
	       	$('#${questionTitleField}').val(trimmedValue);
        }
   }).on('shown', onShownForXEditable)
	  .on('hidden', onHiddenForXEditable);
</script>