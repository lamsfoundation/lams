<%@ include file="/common/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="activity.title" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

	<h2>

		<fmt:message key="title.message.view.topic" />
	</h2>

	<p>
		<c:set var="backToForum">
			<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionScope.toolSessionID}" />
		</c:set>
		<html:button property="backToForum" onclick="javascript:location.href='${backToForum}';" styleClass="button">
			<fmt:message key="label.back.to.forum" />
		</html:button>
	</p>
	<%@ include file="/jsps/learning/message/topicview.jsp"%>
</div>

<div id="footer-learner"></div>
