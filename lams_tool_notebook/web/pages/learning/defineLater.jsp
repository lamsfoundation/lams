<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
	<lams:head>
		<lams:headItems/>
	</lams:head>
	<body class="stripes">
		<c:set var="title" scope="request">
			<fmt:message key="activity.title" />
		</c:set>
		<lams:Page type="learner" title="${title}">
			<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
		</lams:Page>
		<div class="footer"></div>					
	</body>
</lams:html>





