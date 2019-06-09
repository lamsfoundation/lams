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

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">		
		${questionNumber eq 1 ? "<label class=\"required\">" : ""}
			<fmt:message key="authoring.label.question.num"><fmt:param value="${questionNumber}"/></fmt:message>
		${questionNumber eq 1 ? "</label>" : ""}
		</div>
	</div>

	<div class="panel-body">	

	<input type="hidden" name="${containingDivName}assessment${questionNumber}type" id="${containingDivName}assessment${questionNumber}type" value="mcq"/>
	<span class="field-name" class="input"><label for="${containingDivName}assessment${questionNumber}title"><fmt:message key="label.title"/></label></span>
	<input name="${containingDivName}assessment${questionNumber}title" id="${containingDivName}assessment${questionNumber}title" class="form-control" type="text" maxlength="200"  value="${question.title}"/>
	<span class="field-name"><label for="${containingDivName}assessment${questionNumber}" class="input required"><fmt:message key="authoring.label.question.text" /></label></span>
	<lams:CKEditor id="${containingDivName}assessment${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
	
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
	
	<div id="createAssessmentOptionButton${questionNumber}" class="pull-right"><a href="#" onclick="javascript:createAssessmentOption(${questionNumber},${maxOptionCount});" class="btn btn-default btn-sm"><fmt:message key="authoring.create.option"/></a></div>
	
	</div>
</div>
	
	