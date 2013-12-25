<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="content">

	<h1>
		<fmt:message key="activity.title" />
	</h1>

	<c:choose>
		<c:when test="${empty sessionMap.submissionDeadline}">
			<p>
				<fmt:message key="run.offline.message" />
			</p>	
		</c:when>
		<c:otherwise>
			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:otherwise>
	</c:choose>

	<div class="space-bottom-top align-right">
		<c:set var="continue">
			<html:rewrite
				page="/learning/newReflection.do?sessionMapID=${sessionMapID}" />
		</c:set>
		<c:set var="finish">
			<html:rewrite page="/learning/finish.do?sessionMapID=${sessionMapID}" />
		</c:set>
		
		<script type="text/javascript">
			function submitFinish() {
				document.getElementById("finishButton").disabled = true;
				location.href = '${finish}';
			}		
		</script>
		
		<c:choose>
			<c:when
				test="${sessionMap.reflectOn && (not sessionMap.finishedLock)}">
				<html:button property="continue"
					onclick="javascript:location.href='${continue}';"
					styleClass="button">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:link href="#nogo"  property="finish" styleId="finishButton"
					onclick="submitFinish();" styleClass="button">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finish" />
		 					</c:otherwise>
		 				</c:choose>
			 		</span>
				</html:link>
			</c:otherwise>
		</c:choose>

	</div>
</div>


