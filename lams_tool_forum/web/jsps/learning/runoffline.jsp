<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<h1>
	<fmt:message key="activity.title" />
</h1>



<div id="content">
	<table>
		<tr>
			<td>
				<h2>
					<fmt:message key="run.offline.message" />
				</h2>
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
				<c:when test="${sessionMap.reflectOn && (not sessionMap.finishedLock)}">
					<html:button property="continue" onclick="javascript:location.href='${continue}';"  styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					<html:button property="finish" onclick="javascript:location.href='${finish}';" styleClass="button">
						<fmt:message key="label.finish" />
					</html:button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="space-bottom"></div>
</div>


