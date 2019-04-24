<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
		
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet">
		
		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
  	    <script>
  	    	var isEdited = false;
  	    	var previousCellValue = "";
	  	  	$(document).ready(function(){
	  	  		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		  			
	  				jQuery("#session${sessionDto.sessionId}").jqGrid({
	  					datatype: "json",
	  					url: "<c:url value="/monitoring/getUsersByQuestion.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}&questionUid=${questionSummary.question.uid}",
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
		  			   		"<fmt:message key="label.monitoring.user.summary.response" />",
	  						'portraitId'
	  					],
	  						    
	  				   	colModel:[
							{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
							{name:'maxMark', index:'maxMark', width:0, hidden: true},
							{name:'userName',index:'userName', width:120, searchoptions: { clearSearch: false }, formatter:userNameFormatter},
							{name:'grade', index:'grade', width:80, sorttype:"float", search:false, editable:true, editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align' },
	  		  			   	<c:if test="${sessionMap.assessment.enableConfidenceLevels}">
			  			   		{name:'confidence', index:'confidence', width: 80, search:false, classes: 'vertical-align', formatter: gradientNumberFormatter},
			  			  	</c:if>
			  			   	{name:'response', index:'response', width:427, sortable:false, search:false},
		  				   	{name:'portraitId', index:'portraitId', width:0, hidden: true}
	  				   	],
	  				   	multiselect: false,
	  				   	caption: "${sessionDto.sessionName}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
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
  					   	 	initializePortraitPopover('<lams:LAMSURL/>');
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
	  		});  	    	
	  		
    		function refreshSummaryPage()  { 
        		if (isEdited) {
        			self.parent.window.parent.location.href = "<c:url value="/monitoring/summary.do"/>?toolContentID=${sessionMap.toolContentID}&contentFolderID=${sessionMap.contentFolderID}";	
        		} else {
        			self.parent.tb_remove();
        		}
    		}
    		
    		function userNameFormatter (cellvalue, options, rowObject) {
    			<c:choose><c:when test="${assessment.enableConfidenceLevels}">
    			var portraitId = rowObject[6];
    			</c:when><c:otherwise>
    			var portraitId = rowObject[5];
    			</c:otherwise></c:choose>
    			return definePortraitPopover(portraitId, rowObject[0], rowObject[2]);
    		}

  		</script>
	</lams:head>
	
<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.monitoring.question.summary.question" />: <c:out value="${questionSummary.question.title}" escapeXml="true"/>
			</div>
		</div>
			
		<div class="panel-body">
			<lams:errors/>
                
            <c:out value="${questionSummary.question.question}" escapeXml="false"/>


            <div class="row"><div class="col-xs-12 col-sm-6">
            <h5><fmt:message key="label.question.options"/></h5>    
			<table class="table table-condensed table-striped">
				<c:if test="${questionSummary.question.type == 1}">
					<tr>
						<td>
							<fmt:message key="label.incorrect.answer.nullifies.mark" />:
						</td>
						<td style="text-align: right;">
							<c:out value="${questionSummary.question.incorrectAnswerNullifiesMark}" escapeXml="false"/>
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
						<fmt:message key="label.monitoring.question.summary.default.mark" />:
					</td>
					<td style="text-align: right;">
						<c:out value="${questionSummary.question.defaultGrade}" escapeXml="true"/>
					</td>
				</tr>
					
				<tr>
					<td>
						<fmt:message key="label.monitoring.question.summary.penalty" />:
					</td>
					<td style="text-align: right;">
						<c:out value="${questionSummary.question.penaltyFactor}" escapeXml="true"/>
					</td>
				</tr>			
			</table>
            </div></div>
            
            <h5><fmt:message key="label.monitoring.question.summary.history.responses" /></h5>
			
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
				<div class="voffset20">
					<table id="session${sessionDto.sessionId}"></table>
					<div id="pager${sessionDto.sessionId}"></div>
				</div>	
			</c:forEach>
			
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
