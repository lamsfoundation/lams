<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>		
		
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet">
		<c:if test="${not empty codeStyles}">
			<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
		</c:if>
	
		<style>
			[data-toggle="collapse"].collapsed .if-not-collapsed, [data-toggle="collapse"]:not(.collapsed) .if-collapsed {
	  			display: none;
	  		}
	  		
	  		pre {
				background-color: initial;
				border: none;
			}	
			
			.requires-grading {
				background-color: rgba(255, 195, 55, .6);
			}
		</style>
		
		<script>
			// pass settings to jquery.jqGrid.confidence-level-formattter.js
			var confidenceLevelsSettings = {
				type: "${assessment.confidenceLevelsType}",
				LABEL_NOT_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.confident" /></spring:escapeBody>',
				LABEL_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.confident" /></spring:escapeBody>',
				LABEL_VERY_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.confident" /></spring:escapeBody>',
				LABEL_NOT_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.sure" /></spring:escapeBody>',
				LABEL_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.sure" /></spring:escapeBody>',
				LABEL_VERY_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.sure" /></spring:escapeBody>'
			};
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
	 	
	 	<c:if test="${not empty codeStyles}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/runmode-standalone.js"></script>
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/colorize.js"></script>
		</c:if>
		<%-- codeStyles is a set, so each code style will be listed only once --%>
		<c:forEach items="${codeStyles}" var="codeStyle">
			<c:choose>
				<c:when test="${codeStyle == 1}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
				</c:when>
				<c:when test="${codeStyle == 2}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
				</c:when>
				<c:when test="${codeStyle >= 3}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
				</c:when>
			</c:choose>
		</c:forEach>
		
  	    <script>
	    	var isEdited = false;
  	    	var previousCellValue = "";  	    	
	  	  	$(document).ready(function(){
	  			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
	  				<c:set var="question" value="${userSummaryItem.questionDto}"/>
	  			
	  				jQuery("#user${question.uid}").jqGrid({
	  					datatype: "local",
	  					autoencode:false,
	  					rowNum: 10000,
	  					height: 'auto',
	  					autowidth: true,
	  					shrinkToFit: false,
	  					guiStyle: "bootstrap",
	  					iconSet: 'fontAwesome',
	  				   	colNames:[
		  				   	"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.attempt" /></spring:escapeBody>",
	  						'questionResultUid',
	  						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.time" /></spring:escapeBody>",
	  						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.response" /></spring:escapeBody>",
	  						<c:if test="${assessment.enableConfidenceLevels and question.type != 8}">
	  							"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.confidence' /></spring:escapeBody>",
	  						</c:if>
	  						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.grade" /></spring:escapeBody>",
	  	  					"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.marker" /></spring:escapeBody>",
	  	  					"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.marker.comment" /></spring:escapeBody>"
	  					],  
	  				   	colModel:[
	  				   		{name:'id', index:'id', width:52, sorttype:"int"},
	  				   		{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
	  				   		{name:'time', index:'time', width:150, sorttype:'date', datefmt:'Y-m-d'},
	  				   		{name:'response', index:'response', width:341, sortable:false},
	  		  			   	<c:if test="${sessionMap.assessment.enableConfidenceLevels and question.type != 8}">
			  			   		{name:'confidence', index:'confidence', width: 80, classes: 'vertical-align', formatter: gradientNumberFormatter},
			  			  	</c:if>
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, 
		  				   		editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align', title : false },	
	  			  			{name:'marker', index:'marker', width: 80, title: false},
	  			  			{name:'markerComment', index:'markerComment', width:120, editable:true, sortable: false,
	  			  			    editoptions: {maxlength: 100}, align:"left", classes: 'vertical-align', title : false }	
	  				   	],
	  				   	multiselect: false,
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
	  				  	cellEdit: true,
	  	  				formatCell: function(rowid, name, value, iRow, iCol){
	  	  	  				if (name != "grade") {
	  	  	  	  				return value;
	  	  	  	  			}
	  	  	  				if (value == "-") {
	  	  	  	  				value = "0";
	  	  	  	  			}
	  	  	  	  			return value;
	  	  	  			},
	  				  	beforeEditCell: function (rowid,name,val,iRow,iCol){
	  	  	  				if (name != "grade") {
	  	  	  	  				return;
	  	  	  	  			}
  				  			previousCellValue = val;
  				  		},
	  					beforeSaveCell : function(rowid, name, val, iRow, iCol) {
	  						if (name != "grade") {
	  	  	  	  				return val;
	  	  	  	  			}
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
	  	  					if (name != "grade") {
	  	  	  	  				return;
	  	  	  	  			}
	  				  		if (isNaN(val)) { //|| (questionResultUid=="")) {
	  				  			jQuery("#user${question.uid}").restoreCell(iRow,iCol); 
	  				  		} else {
	  				  			isEdited = true;
	  				  			var lastAttemptGrade = eval($("#lastAttemptGrade").html()) - eval(previousCellValue) + eval(val);
	  				  			$("#lastAttemptGrade").html(lastAttemptGrade);
	  				  		}		
  						},	  		
	  				  	beforeSubmitCell : function (rowid,name,val,iRow,iCol){
		  				  	if (name == "grade" && isNaN(val)) {
	  	  						return {nan:true};
	  	  					} else {
	  	  						var questionResultUid = jQuery("#user${question.uid}").getCell(rowid, 'questionResultUid');
	  	  						return {
	  	  	  							questionResultUid:questionResultUid,
	  	  	  						    column:name
	  	  	  						   };		  				  		
	  	  				  	}
	  					},
	  	  				afterSubmitCell : function (serverresponse, rowid, name, value, iRow, iCol) {
	  	  	  				if (serverresponse.statusText == "OK") {
	  	  	  	  				if (serverresponse.responseText != "") {
	  	  	  	  					$(this).setCell(rowid, 'marker', serverresponse.responseText, {}, {});
	  	  	  	  				}
	  	  	  					return [true, ""];
	  	  	  				}
	  	  	  			}
	  				});
	  				
	  	   	        <c:forEach var="questionResult" items="${userSummaryItem.questionResults}" varStatus="i">
	  	   	        	<c:set var="learnerInteraction" value="${learnerInteractions[questionResult.qbToolQuestion.uid]}" />
	  	   	       		<c:set var="requiresMarking"
	 	 	   	   	   		value="${empty questionResult.markedBy and questionResult.questionDto.type eq 6 and questionResult.mark eq 0}" />
		 	 	   	   	   
	  	   	        	var responseStr = "";
	  	   	       		<%@ include file="userresponse.jsp"%>
	  	   	     		var table = jQuery("#user${question.uid}");
	  	   	     		table.addRowData(${i.index + 1}, {
	  	   	   	     		id:"${i.index + 1}",
	  	   	   	   			questionResultUid:"${questionResult.uid}",
	  	   	   	   			time:"${empty learnerInteraction ? questionResult.finishDate : learnerInteraction.formattedDate}",
	  	   	   	   			response:responseStr,
		  	   	   	   		<c:if test="${assessment.enableConfidenceLevels}">
		  	 	   	   			confidence:"${questionResult.confidenceLevel}",
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
	  			setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	  			
				// show etherpads only on Discussion expand
				$('.question-etherpad-collapse').on('show.bs.collapse', function(){
					var etherpad = $('.etherpad-container', this);
					if (!etherpad.hasClass('initialised')) {
						var id = etherpad.attr('id'),
							groupId = id.substring('etherpad-container-'.length);
						etherpadInitMethods[groupId]();
					}
				});
				
				if (typeof CodeMirror != 'undefined') {
					CodeMirror.colorize($('.code-style'));
				}
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
	
<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.monitoring.user.summary.history.responses" />
			</div>
		</div>
			
		<div class="panel-body">	
			<lams:errors/>
			
			<table class="table table-condensed">
				<tr>
					<th style="width: 180px;" >
						<fmt:message key="label.monitoring.user.summary.user.name" />
					</th>
					<td >
						<c:out value="${userSummary.user.lastName}, ${userSummary.user.firstName}" />
					</td>
				</tr>
				
				<tr>
					<th>
						<fmt:message key="label.monitoring.user.summary.number.attempts" />
					</th>
					<td>
						${userSummary.numberOfAttempts}
					</td>
				</tr>
					
				<tr>
					<th>
						<fmt:message key="label.monitoring.user.summary.time.last.attempt" />
					</th>
					<td>
						<fmt:formatDate value="${userSummary.timeOfLastAttempt}" pattern="H" timeZone="GMT" /> <fmt:message key="label.learning.summary.hours" />
						<fmt:formatDate value="${userSummary.timeOfLastAttempt}" pattern="m" timeZone="GMT" /> <fmt:message key="label.learning.summary.minutes" />
					</td>
				</tr>
					
				<tr>
					<th>
						<fmt:message key="label.monitoring.user.summary.last.attempt.grade" />
					</th>
					<td>
						<div id="lastAttemptGrade">${userSummary.lastAttemptGrade}</div>
					</td>
				</tr>
			</table>
			
			<c:forEach var="userSummaryItem" items="${userSummary.userSummaryItems}" varStatus="status">
				<div class="voffset20">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="panel-title">
					<table style="font-size: small;">
						<tr>
							<td width="70px;">
								<fmt:message key="label.monitoring.user.summary.title" />
							</td>
							<td>
								 <c:out value="${userSummaryItem.questionDto.title}" escapeXml="true"/>
							</td>
						</tr>					
						<tr>
							<td>
								<fmt:message key="label.monitoring.user.summary.question" />
							</td>
							<td>
								<c:out value="${userSummaryItem.questionDto.question}" escapeXml="false"/>
							</td>
						</tr>
					</table>
							</div>
						</div>
				

					
					<table id="user${userSummaryItem.questionDto.uid}"></table>
					</div>
				</div>
				
				<%--Display Etherpad for each question --%>
				<c:if test="${isQuestionEtherpadEnabled}">
					<div class="form-group question-etherpad-container">
						<a data-toggle="collapse" data-target="#question-etherpad-${userSummaryItem.questionDto.uid}"
							href="#qe${userSummaryItem.questionDto.uid}" class="collapsed">
							<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
				 				<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
							<fmt:message key="label.etherpad.discussion" />
						</a>
						
						<div id="question-etherpad-${userSummaryItem.questionDto.uid}" class="question-etherpad-collapse collapse">
							<div class="panel panel-default question-etherpad">
								<lams:Etherpad groupId="etherpad-assessment-${toolSessionID}-question-${userSummaryItem.questionDto.uid}" 
								   showControls="true" showChat="false" heightAutoGrow="true" showOnDemand="true" />
							</div>
						</div>
					</div>
				</c:if>
			</c:forEach>

			<a href="#nogo" onclick="refreshSummaryPage();" class="btn btn-default btn-sm voffset20 pull-right">
				<fmt:message key="label.close" /> 
			</a>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		

	</div>		
</body>
</lams:html>
