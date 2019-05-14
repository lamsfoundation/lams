<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

	function masterDetailLoadUp(){
		jQuery("#userSummary${toolSessionID}").clearGridData().setGridParam({scrollOffset: 18});
		
 	    <c:forEach var="userAttempt" items="${userAttempts}" varStatus="i">
 	    	var table = jQuery("#userSummary${toolSessionID}");
 	    	
 	     	table.addRowData(${i.index + 1}, {
				id: "${i.index + 1}",
 	   	   		userAttemptUid: "${userAttempt.uid}",
 	   	   		title: "${userAttempt.mcQueContent.escapedDescription}",
 	   	   		response: "${userAttempt.mcOptionsContent.escapedOptionText}",
 	   	   		<c:if test="${content.enableConfidenceLevels}">
	   	   			confidence: "${userAttempt.confidenceLevel}",
	   	   		</c:if>
 	   	   		grade: "<fmt:formatNumber value='${userAttempt.mark}' maxFractionDigits='3'/>"
 	   	   	 });
 	     	
 	     	// set maxGrade attribute to cell DOM element
 	     	table.setCell(${i.index + 1}, "grade", "", null, {"maxGrade" :  ${userAttempt.mcQueContent.mark}});
		</c:forEach>
	};
	
	masterDetailLoadUp();
</script>
   	  
