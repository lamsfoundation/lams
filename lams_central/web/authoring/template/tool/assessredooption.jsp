<%@ include file="/common/taglibs.jsp"%>
<%-- Generic Q&A page - wraps an option so it is redisplayed after moving. Inputs ${questionNumber}, ${optionCount}, ${option.displayOrder},${option.text} --%>

	<input type="hidden" name="${containingDivName}assmcq${questionNumber}numOptions" id="${containingDivName}assmcq${questionNumber}numOptions" value="${optionCount}"/>
	<c:forEach items="${options}" var="option">
		<div id="${containingDivName}divassmcq${questionNumber}opt${option.displayOrder}">
		<c:set scope="request" var="optionNumber">${option.displayOrder}</c:set>
		<c:set scope="request" var="optionText">${option.text}</c:set>
		<c:set scope="request" var="optionGrade">${option.grade}</c:set>
		<%@ include file="assessoption.jsp" %>
		</div>
	</c:forEach>
