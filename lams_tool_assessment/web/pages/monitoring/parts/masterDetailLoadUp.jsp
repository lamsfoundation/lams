<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<script type="text/javascript">
	function masterDetailLoadUp(){
		jQuery("#userSummary${param.tableName}").clearGridData().setGridParam({scrollOffset: 18});

 	    <c:forEach var="questionResult" items="${assessmentResult.questionResults}" varStatus="i">
	       	<c:set var="question" value="${questionResult.questionDto}"/>
	       	<c:set var="title">
	       		<c:out value="${questionResult.questionDto.titleEscaped}" escapeXml="true"/>
	       	</c:set>	
 	        var responseStr = "";
 	       	<%@ include file="userresponse.jsp"%>
 	       		
 	       	var table = jQuery("#userSummary${param.tableName}");
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

 	   	   	<c:set var="requiresMarking"
 	 	   	   	   value="${empty questionResult.markedBy and question.type eq 6 and questionResult.mark eq 0}" />
 	 	    // set maxGrade attribute to cell DOM element
 	 	    table.setCell(${i.index + 1}, "grade", "", ${requiresMarking ? "'requires-grading'" : "null"},
 	 	 	   {"maxGrade" :  "${questionResult.maxMark}"
 	 	 	    <c:choose>
	 	 	    	<c:when test="${requiresMarking}">
	 	 	    		,"title" : "<fmt:message key='label.monitoring.user.summary.grade.required' />"
	 	 	    		,"data-toggle" : "tooltip"
	 	 	    		,"data-container" : "body"
	 	 	    	</c:when>
	 	 	    	<c:when test="${not empty questionResult.markedBy}">
	 	 	  			,"title" : "<fmt:message key='label.monitoring.user.summary.grade.by'>
		 	 	  						<fmt:param><c:out value='${questionResult.markedBy.fullName}' /></fmt:param>
		 	 	  					</fmt:message>"
		 	 	  		,"data-toggle" : "tooltip"
		 	 	    	,"data-container" : "body"
	 	 	    	</c:when>
	 	 	    </c:choose>
 	 	 	    });
		</c:forEach>

		$('[data-toggle="tooltip"]').bootstrapTooltip();
		
		if (typeof CodeMirror != 'undefined') {
			CodeMirror.colorize($('.code-style'));
		}
	};
	masterDetailLoadUp();
</script>
   	  
