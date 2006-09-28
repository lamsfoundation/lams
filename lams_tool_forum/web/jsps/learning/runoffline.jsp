<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<h1>
	<fmt:message key="activity.title" />
</h1>



<div id="content">
	<table>
		<tr>
			<td>
				<fmt:message key="run.offline.message" />
			</td>
		</tr>
	</table>

	<div class="right-buttons">
		<c:set var="continue">
			<html:rewrite page="/learning/newReflection.do?sessionMapID=${sessionMapID}" />
		</c:set>
		<c:set var="finish">
			<html:rewrite page="/learning/finish.do?sessionMapID=${sessionMapID}" />
		</c:set>

		<div class="buttons-right">
			<c:choose>
				<c:when test="${sessionMap.reflectOn}">
					<html:button property="continue" onclick="javascript:location.href='${continue}';"   disabled="${sessionMap.finishedLock}"  styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${sessionMap.finishedLock}" styleClass="button">
						<fmt:message key="label.finish" />
					</html:button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="space-bottom"></div>
</div>


