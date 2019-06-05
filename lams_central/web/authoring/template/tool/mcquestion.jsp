<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ page import="org.lamsfoundation.lams.authoring.template.web.LdTemplateController"%>
<c:set var="maxOptionCount" value="<%=LdTemplateController.MAX_OPTION_COUNT%>"/>
<%-- Generic Q&A question page. Expects an input of questionNumber, contentFolderID, and creates a text field field question${questionNumber} and three options / as many as are need for a QTI import --%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><label class="required"><fmt:message key="authoring.label.question.num"><fmt:param value="${questionNumber}"/></fmt:message></label></div>
	</div>

	<div class="panel-body">	
	
		<lams:CKEditor id="question${questionNumber}" value="${question.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
		
		<table class="table table-condensed table-no-border">
		<tr><td></td>
		<td width="60px" class="align-center">
			<span class="field-name">
				<fmt:message key="authoring.label.correct.question"/>
			</span>
		</td>
		<td width="40px">
		</td>
		<td width="20px">
		</td>
		</tr>
		</table>
	
		<div id="divq${questionNumber}options">
		<c:choose>	
		<c:when test="${not empty question.answers}">
			<c:set var="numAnswers">${fn:length(question.answers)}</c:set>
			<input type="hidden" name="numOptionsQuestion${questionNumber}" id="numOptionsQuestion${questionNumber}" value="${numAnswers}"/>
			<c:set scope="request" var="optionCount">${numAnswers}</c:set>
			<c:forEach items="${question.answers}" var="answer" varStatus="loopStatus">
				<c:set scope="request" var="optionText">${answer.text}</c:set>
				<c:set scope="request" var="optionCorrect">${answer.score > 0}</c:set>
				<c:set scope="request" var="optionNumber">${loopStatus.count}</c:set>
				<div id="divq${questionNumber}opt${optionNumber}">
					<%@ include file="mcoption.jsp" %>
			</div>
		</c:forEach>
		</c:when>
		
		<c:otherwise>
			<input type="hidden" name="numOptionsQuestion${questionNumber}" id="numOptionsQuestion${questionNumber}" value="4"/>
			<div id="divq${questionNumber}opt1">
			<c:set scope="request" var="optionText"></c:set>
			<c:set scope="request" var="optionCount">4</c:set>
			<c:set scope="request" var="optionNumber">1</c:set>
			<%@ include file="mcoption.jsp" %>
			</div>
			<div id="divq${questionNumber}opt2">
			<c:set scope="request" var="optionNumber">2</c:set>
			<%@ include file="mcoption.jsp" %>
			</div>
			<div id="divq${questionNumber}opt3">
			<c:set scope="request" var="optionNumber">3</c:set>
			<%@ include file="mcoption.jsp" %>
			</div>
			<div id="divq${questionNumber}opt4">
			<c:set scope="request" var="optionNumber">4</c:set>
			<%@ include file="mcoption.jsp" %>
			</div>
		</c:otherwise>
		
		</c:choose>
		</div>
		
		<div id="createOptionButton${questionNumber}" class="pull-right"><a href="#" onclick="javascript:createOption(${questionNumber},${maxOptionCount});" class="btn btn-default btn-sm"><fmt:message key="authoring.create.option"/></a></div>
	</div>		
</div>