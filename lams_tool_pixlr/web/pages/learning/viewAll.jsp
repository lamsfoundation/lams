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
				var f = document.getElementById('learningForm');
				f.submit();
			}
		</script>
	</lams:head>
	
	<body class="stripes">
		<lams:Page type="learner" title="${pixlrDTO.title}">
			<form:form action="finishActivity.do" modelAttribute="learningForm" method="post" id="learningForm">
				<form:hidden path="toolSessionID" id="toolSessionID"/>
				<form:hidden path="mode" value="${mode}" />
				<form:hidden path="redoQuestion" />
				
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
				<button  class="btn btn-secondary btn-sm" onclick="javascript:document.getElementById('learningForm').action = '<lams:WebAppURL />learning/viewAllImages.do';">
					<fmt:message key="button.refresh" />
				</button>
					
				<c:if test="${!pixlrDTO.lockOnFinish}">
					<button  class="btn btn-secondary btn-sm ms-1" onclick="javascript:document.getElementById('learningForm').action = '<lams:WebAppURL />learning.do';">
						<fmt:message key="button.redo" />
					</button>
				</c:if>
									
				<div class="activity-bottom-buttons">
					<c:choose>
						<c:when test="${pixlrDTO.reflectOnActivity}">
							<button class="btn btn-primary" onclick="javascript:document.getElementById('learningForm').action = '<lams:WebAppURL />learning/openNotebook.do';">
								<fmt:message key="button.continue" />
							</button>
						</c:when>
						
						<c:otherwise>
							<a href="#nogo" class="btn btn-primary" id="finishButton" onclick="submitForm('finished');return false">
								<span class="na">
									<c:choose>
										<c:when test="${isLastActivity}">
											<fmt:message key="button.submit" />
										</c:when>
										
										<c:otherwise>
											<fmt:message key="button.finish" />
										</c:otherwise>
									</c:choose>
								</span>
							</a>
						</c:otherwise>
					</c:choose>
				</div>
				
			</form:form>
				
			<div class="footer"></div>
			
		</lams:Page>
	</body>
</lams:html>
