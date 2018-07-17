<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes">
		<tiles:insert attribute="body" />
		<div class="footer">
		</div>					
	</body>
</lams:html>

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>

<lams:Page type="learner" title="${title}">
	<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
</lams:Page>



