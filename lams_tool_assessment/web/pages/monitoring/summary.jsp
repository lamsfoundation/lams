<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<script type="text/javascript">
	$(document).ready(function(){
		
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			jQuery("#list${sessionDto.sessionId}").jqGrid({
			   	multiselect: false,
				datatype: "json",
				url: "<c:url value="/monitoring/getUsers.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}",
				height: '100%',
				autowidth: true,
				shrinkToFit: false,
			    pager: 'listPager${sessionDto.sessionId}',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    viewrecords:true,
			   	colNames:['userId',
						'sessionId',
						"<fmt:message key="label.monitoring.summary.user.name" />",
					    "<fmt:message key="label.monitoring.summary.total" />"],
					    
			   	colModel:[
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, searchoptions: { clearSearch: false }},
			   		{name:'total', index:'total', width:174, align:"right", formatter:'number', search:false}		
			   	],
			   	ondblClickRow: function(rowid) {
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userSummaryUrl = '<c:url value="/monitoring/userSummary.do?sessionMapID=${sessionMapID}"/>';
					var newUserSummaryHref = userSummaryUrl + "&userID=" + userId + "&sessionId=" + sessionId + "&KeepThis=true&TB_iframe=true&modal=true";
					$("#userSummaryHref").attr("href", newUserSummaryHref);	
					$("#userSummaryHref").click(); 		
			  	},
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userMasterDetailUrl = '<c:url value="/monitoring/userMasterDetail.do"/>';
		  	        jQuery("#userSummary${sessionDto.sessionId}").clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
		  	        $("#masterDetailArea").load(
		  	        	userMasterDetailUrl,
		  	        	{
		  	        		userID: userId,
		  	        		sessionId: sessionId
		  	       		}
		  	       	);    
	  	  		},
			    loadError: function(xhr,st,err) {
			    	jQuery("#list${sessionDto.sessionId}").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    }

			})
			<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
				.jqGrid('filterToolbar', { 
					searchOnEnter: false
				})
			</c:if>
			.navGrid("#listPager${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        
	        var oldValue = 0;
			jQuery("#userSummary${sessionDto.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				gridstate:"hidden",
				//hiddengrid:true,
				height: 180,
				autowidth: true,
				shrinkToFit: false,
				caption: "<fmt:message key="label.monitoring.summary.learner.summary" />",
			   	colNames:['#',
						'questionResultUid',
  						'Question',
  						"<fmt:message key="label.monitoring.user.summary.response" />",
  						"<fmt:message key="label.authoring.basic.list.header.mark" />"],
					    
			   	colModel:[
	  			   		{name:'id', index:'id', width:20, sorttype:"int"},
	  			   		{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
	  			   		{name:'title', index:'title', width: 200},
	  			   		{name:'response', index:'response', width:443, sortable:false},
	  			   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }
			   	],
			   	multiselect: false,

				cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
				beforeSaveCell : function(rowid, name, val, iRow, iCol) {
					if (isNaN(val)) {
  						return null;
  					}
					
					// get maxGrade attribute which was set in masterDetailLoadUp.jsp
					var maxGrade = jQuery("table#userSummary${sessionDto.sessionId} tr#" + iRow 
							              + " td[aria-describedby$='_" + name + "']").attr("maxGrade");
					if (+val > +maxGrade) {
						return maxGrade;
					}
				},
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					if (isNaN(val)) {
  						jQuery("#userSummary${sessionDto.sessionId}").restoreCell(iRow,iCol); 
  					} else {
  						var parentSelectedRowId = jQuery("#list${sessionDto.sessionId}").getGridParam("selrow");
  						var previousTotal =  eval(jQuery("#list${sessionDto.sessionId}").getCell(parentSelectedRowId, 'total'));
  						jQuery("#list${sessionDto.sessionId}").setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					if (isNaN(val)) {
  						return {nan:true};
  					} else {
  						var questionResultUid = jQuery("#userSummary${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
  						return {questionResultUid:questionResultUid};		  				  		
  				  	}
  				}
			});
			
		</c:forEach>
		
		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
		});

		//resize jqGrid on openning of bootstrap collapsible
		$('div[id^="collapse"]').on('shown.bs.collapse', function () {
			resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
		})

		$("#questionUid").change(function() {
			var questionUid = $("#questionUid").val();
			if (questionUid != -1) {
				var questionSummaryUrl = '<c:url value="/monitoring/questionSummary.do?sessionMapID=${sessionMapID}"/>';
				var questionSummaryHref = questionSummaryUrl + "&questionUid=" + questionUid + "&KeepThis=true&TB_iframe=true&modal=true";
				$("#questionSummaryHref").attr("href", questionSummaryHref);	
				$("#questionSummaryHref").click(); 		 
			}
	    });
		
		// trigger the resize when the window first opens so that the grid uses all the space available.
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	});

	function resizeJqgrid(jqgrids) {
		jqgrids.each(function(index) {
			var gridId = $(this).attr('id');
	    	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	    	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	    });
	};
	
	function exportSummary() {
		var url = "<c:url value='/monitoring/exportSummary.do'/>";
	    var reqIDVar = new Date();
		var param = "?sessionMapID=${sessionMapID}&reqID="+reqIDVar.getTime();
		url = url + param;
		return downloadFile(url, 'messageArea_Busy', '<fmt:message key="label.summary.downloaded"/>', 'messageArea', 'btn-disable-on-submit');
	};
	
</script>

<div class="panel">
	<h4>
	  <c:out value="${assessment.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${assessment.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessionDtos}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>

	<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
	<div class="voffset5 help-block" id="messageArea"></div>
			
</div>

<c:if test="${not empty sessionDtos}">
	
	<button onclick="return exportSummary();" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
		<i class="fa fa-download" aria-hidden="true"></i> 
		<fmt:message key="label.monitoring.summary.export.summary" />
	</button>
			
	<h5><fmt:message key="label.monitoring.summary.summary" /></h5>
	
	<div class="comments">
		<fmt:message key="label.monitoring.summary.double.click" />
	</div>

	<div id="masterDetailArea" class="voffset10">
		<%@ include file="parts/masterDetailLoadUp.jsp"%>
	</div>
	<a onclick="" href="return false;" class="thickbox initially-hidden" id="userSummaryHref"></a>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>
	
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
	
		<c:if test="${sessionMap.isGroupedActivity}">	
		    <div class="panel panel-default" >
		        <div class="panel-heading" id="heading${sessionDto.sessionId}">
		        	<span class="panel-title collapsable-icon-left">
		        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionId}" 
								aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
							<fmt:message key="monitoring.label.group" />:	<c:out value="${sessionDto.sessionName}" />
						</a>
					</span>
		        </div>
	        
	        <div id="collapse${sessionDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
	        		role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
		</c:if>
				
		<table id="list${sessionDto.sessionId}"></table>
		<div class="voffset10"></div>
		<table id="userSummary${sessionDto.sessionId}"></table>
		<div id="listPager${sessionDto.sessionId}"></div>

		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
		${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

	</c:forEach>
		
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!--  end panel group -->
	</c:if>

	<!-- Dropdown menu for choosing a question type -->	
	<h5><fmt:message key="label.monitoring.summary.report.by.question" /></h5>
	
	<div class="comments">
		<fmt:message key="label.monitoring.summary.results.question" />
	</div>

	<div class="form-inline form-group voffset5">
		<select id="questionUid" class="form-control input-sm">
			<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
    		<c:forEach var="question" items="${sessionMap.questionList}">
				<option value="${question.uid}"><c:out value="${question.title}" escapeXml="true"/></option>
		   	</c:forEach>
		</select>
			
		<a onclick="" href="return false;" class="thickbox initially-hidden" id="questionSummaryHref"></a>
	</div>
</c:if>

<br/>

<c:if test="${assessment.reflectOnActivity && not empty sessionMap.reflectList}">
	<%@ include file="parts/reflections.jsp"%>
</c:if>

<%@ include file="parts/advanceoptions.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>
