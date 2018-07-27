<%@ include file="/common/taglibs.jsp"%>

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>
<lams:Page type="learner" title="${title}">
	<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
</lams:Page>



