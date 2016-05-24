<%@ include file="/common/taglibs.jsp"%>

<c:if test="${notebookEntriesExist == 'true' }"> 		

<div class="panel-group" id="accordionReflection" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="headingReflection">
        	<span class="panel-title">
	    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseReflection" aria-expanded="false" aria-controls="collapseReflection" >
          	<fmt:message key="label.reflection"/>
        	</a>
      		</span>
        </div>

        <div id="collapseReflection" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingReflection">
			<table class="table table-striped table-condensed">
			<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
				<c:set var="userName" scope="request" value="${currentDto.userName}"/>
				<c:set var="userId" scope="request" value="${currentDto.userId}"/>
				<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
				<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
				<tr>			
					<td>
						<c:url value="monitoring.do" var="openNotebook">
							<c:param name="dispatch" value="openNotebook" />
							<c:param name="uid" value="${reflectionUid}" />
							<c:param name="userId" value="${userId}" />
							<c:param name="userName" value="${fn:escapeXml(userName)}" />
							<c:param name="sessionId" value="${sessionId}" />													
							<c:param name="contentFolderID" value="${McMonitoringForm.contentFolderID}" />																								
						</c:url>
			
						<html:link href="javascript:launchPopup('${fn:escapeXml(openNotebook)}');" styleClass="btn btn-default btn-sm">
							<fmt:message key="label.view" />
						</html:link>
						&nbsp;<c:out value="${userName}" escapeXml="true"/> 
					</td>
				</tr>	
			</c:forEach>		
			</table>
		</div>
	</div>
</div>
	
</c:if> 															
