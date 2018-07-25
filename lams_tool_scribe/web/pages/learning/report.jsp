<%@ include file="/common/taglibs.jsp"%>

<lams:Page type="learner" title="${scribeDTO.title}">

	<%@ include file="parts/reportBody.jsp"%>

	
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

<div id="footer"></div>

</lams:Page>