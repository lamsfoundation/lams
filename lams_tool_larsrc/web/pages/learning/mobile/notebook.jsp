<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/mobileheader.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	    function submitForm(methodName){
        	        var f = document.getElementById('reflectionForm');
	                f.submit();
	    }
	</script>
</lams:head>
<body>
<div data-role="page" data-cache="false">

<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID" />

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true"/>
		</h1>
	</div>
	
	<div data-role="content">

		<%@ include file="/common/messages.jsp"%>

		<h2>
			<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
		</h2>

		<html:textarea cols="52" rows="6" property="entryText"
			styleClass="text-area" />

	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<a  href="#nogo" id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
				<span class="nextActivity">
					<c:choose>
				 		<c:when test="${sessionMap.activityPosition.last}">
				 			<fmt:message key="label.submit" />
				 		</c:when>
				 		<c:otherwise>
				 		 	<fmt:message key="label.finished" />
				 		</c:otherwise>
				 	</c:choose>
				</span>
			</a>
		</span>
	</div>
		
</html:form>

</div>
</body>
</lams:html>
