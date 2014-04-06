<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="export.title" /></title>
		<lams:css localLinkPath="../"/>

		<c:set var="lams">	<lams:LAMSURL /></c:set>
		<c:set var="userSummary" value="${sessionMap.userSummary}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		
		<link rel="stylesheet" type="text/css" href="./javascript/css/jquery-ui-redmond-theme.css" />
		<link rel="stylesheet" type="text/css" href="./javascript/css/jquery.jqGrid.css" />
		<script type="text/javascript" src="./javascript/jquery.js"></script>
		<script type="text/javascript" src="./javascript/jquery.jqGrid.locale-en.js"></script>
	 	<script type="text/javascript" src="./javascript/jquery.jqGrid.js"></script>

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
	  					
	  				   	colNames:["<fmt:message key="label.monitoring.user.summary.attempt" />",
	  							"<fmt:message key="label.monitoring.user.summary.time" />",
	  							"<fmt:message key="label.monitoring.user.summary.response" />",
	  						    "<fmt:message key="label.monitoring.user.summary.grade" />"],
	  						    
	  				   	colModel:[
	  				   		{name:'id', index:'id', width:55, sorttype:"int"},
	  				   		{name:'time', index:'time', width:150, sorttype:'date', datefmt:'Y-m-d'},
	  				   		{name:'response', index:'response', width:200, sortable:false},
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4} }		
	  				   	],

	  				   	multiselect: false,
	  				   	caption: "${question.titleEscaped}"
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
	  	   	     		jQuery("#user${question.uid}").addRowData(${i.index + 1}, {
	  	   	   	     		id:"${i.index + 1}",
	  	   	   	   			time:"${questionResult.finishDate}",
	  	   	   	   			response:responseStr,
	  	   	   	   			grade:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>"
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>
	  		});  	
  			-->
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" >
		<div id="content" >
		
			<h1><c:out value="${assessment.title}" escapeXml="true"/> </h1>
			
			<h2><c:out value="${assessment.instructions}" escapeXml="false"/></h2>
			
			<br><br>		
			
			<table class="forum" style="background:none; border: 1px solid #cacdd1; margin-bottom:60px; padding-top:0px; margin-bottom: 10px;" cellspacing="0">
				<tr>
					<th style="width: 180px; border-left: none; padding-top:0px; " >
						<fmt:message key="label.monitoring.user.summary.user.name" />
					</th>
					<td >
						<c:out value="${userSummary.user.lastName}, ${userSummary.user.firstName}" escapeXml="true"/>
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
								 <c:out value="${userSummaryItem.question.title}" />
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
			
			<%-- Display reflection entries --%>
		<c:if test="${assessment.reflectOnActivity}">
			<h3>
				<fmt:message key="label.export.reflection" />
			</h3>
			
			<p>
				<lams:out value="${sessionMap.reflectEntry.reflect}" escapeHtml="true" />
			</p>
		</c:if>	

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
	
