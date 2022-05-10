<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<script type="text/javascript">
	function masterDetailLoadUp(){
		jQuery("#userSummary${assessmentResult.sessionId}").clearGridData().setGridParam({scrollOffset: 18});

 	    <c:forEach var="questionResult" items="${assessmentResult.questionResults}" varStatus="i">
	       	<c:set var="question" value="${questionResult.questionDto}"/>
	       	<c:set var="title">
	       		<c:out value="${questionResult.questionDto.titleEscaped}" escapeXml="true"/>
	       	</c:set>	
 	        var responseStr = "";
 	       	<%@ include file="userresponse.jsp"%>
 	       		
 	       	var table = jQuery("#userSummary${assessmentResult.sessionId}");
 	     	table.addRowData(${i.index + 1}, {
 	   	    	id:"${i.index + 1}",
 	   	   		questionResultUid:"${questionResult.uid}",
 	   	   		title:"${fn:escapeXml(title)}",
 	   	   		response:responseStr,
 	   	   		<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
 	   	   			confidence:"${question.type == 8 ? -1 : questionResult.confidenceLevel}",
 	   	   		</c:if>
 	   	   		grade:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
 	   	   	});

 	 	    // set maxGrade attribute to cell DOM element
 	 	    table.setCell(${i.index + 1}, "grade", "", null, {"maxGrade" :  "${questionResult.maxMark}"});
		</c:forEach>

		if (typeof CodeMirror != 'undefined') {
			CodeMirror.colorize($('.code-style'));
		}
	};
	masterDetailLoadUp();
</script>
   	  
