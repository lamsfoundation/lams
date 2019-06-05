<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Generic assessment tool page. Expects an input of questionNumber & contentFolderID, and creates a field named assessment${questionNumber} suitable for a essay entry.
  Question, and hence question.title and question.text are optional and are only populated if QTI is used to start the questions. --%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">		
		${questionNumber eq 1 ? "<label class=\"required\">" : ""}
			<fmt:message key="authoring.label.application.exercise.num"><fmt:param value="${questionNumber}"/></fmt:message>
		${questionNumber eq 1 ? "</label>" : ""}
		</div>
	</div>

	<div class="panel-body">	
	
	<input type="hidden" name="assessment${questionNumber}type" id="assessment${questionNumber}type" value="essay"/>
	<input name="assessment${questionNumber}title" id="assessment${questionNumber}Title" class="form-control" type="text" maxlength="200" value="${question.title}"/>
	<lams:CKEditor id="assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
	</div>
</div>