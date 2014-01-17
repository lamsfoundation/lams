<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>

	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finish(){
			document.getElementById("finishButton").disabled = true;
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			location.href= finishUrl;
		}
		function notebook(){
			var finishUrl= "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
			location.href= finishUrl;
		}
	</script>
</lams:head>

<body class="stripes">
	<div id="content">

		<h1>
			<fmt:message key="activity.title"></fmt:message>
		</h1>

		<div class="warning">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
			</fmt:message>
		</div>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

				<c:choose>
					<c:when test="${empty learner.reflect}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${learner.reflect}" />
						</p>
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
					<html:link href="#nogo" property="finishButton" onclick="javascript:finish();"
						styleClass="button" styleId="finishButton">
						<span class="nextActivity">
							<c:choose>
								<c:when test="${activityPosition.last}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</span>
					</html:link>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="footer"></div>
</body>
</lams:html>
