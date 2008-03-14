<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="exportDTOList" value="${sessionMap.exportDTOList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

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
	<lams:css localLinkPath="../"/>
</lams:head>
<body class="stripes">


	<div id="content">

	<h1>${title} </h1>

		<table border="0" cellspacing="3" width="98%">
			<c:forEach var="group" items="${exportDTOList}" varStatus="firstGroup">
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
										<th width="325">
											<fmt:message key="monitoring.label.title" />
										</th>
										<th width="175">
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
												${item.itemTitle}
											</td>
											
											<td>
												${item.username}
											</td>
											
											<td align="center">
												<c:forEach var="comment" items="${item.commentDTOs}">
												
														<div>
															<table cellspacing="0" class="forum">
																<tr>
																	<th >
																		<fmt:message key="lable.preview.by" />
																		${comment.createdBy}
																				-				
																		<lams:Date value="${comment.createDate}" />
																	</th>
																</tr>
																
																<tr>
																	<td class="posted-by">
																	</td>
																</tr>
													
																<tr>
																	<td>
																		<c:out value="${comment.comment}" escapeXml="false" />
																	</td>
																</tr>
																
															</table>
														</div>
												</c:forEach>
											
												<ul>
													<c:forEach var="file" items="${item.attachmentDTOs}">
															<li>
																<a href="${file.attachmentLocalUrl}"> 
																	<c:out value="${file.fileName}" />
																</a>
																[${file.createdBy}]
																
															</li>
													</c:forEach>
												</ul>
											</td>
											
											
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


	<div id="footer">
	</div><!--closes footer-->

</body>
</lams:html>
