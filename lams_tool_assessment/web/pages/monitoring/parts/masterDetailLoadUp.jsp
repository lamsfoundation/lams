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
 	   	   	<c:set var="requiresMarking"
	 	   	   	   value="${empty questionResult.markedBy and question.type eq 6 and questionResult.mark eq 0}" />
 	     	table.addRowData(${i.index + 1}, {
 	   	    	id:"${i.index + 1}",
 	   	   		questionResultUid:"${questionResult.uid}",
 	   	   		title:"${fn:escapeXml(title)}",
 	   	   		response:responseStr,
 	   	   		<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
 	   	   			confidence:"${question.type == 8 ? -1 : questionResult.confidenceLevel}",
 	   	   		</c:if>
 	   	   		grade:
	 	 	 	 <c:choose>
	 	 	    	<c:when test="${requiresMarking}">
	 	 	    		"-"
	 	 	    	</c:when>
	 	 	    	<c:otherwise>
	 	 	    		"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
	 	 	    	</c:otherwise>
	 	 	    </c:choose>,
	 	 	    marker : 
		 	 	 <c:choose>
	 	 	    	<c:when test="${requiresMarking}">
		 	    		("<b><fmt:message key='label.monitoring.user.summary.grade.required' /></b>")
		 	    	</c:when>
		 	    	<c:when test="${not empty questionResult.markedBy}">
		 	  			"<c:out value='${questionResult.markedBy.fullName}' />"
		 	    	</c:when>
		 	    	<c:otherwise>
		 	    		""
		 	    	</c:otherwise>
		 	     </c:choose>,
		 	     markerComment: "<c:out value='${questionResult.markerComment}' />"
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
   	  
