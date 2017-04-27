<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:set var="assessment" value="${sessionMap.assessment}"/>
		<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
		
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
		<%-- *LKC* added the following line --%>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.locale-en.js"></script>
	 	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.jqGrid.js"></script>
  	    <script>
  	    	var isEdited = false;
  	    	var previousCellValue = "";
	  	  	$(document).ready(function(){
	  	  		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		  			
	  				jQuery("#session${sessionDto.sessionId}").jqGrid({
	  					datatype: "json",
	  					url: "<c:url value="/monitoring/getUsersByQuestion.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}&questionUid=${questionSummary.question.uid}",
	  					height: 'auto',
	  					autowidth: true,
	  					shrinkToFit: false,
	  				    pager: 'pager${sessionDto.sessionId}',
	  				  	rowList:[10,20,30,40,50,100],
	  				    rowNum:10,
	  				    viewrecords:true,
	  				   	colNames:['questionResultUid',
	  				   	     	'maxMark',
	  		  				   	"<fmt:message key="label.monitoring.summary.user.name" />",
	  							"<fmt:message key="label.monitoring.user.summary.response" />",
	  						    "<fmt:message key="label.monitoring.user.summary.grade" />"],
	  						    
	  				   	colModel:[
							{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
							{name:'maxMark', index:'maxMark', width:0, hidden: true},
							<%-- *LKC* modified the following line --%>
							{name:'userName',index:'userName', width:120, searchoptions: { clearSearch: false }, hidden: ${questionSummary.question.type == 6}},
	  				   		{name:'response', index:'response', width:427, sortable:false, search:false},
	  				   		{name:'grade', index:'grade', width:80, sorttype:"float", search:false, editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }		
	  				   	],
	  				   	multiselect: false,
	  				   	caption: "${sessionDto.sessionName}",
	  				  	cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
	  				  	cellEdit: true,
	  				  	beforeEditCell: function (rowid,name,val,iRow,iCol){
	  				  		previousCellValue = val;
	  				  	},
	  				  	<%-- *LKC* added the following 3 lines --%>
	  				  	loadComplete : function (data){
	  				  		$(".ui-jqgrid-titlebar").hide();
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
	  					}
  						/*  resetSelection() doesn't work in this version
						    hope it'll be fixed in the next one
						    
	  					,
	  					onSelectRow: function (rowid){
	  						$("[id^='user']").resetSelection();
	  					},
	  					onCellSelect: function (rowid, iCol, cellcontent){
	  						jQuery("#session${sessionDto.sessionId}}").resetSelection();
	  					}*/ 	  				  	
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
  				
  				<%-- *LKC* added the following paragraph --%>
	  			$("#display-name-column-switcher").button();
  				$("#display-name-column-switcher").click(function() {
  					if ($("#display-name-column-switcher").is(':checked')) {
  						$("table[id^=session]").showCol("userName");
  					} else {
  						$("table[id^=session]").hideCol("userName");
  					}
  					$("table[id^=session]").trigger("resize");
  					
  					$(".ui-jqgrid-titlebar").toggle();
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
		<div id="content" >
		
			<h1>
				XX<fmt:message key="label.monitoring.question.summary.history.responses" />
			</h1>
			<br><br>		
			<%@ include file="/common/messages.jsp"%>
			
			<table class="forum">
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
				
				<c:if test="${questionSummary.question.type == 1}">
					<tr>
						<th style="width: 180px;" >
							<fmt:message key="label.incorrect.answer.nullifies.mark" />
						</th>
						<td>
							<c:out value="${questionSummary.question.incorrectAnswerNullifiesMark}" escapeXml="false"/>
						</td>
					</tr>
				</c:if>
					
				<%-- *LKC* commented out the following paragraph --%>
				<!--	
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
-->
			</table>
			<br><br>
			
			<%-- *LKC* added the following paragraph --%>
			<c:if test="${questionSummary.question.type == 6}">
				<p class="float-right" style="margin-top: 0px;">
					<input type="checkbox" id="display-name-column-switcher" />
					<label for="display-name-column-switcher">
						<fmt:message key="label.display.name.column" />
					</label>
				</p>
				<br><br><br>
			</c:if>
			
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
				<div style="padding-left: 0px; padding-bottom: 30px; width:99%;">
					<div style="font-size: small; padding-bottom: 5px;">
						<fmt:message key="label.monitoring.question.summary.group" /> ${sessionDto.sessionName}
					</div>
					
					<table id="session${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0" ></table>
					<div id="pager${sessionDto.sessionId}" class="scroll"></div>
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
