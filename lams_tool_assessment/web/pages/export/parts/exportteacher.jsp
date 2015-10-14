<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="export.title" /></title>
		<lams:css localLinkPath="../"/>

		<c:set var="lams">	<lams:LAMSURL /></c:set>
		<c:set var="summaryList" value="${sessionMap.summaryList}"/>
		<c:set var="questionSummaryList" value="${sessionMap.questionSummaryList}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		
		<link rel="stylesheet" type="text/css" href="./javascript/css/jquery-ui-redmond-theme.css" />
		<link rel="stylesheet" type="text/css" href="./javascript/css/jquery.jqGrid.css" />
		<link rel="stylesheet" type="text/css" href="./css/assessment.css" />
		<script type="text/javascript" src="./javascript/jquery.js"></script>
		<script type="text/javascript" src="./javascript/jquery.jqGrid.locale-en.js"></script>
	 	<script type="text/javascript" src="./javascript/jquery.jqGrid.js"></script>

  	    <script>
  			<!--	
  	    	var numberOfValues = 0;
	  		$(document).ready(function(){
	  			<c:forEach var="summary" items="${summaryList}" varStatus="status">
	  			
	  				jQuery("#list${summary.sessionId}").jqGrid({
	  					datatype: "local",
	  					rowNum: 10000,
	  					height: 'auto',
	  					width: 630,
	  					shrinkToFit: false,
	  					
	  				   	colNames:['#',
	  							"<fmt:message key="label.monitoring.summary.user.name" />",
	  				   	        <c:forEach var="question" items="${assessment.questions}">
	  				   	     		"${question.titleEscaped}", 
	  				   	        </c:forEach>
	  						    "<fmt:message key="label.monitoring.summary.total" />"],
	  						    
	  				   	colModel:[
	  				   		{name:'id',index:'id', width:20, sorttype:"int"},
	  				   		{name:'userName',index:'userName', width:100},
	  			   	        <c:forEach var="question" items="${assessment.questions}">
	  			   	     		{name:'${question.uid}', index:'${question.uid}', width:60, align:"right", sorttype:"float"},
	  		   	        	</c:forEach>			   				
	  				   		{name:'total',index:'total', width:50,align:"right",sorttype:"float"}		
	  				   	],
	  				   	
	  				   	multiselect: false,
	  				   	caption: "${summary.sessionName}"
	  				});
	  				
	  	   	        <c:forEach var="assessmentResult" items="${summary.assessmentResults}" varStatus="i">
	  	   	     		jQuery("#list${summary.sessionId}").addRowData(${i.index + 1}, {
	  	   	   	     		id:"${i.index + 1}",
	  	   	   	     		userName:"${assessmentResult.user.lastName}, ${assessmentResult.user.firstName}",
	  			   	   	  	<c:choose>
	  			   	   			<c:when test="${not empty assessmentResult.questionResults}">
	  					   	        <c:forEach var="questionResult" items="${assessmentResult.questionResults}">
	  				   	    			${questionResult.assessmentQuestion.uid}:"<fmt:formatNumber value='${questionResult.mark}' maxFractionDigits='3'/>",
	  		   	        			</c:forEach>		
	  			   	   			</c:when>
	  			   	   			<c:otherwise>
	  					   	        <c:forEach var="question" items="${assessment.questions}">
	  					   	     		${question.uid}:"-",
	  			   	        		</c:forEach>		   	   			
	  			   	   			</c:otherwise>
	  			   	   		</c:choose>	
	  	   	   	     		
	  	   	   	     		total:"<fmt:formatNumber value='${assessmentResult.grade}' maxFractionDigits='3'/>"
	  	   	   	   	    });
	  		        </c:forEach>			
	  				
	  			</c:forEach>

	  			<c:forEach var="questionSummary" items="${questionSummaryList}">
	  				<c:forEach var="questionResultsPerSession" items="${questionSummary.questionResultsPerSession}" varStatus="status">
		  				<c:set var="session" value="${questionResultsPerSession[0].user.session}"/>
			  			
		  				jQuery("#session${questionSummary.question.uid}_${session.sessionId}").jqGrid({
		  					datatype: "local",
		  					rowNum: 10000,
		  					height: 'auto',
		  					width: 500,
		  					shrinkToFit: true,
		  					
		  				   	colNames:["<fmt:message key="label.monitoring.summary.user.name" />",
		  							"<fmt:message key="label.monitoring.user.summary.response" />",
		  						    "<fmt:message key="label.monitoring.user.summary.grade" />"],
		  						    
		  				   	colModel:[
								{name:'userName',index:'userName', width:100},
		  				   		{name:'response', index:'response', width:200, sortable:false},
		  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4} }		
		  				   	],
		  				   	
		  				   	multiselect: false,
		  				   	caption: "<fmt:message key='label.monitoring.question.summary.group' /> ${session.sessionName}"
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
		
		  	   	     		jQuery("#session${questionSummary.question.uid}_${session.sessionId}").addRowData(${i.index + 1}, {
		  	   	     			userName:"${questionResult.user.lastName}, ${questionResult.user.firstName}",
		  	   	   	   			response:responseStr,
		  	   	   	   			grade:grade
		  	   	   	   	    });
		  		        </c:forEach>			
		  				
		  			</c:forEach>

				</c:forEach>	
	  		});
	  		-->		
  		</script>
		
		
	</lams:head>
	
	<body class="stripes" >
		<div id="content" >
		
			<h1><c:out value="${assessment.title}" escapeXml="true"/> </h1>
			
			<h2><c:out value="${assessment.instructions}" escapeXml="false"/> </h2>
			
			<br>		
			<h3><fmt:message key="label.export.portfolio.activity.summary" /></h3>
			<br>
			
			<c:forEach var="summary" items="${summaryList}">
				<div style="padding-left: 30px; padding-bottom: 30px;">
					<div style="padding-bottom: 5px; font-size: small;">
						<B><fmt:message key="monitoring.label.group" /></B> ${summary.sessionName}
					</div>
						
					<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
				</div>	
			</c:forEach>			

			<h3><fmt:message key="label.export.portfolio.question.summaries" /></h3>
			<br>
			
			<div style="margin-left: 30px;">
			<c:forEach var="questionSummary" items="${questionSummaryList}">
				<table class="forum" style="width: 640px;">
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
							${questionSummary.question.defaultGrade}
						</td>
					</tr>
						
					<tr>
						<th style="width: 180px;" >
							<fmt:message key="label.monitoring.question.summary.penalty" />
						</th>
						<td>
							${questionSummary.question.penaltyFactor}
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
					<div style="padding-left: 0px; padding-bottom: 30px;">
						<table id="session${questionSummary.question.uid}_${session.sessionId}" class="scroll" cellpadding="0" cellspacing="0" ></table>
					</div>	
				</c:forEach>	
				<br><br><br><br><br>
			</c:forEach>
			</div>
		
		<%-- Display reflection entries --%>
		<c:if test="${assessment.reflectOnActivity}">
			<h3>
				<fmt:message key="label.export.reflection" />
			</h3>
			
			<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
			
				<h4>
					<c:out value="${reflectDTO.fullName}" escapeXml="true" />
				</h4>
				
				<p>
					<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
				</p>
				
			</c:forEach>
		</c:if>
	
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
	
