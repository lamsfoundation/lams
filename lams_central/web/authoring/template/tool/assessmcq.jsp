<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ page import="org.lamsfoundation.lams.authoring.template.web.LdTemplateController"%>
<c:set var="maxOptionCount" value="<%=LdTemplateController.MAX_OPTION_COUNT%>"/>

<c:set var="required">${questionNumber eq 1 ? "class=\"input required\"" : "class=\"input\""}</c:set>

<%-- Generic MCQ question for assessment. Expects an input of questionNumber, contentFolderID, and creates a text field field question${questionNumber} and three options.
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

	<input type="hidden" name="${containingDivName}assessment${questionNumber}type" id="${containingDivName}assessment${questionNumber}type" value="mcq"/>
	<lams:CKEditor id="${containingDivName}assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
	<label for="${containingDivName}assessment${questionNumber}mark" class="voffset5"><fmt:message key="label.marks"/></label>
	<input type="number" step="1" min="1" value="1" name="${containingDivName}assessment${questionNumber}mark" id="${containingDivName}assessment${questionNumber}mark"  class="form-control form-control-inline voffset5"/>
	
	<table class="table table-condensed table-no-border">
	<tr><td></td><td></td>
	<td width="100px" class="align-center">
		<span class="field-name">
			<fmt:message key="authoring.label.grade"/>
		</span>
	</td>
	<td width="40px">
	</td>
	<td width="20px">
	</td>
	</tr>
	</table>
	
	<div id="${containingDivName}divassmcq${questionNumber}options">
	<c:choose>

	<c:when test="${not empty question.answers}">
	<c:set var="numAnswers">${fn:length(question.answers)}</c:set>
	<input type="hidden" name="${containingDivName}assmcq${questionNumber}numOptions" id="${containingDivName}assmcq${questionNumber}numOptions" value="${numAnswers}"/>
	<c:set scope="request" var="optionCount">${numAnswers}</c:set>
	<c:forEach items="${question.answers}" var="answer" varStatus="loopStatus">
		<c:set scope="request" var="optionText">${answer.text}</c:set>
		<c:set scope="request" var="optionNumber">${loopStatus.count}</c:set>
		<c:set scope="request" var="optionGrade">${answer.score}</c:set>
		<div id="${containingDivName}divassmcq${questionNumber}opt${optionNumber}">
		<%@ include file="assessoption.jsp" %>
		</div>
	</c:forEach>
	</c:when>

	<c:otherwise>
	<input type="hidden" name="${containingDivName}assmcq${questionNumber}numOptions" id="${containingDivName}assmcq${questionNumber}numOptions" value="4"/>
	<c:set scope="request" var="optionCount">4</c:set>
	<c:set scope="request" var="optionText"></c:set>
	<div id="${containingDivName}divassmcq${questionNumber}opt1">
	<c:set scope="request" var="optionNumber">1</c:set>
	<%@ include file="assessoption.jsp" %>
	</div>
	<div id="${containingDivName}divassmcq${questionNumber}opt2">
	<c:set scope="request" var="optionNumber">2</c:set>
	<%@ include file="assessoption.jsp" %>
	</div>
	<div id="${containingDivName}divassmcq${questionNumber}opt3">
	<c:set scope="request" var="optionNumber">3</c:set>
	<%@ include file="assessoption.jsp" %>
	</div>
	<div id="${containingDivName}divassmcq${questionNumber}opt4">
	<c:set scope="request" var="optionNumber">4</c:set>
	<%@ include file="assessoption.jsp" %>
	</div>
	</c:otherwise>
	
	</c:choose>
	</div>
	
	<div id="${containingDivName}createAssessmentOptionButton${questionNumber}" class="pull-right"><a href="#" onclick="javascript:createAssessmentOption(${questionNumber},${maxOptionCount}, '${containingDivName}');" class="btn btn-default btn-sm"><i class="fa fa-plus"></i> <fmt:message key="authoring.create.option"/></a></div>
	
	</div>
</div>

<script type="text/javascript">
   $('#${questionTitleDisplay}').editable({
   	mode: 'inline',
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
	