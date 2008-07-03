<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="title" value="${sessionMap.title}" />

<lams:html>
<lams:head>
	<title><fmt:message key="export.title" /></title>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<script type="text/javascript">
		function launchPopup(url,title) {
			var wd = null;
			if(wd && wd.open && !wd.closed){
				wd.close();
			}
			wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
			wd.window.focus();
		}
	</script>
	<lams:css localLinkPath="../" />
</lams:head>
<body class="stripes">


<div id="content">

<h1>${title}</h1>

<table border="0" cellspacing="3" width="98%">
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:forEach var="question" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.index == 0}">
				<tr>
					<td><c:choose>
						<c:when test="${question.initGroup}">
							<B><fmt:message key="export.init.question" /></B>
						</c:when>
						<c:otherwise>
							<B><fmt:message key="label.monitoring.group" />
							${question.sessionName}</B>
						</c:otherwise>
					</c:choose></td>
				</tr>
				<tr>
					<td>
					<table border="0" cellspacing="3" width="98%">
						<tr>
							<th width="50"><fmt:message key="label.monitoring.type" />
							</th>
							<th width="300"><fmt:message key="label.monitoring.title" />
							</th>
							<th width="150"><fmt:message key="label.monitoring.suggest" />
							</th>
							<th width="300" align="center"><fmt:message
								key="export.label.question" /></th>
							<c:if test="${mode == 'teacher'}">
								<th width="50" align="center"><!-- hide/show --></th>
							</c:if>
						</tr>
						</c:if>
						<c:if test="${question.questionUid == -1}">
							<tr>
								<td colspan="4">
								<div align="left"><b> <fmt:message
									key="message.monitoring.summary.no.question.for.group" /> </b></div>
								</td>
							</tr>
						</c:if>
						<c:if test="${question.questionUid != -1}">
							<tr>
								<td><c:choose>
									<c:when test="${question.questionType == 1}">
										<fmt:message key="label.authoring.basic.textfield" />
									</c:when>
									<c:when test="${question.questionType == 2}">
										<fmt:message key="label.authoring.basic.textarea" />
									</c:when>
									<c:when test="${question.questionType == 3}">
										<fmt:message key="label.authoring.basic.number" />
									</c:when>
									<c:when test="${question.questionType == 4}">
										<fmt:message key="label.authoring.basic.date" />
									</c:when>
									<c:when test="${question.questionType == 5}">
										<fmt:message key="label.authoring.basic.file" />
									</c:when>
									<c:when test="${question.questionType == 6}">
										<fmt:message key="label.authoring.basic.image" />
									</c:when>
									<c:when test="${question.questionType == 7}">
										<fmt:message key="label.authoring.basic.radio" />
									</c:when>
									<c:when test="${question.questionType == 8}">
										<fmt:message key="label.authoring.basic.dropdown" />
									</c:when>
									<c:when test="${question.questionType == 9}">
										<fmt:message key="label.authoring.basic.checkbox" />
									</c:when>
								<c:when test="${question.questionType == 10}">
										<fmt:message key="label.authoring.basic.longlat" />
									</c:when>
								</c:choose></td>
								<td>${question.questionTitle}</td>
								<td>${question.username}</td>
								<td align="center"><c:choose>
									<c:when test="${question.questionType == 1}">
										<a href="#"
											onclick="launchPopup('${question.url}','openurl');">It
										was preview...</a>
									</c:when>
									<c:when test="${question.questionType == 5}">
										<a href="${question.attachmentLocalUrl}"> <fmt:message
											key="label.download" /> </a>
									</c:when>

								</c:choose></td>
								<c:if test="${mode == 'teacher'}">
									<td align="center"><c:if test="${question.questionHide}">
										<fmt:message key="label.monitoring.hidden" />
									</c:if></td>
								</c:if>
							</tr>
						</c:if>
						<c:if test="${status.count == groupSize}">
					</table>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</c:forEach>
</table>

</div>
<!--closes content-->


<div id="footer"></div>
<!--closes footer-->

</body>
</lams:html>
