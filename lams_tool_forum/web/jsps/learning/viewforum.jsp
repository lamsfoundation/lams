<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<h1 class="no-tabs-below">
	<c:out value="${sessionMap.title}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>


<div id="content-learner">

	<table>
		<tr>
			<td>
				<c:out value="${sessionMap.instruction}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td>
				<%@ include file="/common/messages.jsp"%>
			</td>
		</tr>
	</table>

	<%@ include file="/jsps/learning/message/topiclist.jsp"%>

	<table>

		<tr>
			<td>

				<c:set var="newtopic">
					<html:rewrite page="/learning/newTopic.do?sessionMapID=${sessionMapID}" />
				</c:set>
				<c:set var="refresh">
					<html:rewrite page="/learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}" />
				</c:set>
				<c:set var="finish">
					<html:rewrite page="/learning/finish.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}" />
				</c:set>

				<div class="left-buttons">
					<c:if test='${sessionMap.mode != "teacher" && sessionMap.allowNewTopics}'>
						<html:button property="newtopic" onclick="javascript:location.href='${newtopic}';" disabled="${sessionMap.finishedLock}" styleClass="button">
							<fmt:message key="label.newtopic" />
						</html:button>
					</c:if>
					<html:button property="refresh" onclick="javascript:location.href='${refresh}';" disabled="${sessionMap.finishedLock}" styleClass="button">
						<fmt:message key="label.refresh" />
					</html:button>
				</div>

				<div class="right-buttons">

					<c:if test='${sessionMap.mode != "teacher"}'>
						<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${sessionMap.finishedLock}" styleClass="button">
							<fmt:message key="label.finish" />
						</html:button>
					</c:if>
				</div>
			</td>
		</tr>
	</table>
</div>

<div id="footer-learner"></div>
