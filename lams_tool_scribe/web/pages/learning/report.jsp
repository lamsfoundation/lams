<%@ include file="/common/taglibs.jsp"%>

<div id="content">

	<%@ include file="parts/reportBody.jsp"%>

	
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

<div id="footer"></div>
