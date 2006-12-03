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

	<div>
		
		<c:set var="buttonClass" value="button" />
		<c:if test="${sessionMap.finishedLock}">
			<c:set var="buttonClass" value="disabled" />
		</c:if>
	
		<c:if	test='${sessionMap.mode != "teacher" && sessionMap.allowNewTopics}'>
			<html:button property="newtopic"
				onclick="javascript:location.href='${newtopic}';"
				disabled="${sessionMap.finishedLock}" styleClass="${buttonClass}">
				<fmt:message key="label.newtopic" />
			</html:button>
		</c:if>
		
		<html:button property="refresh"
			onclick="javascript:location.href='${refresh}';"
			styleClass="button">
			<fmt:message key="label.refresh" />
		</html:button>
	</div>
	
	<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
		<div class="small-space-top">
			<h2>${sessionMap.reflectInstructions}</h2>
	
			<c:choose>
				<c:when test="${empty sessionMap.reflectEntry}">
					<p>
						<em>
							<fmt:message key="message.no.reflection.available" />
						</em>
					</p>
				</c:when>
				<c:otherwise>
					<p> <lams:out escapeXml="true" value="${sessionMap.reflectEntry}" />  </p>				
				</c:otherwise>
			</c:choose>
			
			<html:button property="continue"
				onclick="javascript:location.href='${continue}';"
				styleClass="button">
				<fmt:message key="label.edit" />
			</html:button>											
		</div>
	</c:if>
	
	<script type="text/javascript">
		function submitFinish() {
			document.getElementById("finish").disabled = true;
			location.href = '${finish}';
		}		
	</script>
	
	<div align="right" class="space-bottom-top">
		<c:if test='${sessionMap.mode != "teacher"}'>
			<c:choose>
				<c:when
					test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
					<html:button property="continue"
						onclick="javascript:location.href='${continue}';"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>
	
				<c:otherwise>
					<html:button property="finish" styleId="finish"
						onclick="submitFinish();"
						styleClass="button">
						<fmt:message key="label.finish" />
					</html:button>
				</c:otherwise>
	
			</c:choose>
		</c:if>
	</div>
</div>