<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionDto" value="${questionSummary.questionDto}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css suffix="jquery.jRating"/>
		
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet">
		<style>
			.sortable-on {
				background: lightgoldenrodyellow;
    			min-height: 110px;
    			padding: 10px;
			}
			.filtered {
			    background-color: lightgrey;
			}
			.list-group-item {
				cursor: pointer;
			}
		</style>
		
		<script>
			// pass settings to jquery.jqGrid.confidence-level-formattter.js
			var confidenceLevelsSettings = {
				type: "${assessment.confidenceLevelsType}",
				LABEL_NOT_CONFIDENT : '<fmt:message key="label.not.confident" />',
				LABEL_CONFIDENT : '<fmt:message key="label.confident" />',
				LABEL_VERY_CONFIDENT : '<fmt:message key="label.very.confident" />',
				LABEL_NOT_SURE : '<fmt:message key="label.not.sure" />',
				LABEL_SURE : '<fmt:message key="label.sure" />',
				LABEL_VERY_SURE : '<fmt:message key="label.very.sure" />'
			};
			
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";
			
			//vars for rating.js
			var MAX_RATES = 0,
				MIN_RATES = 0,
				COMMENTS_MIN_WORDS_LIMIT = 0,
				LAMS_URL = '',
				COUNT_RATED_ITEMS = 0,
				COMMENT_TEXTAREA_TIP_LABEL = '',
				WARN_COMMENTS_IS_BLANK_LABEL = '',
				WARN_MIN_NUMBER_WORDS_LABEL = '';
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
  	    <script>
  	    	var isEdited = false;
  	    	var previousCellValue = "";
	  	  	$(document).ready(function(){
	  	  		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		  			
	  				jQuery("#session${sessionDto.sessionId}").jqGrid({
	  					datatype: "json",
	  					url: "<c:url value="/monitoring/getUsersByQuestion.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}&questionUid=${questionDto.uid}",
	  					autoencode:false,
	  					height: 'auto',
	  					autowidth: true,
	  					shrinkToFit: false,
	  				    pager: 'pager${sessionDto.sessionId}',
	  				  	rowList:[10,20,30,40,50,100],
	  				    rowNum:10,
	  				    viewrecords:true,
	  					guiStyle: "bootstrap",
	  					iconSet: 'fontAwesome',
	  				   	colNames:[
		  				   	'questionResultUid',
	  				   	    'maxMark',
	  		  				"<fmt:message key="label.monitoring.summary.user.name" />",
	  		  			    "<fmt:message key="label.monitoring.user.summary.grade" />",
		  			   		<c:if test="${assessment.enableConfidenceLevels}">
		  			   			"<fmt:message key="label.confidence" />",
		  			  		</c:if>
			  			   	<c:if test="${questionDto.groupsAnswersDisclosed}">
		  			   			"<fmt:message key="label.answer.rating.title" />",
		  			  		</c:if>
		  			   		"<fmt:message key="label.monitoring.user.summary.response" />",
		  			   		'userId',
	  						'portraitId'
	  					],
	  						    
	  				   	colModel:[
							{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
							{name:'maxMark', index:'maxMark', width:0, hidden: true},
							{name:'userName',index:'userName', width:120, searchoptions: { clearSearch: false }, formatter : function(cellvalue, options, rowObject) {
				    			return definePortraitPopover(rowObject[rowObject.length - 1], rowObject[rowObject.length - 2], rowObject[2]);
							}},
							{name:'grade', index:'grade', width:80, sorttype:"float", search:false, editable:true, editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align' },
	  		  			   	<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
			  			   		{name:'confidence', index:'confidence', width: 80, search:false, classes: 'vertical-align', formatter: gradientNumberFormatter},
			  			  	</c:if>
				  			<c:if test="${questionDto.groupsAnswersDisclosed}">
				  				{name:'rating', index:'rating', width:120, align:"center", search:false},
		  			  		</c:if>
			  			   	{name:'response', index:'response', width:400, sortable:false, search:false},
			  				{name:'userId', index:'userId', width:0, hidden: true},
		  				   	{name:'portraitId', index:'portraitId', width:0, hidden: true}
	  				   	],
	  				   	multiselect: false,
	  				   	caption: "${sessionDto.sessionName}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
	  				  	cellEdit: true,
	  				  	beforeEditCell: function (rowid,name,val,iRow,iCol){
	  				  		previousCellValue = val;
	  				  	},
	  					beforeSaveCell : function(rowid, name, val, iRow, iCol) {
	  						if (isNaN(val)) {
	  	  						return null;
	  	  					}
	  						
	  						var maxMark = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'maxMark');
	  						
	  						if (+val > +maxMark) {
	  							return maxMark;
	  						}
	  					},
	  				  	afterSaveCell : function (rowid,name,val,iRow,iCol){
	  				  		var questionResultUid = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
	  				  		if (isNaN(val) || (questionResultUid=="")) {
	  				  			jQuery("#session${sessionDto.sessionId}").restoreCell(iRow,iCol); 
	  				  		} else {
	  				  			isEdited = true;
	  				  		}
  						},	  		
	  				  	beforeSubmitCell : function (rowid,name,val,iRow,iCol){
	  				  		if (isNaN(val)) {
	  				  			return {nan:true};
	  				  		} else {
	  							var questionResultUid = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
	  							return {questionResultUid:questionResultUid};		  				  		
	  				  		}
	  					},
  						loadComplete: function () {
  							initializeJRating();
  					   	 	initializePortraitPopover('<lams:LAMSURL/>');
  					    },
  					    subGrid: true,
  						subGridOptions: {
  							reloadOnExpand : false,
						    hasSubgrid: function (options) {
						    	// if there are no ratings for the given answer, there will be no subgrid
						        return options.data.rating != '-';
						    }
  						},
  						subGridRowExpanded: function(subgrid_id, row_id) {
  								var subgridTableId = subgrid_id+"_t";
  								var questionResultUid = jQuery("#session${sessionDto.sessionId}")
  														.getRowData(row_id)["questionResultUid"];
  								   
  								jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");
  								   
  								jQuery("#"+subgridTableId).jqGrid({
  									datatype: "json",
  									loadonce:true,
  									rowNum: 100,
  									url: "<c:url value='/monitoring/getAnswerRatings.do'/>?questionResultUid=" + questionResultUid,
  									height: "100%",
  									autowidth:true,
  									autoencode:false,
  									guiStyle: "bootstrap",
  									iconSet: 'fontAwesome',
  									colNames:[
  										'ratingId',
  				  		  				'<fmt:message key="label.monitoring.summary.user.name" />',
  				  		  				'<fmt:message key="label.comment.date" />',
  				  		  				'<fmt:message key="label.rating" />',
  				  		  				'<fmt:message key="label.comment" />',
  				  		  				'userId',
  				  		  				'portraitId'
  										],
  									colModel:[
  									   {name:'ratingId', index:'ratingId', width:0, hidden:true},
  									   {name:'userName',index:'userName', width:45, formatter : function(cellvalue, options, rowObject) {
  										    // get portrait UUID, user ID and user name
  							    			return definePortraitPopover(rowObject[rowObject.length - 1], rowObject[rowObject.length - 2], rowObject[1]);
  									   }},
  									   {name:'date', index:'date', width:45, align:"center",  title:false},
  									   {name:'rating', index:'rating', width:31, align:"center",  title:false},
  									   {name:'comment', index:'comment', title:false},
  						  			   {name:'userId', index:'userId', width:0, hidden: true},
  									   {name:'portraitId', index:'portraitId', width:0, hidden: true}
  									],
  									loadComplete: function () {
  			  					   	 	initializePortraitPopover('<lams:LAMSURL/>');
  			  					    },
  									loadError: function(xhr,st,err) {
  								    	jQuery("#"+subgridTableId).clearGridData();
  								    }
  								});
  						}				  	
	  				})
	  				<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
		  				.jqGrid('filterToolbar', { 
		  					searchOnEnter: false
		  				})
	  				</c:if>
	  				.navGrid("#pager${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false, refresh:true});		
	  				
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

	  		    //init options sorting feature
	  			$('.sortable-on').each(function() {
	  			    new Sortable($(this)[0], {
	  			    	group: 'shared',
	  				    animation: 150,
	  				  	filter: '.filtered', // 'filtered' class is not draggable
	  				    //ghostClass: 'sortable-placeholder',
	  				    //direction: 'vertical',
	  					onStart: function (evt) {
	  						//stop answers' hover effect, once element dragging started
	  						//$("#option-table").removeClass("hover-active");
	  					},
	  					onEnd: function (evt) {
	  				        $.ajax({
	  				            url: '<c:url value="/monitoring/allocateUserAnswer.do"/>',
	  				            data: {
	  				            	sessionMapID: "${sessionMapID}",
	  				            	questionUid: ${questionDto.uid},
	  				            	targetOptionUid: $(evt.to).data("option-uid"),
	  				            	previousOptionUid: $(evt.from).data("option-uid"),
	  				            	questionResultUid: $(evt.item).data("question-result-uid"),
	  								"<csrf:tokenname/>":"<csrf:tokenvalue/>"
	  						    },
	  				            method: 'post',
	  				          	dataType: "json",
		  				        success: function (data) {
		  				            if (data.isAnswerDuplicated) {
			  				        	alert("<fmt:message key="label.someone.allocated.this.answer" />");
			  				        	$(evt.item).appendTo("#answer-group" + data.optionUid);
			  				        	$(evt.item).addClass("filtered");
				  				    }
		  				        }
	  				            	
	  				       	});
	  					}
	  				});
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
	
<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.monitoring.question.summary.question" />: <c:out value="${questionDto.title}" escapeXml="true"/>
			</div>
		</div>
			
		<div class="panel-body">
			<lams:errors/>
                
            <c:out value="${questionDto.question}" escapeXml="false"/>

            <div class="row"><div class="col-xs-12 col-sm-6">
            <h5><fmt:message key="label.question.options"/></h5>    
			<table class="table table-condensed table-striped">
				<c:if test="${questionDto.type == 1}">
					<tr>
						<td>
							<fmt:message key="label.incorrect.answer.nullifies.mark" />:
						</td>
						<td style="text-align: right;">
							<c:out value="${questionDto.incorrectAnswerNullifiesMark}" escapeXml="false"/>
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
						<fmt:message key="label.monitoring.question.summary.default.mark" />:
					</td>
					<td style="text-align: right;">
						<c:out value="${questionDto.maxMark}" escapeXml="true"/>
					</td>
				</tr>
				<tr>
					<td>
						<fmt:message key="label.monitoring.question.summary.penalty" />:
					</td>
					<td style="text-align: right;">
						<c:out value="${questionDto.penaltyFactor}" escapeXml="true"/>
					</td>
				</tr>			
			</table>
            </div></div>
            
            <!--history responses-->
            <h5><fmt:message key="label.monitoring.question.summary.history.responses" /></h5>
			<c:forEach var="sessionDto" items="${sessionDtos}">
				<div class="voffset20">
					<table id="session${sessionDto.sessionId}"></table>
					<div id="pager${sessionDto.sessionId}"></div>
				</div>	
			</c:forEach>
			
            <!--allocate responses-->
			<c:if test="${questionDto.type == 3}">
				<br><br>
				
				<div class="row">
					<div class="col-sm-4 text-center">
						<c:set var="option0" value="${questionDto.optionDtos.toArray()[0]}"/>
						<h4>
							<c:choose>
								<c:when test="${questionSummary.tbl && option0.maxMark == 1}">
									<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
								</c:when>
								<c:when test="${questionSummary.tbl && option0.maxMark == 0}">
									<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.authoring.basic.option.grade"/>: ${option0.maxMark}
								</c:otherwise>
							</c:choose>
						</h4>
						
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(option0.name, newLineChar, ', ')}
						
						<div class="list-group col sortable-on" data-option-uid="${option0.uid}" id="answer-group${option0.uid}"></div>	
					</div>
					
					<div class="col-sm-4 text-center">
		            	<h4><fmt:message key="label.answer.queue" /></h4>
	            		(<fmt:message key="label.drag.and.drop" />)	
	            		
	            		<div class="list-group col sortable-on" data-option-uid="-1" id="answer-group-1">
		            		<c:forEach var="questionResult" items="${questionSummary.notAllocatedQuestionResults}">
		            			<div class="list-group-item" data-question-result-uid="${questionResult.uid}">
		            				<lams:Portrait userId="${questionResult.assessmentResult.user.userId}"/>&nbsp;
		            				${questionResult.answer}
		            			</div>
		            		</c:forEach>
	            		</div>		
					</div>
					
					<div class="col-sm-4 text-center">
						<c:set var="option1" value="${questionDto.optionDtos.toArray()[1]}"/>
						<h4>
							<c:choose>
								<c:when test="${questionSummary.tbl && option1.maxMark == 1}">
									<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
								</c:when>
								<c:when test="${questionSummary.tbl && option1.maxMark == 0}">
									<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.authoring.basic.option.grade"/>: ${option1.maxMark}
								</c:otherwise>
							</c:choose>
						</h4>
						
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(option1.name, newLineChar, ', ')}
						
						<div class="list-group col sortable-on" data-option-uid="${option1.uid}" id="answer-group${option1.uid}"></div>	
					</div>
				</div>
				
				<c:forEach var="optionDto" items="${questionDto.optionDtos}" begin="2" varStatus="status">
				
					<c:if test="${status.count % 3 == 0}">
						<div class="row">
					</c:if>
				
					<div class="col-sm-4 text-center">
						<h4>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${optionDto.maxMark}
						</h4>
	
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(optionDto.name, newLineChar, ', ')}
						
						<div class="list-group col sortable-on" data-option-uid="${optionDto.uid}" id="answer-group${optionDto.uid}"></div>	
					</div>
					
					<c:if test="${status.count % 3 == 0 || status.last}">
						</div>
					</c:if>
				</c:forEach>
			</c:if>
			
			<a href="#nogo" onclick="refreshSummaryPage();" class="btn btn-default btn-sm voffset10 pull-right">
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
