<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="content">

	<h1>
		<fmt:message key="activity.title" />
	</h1>

	<p>
		<fmt:message key="run.offline.message" />
	</p>

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
				document.getElementById("finish").disabled = "disabled";
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
				<html:button property="finish" styleId="finish"
					onclick="submitFinish();" styleClass="button">
					<fmt:message key="label.finish" />
				</html:button>
			</c:otherwise>
		</c:choose>

	</div>
</div>


