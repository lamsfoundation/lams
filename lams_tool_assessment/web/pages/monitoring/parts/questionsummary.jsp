<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		
		<link type="text/css" href="<lams:LAMSURL />css/jquery-ui-redmond-theme.css" rel="stylesheet">
		<link type="text/css" href="<lams:LAMSURL />css/jquery.jqGrid.css" rel="stylesheet" />
		<style media="screen,projection" type="text/css">
			.ui-jqgrid tr.jqgrow td {
			    white-space: normal !important;
			    height:auto;
			    vertical-align:text-top;
			    padding-top:2px;
			}
		</style>
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.locale-en.js"></script>
	 	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.js"></script>
  	    <script>
  	    	var isEdited = false;
  	    	var previousCellValue = "";  	    	
  	    	var numberOfValues = 0;
	  	  	$(document).ready(function(){
	  			<c:forEach var="questionResultsPerSession" items="${questionSummary.questionResultsPerSession}" varStatus="status">
	  				<c:set var="session" value="${questionResultsPerSession[0].user.session}"/>
		  			
	  				jQuery("#session${session.sessionId}").jqGrid({
	  					datatype: "local",
	  					height: 'auto',
	  					autowidth: true,
	  					shrinkToFit: false,
	  					
	  				   	colNames:['questionResultUid',
	  		  				   	"<fmt:message key="label.monitoring.summary.user.name" />",
	  							"<fmt:message key="label.monitoring.user.summary.response" />",
	  						    "<fmt:message key="label.monitoring.user.summary.grade" />"],
	  						    
	  				   	colModel:[
							{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
							{name:'userName',index:'userName', width:120},
	  				   		{name:'response', index:'response', width:427, sortable:false},
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }		
	  				   	],
	  				   	
	  				   	multiselect: false,
	  				   	caption: "${session.sessionName}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
	  				  	cellEdit: true,
	  				  	beforeEditCell: function (rowid,name,val,iRow,iCol){
	  				  		previousCellValue = val;
	  				  	},
	  				  	afterSaveCell : function (rowid,name,val,iRow,iCol){
	  				  		var questionResultUid = jQuery("#session${session.sessionId}").getCell(rowid, 'questionResultUid');
	  				  		if (isNaN(val) || (questionResultUid=="")) {
	  				  			jQuery("#session${session.sessionId}").restoreCell(iRow,iCol); 
	  				  		} else {
	  				  			isEdited = true;
	  				  			var averageMark = (eval($("#averageMark").html())*numberOfValues - eval(previousCellValue) + eval(val))/numberOfValues;
	  				  			$("#averageMark").html(averageMark);
	  				  		}	
  						},	  		
	  				  	beforeSubmitCell : function (rowid,name,val,iRow,iCol){
	  				  		if (isNaN(val)) {
	  				  			return {nan:true};
	  				  		} else {
	  							var questionResultUid = jQuery("#session${session.sessionId}").getCell(rowid, 'questionResultUid');
	  							return {questionResultUid:questionResultUid};		  				  		
	  				  		}
	  					}
  						/*  resetSelection() doesn't work in this version
						    hope it'll be fixed in the next one
						    
	  					,
	  					onSelectRow: function (rowid){
	  						$("[id^='user']").resetSelection();
	  					},
	  					onCellSelect: function (rowid, iCol, cellcontent){
	  						jQuery("#session${session.sessionId}}").resetSelection();
	  					}*/ 	  				  	
	  				});
	  				
	  	   	        <c:forEach var="questionResult" items="${questionResultsPerSession}" varStatus="i">
			   	   	  	<c:choose>
			   	   			<c:when test="${questionResult.uid != null}">
			  	   	        	var responseStr = "";
								numberOfValues++;
			  	   	       		<c:set var="question" value="${questionResult.assessmentQuestion}"/>			  	   	        	
			  	   	       		<%@ include file="userresponse.jsp"%>	
			  	   	       		var grade = "<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>";	
			   	   			</c:when>
			   	   			<c:otherwise>
			  	   	        	var responseStr = "-";
			  	   	       		var grade = "-";	
			   	   			</c:otherwise>
		   	   			</c:choose>		  	   	        

	  	   	     		jQuery("#session${session.sessionId}").addRowData(${i.index + 1}, {
	  	   	     			questionResultUid:"${questionResult.uid}",
	  	   	     			userName:"${questionResult.user.lastName}, ${questionResult.user.firstName}",
	  	   	   	   			response:responseStr,
	  	   	   	   			grade:grade
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>

  				//jqgrid autowidth (http://stackoverflow.com/a/1610197)
	  			$(window).bind('resize', function() {
	  				var grid;
	  		        if (grid = jQuery(".ui-jqgrid-btable:visible")) {
	  		            grid.each(function(index) {
	  		                var gridId = $(this).attr('id');
	  		                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	  		                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	  		            });
	  		        }
	  			});
	  		});  	    	
	  		
    		function refreshSummaryPage()  { 
        		if (isEdited) {
        			self.parent.window.parent.location.href = "<c:url value="/monitoring/summary.do"/>?toolContentID=${sessionMap.toolContentID}&contentFolderID=${sessionMap.contentFolderID}";	
        		} else {
        			self.parent.tb_remove();
        		}
    		}
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.monitoring.question.summary.history.responses" />
			</h1>
			<br><br>		
			<%@ include file="/common/messages.jsp"%>
			
			<table class="forum" style="background:none; border: 1px solid #cacdd1; margin-bottom:60px; padding-top:0px; margin-bottom: 10px;" cellspacing="0">
				<tr>
					<th style="width: 180px; border-left: none; padding-top:0px; " >
						<fmt:message key="label.monitoring.question.summary.title" />
					</th>
					<td >
						<c:out value="${questionSummary.question.title}" escapeXml="true"/>
					</td>
				</tr>
				
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.question.summary.question" />
					</th>
					<td>
						<c:out value="${questionSummary.question.question}" escapeXml="false"/>
					</td>
				</tr>
					
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.question.summary.default.mark" />
					</th>
					<td>
						<c:out value="${questionSummary.question.defaultGrade}" escapeXml="true"/>
					</td>
				</tr>
					
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.question.summary.penalty" />
					</th>
					<td>
						<c:out value="${questionSummary.question.penaltyFactor}" escapeXml="true"/>
					</td>
				</tr>
				
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.question.summary.average.mark" />
					</th>
					<td>
						<div id="averageMark">${questionSummary.averageMark}</div>
					</td>
				</tr>				
			</table>
			<br><br>
			
			<c:forEach var="questionResultsPerSession" items="${questionSummary.questionResultsPerSession}" varStatus="status">
				<c:set var="session" value="${questionResultsPerSession[0].user.session}"/>
				<div style="padding-left: 0px; padding-bottom: 30px; width:99%;">
					<div style="font-size: small; padding-bottom: 5px;">
						<fmt:message key="label.monitoring.question.summary.group" /> ${session.sessionName}
					</div>
					
					<table id="session${session.sessionId}" class="scroll" cellpadding="0" cellspacing="0" ></table>
				</div>	
			</c:forEach>	


			<lams:ImgButtonWrapper>
				<a href="#" onclick="refreshSummaryPage();" class="button space-left" style="float:right; margin-right:10px; padding-top:5px;">
					<fmt:message key="label.monitoring.question.summary.ok" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
