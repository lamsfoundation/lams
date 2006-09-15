<%@ include file="/common/taglibs.jsp"%>

<!--  Scribe Client -->
<script type="text/javascript"
	src="${tool}includes/javascript/learning.js"></script>

<h1 class="no-tabs-below">
	<c:out value="${scribeDTO.title}" escapeXml="false" />
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">

	<p>
		Scribe Learning Page
	</p>

	<br />
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>

	</c:if>

</div>
<div id="footer-learner"></div>
