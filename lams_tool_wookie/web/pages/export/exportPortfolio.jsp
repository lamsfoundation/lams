<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
<lams:head>
	<title><c:out value="${wookieDTO.title}" escapeXml="false" /></title>
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

<h1><c:out value="${wookieDTO.title}" escapeXml="false" /></h1>

<p><c:out value="${wookieDTO.instructions}" escapeXml="false" /></p>

<br />


<c:choose>
	<c:when test='${mode == "teacher"}'>

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

		<hr>

		<c:forEach var="session" items="${wookieDTO.sessionDTOs}">
			<div id="sid-${session.sessionID}">
			<h3>${session.sessionName}</h3>


				<c:forEach var="user" items="${session.userDTOs}">
					
				</c:forEach>
			</div>
		</c:forEach>

	</c:when>
	<c:otherwise>
		<br />
		<h2>
			${userDTO.firstName} ${userDTO.lastName}
		</h2>
		
		
		<c:if test="${wookieDTO.reflectOnActivity}">
			<h2>
				<fmt:message key="monitoring.th.reflection" />
			</h2>
			
			<h4>
				${wookieDTO.reflectInstructions}
			</h4>
			
			<c:choose>
				<c:when test="${userDTO.finishedReflection}">
					<p>
						${userDTO.notebookEntry}
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

