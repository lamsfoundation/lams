<%@ include file="/common/taglibs.jsp"%>


<style>
    /* when item is editable - show pencil icon on hover */
    .editable-comment:hover +span+ i, /* when link is hovered select i */
	.editable-comment + span:hover+ i, /* when space after link is hovered select i */
	.editable-comment + span + i:hover { /* when icon is hovered select i */
	  visibility: visible;
	}
	
	.editable-comment +span+ i { /* in all other case hide it */
	  visibility: hidden;
	}
</style>
<c:choose>
<c:when test="${criteria.ratingStyle eq 0 }">
	<c:set var="heading"></c:set>
</c:when>
<c:when test="${criteria.ratingStyle eq 1 }">
	<c:set var="heading"><fmt:message key="label.rating" /></c:set>
</c:when>
<c:when test="${criteria.ratingStyle eq 2 }">
	<c:set var="heading"><fmt:message key="label.rating.rank" /></c:set>
</c:when>
<c:when test="${criteria.ratingStyle eq 3 }">
	<c:set var="heading"><fmt:message key="label.mark" /></c:set>
</c:when>
</c:choose>

<script type="text/javascript">
	$(document).ready(function(){
	    let savedCellValue = "";

		jQuery("#group${toolSessionId}").jqGrid({
		   	url: "<c:url value='/monitoring/getUsers.do'/>?toolContentId=${sessionMap.toolContentID}&toolSessionId=${toolSessionId}&criteriaId=${criteria.ratingCriteriaId}",
			datatype: "json",
			autoencode: false,
			height: 'auto',
			autowidth: true,
			shrinkToFit: false,
			guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
		   	colNames:[
				'itemId',
				'<fmt:message key="label.user.name" />',
				'${heading}',
				'portraitId',
				''
			],
		   	colModel:[
		   		{name:'itemId', index:'itemId', width:0, hidden: true},
		   		{name:'itemDescription', index:'itemDescription', width:200, searchoptions: { clearSearch: false }, formatter:userNameFormatter},
		   		{name:'rating', index:'rating', width:100, align:"center", search:false},
		   		{name:'itemDescription2', index:'itemId', width:0, hidden: true},
				{name:'email', index:'email', width:100, align:"center", search:false}		   		
		   	],
		   	rowNum:10,
		   	rowList:[10,20,30,40,50,100],
		   	pager: '#pager${toolSessionId}',
		   	viewrecords:true,
			loadComplete: function(){
				initializeJRating();
				initializePortraitPopover('<lams:LAMSURL/>');
			},
		   	// caption: "${groupSummary.sessionName}" use Bootstrap panels as the title bar
			subGrid: true,
			subGridOptions: {
				reloadOnExpand : false 
			},
			subGridRowExpanded: function(subgrid_id, row_id) {
					var subgridTableId = subgrid_id+"_t";
					var itemId = jQuery("#group${toolSessionId}").getRowData(row_id)["itemId"];
					   
					jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");
					   
					jQuery("#"+subgridTableId).jqGrid({
						datatype: "json",
						loadonce:true,
						rowNum: 10000,
						url: "<c:url value='/monitoring/getSubgridData.do'/>?toolContentId=${sessionMap.toolContentID}&toolSessionId=${toolSessionId}&criteriaId=${criteria.ratingCriteriaId}&itemId=" + itemId,
						height: "100%",
						autowidth:true,
						grouping:true,	
						autoencode:false,
						groupingView : {
							groupField : ['criteriaId'],
							groupColumnShow : [false]
						},
						guiStyle: "bootstrap",
						iconSet: 'fontAwesome',
						colNames:[
							'id',
							'<fmt:message key="label.user.name" />',
							'${heading}',
							'criteriaId',
							'userId'
							],
						colModel:[
						   {name:'id', index:'id', width:0, hidden:true},
						   {name:'userName', index:'userName', width:40 },
						   {name:'rating', index:'rating',  title:false, edittype: 'textarea', editoptions: {rows:"8"},
						   		formatter:function(cellvalue, options, rowObject, event) {
						   			if (event == "edit") {
						   				cellvalue = cellvalue.replace(/\n/g, '\n<br>');
						   			}
						   			var rowData = $(this).jqGrid("getLocalRow", options.rowId);

						   			// when item is editable - show pencil icon on hover
									return rowData.criteriaId == 'Comments' ? 
									   		"<span class='editable-comment'>" + cellvalue +
									   		"</span><span>&nbsp;</span><i class='fa fa-pencil'></i>" 
									   		: cellvalue;
				   				},
				   				unformat:function(cellvalue, options, rowObject) {
				   					var text = $('<div>' + cellvalue + '</div>').text();
									return text.trim();
				   				},
					   			editable: function (options) {
					   				// only comments are editable
					   	            var rowData = $(this).jqGrid("getLocalRow", options.rowid);
						  			return rowData.criteriaId == 'Comments';
					   	        }
						   },
						   {name:'criteriaId', width:0, hidden:true},
						   {name:'userId', width:0, hidden:true}
						],

					    editurl: "<c:url value='/monitoring/saveComment.do'/>?<csrf:token/>&toolSessionId=${toolSessionId}&criteriaId=${criteria.ratingCriteriaId}&itemId=" + itemId,
	  				  	inlineEditing: { keys: true, defaultFocusField: "rating", focusField: "rating" },
	  					onSelectRow: function (rowid) {
		  					var grid = $(this),
	  							rowData = grid.jqGrid("getRowData", rowid);	  	
  							                
							if (rowData.criteriaId == 'Comments') {
			  					grid.jqGrid("editRow", rowid, { focusField: "rating" });
			  	            	// Modify event handler to save on blur
			  	              	$("textarea[id^='" + rowid + "_rating']", grid).bind('blur', function(){
			  	              		grid.saveRow(rowid, null, null, {
			  	              			userId : rowData.userId
				  	              	});
			  	              	});
							}
	  	            	},
						loadComplete: function(){
							//remove empty subgrids
					        var table_value = $('#'+subgridTableId).getGridParam('records');
					        if(table_value === 0){
					            $('#'+subgrid_id).parent().unbind('click').html('<fmt:message key="label.no.ratings.left" />');
					        }
						},
						loadError: function(xhr,st,err) {
					    	jQuery("#"+subgridTableId).clearGridData();
					    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
					    }
					})
			}
		}).jqGrid('filterToolbar', { 
			searchOnEnter: false
		})
		.navGrid('#pager${toolSessionId}',{add:false,del:false,edit:false,search:false});
        
        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
        $(window).bind('resize', function() {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
        });

        //resize jqGrid on openning of bootstrap collapsible
        $('div[id^="collapse"]').on('shown.bs.collapse', function () {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
        })

        function userNameFormatter (cellvalue, options, rowObject) {
			return definePortraitPopover(rowObject.itemDescription2, rowObject.itemId,  rowObject.itemDescription);
		}

        function resizeJqgrid(jqgrids) {
            jqgrids.each(function(index) {
                var gridId = $(this).attr('id');
                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
            });
        };
        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
        
	});

	function getResultsElement(sessionId, selector) {
		let element = null;
		if (sessionId) {
			// if Peer Review is grouped, try to find the element within own group
			element = $('#collapse' + sessionId + ' ' + selector);
		}
		if (!element || element.length == 0) {
			element = $(selector);
		}
		return element;
	}
	
	function closeResultsForLearner(sessionId) {
		let emailPreviewArea = getResultsElement(sessionId, '.emailPreviewArea');
		emailPreviewArea .html("").hide();
		return false;
	}
	
	// Preview the email to be sent to the learner
	function previewResultsForLearner(sessionId, userId) {
		let buttons = getResultsElement(sessionId, ".btn-disable-on-submit"),
			messageArea = getResultsElement(sessionId, ".messageArea2"),
			messageAreaBusy = getResultsElement(sessionId, ".messageArea2_Busy"),
			emailPreviewArea = getResultsElement(sessionId, '.emailPreviewArea'),
			url = "<c:url value="/monitoring/previewResultsToUser.do"/>";
			
		buttons.prop("disabled", true);
		messageArea.html("");
		messageAreaBusy.show();
		
		emailPreviewArea.load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				userID: userId,
				reqID: (new Date()).getTime()
			},
			function() {
				messageAreaBusy.hide();
				emailPreviewArea.show();
				// scroll to preview area
				$('html, body').animate({scrollTop: emailPreviewArea.offset().top}, 1000);
				buttons.prop("disabled", false);
			}
		);
		return false;
	}
	
	// Send the previewed email to the learner
	function sendResultsForLearner(sessionId, userId, dateTimeStamp) {
		let buttons = getResultsElement(sessionId, ".btn-disable-on-submit"),
			messageArea = getResultsElement(sessionId, ".messageArea2"),
			messageAreaBusy = getResultsElement(sessionId, ".messageArea2_Busy"),
			url = "<c:url value="/monitoring/sendPreviewedResultsToUser.do"/>";
		
		buttons.prop("disabled", true);
		messageArea.html("");
		messageAreaBusy.show();
		
		messageArea.load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				dateTimeStamp: dateTimeStamp,
				userID: userId,
				reqID: (new Date()).getTime()
			},
			function() {
				messageAreaBusy.hide();
				closeResultsForLearner(sessionId);
				buttons.prop("disabled", false);
			}
		);
		return false;
	}
</script>

<!--For send results feature-->
<i class="fa fa-spinner messageArea2_Busy" style="display:none"></i>
<div class="voffset5 messageArea2"></div>

<p>
	<c:choose>
	<c:when test="${criteria.ratingStyle eq 2}">
		<c:choose>
		<c:when test="${criteria.maxRating gt 0 }">
			<fmt:message key="label.monitoring.instructions.rank.some.learners">
				<fmt:param>${criteria.maxRating}</fmt:param>
			</fmt:message>
		</c:when>
		<c:otherwise>
			<fmt:message key="label.monitoring.instructions.rank.all.learners"/>
		</c:otherwise>
		</c:choose>			
	</c:when>
	<c:when test="${criteria.ratingStyle eq 3 }">
		<fmt:message key="label.monitoring.instructions.assign.some.marks">
			<fmt:param>${criteria.maxRating}</fmt:param>
		</fmt:message>
	</span>
	</c:when>
	</c:choose>
</p>

<table id="group${toolSessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
<div id="pager${toolSessionId}"></div> 

<div class="voffset10 emailPreviewArea" style="display:none" ></div>