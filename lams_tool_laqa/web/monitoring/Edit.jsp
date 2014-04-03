<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<%@ include file="/common/messages.jsp"%>

<c:if test="${editActivityDTO.monitoredContentInUse != 'true'}"> 
	<c:if test="${qaGeneralMonitoringDTO.defineLaterInEditMode != 'true'}">
		<jsp:include page="/authoring/BasicContentViewOnly.jsp" />
	</c:if>
						
	<c:if test="${qaGeneralMonitoringDTO.defineLaterInEditMode == 'true'}">
		<jsp:include page="/authoring/BasicContent.jsp" />
	</c:if>
</c:if>
