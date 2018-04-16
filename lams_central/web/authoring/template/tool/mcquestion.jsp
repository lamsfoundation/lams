<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.authoring.template.web.LdTemplateAction"%>
<c:set var="maxOptionCount" value="<%= LdTemplateAction.MAX_OPTION_COUNT %>"/>
<%-- Generic Q&A question page. Expects an input of questionNumber, contentFolderID, and creates a text field field question${questionNumber} and three options --%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><label class="required"><fmt:message key="authoring.label.question.num"><fmt:param value="${questionNumber}"/></fmt:message></label></div>
	</div>

	<div class="panel-body">	
	
		<lams:CKEditor id="question${questionNumber}" value="${option.text}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
		
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
		</div>

		<div id="createOptionButton${questionNumber}" class="pull-right"><a href="#" onclick="javascript:createOption(${questionNumber},${maxOptionCount});" class="btn btn-default btn-sm"><fmt:message key="authoring.create.option"/></a></div>
	</div>		
</div>