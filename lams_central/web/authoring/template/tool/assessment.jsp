<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
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
	<div class="panel-heading">
		<div class="panel-title">		
		${questionNumber eq 1 ? "<label class=\"required\">" : ""}
			<span class="hoverEdit" name="${questionTitleDisplay}" id="${questionTitleDisplay}" ><c:out value="${questionTitle}" /></span><span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
		${questionNumber eq 1 ? "</label>" : ""}
			<input name="${questionTitleField}" id="${questionTitleField}" type="hidden" value="${questionTitle}"/>
		</div>
	</div>

	<div class="panel-body">	
	
	<input type="hidden" name="${containingDivName}assessment${questionNumber}type" id="${containingDivName}assessment${questionNumber}type" value="essay"/>
	<lams:CKEditor id="${containingDivName}assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
	</div>
</div>

<script type="text/javascript">
   $('#${questionTitleDisplay}').editable({
   	mode: 'inline',
       type: 'text',
    validate: function(value) {
	    //close editing area on validation failure
           if (!value.trim()) {
               $('.editable-open').editableContainer('hide', 'cancel');
               return 'Can not be empty!';
           }
       },
       success: function(response, newValue) {
       	var trimmedValue = newValue.trim();
       	$('#${questionTitleDisplay}').val(trimmedValue);
       	$('#${questionTitleField}').val(trimmedValue);
       }
   }).on('shown', function(e, editable) {
	$(this).nextAll('i.fa-pencil').hide();
}).on('hidden', function(e, reason) {
	$(this).nextAll('i.fa-pencil').show();
});;
</script>