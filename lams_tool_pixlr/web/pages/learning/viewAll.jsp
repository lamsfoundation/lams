<!DOCTYPE html>        
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>

	<lams:head>  
		<title>
			<fmt:message>learner.title.viewAll</fmt:message>
		</title>
		<%@ include file="/common/learnerheader.jsp"%>
	
		<script type="text/javascript">
			var popupWindow = null;
			function openPopup(url, height, width) {	
				if(popupWindow && popupWindow.open && !popupWindow.closed){
					popupWindow.close();
				}
				
				popupWindow = window.open(url,'popupWindow','resizable,width=' +width+ ',height=' +height+ ',scrollbars');
			}

		    function submitForm(methodName) {
				var f = document.getElementById('messageForm');
				f.submit();
			}
		</script>
	</lams:head>
	
	<body class="stripes">
		<lams:Page type="learner" title="${pixlrDTO.title}">
			<html:form action="/learning" method="post" styleId="messageForm">
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="mode" value="${mode}" />
				<html:hidden property="dispatch" styleId = "dispatch" value="finishActivity" />
				<html:hidden property="redoQuestion" value="true" />
				
				<c:choose>
					<c:when test="${!empty learnerDTOs}">
						<table class="table">
							<c:forEach var="learner" items="${learnerDTOs}">
								<tr>
									<td width="30%">
										<a href="javascript:openPopup('${pixlrImageFolderURL}/${learner.imageFileName}', ${learner.imageHeight}, ${learner.imageWidth})">
											<c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/>
										</a>
									</td>
									
									<td>
										<c:choose>
											<c:when test="${!learner.imageHidden}">
												<img src="${pixlrImageFolderURL}/${learner.imageFileName}" 
													height="${learner.imageThumbnailHeight}" 
													width="${learner.imageThumbnailWidth}"
													title="<fmt:message key="tooltip.openfullsize" />"
													onclick="openPopup('${pixlrImageFolderURL}/${learner.imageFileName}', ${learner.imageHeight}, ${learner.imageWidth})"
												/>
											</c:when>
											<c:otherwise>
												<i><fmt:message key="message.imageHidden" /></i>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>	
							</c:forEach>
						</table>
					</c:when>
						
					<c:otherwise>
						<p>
							<i><fmt:message key="message.imageListEmpty" /></i>
						</p>
					</c:otherwise>
				</c:choose>
					
				<%--Bottom buttons--------------------------------------------------%>
				<html:submit styleClass="btn btn-default btn-sm" onclick="javascript:document.getElementById('dispatch').value = 'viewAllImages';">
					<fmt:message key="button.refresh" />
				</html:submit>
					
				<c:if test="${!pixlrDTO.lockOnFinish}">
					<html:submit styleClass="btn btn-default btn-sm loffset5" onclick="javascript:document.getElementById('dispatch').value = 'unspecified';">
						<fmt:message key="button.redo" />
					</html:submit>
				</c:if>
									
				<div class="voffset10 pull-right">
					<c:choose>
						<c:when test="${pixlrDTO.reflectOnActivity}">
							<html:submit styleClass="btn btn-primary" onclick="javascript:document.getElementById('dispatch').value = 'openNotebook';">
								<fmt:message key="button.continue" />
							</html:submit>
						</c:when>
						
						<c:otherwise>
							<html:link href="#nogo" styleClass="btn btn-primary" styleId="finishButton" onclick="submitForm('finished');return false">
								<span class="na">
									<c:choose>
										<c:when test="${activityPosition.last}">
											<fmt:message key="button.submit" />
										</c:when>
										
										<c:otherwise>
											<fmt:message key="button.finish" />
										</c:otherwise>
									</c:choose>
								</span>
							</html:link>
						</c:otherwise>
					</c:choose>
				</div>
				
			</html:form>
				
			<div class="footer"></div>
			
		</lams:Page>
	</body>
</lams:html>
