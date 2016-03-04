<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page type="learner" title="${title}">

	<lams:Alert type="danger" id="submission-deadline" close="false">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
		</fmt:message>
	</lams:Alert>

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
				document.getElementById("finish").disabled = true;
				location.href = '${finish}';
			}		
		</script>
		
		<c:choose>
			<c:when
				test="${sessionMap.reflectOn && (not sessionMap.finishedLock)}">
				<html:button property="continue"
					onclick="javascript:location.href='${continue}';"
					styleClass="btn btn-primary voffset5 pull-right">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:link href="#nogo"  property="finish" styleId="finish"
					onclick="submitFinish();" styleClass="btn btn-primary voffset5 pull-right na">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finish" />
		 					</c:otherwise>
		 				</c:choose>
				</html:link>
			</c:otherwise>
		</c:choose>

	</div>
</lams:Page>