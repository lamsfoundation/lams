<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
<lams:head>
	<title><c:out value="${wookieDTO.title}" escapeXml="true" /></title>
	<lams:css localLinkPath="../" />

	<script type="text/javascript">
		<!--
			var popupWindow = null;
			
			function openPopup(url, height, width)
			{	
				if(popupWindow && popupWindow.open && !popupWindow.closed){
					popupWindow.close();
				}
				popupWindow = window.open(url,'popupWindow','resizable,width=' +width+ ',height=' +height+ ',scrollbars');
				popupWindow.window.focus();
			}
			
			function init()
			{
			}
		//-->
		</script>

</lams:head>

<body class="stripes" onload="init();">

<div id="content">

<h1><c:out value="${wookieDTO.title}" escapeXml="true" /></h1>

<p><c:out value="${wookieDTO.instructions}" escapeXml="false" /></p>

<br />


<c:choose>
	<c:when test='${mode == "teacher"}'>

		
		<c:if test="${multipleSessionFlag}">
			<h2><fmt:message key="title.sessions" /></h2>

			<!-- Set up the anchor links -->
			<div id="sessionContents">
			<ul>
				<c:forEach var="session" items="${wookieDTO.sessionDTOs}">
					<li><a href="#sid-${session.sessionID}">${session.sessionName}</a>
					</li>
				</c:forEach>
			</ul>
			</div>
			<br />
			<hr>
			<br />
		</c:if>

		<c:forEach var="session" items="${wookieDTO.sessionDTOs}">
			<div id="sid-${session.sessionID}">
				<c:if test="${multipleSessionFlag}">
				<h3>
					${session.sessionName}
				</h3>
				</c:if>

				<table cellpadding="0">
					<tr>
						<td class="field-name" width="30%">
							<fmt:message key="heading.totalLearners" />
						</td>
						<td width="70%">
							${session.numberOfLearners}
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr align="center">
						<td>
							<iframe
									id="widgetIframe"
									src="${session.sessionUserWidgetUrl}" 
									name="widgetIframe"
									style="width:${session.widgetWidth}px;height:${session.widgetHeight}px;border:0px;" 
									frameborder="no"
									scrolling="no"
									>
							</iframe>
						</td>
					</tr>
				</table>
		
				<c:if test="${wookieDTO.reflectOnActivity}">
					<br />
					
					<h3> <fmt:message key="monitoring.reflections" /></h3>
					
					<table cellpadding="0" class="alternative-color">
						<tr>
							<th><fmt:message key="monitoring.th.learner" /></th>
							
							<th><fmt:message key="monitoring.th.reflection" /></th>
							
						</tr>
						
						<c:forEach var="user" items="${session.userDTOs}">
							<tr>
								<td>
									<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
								</td>
								
									<td >
										<c:choose>
											<c:when test="${user.finishedReflection}">
												<lams:out escapeHtml="true" value="${user.notebookEntry}"/>
											</c:when>
											<c:otherwise>
												<fmt:message key="label.notAvailable" />
											</c:otherwise>
										</c:choose>
									</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
				
				<br /> 
				<br />
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<br />
		<h2>
			<c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/>
		</h2>
		
		<c:choose>
			<c:when test="${wookieDTO.lockOnFinish}">
				<div class="info">
					<fmt:message key="export.portfolio.contentlocked" />
				</div>
			</c:when>
			<c:otherwise>
				<table width="100%" height>
					<tr align="center">
						<td>
							<iframe
									id="widgetIframe"
									src="${userWidgetURL}" 
									name="widgetIframe"
									style="width:${widgetWidth}px;height:${widgetHeight}px;border:0px;" 
									frameborder="no"
									scrolling="no"
									>
							</iframe>
						</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
		
		
		<c:if test="${wookieDTO.reflectOnActivity}">
			<h2>
				<fmt:message key="monitoring.th.reflection" />
			</h2>
			
			<h4>
				<lams:out value="${wookieDTO.reflectInstructions}" escapeHtml="true"/>
			</h4>
			
			<c:choose>
				<c:when test="${userDTO.finishedReflection}">
					<p>
						<lams:out value="${userDTO.notebookEntry}" escapeHtml="true"/>
					</p>
				</c:when>
				<c:otherwise>
					<fmt:message key="label.notAvailable" />
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:otherwise>
</c:choose></div>
<!--closes content-->

<div id="footer"></div>
<!--closes footer-->

</body>
</html>

