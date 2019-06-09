<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Generic assessment tool page. Expects an input of questionNumber & contentFolderID, and creates a field named assessment${questionNumber} suitable for a essay entry.
  Question, and hence question.title and question.text are optional and are only populated if QTI is used to start the questions. ${containingDivName} is set if this is being called from 
         a page with multiple sets of assessments, like the Application Exercises for TBL. --%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">		
		${questionNumber eq 1 ? "<label class=\"required\">" : ""}
			<fmt:message key="authoring.label.question.num"><fmt:param value="${questionNumber}"/></fmt:message>
		${questionNumber eq 1 ? "</label>" : ""}
		</div>
	</div>

	<div class="panel-body">	
	
	<input type="hidden" name="${containingDivName}assessment${questionNumber}type" id="${containingDivName}assessment${questionNumber}type" value="essay"/>
	
	<span class="field-name" class="input"><label for="${containingDivName}assessment${questionNumber}title"><fmt:message key="label.title"/></label></span>
	<input name="${containingDivName}assessment${questionNumber}title" id="${containingDivName}assessment${questionNumber}Title" class="form-control" type="text" maxlength="200" value="${question.title}"/>
	<span class="field-name"><label for="${containingDivName}assessment${questionNumber}" class="input required"><fmt:message key="authoring.label.question.text" /></label></span>
	<lams:CKEditor id="${containingDivName}assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
	</div>
</div>