<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams"> <lams:LAMSURL /> </c:set>
	<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>

	</lams:head>
	
	<body class="stripes">
	
		<c:set scope="request" var="title">
			<fmt:message key="activity.title" />
		</c:set>
		<lams:Page type="learner" title="${title}">
			<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
		</lams:Page>

	<div class="footer">
		</div>					
	</body>
</lams:html>

