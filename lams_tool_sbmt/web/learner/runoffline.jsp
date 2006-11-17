<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<head>

	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finish(){
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			location.href= finishUrl;
		}
		function notebook(){
			var finishUrl= "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
			location.href= finishUrl;
		}
	</script>
</head>

<body class="stripes">
	<div id="content">

		<h1>
			<fmt:message key="activity.title"></fmt:message>
		</h1>

		<p>
			<fmt:message key="run.offline.message" />
		</p>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>${sessionMap.reflectInstructions}</h2>
				
				<c:choose>
					<c:when test="${empty learner.reflect}">
						<p>
							<em>
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p> <lams:out escapeXml="true" value="${learner.reflect}" />  </p>				
					</c:otherwise>
				</c:choose>
				
				<html:button property="notebookButton"
					onclick="javascript:notebook();" styleClass="button">
					<fmt:message key="label.edit" />					
				</html:button>				
								
			</div>
		</c:if>

		<div class="space-bottom-top align-right">
			<c:choose>
				<c:when
					test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
					<html:button property="continueButton"
						onclick="javascript:notebook();" styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					<html:button property="finishButton" onclick="javascript:finish();"
						styleClass="button">
						<fmt:message key="button.finish" />
					</html:button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="footer"></div>
</body>
</lams:html>
