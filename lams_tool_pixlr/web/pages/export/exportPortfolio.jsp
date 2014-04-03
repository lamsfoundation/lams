<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
<lams:head>
	<title><c:out value="${pixlrDTO.title}" escapeXml="false" /></title>
	<lams:css localLinkPath="../" />

	<script type="text/javascript">
		<!--
			var popupWindow = null;
			var origImageHeight;
			var origImageWidth;
			
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
				image = document.getElementById("originalImage");
				if (image != null)
				{
					origImageHeight = image.height;
					origImageWidth =  image.width;
				
					if (image.height >= image.width)
					{
						if (image.height > 300)
						{
							image.height = 300;
						}
					}
					else
					{
						if (image.width > 300)
						{
							image.width = 300;
						}
					}		
				}
			}
		//-->
		</script>

</lams:head>

<body class="stripes" onload="init();">

<div id="content">

<h1><c:out value="${pixlrDTO.title}" escapeXml="true" /></h1>

<p><c:out value="${pixlrDTO.instructions}" escapeXml="false" /></p>

<br />

<h2><fmt:message key="title.originalImage" /></h2>

<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img
	src="tool_images/${pixlrDTO.imageFileName}" id="originalImage"
	title="<fmt:message key="tooltip.openfullsize" />"
	onclick="openPopup('tool_images/${pixlrDTO.imageFileName}', origImageHeight, origImageWidth);" />

<br />

<c:choose>
	<c:when test='${mode == "teacher"}'>

		<h2><fmt:message key="title.sessions" /></h2>

		<!-- Set up the anchor links -->
		<div id="sessionContents">
		<ul>
			<c:forEach var="session" items="${pixlrDTO.sessionDTOs}">
				<li><a href="#sid-${session.sessionID}">${session.sessionName}</a>
				</li>
			</c:forEach>
		</ul>
		</div>

		<hr>

		<c:forEach var="session" items="${pixlrDTO.sessionDTOs}">
			<div id="sid-${session.sessionID}">
			<h3>${session.sessionName}</h3>

			<table cellpadding="0" class="alternative-color">
				<tr>
					<th><fmt:message key="monitoring.th.learner" /></th>
					<th><fmt:message key="monitoring.th.image" /></th>
					<c:if test="${pixlrDTO.reflectOnActivity}">
						<th><fmt:message key="monitoring.th.reflection" /></th>
					</c:if>
				</tr>

				<c:forEach var="user" items="${session.userDTOs}">
					<tr>
						<td><a
							href="javascript:openPopup('${pixlrImageFolderURL}/${user.imageFileName}', ${user.imageHeight}, ${user.imageWidth})">
						${user.firstName} ${user.lastName} </a></td>
						<td align="center"><c:choose>
							<c:when
								test="${user.imageFileName != null && user.imageFileName != pixlrDTO.imageFileName}">
								<img src="tool_images/${user.imageFileName}"
									height="${user.imageThumbnailHeight}"
									width="${user.imageThumbnailWidth}"
									title="<fmt:message key="tooltip.openfullsize" />"
									onclick="openPopup('tool_images/${user.imageFileName}', ${user.imageHeight}, ${user.imageWidth})" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.notAvailable" />
							</c:otherwise>
						</c:choose></td>

						<c:if test="${pixlrDTO.reflectOnActivity}">
							<td><c:choose>
								<c:when test="${user.finishedReflection}">
															<lams:out value="${user.notebookEntry}" escapeHtml="true"/>
														</c:when>
								<c:otherwise>
									<fmt:message key="label.notAvailable" />
								</c:otherwise>
							</c:choose></td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
			</div>
		</c:forEach>

	</c:when>
	<c:otherwise>
		<br />
		<h2>
			<c:out value="${userDTO.firstName} ${userDTO.lastName}"/>
		</h2>
		<c:choose>
			<c:when
				test="${userDTO.imageFileName != null && userDTO.imageFileName != pixlrDTO.imageFileName}">
				<img src="tool_images/${userDTO.imageFileName}" />
			</c:when>
			<c:otherwise>
				<p>
				<fmt:message key="message.imageNotAvailable" />
				</p>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${pixlrDTO.reflectOnActivity}">
			<h2>
				<fmt:message key="monitoring.th.reflection" />
			</h2>
			
			<h4>
				<lams:out value="${pixlrDTO.reflectInstructions}" escapeHtml="true"/>
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

