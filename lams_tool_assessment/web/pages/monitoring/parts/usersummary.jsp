<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		
		<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jqGrid.grid.css'/>" />
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script>
		<script type="text/javascript"> 
			var pathToJsFolder = "<html:rewrite page='/includes/javascript/'/>"; 
		</script>
	 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.jqGrid.js'/>"></script>

  	    <script>
  	    	<!--
	  	  	$(document).ready(function(){
	  			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
	  				<c:set var="question" value="${userSummaryItem.question}"/>
	  			
	  				jQuery("#user${question.uid}").jqGrid({
	  					datatype: "local",
	  					height: 'auto',
	  					width: 500,
	  					shrinkToFit: true,
	  					
	  				   	colNames:['<fmt:message key="label.monitoring.user.summary.attempt" />',
	  							'questionResultUid',
	  							'<fmt:message key="label.monitoring.user.summary.time" />',
	  							'<fmt:message key="label.monitoring.user.summary.response" />',
	  						    '<fmt:message key="label.monitoring.user.summary.grade" />'],
	  						    
	  				   	colModel:[
	  				   		{name:'id', index:'id', width:55, sorttype:"int"},
	  				   		{name:'questionResultUid', index:'questionResultUid', width:0},
	  				   		{name:'time', index:'time', width:150, sorttype:'date', datefmt:'Y-m-d'},
	  				   		{name:'response', index:'response', width:200, sortable:false},
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4} }		
	  				   	],
	  				   	
	  				   	imgpath:  "<html:rewrite page='/includes/images/'/>" + "jqGrid.basic.theme", 
	  				   	multiselect: false,
	  				   	caption: "${question.title}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
	  				  	cellEdit: true,
	  				  	afterSaveCell : function (rowid,name,val,iRow,iCol){
	  				  		if (isNaN(val) || (questionResultUid=="")) {
	  				  			jQuery("#user${question.uid}").restoreCell(iRow,iCol); 
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
	  				}).hideCol("questionResultUid");
	  				
	  	   	        <c:forEach var="questionResult" items="${userSummaryItem.questionResults}" varStatus="i">
	  	   	        	var responseStr = "";
	  	   	       		<%@ include file="userresponse.jsp"%>
	  	   	     		jQuery("#user${question.uid}").addRowData(${i.index + 1}, {
	  	   	   	     		id:"${i.index + 1}",
	  	   	   	   			questionResultUid:"${questionResult.uid}",
	  	   	   	   			time:"${questionResult.finishDate}",
	  	   	   	   			response:responseStr,
	  	   	   	   			grade:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>
	  		});  	    	

    		function refreshSummaryPage()  { 
    			self.parent.window.parent.location.href = "<c:url value="/monitoring/summary.do"/>?toolContentID=${sessionMap.toolContentID}&contentFolderID=${sessionMap.contentFolderID}";
    		}
  			-->
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		
			<h1>
				<fmt:message key="label.monitoring.user.summary.history.responses" />
			</h1>
			<br><br>		
			<%@ include file="/common/messages.jsp"%>
			
			<table class="forum" style="background:none; border: 1px solid #cacdd1; margin-bottom:60px; padding-top:0px; margin-bottom: 10px;" cellspacing="0">
				<tr>
					<th style="width: 180px; border-left: none; padding-top:0px; " >
						<fmt:message key="label.monitoring.user.summary.user.name" />
					</th>
					<td >
						${userSummary.user.lastName}, ${userSummary.user.firstName}
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
						${userSummary.lastAttemptGrade}
					</td>
				</tr>
			</table>
			<br><br>
			
			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
				<div style="padding-left: 0px; padding-bottom: 30px;">
					<table style="font-size: small; padding-bottom: 5px;">
						<tr>
							<td width="50px;">
								<fmt:message key="label.monitoring.user.summary.title" />
							</td>
							<td>
								 ${userSummaryItem.question.title}
							</td>
						</tr>					
						<tr>
							<td width="50px;">
								<fmt:message key="label.monitoring.user.summary.question" />
							</td>
							<td>
								${userSummaryItem.question.question}
							</td>
						</tr>
					</table>
					
					<table id="user${userSummaryItem.question.uid}" class="scroll" cellpadding="0" cellspacing="0" ></table>
				</div>	
			</c:forEach>	


			<lams:ImgButtonWrapper>
				<a href="#" onclick="refreshSummaryPage();" class="button space-left" style="float:right; margin-right:40px; padding-top:5px;">
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
