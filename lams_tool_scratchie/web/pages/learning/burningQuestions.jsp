<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<style type="text/css">
    </style>

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">

		function disableFinishButton() {
			document.getElementById("finish-button").disabled = true;
		}
	    function submitForm(){
	    	var f = document.getElementById('burning-questions');
	        f.submit();
	    }
		
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${scratchie.title}" escapeXml="true"/>
		</h1>
		<h2><fmt:message key="label.burning.questions" />:</h2>
		
		<form id="burning-questions" name="burning-questions" method="post" action="<c:url value='/learning/saveBurningQuestions.do?sessionMapID=${sessionMapID}'/>" onsubmit="disableFinishButton();">
		
			<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
				<input type="hidden" name="itemUid${status.index}" value="${item.uid}" />
			
				<h3><c:out value="${item.title}" escapeXml="true"/></h3>
				<textarea cols="60" rows="8" name="burningQuestion${status.index}" class="text-area" >${item.burningQuestion}</textarea>
			</c:forEach>
			
		</form>
		
		<div class="space-bottom-top align-right">
			<html:button property="finish-button" styleId="finish-button" onclick="submitForm()" styleClass="button">
				<fmt:message key="label.submit" />
			</html:button>
		</div>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
