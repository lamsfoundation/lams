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
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		
		<script type="text/javascript">
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			function submitForm(methodName){
		 		var f = document.getElementById('learningForm');
				f.submit();
		 	}
			$(document).ready(function() {
				document.getElementById("focused").focus();
			});
		</script>
	</lams:head>

	<body class="stripes">
		<lams:Page type="learner" title="${reflectTitle}" formID="learningForm">
		<form:form action="finishActivity.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
			<form:hidden path="toolSessionID" />
			<form:hidden path="mindmapContent" id="mindmapContent" />
			<form:hidden path="mode" />
		
			<div class="panel">
				<lams:out value="${reflectInstructions}" escapeHtml="true"/>
			</div>
			
			<c:choose>
				<c:when test="${contentEditable}">
					<textarea rows="5" name="entryText" class="form-control" id="focused">${reflectEntry}</textarea>
				</c:when>
			
				<c:otherwise>
					<p>
						<c:out value="${reflectEntry}" escapeXml="false" />
					</p>
				</c:otherwise>
			</c:choose>
			
			<a  href="#nogo" class="btn btn-primary voffset5 pull-right" id="finishButton" onclick="submitForm('finish')">
				<span class="na">
					<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</span>
			</a>
	
		</form:form>
	</lams:Page>
		<div class="footer">
		</div>					
	</body>
</lams:html>