<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="title"><fmt:message key="index.welcome" /></c:set>

<!-- Index page flag - do not remove, TestHarness looks for it -->

<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true" hideTitle="true" refresh="1;URL=index.do">
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/lottie/lottie-player.js"></script>

	<div id="container-main">
		<div class="text-center mx-auto" aria-live="assertive">
			<lottie-player
			  autoplay
			  loop
			  mode="normal"
			  src="<lams:LAMSURL/>includes/javascript/lottie/circles.json"
			  style="width: 320px; height: 200px; margin-top: 10rem;"
			  class="mx-auto"
			>
			</lottie-player>
		
			<h1 class="fs-3">
				<fmt:message key="msg.loading" />
			</h1>
		</div>
	</div>
</lams:PageLearner>
