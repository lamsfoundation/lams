<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.vote.nominations" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>

<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<table id="itemTable" style="align:left;width:650px" >
    <c:set var="queIndex" scope="request" value="0"/>
    
    
    <c:forEach items="${listNominationContentDTO}" var="currentDTO" varStatus="status">
	<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
	<c:set var="question" scope="request" value="${currentDTO.question}"/>
	<c:set var="feedback" scope="request" value="${currentDTO.feedback}"/>
	<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}"/>	

	<tr>
		<td width="10%" class="field-name align-right">
			<fmt:message key="label.nomination" />:
		</td>

		<td width="60%" class="align-left">
			<c:out value="${question}" escapeXml="false"/> 
		</td>		


			<td width="10%" class="align-right">
		 		<c:if test="${totalNominationCount != 1}"> 		
		 		
			 		<c:if test="${queIndex == 1}"> 		
						<a title="<fmt:message key='label.tip.moveNominationDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringNomination('<c:out value="${queIndex}"/>','moveNominationDown');">
		                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
						</a> 
					</c:if> 							
	
			 		<c:if test="${queIndex == totalNominationCount}"> 		
						<a title="<fmt:message key='label.tip.moveNominationUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringNomination('<c:out value="${queIndex}"/>','moveNominationUp');">
		                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
						</a> 
					</c:if> 							
					
					<c:if test="${(queIndex != 1)  && (queIndex != totalNominationCount)}"> 		
						<a title="<fmt:message key='label.tip.moveNominationDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringNomination('<c:out value="${queIndex}"/>','moveNominationDown');">
		                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
						</a> 
	
						<a title="<fmt:message key='label.tip.moveNominationUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringNomination('<c:out value="${queIndex}"/>','moveNominationUp');">
		                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
						</a> 
					</c:if> 							
				
				</c:if> 			
			</td>

		
		

		<td width="10%" class="align-right">
				<a title="<fmt:message key='label.tip.editNomination'/>" href="javascript:;" onclick="javascript:showMessage('<html:rewrite page="/monitoring.do?dispatch=newEditableNominationBox&questionIndex=${queIndex}&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&activeModule=${voteGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${voteGeneralAuthoringDTO.defaultContentIdStr}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}"/>');">
	                    <img src="<c:out value="${tool}"/>images/edit.gif" border="0">
				</a> 
		</td>

		<td width="10%" class="align-right">
				<a title="<fmt:message key='label.tip.deleteNomination'/>" href="javascript:;" onclick="removeMonitoringNomination(${queIndex});">
	                    <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
				</a> 				
		</td>
	</tr>
</c:forEach>

</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>

