<%@ include file="/common/taglibs.jsp"%>

<!--  Scribe Client -->
<script type="text/javascript"
	src="${tool}includes/javascript/learning.js"></script>

<div id="content">

<h1>
	<c:out value="${scribeDTO.title}" escapeXml="false" />
</h1>

	<p>
		Scribe Learning Page
	</p>

	<br />
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>

	</c:if>

</div>

