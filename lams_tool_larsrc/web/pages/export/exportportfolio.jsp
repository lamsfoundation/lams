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
		.rsrc-boldtext {
			font-weight: bold;
		}
	</style>

</lams:head>
<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${title}" escapeXml="true"/>
		</h1>
		
		<div>
			<c:out value="${instructions}" escapeXml="false"/>
		</div>

		<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
			<!-- 'group' always contains at least one Summary element  -->
			<h2>
				<c:choose>
					<c:when test="${group[0].initGroup}">
						<fmt:message key="export.init.resource" />
					</c:when>
					<c:otherwise>
						<fmt:message key="monitoring.label.group" />
						${group[0].sessionName}
				</c:otherwise>
				</c:choose>
			</h2>

			<table cellspacing="0" width="98%" class="alternative-color">
				<tr>
					<th>
						<fmt:message key="monitoring.label.type" />
					</th>
					<th width="20%">
						<fmt:message key="monitoring.label.title" />
					</th>
					<th width="45%">
						<fmt:message key="monitoring.label.instructions" />
					</th>
					<th>
						<fmt:message key="monitoring.label.suggest" />
					</th>
					<th align="center">
						<fmt:message key="export.label.resource" />
					</th>

					<c:if test="${mode == 'teacher'}">
						<th align="center">
							<!-- hide/show -->
						</th>
					</c:if>
				</tr>

				<c:forEach var="item" items="${group}" varStatus="status">

					<%-- Bold styling is to indicate an item created by the author --%>
					<c:choose>
						<c:when test="${item.itemCreateByAuthor}">
							<c:set var="itemRowStyle" value="rsrc-boldtext" />
						</c:when>
						<c:otherwise>
							<c:set var="itemRowStyle" value="" />
						</c:otherwise>
					</c:choose>

					<c:if test="${item.itemUid == -1}">
						<tr>
							<td colspan="5">
								<div align="left">
									<fmt:message
										key="message.monitoring.summary.no.resource.for.group" />
								</div>
							</td>
						</tr>
					</c:if>

					<c:if test="${item.itemUid != -1}">
						<tr class="${itemRowStyle}">
							<td>
								<c:choose>
									<c:when test="${item.itemType == 1}">
										<fmt:message key="label.authoring.basic.resource.url" />
									</c:when>
									<c:when test="${item.itemType == 2}">
										<fmt:message key="label.authoring.basic.resource.file" />
									</c:when>
									<c:when test="${item.itemType == 3}">
										<fmt:message key="label.authoring.basic.resource.website" />
									</c:when>
									<c:when test="${item.itemType == 4}">
										<fmt:message
											key="label.authoring.basic.resource.learning.object" />
									</c:when>
								</c:choose>
							</td>

							<td>
								<c:out value="${item.itemTitle}"/>
							</td>
							
							<td>
								<c:choose>
									<c:when test="${not empty item.itemInstructions}">
										<ol>
											<c:forEach var="itemInstruction" items="${item.itemInstructions}">
												<li>
													<c:out value="${itemInstruction}" escapeXml="true"/>
												</li>
											</c:forEach>
										</ol>
									</c:when>									
									<c:otherwise>
										&nbsp;
									</c:otherwise>								
								</c:choose>								
							</td>

							<td>
								<c:out value="${item.username}" escapeXml="true"/>
							</td>

							<td align="center">
								<c:choose>
									<c:when test="${item.itemType == 1}">
										<a href="javascript:;"
											onclick="launchPopup('${item.url}','openurl');"> <fmt:message
												key="label.authoring.basic.resource.preview" /> </a>
									</c:when>
									<c:when test="${item.itemType == 2}">
										<a href="${item.attachmentLocalUrl}"> <fmt:message
												key="label.download" /> </a>
									</c:when>
									<c:when test="${item.itemType == 3}">
										<a href="${item.attachmentLocalUrl}"> <fmt:message
												key="label.download" /> </a>
									</c:when>
									<c:when test="${item.itemType == 4}">
										<fmt:message key="export.label.no.learning.object" />
									</c:when>
								</c:choose>
							</td>

							<c:if test="${mode == 'teacher'}">
								<td align="center">
									<c:if test="${item.itemHide}">
										<fmt:message key="monitoring.label.hidden" />
									</c:if>
								</td>
							</c:if>
						</tr>
					</c:if>
				</c:forEach>
			</table>

		</c:forEach>

		<%-- Display reflection entries --%>
		<c:if test="${sessionMap.reflectOn}">
			<h3>
				<fmt:message key="label.export.reflection" />
			</h3>
			<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
				<h4>
					<c:out value="${reflectDTO.fullName}" escapeXml="true"/>
				</h4>
				<p>
					<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
				</p>
			</c:forEach>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>
