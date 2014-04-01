<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	<!--
	function masterDetailLoadUp(){
			jQuery("#userSummary${assessmentResult.sessionId}").clearGridData().setGridParam({scrollOffset: 18});
 	        <c:forEach var="questionResult" items="${assessmentResult.questionResults}" varStatus="i">
	       		<c:set var="question" value="${questionResult.assessmentQuestion}"/>
	       		<c:set var="title">
	       			<c:out value="${questionResult.assessmentQuestion.titleEscaped}" escapeXml="true"/>
	       		</c:set>	
 	        	var responseStr = "";
 	       		<%@ include file="userresponse.jsp"%>
 	     		jQuery("#userSummary${assessmentResult.sessionId}").addRowData(${i.index + 1}, {
 	   	     		id:"${i.index + 1}",
 	   	   			questionResultUid:"${questionResult.uid}",
 	   	   			title:"${title}",
 	   	   			response:responseStr,
 	   	   			grade:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
 	   	   	    });	
	        </c:forEach>
	};	  	   	  
	masterDetailLoadUp();
	-->	
</script>
   	  
