<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> 	<lams:WebAppURL />	</c:set>
	
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="includes/javascript/common.js" />
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