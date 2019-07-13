<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Generic Q&A page - wraps an option so it is redisplayed after moving. Inputs ${questionNumber}, ${optionCount}, ${option.displayOrder},${option.text} --%>

	<input type="hidden" name="numOptionsQuestion${questionNumber}" id="numOptionsQuestion${questionNumber}" value="${optionCount}"/>
	<c:forEach items="${options}" var="option">
		<div id="divq${questionNumber}opt${option.displayOrder}">
		<c:set scope="request" var="optionNumber">${option.displayOrder}</c:set>
		<c:set scope="request" var="optionText">${option.text}</c:set>
		<c:set scope="request" var="optionCorrect">${option.correct}</c:set>
		<%@ include file="mcoption.jsp" %>
		</div>
	</c:forEach>