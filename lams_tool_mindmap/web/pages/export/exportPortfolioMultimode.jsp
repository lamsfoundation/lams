<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="swfobject.js"></script>
<script type="text/javascript" src="resize.js"></script>
<script type="text/javascript" src="jquery.js"></script>

<script type="text/javascript">

<!--
	<c:set var="MindmapUser">
		<c:out value="${currentMindmapUser}" escapeXml="true"/>
	</c:set>
	
	flashvars = { xml: "${mindmapContentPath}", user: "${MindmapUser}", dictionary: "${localizationPath}" }
	
	$(window).resize(makeNice);

	embedFlashObject(700, 525);

	function embedFlashObject(x, y)
	{
		swfobject.embedSWF("mindmap.swf", "flashContent", x, y, "9.0.0", false, flashvars);
	}
-->
</script>

<html>
	<lams:head>
		<title><c:out value="${mindmapDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">
		<div id="content">

		<h1>
			<c:out value="${mindmapDTO.title}" escapeXml="false" />
		</h1>

		<p>
			<c:out value="${mindmapDTO.instructions}" escapeXml="false" />
		</p>
	
			<table>
				<tr>
					<th colspan="2">
						<fmt:message>label.multimode</fmt:message>
					</th>
				</tr>
								
				<tr>
					<td class="field-name" width="20%">
						<fmt:message key="label.mindmapEntry" />
					</td>
							
					<td>
						<center id="center12">
							<div id="flashContent">
								<fmt:message>message.enableJavaScript</fmt:message>
							</div>
						</center>
					</td>
				</tr>
				
				<c:forEach var="user" items="${userDTOs}">
					<c:if test="${!empty user.entryDTO.entry}">
						<tr>
							<td class="field-name" width="20%">
								${user.firstName} ${user.lastName}
							</td>
							<td>
								${user.entryDTO.entry}
							</td>
						</tr>
					</c:if>
				</c:forEach>		
				
			</table>
				
		</div>
		<!--closes content-->

		<div id="footer">
			</div>
		<!--closes footer-->

	</body>
</html>
