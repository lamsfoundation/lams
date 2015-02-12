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
    	#scratches {border-spacing: 0;}
    	#scratches tr td { padding: 12px 15px; }
    	#scratches a, #scratches a:hover {border-bottom: none;}
    	.scartchie-image {border: 0;}
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
		
		<form id="burning-questions" name="burning-questions" method="post" action="<c:url value='/learning/saveBurningQuestions.do?sessionMapID=${sessionMapID}'/>" onsubmit="disableFinishButton();">
		
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="j">
			<h3><c:out value="${item.title}" escapeXml="true"/></h3>
			<div><c:out value="${item.description}" escapeXml="false"/></div>
			
			<table id="scratches" class="alternative-color">
				<c:forEach var="answer" items="${item.answers}" varStatus="status">
					<tr>
						<td style="width: 40px;">
							<c:choose>
								<c:when test="${answer.scratched && answer.correct}">
									<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image">
								</c:when>
								<c:when test="${answer.scratched && !answer.correct}">
									<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" class="scartchie-image">
								</c:when>
								<c:otherwise>
									<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image">
								</c:otherwise>
							</c:choose>
						</td>
								
						<td style="vertical-align: middle;">
							${answer.description} 
						</td>
					</tr>
				</c:forEach>
			</table>
			
			<h4><fmt:message key="label.burning.question" /></h4>
			<input type="hidden" name="itemUid${j.index}" value="${item.uid}" />
			<textarea cols="55" rows="5" name="burningQuestion${j.index}" class="text-area" >${item.burningQuestion}</textarea>
			<br>	<br><br>		
		</c:forEach>
		
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
