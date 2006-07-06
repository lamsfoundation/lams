<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<%-- flag indicated to header.jsp to use the local path option for the css --%>
<c:set scope="page" var="localLinkPath" value="../"/>
<head>
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
</head>
<body>

<div id="page-learner"><!--main box 'page'-->

	<h1 class="no-tabs-below"> </h1>
	<div id="header-no-tabs-learner">

	</div><!--closes header-->

	<div id="content-learner">

		<table border="0" cellspacing="3" width="98%">
			<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
				<c:set var="groupSize" value="${fn:length(group)}" />
				<c:forEach var="item" items="${group}" varStatus="status">
					<%-- display group name on first row--%>
					<c:if test="${status.index == 0}">
						<tr>
							<td>
								<c:choose>
									<c:when test="${item.initGroup}">
										<B><fmt:message key="export.init.resource" /></B>
									</c:when>
									<c:otherwise>
										<B><fmt:message key="monitoring.label.group" /> ${item.sessionName}</B>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td>
								<table border="0" cellspacing="3" width="98%">
									<tr>
										<th width="50">
											<fmt:message key="monitoring.label.type" />
										</th>
										<th width="300">
											<fmt:message key="monitoring.label.title" />
										</th>
										<th width="150">
											<fmt:message key="monitoring.label.suggest" />
										</th>
										<th width="300" align="center">
											<fmt:message key="export.label.resource" />
										</th>
										<c:if test="${mode == 'teacher'}">
											<th width="50" align="center">
												<!-- hide/show -->
											</th>
										</c:if>
									</tr>
									</c:if>
									<c:if test="${item.itemUid == -1}">
										<tr>
											<td colspan="4">
												<div align="left">
													<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
												</div>
											</td>
										</tr>
									</c:if>
									<c:if test="${item.itemUid != -1}">
										<tr>
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
														<fmt:message key="label.authoring.basic.resource.learning.object" />
													</c:when>
												</c:choose>
											</td>
											<td>
												${item.itemTitle}
											</td>
											<td>
												${item.username}
											</td>
											<td align="center">
												<c:choose>
													<c:when test="${item.itemType == 1}">
														<a href="javascript:;" onclick="launchPopup('${item.url}','openurl');"> <fmt:message key="label.authoring.basic.resource.preview" /> </a>
													</c:when>
													<c:when test="${item.itemType == 2}">
														<c:set var="downloadUrl">
															<html:rewrite page="/download/?uuid=${item.fileUuid}&versionID=${item.fileVersionId}&preferDownload=false" />
														</c:set>
														<a href="${downloadUrl}"> <fmt:message key="label.download" /> </a>
													</c:when>
													<c:when test="${item.itemType == 3}">
														<c:set var="downloadUrl">
															<html:rewrite page="/download/?uuid=${item.fileUuid}&versionID=${item.fileVersionId}&preferDownload=false" />
														</c:set>
														<a href="${downloadUrl}"> <fmt:message key="label.download" /> </a>
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
									<c:if test="${status.count == groupSize}">
								</table>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>
		</table>

	</div>  <!--closes content-->


	<div id="footer-learner">
	</div><!--closes footer-->

</div><!--closes page-->

</body>
</html:html>
