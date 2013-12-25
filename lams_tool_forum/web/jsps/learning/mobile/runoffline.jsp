<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<fmt:message key="activity.title" />
	</h1>
</div>

<div data-role="content">

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


</div>

<div data-role="footer" data-theme="b" class="ui-bar">
	<span class="ui-finishbtn-right">
	
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
				<button name="continue" onclick="javascript:location.href='${continue}';" data-icon="arrow-r" data-theme="b">
					<fmt:message key="label.continue" />
				</button>
			</c:when>
			<c:otherwise>
				<a href="#nogo"  name="finishButton" id="finishButton"
					onclick="submitFinish();" data-role="button" data-icon="arrow-r" data-theme="b">
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
				</a>
			</c:otherwise>
		</c:choose>
		
	</span>
</div>


