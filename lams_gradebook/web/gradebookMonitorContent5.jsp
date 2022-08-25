<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="usesWeights">${not empty weights}</c:set>

<style>
	#releaseMarksPanel {
		display: none;
		margin: 20px 0;
	}
	
	.gbTopButtonsContainer button {
		margin: 0 0 0 .8rem;
	}
</style>
	
<script type="text/javascript">

	var graphLoaded = false;

	function toggleMarkChart() {
		// the two methods showMarkChart and hideMarkChart are used in the Monitoring tour
		if ( $("#markChartDiv").css("display") == "none" ) {
			showMarkChart();
		} else {
			hideMarkChart();
		}
	}

	function showMarkChart() {
		$("#markChartDiv").css("display", "block");
		$("#markChartHidden").css("display", "none");
		if ( ! graphLoaded ) {
			$("#markChartBusy").css("display", "block");
			drawHistogram('markChartDiv',
					'<lams:LAMSURL/>gradebook/gradebookMonitoring/getMarkChartData.do?lessonID=${lessonDetails.lessonID}',
					'<fmt:message key="label.marks"/>', '<fmt:message key="label.number.learners.in.mark.range"/>');
			graphLoaded = true;
			$("#markChartBusy").css("display", "none");
		}
		$("#markChartShown").css("display", "inline");
	}
	
	function hideMarkChart() {
		$("#markChartDiv").css("display", "none");
		$("#markChartShown").css("display", "none");
		$("#markChartHidden").css("display", "inline");
	}

	<c:if test="${usesWeights}">
	function toggleWeight() {
		if ( $("#weights").css("display") == "none" ) {
			$("#weights").css("display","block");
			$("#weightShown").css("display","inline");
			$("#weightHidden").css("display","none");
		} else {
			$("#weights").css("display","none");
			$("#weightShown").css("display","none");
			$("#weightHidden").css("display","inline");
		}
	}
	</c:if>

	function toggleReleaseMarksPanel(reload){
		var releaseMarksPanel = $('#releaseMarksPanel');
		if (reload) {
			// force reload
			releaseMarksPanel.empty();
		}
		
		if (releaseMarksPanel.is(':empty')) {
			releaseMarksPanel.load('<lams:LAMSURL/>gradebook/gradebookMonitoring/displayReleaseMarksPanel.do',{
				'lessonID' : ${lessonDetails.lessonID},
				'isTab'	   : true
			}, function(){
				releaseMarksPanel.slideDown();
			});
		} else {
			releaseMarksPanel.slideUp(function(){
				$(this).empty();
			});
		}
	}

	jQuery(document).ready(function(){
		var jqgridWidth = $(window).width() - 100;
		
		// Create the user view grid with sub grid for activities
		jQuery("#userView").jqGrid({
			guiStyle: "bootstrap4",
			iconSet: 'fontAwesomeSolid',
			autoencode:false,
			caption: "<fmt:message key="gradebook.gridtitle.usergrid"/>",
		    datatype: "xml",
		    url: "<lams:LAMSURL />gradebook/gradebook/getUserGridData.do?view=monUserView&lessonID=${lessonDetails.lessonID}",
		    height: "100%",
		    width: jqgridWidth,
			shrinkToFit: false,
		    cellEdit: true,
		    cmTemplate: { title: false },
		    viewrecords: true,
		    sortorder: "asc", 
		    sortname: "rowName", 
		    pager: 'userViewPager',
		    rowList:[10,20,30,40,50,100],
		    rowNum:10,
			cellurl: "<lams:LAMSURL />gradebook/gradebookMonitoring/updateUserLessonGradebookData.do?<csrf:token/>&lessonID=${lessonDetails.lessonID}",
		    colNames:["", 
		    	"<fmt:message key="gradebook.columntitle.name"/>", 
		    	"<fmt:message key="gradebook.columntitle.progress"/>", 
		    	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
		    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
		    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
		    	"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
		    	"<fmt:message key="gradebook.columntitle.mark"/>",
		    	'portraitId',
		    	'hasArchivedMarks'
		    ],
		    colModel:[
		      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
		      {name:'rowNamer',index:'rowName', sortable:true, editable:false, autoencode:true, width: 150, formatter:userNameFormatter},
		      {name:'status',index:'status', sortable:false, editable:false, search:false, width:30, align:"center"},
			  {name:'timeTaken',index:'timeTaken', sortable:true, editable: false, search:false, width:50, title: true, align:"center",
					cellattr: function(rowID, val, rawObject, cm, rdata) {
						if (rdata.startDate != "-") {
							return 'title="' + rdata.startDate + ' - ' + rdata.finishDate + '"';
						}
			    	}
		      },
			  {name:'startDate',index:'startDate', width:0, hidden: true},
			  {name:'finishDate',index:'finishDate', width:0, hidden: true},
		      {name:'feedback',index:'feedback', sortable:true, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, search:false },
		      {name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"},
		      {name:'portraitId', index:'portraitId', width:0, hidden: true},
		      {name:'hasArchivedMarks', index:'hasArchivedMarks', width:0, hidden: true}
		      ],
		    loadError: function(xhr,st,err) {
			    jQuery("#userView").clearGridData();
			   	alert("<fmt:message key="gradebook.error.loaderror"/>");
		    },

		    subGrid: true,
			subGridRowExpanded: function(subgrid_id, row_id) {
				var subgrid_table_id = subgrid_id+"_t",
			   	   rowData = jQuery("#userView").getRowData(row_id),
			   	   userID = rowData["id"],
			   	   hasArchivedMarks = rowData["hasArchivedMarks"] == "true";
		   	   
				jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
				jQuery("#"+subgrid_table_id).jqGrid({
						 guiStyle: "bootstrap4",
						 iconSet: 'fontAwesomeSolid',
						 autoencode:false,
					     datatype: "xml",
					     url: "<lams:LAMSURL />gradebook/gradebook/getActivityGridData.do?lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
					     height: "100%",
					     autowidth:true,
					     cmTemplate: { title: false },
					     cellEdit:true,
					     pager: subgrid_table_id + "_pager",
					     rowList:[10,20,30,40,50,100],
						 rowNum:10,
						 sortorder: "asc", 
						 sortname: "sequence", 
					     cellurl: "<lams:LAMSURL />gradebook/gradebookMonitoring/updateUserActivityGradebookData.do?<csrf:token/>&lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
					     colNames: [
					     	'',
					     	'',
					     	"<fmt:message key="gradebook.columntitle.activity"/>",
					     	"<fmt:message key="gradebook.columntitle.progress"/>",
					     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
					    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
					    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
					     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
					     	"<fmt:message key="gradebook.columntitle.outcome"/>",
					     	"<fmt:message key="gradebook.columntitle.mark"/>"
					     ],
					     colModel: [
					       	{name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
					       	{name:'marksAvailable',index:'marksAvailable', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
							{name:'rowName',  index:'rowName', sortable:false, editable: false, width: 140},
							{name:'status',  index:'status', sortable:false, editable:false, width:30, align:"center"},
							{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:51, title : true, align:"center",
								cellattr: function(rowID, val, rawObject, cm, rdata) {
									if (rdata.startDate != "-") {
										return 'title="' + rdata.startDate + ' - ' + rdata.finishDate + '"';
									}
						    	}
					    	},
						    {name:'startDate',index:'startDate', width:0, hidden: true},
						    {name:'finishDate',index:'finishDate', width:0, hidden: true},
							{name:'feedback',  index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, width:200, hidden:true},
							{name:'outcome', index:'outcome', sortable:false, editable: false,
								cellattr: function(rowID, val, rawObject, cm, rdata) {
						    	    return 'style="text-align:' + (rdata.outcome.startsWith('[') ? 'left"' : 'center"');
						    	}
					    	},
							{name:'mark', index:'mark', sortable:true, editable: true, editrules:{number:true}, width:49, align:"center" }
					     ],
					     loadError: function(xhr,st,err) {
					    	jQuery("#"+subgrid_table_id).clearGridData();
					 	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
					     },
					     formatCell: function(rowid, cellname,value, iRow, iCol) {
				    	 	if (cellname == "mark") {
				    	 		
				    	 		var rowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
				    	 		var string = removeHTMLTags(value);
				    	 		
				    	 		
				    	 		if (string.indexOf("-") != -1)
				    	 		{
				    	 			string = " ";
				    	 			
				    	 		} else if (string.indexOf("/") != -1) {
				    	 			splits = string.split("/");
				    	 			
				    	 			if(splits.length == 2) {
				    	 				tempMark = splits[0];
				    	 				string = " ";
				    	 			} else {
				    	 				string = " ";
				    	 			}
				    	 		}
				    	 		
				    	 		return string;
				    	 		
				    	 	}
				    	 },
				    	 beforeSaveCell: function(rowid, cellname,value, iRow, iCol){
				    	 	value = value ? value.trim() : value;
				    	 	
				    	 	if (cellname == "mark") {
				    	 		if (value == "") {
				    	 			jQuery("#"+subgrid_table_id).restoreCell( iRow, iCol);
				    	 			throw("Value required for mark.");
				    	 		}
				    	 		
				    	 		var currRowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					     		if (currRowData['marksAvailable'] != null && currRowData['marksAvailable'] != "") {
					     			if (parseFloat(value) > parseFloat(currRowData['marksAvailable'])){
					     				displayCellErrorMessage(jQuery("#"+subgrid_table_id)[0], iRow, iCol, "<fmt:message key="label.error"/>", "<fmt:message key="error.markhigher"/>", "<fmt:message key="label.ok"/>");
					     				jQuery("#"+subgrid_table_id).restoreCell( iRow, iCol);
					     				throw("Mark must be lower than maximum mark");
					     			}
					     		}
				    	 	}
				    	 },
					     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
					     	var currRowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					     	if (cellname == "mark") {
						     	
						     	if (cellname == "mark") {
						     		if (currRowData['marksAvailable'] != null && currRowData['marksAvailable'] != "") {
						     			var markStr = "<font color='green'>" + value + "/" + currRowData['marksAvailable'] + "</font>";
						     			jQuery("#"+subgrid_table_id).setCell(rowid, "mark", markStr, "", "");
						     		}
						     	}
						     	
						     	// Update the aggregated lesson mark
						     	$.get(
									"<lams:LAMSURL/>/gradebook/gradebook/getLessonMarkAggregate.do", 
									{
										lessonID:"${lessonDetails.lessonID}", 
										userID:row_id
									}, 
									function(xml) {
								    	if (xml!=null) {
								    		jQuery("#userView").setCell(row_id, "mark", xml, "", "");
								    	} 
							    	}
							    );
					     	}
					     },
					     errorCell: function(serverresponse, status) {
					     	alert("<fmt:message key="error.cellsave"/>");
					     },
						 gridComplete: function(){
						 	fixPagerInCenter(subgrid_table_id+"_pager", 1);

						 	// if there are outcomes mapped to this activity, make them editable
						 	var subgrid = $(this);
						 	$('tr[role="row"]', subgrid).each(function(){
							 	// find out row ID
							 	var row = $(this),
							 		id = row.attr('id');
						 		// first row does not have ID
							 	if (id) {
								 	// content is JSON code sent from server
							 		var content = $(subgrid).jqGrid('getCell', id, 'outcome');
									if (content && content.startsWith('[')) {
										var outcomes = JSON.parse(content),
											outcomeValues = {},
											result = '';
										// go through each JSON item
										$.each(outcomes, function() {
											// assign values 0..n to ordered value names
											var editablePossibleValues = this.possibleValues.map(function(value, index){
												return {
													value : index,
													text  : value
												}
											});
											// add a marker for unsetting result
											editablePossibleValues.unshift({
												value : -1,
												text  : '<fmt:message key="outcome.result.not.set"/>'
											});
											outcomeValues[this.mappingId] = editablePossibleValues;
											// build HTML code for x-editable
											result += '<span title="' + this.code + '">' + this.name + 
													': <a href="#" class="outcome" mappingId="' + this.mappingId + '"></a></span><br />';
										});
										// set HTML to the cell
										$(subgrid).jqGrid('setCell', id, 'outcome', result);

										// enable editable for each outcome
										$('.outcome', row).each(function() {
											var editable = $(this),
												mappingId = editable.attr('mappingId');
											editable.editable({
											    type: 'select',
											    emptytext : '<fmt:message key="outcome.result.error"/>',
											    pk : mappingId,
											    // a trick to send extra param to the server
											    name : userID,
											    url : '<lams:LAMSURL/>outcome/outcomeSetResult.do',
											    mode : 'inline',
											    showbuttons : false,
											    source : outcomeValues[mappingId],
											    // sending -1 removes the result
											    value : typeof this.value == 'undefined' ? -1 : this.value,
											    success: function(response, newValue) {
												    if (response != "OK"){
													    // it will set the value to empty and display emptytext, i.e. "ERROR!"
													    return {'newValue' : null}
													}
												}
											});
											// prevent onSelectCell jqGrid event from firing and focusing out from the cell
											editable.parent().on('click','.editable-container', function(e) {
												e.stopPropagation();
											});
										});
									}
						 		}
							 });
						 },
						 subGrid : hasArchivedMarks,
						 subGridRowExpanded: function(subgrid_id, row_id) {
						    var subgrid_table_id = subgrid_id + "_t",
						   	    rowData = jQuery("#" + subgrid_id.substring(0, subgrid_id.lastIndexOf('_'))).getRowData(row_id),
						   	    activityID = rowData["id"].split("_")[0];
							jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll archive'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
							jQuery("#"+subgrid_table_id).jqGrid({
									 guiStyle: "bootstrap4",
									 iconSet: 'fontAwesomeSolid',
									 autoencode:false,
									 autowidth: true,
								     datatype: "xml",
								     url: "<lams:LAMSURL />gradebook/gradebook/getActivityArchiveGridData.do?lessonID=${lessonDetails.lessonID}&activityID="
									      + activityID + "&view=monUserView&userID=" + userID,
								     height: "100%",
								     cmTemplate: { title: false },
								     cellEdit:false,
									 sortorder: "asc", 
									 sortname: "sequence",
								     pager: false,
								     colNames: [
								    	"<fmt:message key="gradebook.columntitle.attempt"/>",
								     	"<fmt:message key="gradebook.columntitle.progress"/>",
								     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
								    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
								    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
								     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
								     	"<fmt:message key="gradebook.columntitle.lesson.mark"/>",
								     	"<fmt:message key="gradebook.columntitle.mark"/>"
								     ],
								     colModel: [
								       	{name:'id', index:'id',  sortable:false, editable: false ,width:140, align:"right"},
										{name:'status',  index:'status', sortable:false, editable:false, width:30, align:"center"},
										{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:52, title : true, align:"center",
											cellattr: function(rowID, val, rawObject, cm, rdata) {
												if (rdata.startDate != "-") {
													return 'title="' + rdata.startDate + ' - ' + rdata.finishDate + '"';
												}
									    	}
								    	},
									    {name:'startDate',index:'startDate', width:0, hidden: true},
									    {name:'finishDate',index:'finishDate', width:0, hidden: true},
										{name:'feedback',  index:'feedback', sortable:false, editable: false, width:0, hidden:true},
										{name:'lessonMark',  index:'lessonMark', sortable:false, editable: false, align:"center" },
										{name:'mark', index:'mark', sortable:false, editable: false, width:49, align:"center" }
								     ],
								     loadError: function(xhr,st,err) {
								    	jQuery("#"+subgrid_table_id).clearGridData();
								 	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
								     }
							  	});
							}
				  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
				},
				gridComplete: function(){
			   	 	initializePortraitPopover('<lams:LAMSURL/>');
				}
			}).navGrid("#userViewPager", {edit:false,add:false,del:false,search:false})// applying refresh button
			  .on('jqGridBeforeEditCell', function(){
				  $(this).data('isCellEdited', true);
			  })
			  .on('jqGridAfterSaveCell jqGridAfterRestoreCell', function(){
				  $(this).data('isCellEdited', false);
			  });
			jQuery("#userView").jqGrid('filterToolbar');

			// Creating activity view with sub learner view
			jQuery("#activityView").jqGrid({
				guiStyle: "bootstrap4",
				iconSet: 'fontAwesomeSolid',
				autoencode:false,
				caption: "<fmt:message key="gradebook.gridtitle.activitygrid"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />gradebook/gradebook/getActivityGridData.do?view=monActivityView&lessonID=${lessonDetails.lessonID}",
			    height: "100%",
			    width: jqgridWidth,
			    shrinkToFit: false,
			    cmTemplate: { title: false },
			    cellEdit: true,
			    pager: "activityViewPager",
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    sortorder: "asc", 
			    sortname: "sequence", 
			    colNames:[
			    	'', 
			    	'',
			    	"<fmt:message key="gradebook.columntitle.name"/>", 
			    	"<fmt:message key="gradebook.columntitle.averageTimeTaken"/>", 
			    	"<fmt:message key="gradebook.columntitle.competences"/>", 
			    	"<fmt:message key="gradebook.columntitle.averageMark"/>"
			    ],
			    colModel:[
			      {name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
			      {name:'groupId', index:'groupId', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
				  {name:'rowName', index:'rowName', sortable:true, editable: false},
				  {name:'avgTimeTaken', index:'avgTimeTaken', sortable:true, editable: false, width:80, align:"center"},
				  {name:'competences', index:'competences', sortable:false, editable: false, hidden:true},
			      {name:'avgMark',index:'avgMark', sortable:true, editable:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
		    		jQuery("#activityView").clearGridData();
		    		alert("<fmt:message key="gradebook.error.loaderror"/>");
		    	},
		    	beforeEditCell : function() {
				    $(this).data('isCellEdited', true);
				},
				afterEditCell : function(){
					$(this).data('isCellEdited', false);
				},
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var activityID = jQuery("#activityView").getRowData(row_id)["id"];
				   var groupID = jQuery("#activityView").getRowData(row_id)["groupId"];
				   subgrid_table_id = subgrid_id+"_t";
				   
				   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
				   jQuery("#"+subgrid_table_id).jqGrid({
							 guiStyle: "bootstrap4",
						 iconSet: 'fontAwesomeSolid',
						 autoencode:false,
					     datatype: "xml",
					     url: "<lams:LAMSURL />gradebook/gradebook/getUserGridData.do?view=monActivityView&lessonID=${lessonDetails.lessonID}&activityID=" + activityID + "&groupId=" + groupID,
					     height: "100%",
					     autowidth:true,
					     cmTemplate: { title: false },
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />gradebook/gradebookMonitoring/updateUserActivityGradebookData.do?<csrf:token/>&lessonID=${lessonDetails.lessonID}&view=monActivityView&activityID=" + activityID,
					     sortorder: "asc", 
						 sortname: "fullName", 
						 pager: subgrid_table_id + "_pager",
					     rowList:[10,20,30,40,50,100],
						 rowNum:10,
					     colNames: [
					     	'',
					     	'',
					     	"<fmt:message key="gradebook.columntitle.name"/>",
					     	"<fmt:message key="gradebook.columntitle.progress"/>", 
					     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
					    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
					    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
					     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
					     	"<fmt:message key="gradebook.columntitle.mark"/>",
						    'portraitId',
					     	'activityURL',
					     	'hasArchivedMarks'
					     ],
					     colModel:[
					     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'marksAvailable',index:'marksAvailable', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'rowName',index:'rowName', sortable:true, editable:false, formatter:userNameFormatterActivity},
					      	{name:'status', index:'status', sortable:false, editable:false, search:false, width:30, align:"center"},
							{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:51, align:"center",
								cellattr: function(rowID, val, rawObject, cm, rdata) {
						    	    return 'title="' + rdata.startDate + ' - ' + rdata.finishDate + '"';
						    	}
					    	},
						    {name:'startDate',index:'startDate', width:0, hidden: true},
						    {name:'finishDate',index:'finishDate', width:0, hidden: true},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false, width:200, hidden:true},
					     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"},
					     	{name:'portraitId', index:'portraitId', width:0, hidden: true},
					     	{name:'activityURL', index:'activityURL', width:0, hidden: true},
						    {name:'hasArchivedMarks', index:'hasArchivedMarks', width:0, hidden: true}
					     ],
					     loadError: function(xhr,st,err) {
					    		jQuery("#"+subgrid_table_id).clearGridData();
					    		alert("<fmt:message key="gradebook.error.loaderror"/>");
					    	 },
				    	 beforeEditCell : function() {
						    $("#activityView").data('isCellEdited', true);
						 },
						 afterEditCell : function(){
							$("#activityView").data('isCellEdited', false);
						 },
				    	 formatCell: function(rowid, cellname,value, iRow, iCol) {
				    	 	if (cellname == "mark") {
				    	 		
				    	 		var rowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
				    	 		var string = removeHTMLTags(rowData["mark"]);
				    	 		
				    	 		
				    	 		if (string.indexOf("-") != -1) {
				    	 			string = " ";
				    	 			
				    	 		} else if (string.indexOf("/") != -1) {
				    	 			splits = string.split("/");
				    	 			
				    	 			if(splits.length == 2) {
				    	 				tempMark = splits[0];
				    	 				string = " ";
				    	 			} else {
				    	 				string = " ";
				    	 			}
				    	 		}
				    	 		
				    	 		return string;
				    	 		
				    	 	}
				    	 },
				    	 beforeSaveCell: function(rowid, cellname,value, iRow, iCol){
				    		 value = value ? value.trim() : value;
				    	 	
				    	 	if (cellname == "mark") {
				    	 		if (value == "") {
				    	 			jQuery("#"+subgrid_table_id).restoreCell( iRow, iCol);
				    	 			throw("Value required for mark.");
				    	 		}
				    	 		
				    	 		var currRowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					     		if (currRowData['marksAvailable'] != null && currRowData['marksAvailable'] != "") {
					     			if (parseFloat(value) > parseFloat(currRowData['marksAvailable'])){
					     				displayCellErrorMessage(jQuery("#"+subgrid_table_id)[0], iRow, iCol, "<fmt:message key="label.error"/>", "<fmt:message key="error.markhigher"/>", "<fmt:message key="label.ok"/>");
					     				jQuery("#"+subgrid_table_id).restoreCell( iRow, iCol);
					     				throw("Mark must be lower than maximum mark");
					     			}
					     		}
				    	 	}
				    	 },
					     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
					     	if (cellname == "mark") {
					     		var currRowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					     		if (currRowData['marksAvailable'] != null && currRowData['marksAvailable'] != "") {
					     			var markStr = "<font color='green'>" + value + "/" + currRowData['marksAvailable'] + "</font>";
					     			jQuery("#"+subgrid_table_id).setCell(rowid, "mark", markStr, "", "");
					     		}
					     		
					     		// Update the average activity mark
						     	$.get(
									"<lams:LAMSURL/>/gradebook/gradebook/getActivityMarkAverage.do", 
									{activityID:row_id}, 
									function(xml) {
								    	if (xml!=null) {
								    		jQuery("#activityView").setCell(row_id, "avgMark", xml, "", "");
								    	} 
							    	}
							    );
					     	}
					     },
					     errorCell: function(serverresponse, status) {
					     	alert("<fmt:message key="error.cellsave"/>");
					     },
						 gridComplete: function(){
					   	 	initializePortraitPopover('<lams:LAMSURL/>');
					   	 	fixPagerInCenter(subgrid_table_id+"_pager", 1);
						 },
						 subGrid : true,
						 subGridOptions: {
						    hasSubgrid: function (options) {
						        return options.data.hasArchivedMarks == 'true';
						    }
						 },
						 subGridRowExpanded: function(subgrid_id, row_id) {
						    var subgrid_table_id = subgrid_id + "_t",
						    	nameParts = subgrid_id.split("_"),
						   	    activityID = nameParts[1],
						   	    userID = nameParts[3];
							jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll archive'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
							jQuery("#"+subgrid_table_id).jqGrid({
									 guiStyle: "bootstrap4",
									 iconSet: 'fontAwesomeSolid',
									 autoencode:false,
								     datatype: "xml",
								     url: "<lams:LAMSURL />gradebook/gradebook/getActivityArchiveGridData.do?lessonID=${lessonDetails.lessonID}&activityID="
									      + activityID + "&view=monActivityView&userID=" + userID,
								     height: "100%",
								     autowidth:true,
								     cellEdit:false,
								     pager: false,
									 sortorder: "asc", 
									 sortname: "sequence", 
								     colNames: [
								    	"<fmt:message key="gradebook.columntitle.attempt"/>",
								     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
								     	"<fmt:message key="gradebook.columntitle.lesson.mark"/>",
								     	"<fmt:message key="gradebook.columntitle.progress"/>",
								     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
								    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
								    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
								     	"<fmt:message key="gradebook.columntitle.mark"/>"
								     ],
								     colModel: [
								       	{name:'id', index:'id',  sortable:false, editable: false, align:"right"},
										{name:'feedback',  index:'feedback', sortable:false, editable: false, hidden:true},
										{name:'lessonMark',  index:'lessonMark', sortable:false, editable: false,width: 180, align:"center" },
										{name:'status',  index:'status', sortable:false, editable:false, width:65, align:"center"},
										{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:112, title : true, align:"center",
											cellattr: function(rowID, val, rawObject, cm, rdata) {
												if (rdata.startDate != "-") {
													return 'title="' + rdata.startDate + ' - ' + rdata.finishDate + '"';
												}
									    	}
								    	},
									    {name:'startDate',index:'startDate', width:0, hidden: true},
									    {name:'finishDate',index:'finishDate', width:0, hidden: true},
										{name:'mark', index:'mark', sortable:false, editable: false, width:108, align:"center" }
								     ],
								     loadError: function(xhr,st,err) {
								    	jQuery("#"+subgrid_table_id).clearGridData();
								 	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
								     },
							    	 beforeEditCell : function() {
									    $("#activityView").data('isCellEdited', true);
									 },
									 afterEditCell : function(){
										$("#activityView").data('isCellEdited', false);
									 },
							  	});
							}
					 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}) // applying refresh button
					 jQuery("#"+subgrid_table_id).jqGrid('filterToolbar');

				},
				 gridComplete: function(){
				 	fixPagerInCenter('activityViewPager', 0);
				 }	 
			}).navGrid("#activityViewPager", {edit:false,add:false,del:false,search:false}) // enable refresh button
			  .on('jqGridBeforeEditCell', function(){
				  $(this).data('isCellEdited', true);
			  })
			  .on('jqGridAfterSaveCell jqGridAfterRestoreCell', function(){
				  $(this).data('isCellEdited', false);
			  });
			  
			$("#export-grades-button").click(function() {
				var areaToBlock = "export-link-area";
				var exportExcelUrl = "<lams:WebAppURL/>gradebookMonitoring/exportExcelLessonGradebook.do?<csrf:token/>&lessonID=${lessonDetails.lessonID}";
				// if exportSpan is hidden then icon only mode, use small font.
				blockExportButton(areaToBlock, exportExcelUrl);
				
				return false;
			});
			
	        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
	        $(window).bind('resize', function() {
	            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
	        });

	        //resize jqGrid on openning of bootstrap collapsible
	        $('div[id^="collapse"]').on('shown.bs.collapse', function () {
	            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
	        })


	        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	    	
	    	$("#userView, #activityView").bind("jqGridAfterGridComplete", function () {
		   	   let grid = $(this), 
		   	       expandedGridIds = grid.data('expandedGridIds');
		   	   $(this).data('expandedGridIds', null);
		   	   if (expandedGridIds) {
			   	   $(expandedGridIds).each(function(index, id){
				   		grid.jqGrid('expandSubGridRow', id);
			   	   });
		   	   }
	    	});
	    	
	    	$("#userView").bind("jqGridAfterGridComplete", function () {
		   	   if (sequenceSearchedLearner) {
			   	   $('tr[id="' + sequenceSearchedLearner + '"]', this).addClass('table-success');
			   	}
	    	});

	});

    function userNameFormatter (cellvalue, options, rowObject) {
		return definePortraitPopover(rowObject[8].innerHTML, rowObject.id, cellvalue, cellvalue, true);
	}

    // Combine portraits with activityURL. Both are optional so it is mix and match.
 	function userNameFormatterActivity (cellvalue, options, rowObject) {
 		var portProcessed = definePortraitPopover(rowObject[9].innerHTML, rowObject.id, cellvalue, cellvalue, true);
 		if ( rowObject.children.length > 10 && rowObject[10].innerHTML.length > 0 ) {
 			var activityURL = rowObject[10].innerHTML;
 			if ( portProcessed.indexOf('<a') != -1 ) {
 				return portProcessed.replace("<a ", "<a href='"+activityURL+"' ");
 			} else {
 				return "<a href='"+activityURL+"'>"+cellvalue+"</a>";
 			}
 		} 
 		return portProcessed;
	}
</script>

<div class="gbTopButtonsContainer d-flex justify-content-end" id="export-link-area">

	<div>
		<button type="button" id="export-grades-button" class="btn btn-sm btn-secondary" title="<fmt:message key='gradebook.export.excel'/>" >
			<i class="fa-solid fa-download"></i><span id="exportSpan" class="hidden-xs">
			<fmt:message key="gradebook.export.excel" />
			</span>
		</button> 
	</div>

	<div id="tour-release-marks">
		<button type="button" onClick="javascript:toggleReleaseMarksPanel()" class="btn btn-sm btn-secondary" 
			title="<fmt:message key="gradebook.monitor.releasemarks.toggle.panel.tooltip" />">
			<i class="fa-solid fa-share-alt "></i> <span class="hidden-xs">
				<fmt:message key="gradebook.monitor.releasemarks.toggle.panel" />
			</span>
		</button>
	</div>

	<div id="tour-mark-chart-button">
		<div id="markChartShown" style="display:none">
			<button type="button" onClick="javascript:toggleMarkChart()" class="btn btn-sm btn-secondary" title="<fmt:message key='label.hide.marks.chart'/>" >
				<i class="fa-solid fa-bar-chart"></i> <span class="hidden-xs">
				<fmt:message key="label.hide.marks.chart"/>
				</span>
			</button> 
		</div>
		<div id="markChartHidden">
			<button type="button" onClick="javascript:toggleMarkChart()" class="btn btn-sm btn-secondary" title="<fmt:message key='label.show.marks.chart'/>" >
				<i class="fa-solid fa-bar-chart"></i> <span class="hidden-xs">
				<fmt:message key="label.show.marks.chart"/>
				</span>
			</button> 
		</div>
	</div>
	
	<c:if test="${usesWeights}">
		<div id="tour-weight-button">
			<div id="weightShown" style="display:none">
			<button type="button" onClick="javascript:toggleWeight()" class="btn btn-sm btn-secondary" title="<fmt:message key='label.button.hide.weights'/>" >
				<i class="fa-solid fa-balance-scale"></i> <span class="hidden-xs">
				<fmt:message key="label.button.hide.weights"/>
				</span>
			</button>  
			</div>
			<div id="weightHidden">
				<button type="button" onClick="javascript:toggleWeight()" class="btn btn-sm btn-secondary" title="<fmt:message key='label.button.show.weights'/>" >
					<i class="fa-solid fa-balance-scale"></i> <span class="hidden-xs">
					<fmt:message key="label.button.show.weights"/>
					</span>
				</button>  
			</div>
		</div>
	</c:if>
	
	<!-- 
	<div>
		<button onclick="javascript:startTour();return false;" class="btn btn-sm btn-secondary tour-button"> 
			<i class="fa-solid fa-question-circle"></i> <span class="hidden-xs">Tour</span>
		</button>
	</div>
	 -->
</div> <!-- Closes buttons -->
		
<div id="releaseMarksPanel"></div>
		
<div>
	 <lams:WaitingSpinner id="markChartBusy"/>
	 <div id="markChartDiv" class="markChartDiv" style="display:none"></div>
</div>

<c:if test="${usesWeights}">
	<div id="weights" class="grid-holder voffset20" style="display:none" >
		<div class="panel panel-default">
			<div class="panel-heading">
				<fmt:message key="label.weights.title"/>
			</div>
			<div class="panel-body">
				<%-- Display weights in four columns --%>
				<c:forEach var="weightArray" items="${weights}" varStatus="weightCounter">
					<c:if test="${(weightCounter.index mod 3) == 0}">
						<c:if test="${weightCounter.index gt 0}">
							</div>
						</c:if>
						<div class="row">
					</c:if>
					<div class="col-sm-4">${weightArray[0]}: ${weightArray[2]}</div>
				</c:forEach>
				</div> <%-- close off row started in the loop --%>
			</div>
		</div>	
	</div>	
</c:if>

<div class="grid-holder voffset20 mt-5">
	<table id="userView" class="scroll" ></table>
	<div id="userViewPager" class="scroll" ></div>
	
	<br />
	<br />
	
	<table id="activityView" class="scroll" ></table>
	<div id="activityViewPager" class="scroll" ></div>
</div>