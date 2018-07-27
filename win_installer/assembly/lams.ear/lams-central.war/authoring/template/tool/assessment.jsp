<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Generic assessment tool page. Expects an input of questionNumber & contentFolderID, and creates a field named assessment${questionNumber} suitable for a essay entry --%>

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
	<lams:CKEditor id="assessment${questionNumber}" value="" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
	</div>
</div>