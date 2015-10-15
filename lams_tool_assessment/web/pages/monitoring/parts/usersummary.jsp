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
	  	  	$(document).ready(function(){
	  			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
	  				<c:set var="question" value="${userSummaryItem.question}"/>
	  			
	  				jQuery("#user${question.uid}").jqGrid({
	  					datatype: "local",
	  					rowNum: 10000,
	  					height: 'auto',
	  					autowidth: true,
	  					shrinkToFit: false,
	  					
	  				   	colNames:["<fmt:message key="label.monitoring.user.summary.attempt" />",
	  							'questionResultUid',
	  							"<fmt:message key="label.monitoring.user.summary.time" />",
	  							"<fmt:message key="label.monitoring.user.summary.response" />",
	  						    "<fmt:message key="label.monitoring.user.summary.grade" />"],
	  						    
	  				   	colModel:[
	  				   		{name:'id', index:'id', width:52, sorttype:"int"},
	  				   		{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
	  				   		{name:'time', index:'time', width:150, sorttype:'date', datefmt:'Y-m-d'},
	  				   		{name:'response', index:'response', width:341, sortable:false},
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }		
	  				   	],
	  				   	
	  				   	multiselect: false,
	  				   	caption: "${question.titleEscaped}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
	  				  	cellEdit: true,
	  				  	beforeEditCell: function (rowid,name,val,iRow,iCol){
  				  			previousCellValue = val;
  				  		},
	  					beforeSaveCell : function(rowid, name, val, iRow, iCol) {
	  						if (isNaN(val)) {
	  	  						return null;
	  	  					}
	  						
	  						var maxGrade = jQuery("table#user${question.uid} tr#" + iRow 
	  								              + " td[aria-describedby$='_" + name + "']").attr("maxGrade");
	  						if (+val > +maxGrade) {
	  							return maxGrade;
	  						}
	  					},
	  				  	afterSaveCell : function (rowid,name,val,iRow,iCol){
	  				  		//var questionResultUid = jQuery("#user${question.uid}").getCell(rowid, 'questionResultUid');
	  				  		if (isNaN(val)) { //|| (questionResultUid=="")) {
	  				  			jQuery("#user${question.uid}").restoreCell(iRow,iCol); 
	  				  		} else {
	  				  			isEdited = true;
	  				  			var lastAttemptGrade = eval($("#lastAttemptGrade").html()) - eval(previousCellValue) + eval(val);
	  				  			$("#lastAttemptGrade").html(lastAttemptGrade);
	  				  		}		
  						},	  		
	  				  	beforeSubmitCell : function (rowid,name,val,iRow,iCol){
	  				  		if (isNaN(val)) {
	  				  			return {nan:true};
	  				  		} else {
	  							var questionResultUid = jQuery("#user${question.uid}").getCell(rowid, 'questionResultUid');
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
	  						jQuery("#user${question.uid+1}").resetSelection();
	  					}*/ 	  				  	
	  				});
	  				
	  	   	        <c:forEach var="questionResult" items="${userSummaryItem.questionResults}" varStatus="i">
	  	   	        	var responseStr = "";
	  	   	       		<%@ include file="userresponse.jsp"%>
	  	   	     		var table = jQuery("#user${question.uid}");
	  	   	     		table.addRowData(${i.index + 1}, {
	  	   	   	     		id:"${i.index + 1}",
	  	   	   	   			questionResultUid:"${questionResult.uid}",
	  	   	   	   			time:"${questionResult.finishDate}",
	  	   	   	   			response:responseStr,
	  	   	   	   			grade:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
	  	   	   	   	    });
	  	   	     		
	  	   	    		// set maxGrade attribute to cell DOM element
	  	 	 	     	table.setCell(${i.index + 1}, "grade", "", null, {"maxGrade" :  ${questionResult.maxMark}});
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
				<fmt:message key="label.monitoring.user.summary.history.responses" />
			</h1>
			<br><br>		
			<%@ include file="/common/messages.jsp"%>
			
			<table class="forum">
				<tr>
					<th style="width: 180px; border-left: none; padding-top:0px; " >
						<fmt:message key="label.monitoring.user.summary.user.name" />
					</th>
					<td >
						<c:out value="${userSummary.user.lastName}, ${userSummary.user.firstName}" />
					</td>
				</tr>
				
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.user.summary.number.attempts" />
					</th>
					<td>
						${userSummary.numberOfAttempts}
					</td>
				</tr>
					
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.user.summary.time.last.attempt" />
					</th>
					<td>
						<fmt:formatDate value="${userSummary.timeOfLastAttempt}" pattern="H" timeZone="GMT" /> <fmt:message key="label.learning.summary.hours" />
						<fmt:formatDate value="${userSummary.timeOfLastAttempt}" pattern="m" timeZone="GMT" /> <fmt:message key="label.learning.summary.minutes" />
					</td>
				</tr>
					
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.user.summary.last.attempt.grade" />
					</th>
					<td>
						<div id="lastAttemptGrade">${userSummary.lastAttemptGrade}</div>
					</td>
				</tr>
			</table>
			<br><br>
			
			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
				<div style="padding-left: 0px; padding-bottom: 30px; width:99%;">
					<table style="font-size: small; padding-bottom: 5px;">
						<tr>
							<td width="50px;">
								<fmt:message key="label.monitoring.user.summary.title" />
							</td>
							<td>
								 <c:out value="${userSummaryItem.question.title}" escapeXml="true"/>
							</td>
						</tr>					
						<tr>
							<td width="50px;">
								<fmt:message key="label.monitoring.user.summary.question" />
							</td>
							<td>
								<c:out value="${userSummaryItem.question.question}" escapeXml="false"/>
							</td>
						</tr>
					</table>
					
					<table id="user${userSummaryItem.question.uid}" class="scroll" cellpadding="0" cellspacing="0" ></table>
				</div>	
			</c:forEach>	


			<lams:ImgButtonWrapper>
				<a href="#" onclick="refreshSummaryPage();" class="button space-left" style="float:right; margin-right:10px; padding-top:5px;">
					<fmt:message key="label.monitoring.user.summary.ok" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
