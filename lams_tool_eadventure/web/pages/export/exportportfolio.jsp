<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="title" value="${sessionMap.title}" />
<c:set var="instructions" value="${sessionMap.instructions}" />

<lams:html>
<lams:head>
	<title><fmt:message key="export.title" />
	</title>
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

	<style type="text/css">
		.eadventure-boldtext {
			font-weight: bold;
		}
	</style>

</lams:head>
<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${title}" escapeXml="true"/>
		</h1>
		
		<div style="height:40px">
		<a href="javascript:launchPopup('${sessionMap.localURL}','${title}')" > <fmt:message
												key="label.download" /> </a>

		</div>
		
		
		<div style="height:40px">
			<c:out value="${instructions}" escapeXml="false"/>
		</div>

		<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
			<!-- 'group' always contains at least one Summary element  -->
			<h2>
				${group.sessionName}
			</h2>

			<table border="0" cellspacing="30" width="98%">
	
			
		<c:forEach var="user" items="${group.users}" varStatus="status" >
			<tr>
				<fmt:message key="monitoring.label.access.time" />
			</th> 
			<th>
				<fmt:message key="monitoring.label.user.name" />
			</th>
		
		</tr>
			<tr>
				<td>
					<lams:Date value="${user.accessDate}"/>
				</td> 
				<td>
					<c:out value="${user.firstName},${user.lastName}" escapeXml="true"/>
				</td>	
				
	
			</tr>
			<tr>
				
			<th>
				<fmt:message key="monitoring.label.user.report" />
			</th>
			</tr>
			<tr>
			<c:choose>
				<c:when test="${group.existList[status.index]}">
				<td>
					${group.reportList[status.index]}
				</td>
				</c:when>
				<c:otherwise>
				<td>
					<fmt:message key="monitoring.no.report" />
				</td>
				</c:otherwise>
			</c:choose>
			</tr>	
			
		</c:forEach>
				
	</table>



			<%-- Display reflection entries --%>
			

				<c:if test="${sessionMap.reflectOn}">
					<%-- End all answers for this question --%>
					<h3>
						<fmt:message key="label.export.reflection" />
					</h3>
					<c:set var="reflectDTOSet"
						value="${sessionMap.reflectList[group[0].sessionId]}" />
					<c:forEach var="reflectDTO" items="${reflectDTOSet}">
						<h4>
							<c:out value="${reflectDTO.fullName}" escapeXml="true"/>
						</h4>
						<p>
							<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
						</p>
					</c:forEach>
				</c:if>



		</c:forEach>

	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>
