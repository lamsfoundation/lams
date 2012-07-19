<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	<!--
	function masterDetailLoadUp(){
			jQuery("#userSummary${userSessionId}").clearGridData().setGridParam({scrollOffset: 18});
 	        <c:forEach var="visitLog" items="${visitLogs}" varStatus="i">
	       		<c:set var="item" value="${visitLog.scratchieItem}"/>
	       		
 	     		jQuery("#userSummary${userSessionId}").addRowData(${i.index + 1}, {
 	   	     		attempt:"${i.index + 1}",
 	   	   			scratchie:"${visitLog.scratchieItem.description}",
 	   	   			correct:" <c:if test='${visitLog.scratchieItem.correct}'> <img src='<html:rewrite page='/includes/images/tick.gif'/>' border='0'> </c:if>",
 	   	   			date:"${visitLog.accessDate}"
 	   	   	    });	
	        </c:forEach>
	        jQuery("#userSummary${userSessionId}").setGridWidth("629");
	};	  	   	  
	masterDetailLoadUp();
	-->	
</script>
   	  
