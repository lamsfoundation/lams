<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="title"><fmt:message key="index.welcome" /></c:set>

<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true" hideTitle="true" refresh="0.5;URL=index.do">

	<!-- Index page flag - do not remove, TestHarness looks for it -->

	<div id="container-main">
		<div class="text-center mt-4">
			<button class="btn btn-outline-secondary btn-lg" type="button" disabled>
				<span class="spinner-border me-2" role="status" aria-hidden="true" style="width: 1.35rem; height: 1.35rem;"></span>
				<fmt:message key="msg.loading" />
			</button>
		</div>
			
		<div class="text-center placeholder-glow">
			<span class="placeholder col-12 col-md-10 placeholder-xs mt-5 mb-3"></span>
		    <c:forEach var="i" begin="1" end="7">
		    	<span class="placeholder col-12 col-md-10 placeholder-xs my-3"></span>
		    </c:forEach>
		</div>
	</div>
</lams:PageLearner>