<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="content">

	<h1>
		<c:out value="${sessionMap.title}" escapeXml="false" />
	</h1>


	<div class="small-space-top">

		<c:out value="${sessionMap.instruction}" escapeXml="false" />

		</div>
		
		<%@ include file="/common/messages.jsp"%>

		

			<%@ include file="/jsps/learning/message/topiclist.jsp"%>


			<c:set var="newtopic">
				<html:rewrite
					page="/learning/newTopic.do?sessionMapID=${sessionMapID}" />
			</c:set>
			<c:set var="refresh">
				<html:rewrite
					page="/learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}" />
			</c:set>
			<c:set var="continue">
				<html:rewrite
					page="/learning/newReflection.do?sessionMapID=${sessionMapID}" />
			</c:set>
			<c:set var="finish">
				<html:rewrite
					page="/learning/finish.do?sessionMapID=${sessionMapID}" />
			</c:set>



			<div class="right-buttons">
				<c:if test='${sessionMap.mode != "teacher"}'>
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
							<html:button property="finish"
								onclick="javascript:location.href='${finish}';"
								styleClass="button">
								<fmt:message key="label.finish" />
							</html:button>
						</c:otherwise>
					</c:choose>
				</c:if>



			</div>


			<div class="space-bottom">
				<c:if	test='${sessionMap.mode != "teacher" && sessionMap.allowNewTopics}'>
					<html:button property="newtopic"
						onclick="javascript:location.href='${newtopic}';"
						disabled="${sessionMap.finishedLock}" styleClass="button">
						<fmt:message key="label.newtopic" />
					</html:button>
				</c:if>
				<html:button property="refresh"
					onclick="javascript:location.href='${refresh}';"
					styleClass="button">
					<fmt:message key="label.refresh" />
				</html:button>

			</div>





		</div>