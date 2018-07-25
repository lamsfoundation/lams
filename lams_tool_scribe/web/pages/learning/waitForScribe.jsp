<%@ include file="/common/taglibs.jsp"%>

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>
<c:set var="msg" scope="request">
	<fmt:message key="message.waitForScribe" />
</c:set>

<lams:Page type="learner" title="${title}">
	<p>
		<c:out value="${msg}"/>
	</p>


		<a href="javascript:location.reload(true);" class="btn btn-primary voffset10 pull-right"><fmt:message
	key="button.try.again" /> </a>
</lams:Page>

