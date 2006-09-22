<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
	-->        
    </script>

</head>
<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			${sessionMap.title}
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<!--closes header-->

		<div id="content-learner">
			<h2>
				${sessionMap.instructions}
			</h2>
			<BR>
			<BR>
			<table cellpadding="0" cellspacing="0" class="alternative-color">
				<c:forEach var="element" items="${sessionMap.questionList}">
					<c:set var="question" value="${element.value}" />
					<tr>
						<th class="first">
							<c:if test="${not question.optional}">
								<span style="color: #FF0000">*</span>
							</c:if>
							<c:out value="${question.description}" escapeXml="false"/>
						</th>
					</tr>
					<tr>
						<td>
							<c:set var="answerText" value=""/>
							<c:if test="${not empty question.answer}">
								<c:set var="answerText" value="${question.answer.answerText}"/>
							</c:if>
							<c:forEach var="option" items="${question.options}">
								<c:set var="checked" value="true"/>
								<c:if test="${not empty question.answer}">
									<c:forEach var="choice" items="${question.answer.choices}">
										<c:if test="${choice == option.uid}">
											<c:set var="checked" value="false"/>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${checked}">
									${option.description}<BR><BR>
								</c:if>
							</c:forEach>
							<c:if test="${question.type == 3}">
								<lams:out value="${answerText}"/>
							</c:if>
							<c:if test="${question.appendText}">
								<lams:out value="${answerText}"/><BR><BR>
							</c:if>
						</td>
					</tr>					
										
					
				</c:forEach>
					
			</table>
			<c:if test="${mode != 'teacher'}">
				<div class="right-buttons">
					<c:choose>
						<c:when test="${sessionMap.reflectOn}">
							<html:button property="ContinueButton"
								onclick="return continueReflect()" disabled="${finishedLock}"
								styleClass="button">
								<fmt:message key="label.continue" />
							</html:button>
						</c:when>
						<c:otherwise>
							<html:button property="FinishButton"
								onclick="return finishSession()" disabled="${finishedLock}"
								styleClass="button">
								<fmt:message key="label.finished" />
							</html:button>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>

		</div>
		<!--closes content-->

		<div id="footer-learner">
		</div>
		<!--closes footer-->

	</div>
	<!--closes page-->
</body>
</html:html>


