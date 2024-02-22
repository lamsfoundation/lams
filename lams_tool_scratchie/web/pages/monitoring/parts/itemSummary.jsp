<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<c:set var="title">
	<fmt:message key="label.monitoring.summary.report.for" >
		<fmt:param>${fn:escapeXml(item.qbQuestion.name)}</fmt:param>
	</fmt:message>
</c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<link href="<lams:WebAppURL/>includes/css/assessment.css" rel="stylesheet" type="text/css">
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
	<link href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		/* remove jqGrid borders */
		.ui-jqgrid {
			border-left-style: none !important;
			border-right-style: none !important;
			border-bottom-style: none !important;
		}
		
		.ui-jqgrid tr {
			border-left-style: none !important;
		}
		
		.ui-jqgrid td {
			border-style: none !important;
		}
		
		/* remove jqGrid border radius */
		.ui-jqgrid.ui-jqgrid-bootstrap {
			border-radius: 0;
			-moz-border-radius: 0;
			-webkit-border-radius: 0;
			-khtml-border-radius: 0;
		}
	</style>

 	<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
  	<script>
		$(document).ready(function(){
	  			<c:forEach var="summary" items="${summaryList}" varStatus="status">
	  				jQuery("#session${summary.sessionId}").jqGrid({
	  					datatype: "local",
	  					autoencode:false,
	  					rowNum: 10000,
	  					height: 'auto',
	  					width: '100%',
	  					guiStyle: "bootstrap",
	  					iconSet: 'fontAwesome',
	  				   	colNames:[
		  				   	"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.answer' /></spring:escapeBody>"
		  			  	   	<c:forEach var="i" begin="0" end="${summary.numberColumns-1}">
		  			  	   		,"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.choice' /></spring:escapeBody>&nbsp;${i+1}"
					  		</c:forEach>
	  					],
	  				   	colModel:[
							{name:'option',index:'option', width:180}
  			  	   	        <c:forEach var="i" begin="0" end="${summary.numberColumns-1}">
			  	   	 			,{name:'choice${i}', index:'${i}choice', align:"center", sorttype:"int"}
		  		        	</c:forEach>		
	  				   	],
	  				   	multiselect: false
	  				});
	  				
	  	   	        <c:forEach var="optionDto" items="${summary.optionDtos}" varStatus="i">
	  	   	     		jQuery("#session${summary.sessionId}").addRowData(${i.index + 1}, {
	  	   	     			option:"<c:out value='${optionDto.answer}' escapeXml='${!optionDto.mcqType}' /><c:if test='${optionDto.correct}'> (<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.item.summary.correct' /></spring:escapeBody>)</c:if>"
	  	   	     			<c:forEach var="j" begin="0" end="${summary.numberColumns-1}">
	  	   	     				,choice${j}:"${optionDto.attempts[j]}"
	  	   	     			</c:forEach>
	  	   	   	   	    });
	  		        </c:forEach>			
	  			</c:forEach>
	  			
	  	        //jqgrid autowidth
	  	        $(window).bind('resize', function() {
	  	            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
	  	        });
	  	        function resizeJqgrid(jqgrids) {
	  	            jqgrids.each(function(index) {
	  	                var gridId = $(this).attr('id');
	  	                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	  	                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	  	            });
	  	        };
	  	        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	  	});  	

    	function refreshSummaryPage()  {
        	self.parent.tb_remove();
    	}
  	</script>
  		
	<h1 class="fs-3">
		${title}
	</h1>
		
	<div class="instructions">
		<c:out value="${item.qbQuestion.description}" escapeXml="false"/>
	</div>
			
	<c:forEach var="summary" items="${summaryList}" varStatus="status">
		<div class="lcard" >
	       	<div class="card-header">
				<fmt:message key="label.monitoring.item.summary.group" />&nbsp;${summary.sessionName}
			</div>
				
			<table id="session${summary.sessionId}" class="scroll"></table>
		</div>
	</c:forEach>	

	<div class="activity-bottom-buttons">
		<button type="button" onclick="refreshSummaryPage()" class="btn btn-primary ms-2">
			<i class="fa fa-close"></i>
			<fmt:message key="button.close" />
		</button>
	</div>
</lams:PageMonitor>
