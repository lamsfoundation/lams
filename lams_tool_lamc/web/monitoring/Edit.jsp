	<%@ include file="/common/taglibs.jsp"%>

	<c:set var="lams"><lams:LAMSURL/></c:set>
	<c:set var="tool"><lams:WebAppURL/></c:set>

	<%@ include file="/common/messages.jsp"%>								
	
	<c:if test="${editActivityDTO.monitoredContentInUse != 'true'}"> 			
		<c:if test="${mcGeneralMonitoringDTO.defineLaterInEditMode != 'true'}"> 			
			<jsp:include page="/authoring/BasicContentViewOnly.jsp" />
		</c:if> 				
		<c:if test="${mcGeneralMonitoringDTO.defineLaterInEditMode == 'true'}"> 			
			<jsp:include page="/authoring/BasicContent.jsp" />
		</c:if> 				
	</c:if> 											

	<c:if test="${editActivityDTO.monitoredContentInUse == 'true'}"> 			
		<table border="0" cellspacing="2" cellpadding="2">									
			<tr> <td NOWRAP valign=top>
					<fmt:message key="error.content.inUse"/> 
			</td> </tr>
		</table>
	</c:if> 																									

		
		
