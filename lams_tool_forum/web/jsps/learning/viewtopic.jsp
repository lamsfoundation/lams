<%@ include file="/common/taglibs.jsp"%>

<p>
	<c:set var="backToForum">
		<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionScope.toolSessionID}" />
	</c:set>
	<html:button property="backToForum" onclick="javascript:location.href='${backToForum}';" styleClass="buttonStyle">
		<fmt:message key="label.back.to.forum" />
	</html:button>
</p>

<%@ include file="/jsps/learning/message/topicview.jsp"%>
