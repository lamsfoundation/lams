<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${pixlrDTO}" />

<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
	
	function submitForm(method, uid) {
		document.getElementById("monitoringForm").action = method+".do";
		document.getElementById("hideUserImageUid").value = uid;
		document.getElementById("monitoringForm").submit();
	}
</script>

<div class="panel">
	<h4>
	  <c:out value="${dto.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${dto.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty dto.sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.summary" />
		</lams:Alert>
	</c:if>
</div>

<c:if test="${isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<form:form action="toggleHideImage.do" modelAttribute="monitoringForm" method="post" id="monitoringForm">
	
	<form:hidden path="contentFolderID" value="${contentFolderID}" />	
	<form:hidden path="toolContentID" value="${toolContentID}"/>
	<form:hidden path="hideUserImageUid" id="hideUserImageUid"/>
	
	<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
	
		<c:if test="${isGroupedActivity}">	
		    <div class="panel panel-default" >
		        <div class="panel-heading" id="heading${session.sessionID}">
		        	<span class="panel-title collapsable-icon-left">
		        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
								aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${session.sessionID}" >
							<fmt:message key="heading.group" >
								<fmt:param><c:out value="${session.sessionName}" /></fmt:param>
							</fmt:message>
						</a>
					</span>
		        </div>
	        
				<div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
	        			role="tabpanel" aria-labelledby="heading${session.sessionID}">
		</c:if>
	
		<table class="table table-centered voffset10">
			<tr>
				<th>
					<fmt:message key="monitoring.th.learner" />
				</th>
				<th>
					<fmt:message key="monitoring.th.image" />
				</th>
			</tr>
			
			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
					</td>
					
					<td>
						<c:choose>
							<c:when test="${user.imageFileName != null && user.imageFileName != pixlrDTO.imageFileName}">
								<img src="${pixlrImageFolderURL}/${user.imageFileName}" 
									height="${user.imageThumbnailHeight}" 
									width="${user.imageThumbnailWidth}"
									title="<fmt:message key="tooltip.openfullsize" />"
									onclick="openPopup('${pixlrImageFolderURL}/${user.imageFileName}', ${user.imageHeight}, ${user.imageWidth})"
								 />
								 <c:if test="${pixlrDTO.allowViewOthersImages}">
								 	<br />
								 	<a href="javascript:submitForm('toggleHideImage', '${user.uid}')">
								 	<c:choose>
								 		<c:when test="${user.imageHidden}">
											<fmt:message key="monitoring.showImage" />
								 		</c:when>
								 		<c:otherwise>
								 			<fmt:message key="monitoring.hideImage" />
								 		</c:otherwise>
								 	</c:choose>
								 	</a>
								 </c:if>
							</c:when>
							<c:otherwise>
								<fmt:message key="label.notAvailable" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<c:if test="${isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
		${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
	</c:forEach>

</form:form>

<c:if test="${isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<%@ include file="advanceOptions.jsp"%>
