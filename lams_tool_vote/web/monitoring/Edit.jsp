	<%@ include file="/common/taglibs.jsp"%>

	<c:set var="lams"><lams:LAMSURL/></c:set>
	<c:set var="tool"><lams:WebAppURL/></c:set>

	<%@ include file="/common/messages.jsp"%>								
	
	<c:if test="${editActivityDTO.monitoredContentInUse != 'true'}"> 			
		<c:if test="${voteGeneralMonitoringDTO.defineLaterInEditMode != 'true'}"> 			
			<jsp:include page="/authoring/BasicContentViewOnly.jsp" />
		</c:if> 				
		<c:if test="${voteGeneralMonitoringDTO.defineLaterInEditMode == 'true'}"> 			
			<jsp:include page="/authoring/BasicContent.jsp" />
		</c:if> 				
	</c:if> 											

	<c:if test="${editActivityDTO.monitoredContentInUse == 'true'}"> 			
		<div class="warning">
			<fmt:message key="error.content.inUse"/>
		</div>
		<br/>
		<table>
			<tr>
			 <td width="10%" nowrap valign="top" class="field-name">
			 	<fmt:message key="label.authoring.title"/>:
			 </td>
			  <td>
			  	<c:out value="${voteGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
			 </td>
			</tr>
			<tr>
			 <td width="10%" nowrap valign="top" class="field-name">
			 	<fmt:message key="label.authoring.instructions"/>:
			 </td>
			 <td>
			 	<c:out value="${voteGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
			 </td>
			</tr>			 
		</table>
		
	</c:if> 																									

		
		
