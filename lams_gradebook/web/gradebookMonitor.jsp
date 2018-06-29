<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<c:set var="usesWeights">${not empty weights}</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.lessonMonitor"/></title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<lams:css suffix="chart"/>
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
 	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />gradebook/includes/javascript/blockexportbutton.js"></script>
	
	<script type="text/javascript">
	
		var marksReleased = ${marksReleased};
		var graphLoaded = false;
		var lessonDatesHidden = true;

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
						'<lams:LAMSURL/>/gradebook/gradebookMonitoring.do?dispatch=getMarkChartData&lessonID=${lessonDetails.lessonID}',
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


		function toggleRelease() {
			
			var conf;
			if (marksReleased) {
				conf = confirm("<fmt:message key="gradebook.monitor.releasemarks.check2"/>");
			} else {
				conf = confirm("<fmt:message key="gradebook.monitor.releasemarks.check"/>");
			}
			
			if (conf) {
				$.get("<lams:LAMSURL/>/gradebook/gradebookMonitoring.do", {dispatch:"toggleReleaseMarks", lessonID:"${lessonDetails.lessonID}"}, function(xml) {
					var str = new String(xml)
			    	if (str.indexOf("success") != -1) {
			    		
			    		if (marksReleased) {
			    			marksReleased = false;
			    		} else {
		    			marksReleased = true;
		    		}
		    		displayReleaseOption();
		    	} else {
		    		
		    		alert("<fmt:message key="error.releasemarks.fail"/>");
		    	}
			    });
		    }
		}
		
		function displayReleaseOption() {
			if (marksReleased) {
				document.getElementById("marksReleased").style.display="inline";
				document.getElementById("marksNotReleased").style.display="none";
				document.getElementById("padlockUnlocked").style.display="inline";
				document.getElementById("padlockLocked").style.display="none";
			} else {
				document.getElementById("marksReleased").style.display="none";
				document.getElementById("marksNotReleased").style.display="inline";
				document.getElementById("padlockUnlocked").style.display="none";
				document.getElementById("padlockLocked").style.display="inline";
			}
		}
		
		// Show/hide the dates for the start and end of the lesson. 
		function toggleLessonDates() {
			lessonDatesHidden = !lessonDatesHidden;
			processLessonDateFields( lessonDatesHidden ) 
		}
		
		function processLessonDateFields( hide ) {

            if ( hide ) {
				jQuery("#userView").jqGrid('hideCol','startDate');
				jQuery("#userView").jqGrid('hideCol','finishDate');
				document.getElementById("datesShown").style.display="none";
				document.getElementById("datesNotShown").style.display="inline";
            } else { 
				jQuery("#userView").jqGrid('showCol','startDate');
				jQuery("#userView").jqGrid('showCol','finishDate');
				document.getElementById("datesShown").style.display="inline";
				document.getElementById("datesNotShown").style.display="none";
            }

            resizeJqgrid(jQuery("#userView"));
 		}

		jQuery(document).ready(function(){
  
			var jqgridWidth = $(window).width() - 100;
			displayReleaseOption();
			
			// Create the user view grid with sub grid for activities	
			jQuery("#userView").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				autoencode:false,
				caption: "<fmt:message key="gradebook.gridtitle.usergrid"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monUserView&lessonID=${lessonDetails.lessonID}",
			    height: "100%",
			    width: jqgridWidth,
				shrinkToFit: false,
			    cellEdit: true,
			    viewrecords: true,
			    sortorder: "asc", 
			    sortname: "rowName", 
			    pager: 'userViewPager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=${lessonDetails.lessonID}",
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
			      {name:'rowNamer',index:'rowName', sortable:true, editable:false, autoencode:true, formatter:userNameFormatter},
			      {name:'status',index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
			      {name:'timeTaken',index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'startDate',index:'startDate', sortable:true, editable:false, hidden:false, search:false, width:85, align:"left"},
			      {name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:false, search:false, width:85, align:"left"},
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
							 guiStyle: "bootstrap",
							 iconSet: 'fontAwesome',
							 autoencode:false,
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
						     height: "100%",
						     autowidth:true,
						     cellEdit:true,
						     pager: subgrid_table_id + "_pager",
						     rowList:[10,20,30,40,50,100],
							 rowNum:10,
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradebookData&lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
						     colNames: [
						     	'',
						     	'',
						     	"<fmt:message key="gradebook.columntitle.activity"/>",
						     	"<fmt:message key="gradebook.columntitle.progress"/>",
						     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
						    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
						    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
						     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
						     	"<fmt:message key="gradebook.columntitle.mark"/>"
						     ],
						     colModel: [
						       	{name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
						       	{name:'marksAvailable',index:'marksAvailable', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
								{name:'rowName',  index:'rowName', sortable:false, editable: false},
								{name:'status',  index:'status', sortable:false, editable:false, width:50, align:"center"},
								{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:80, align:"center"},
							    {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:85, align:"left"},
							    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
								{name:'feedback',  index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, width:200, hidden:true},
								{name:'mark', index:'mark', sortable:true, editable: true, editrules:{number:true}, width:50, align:"center" }
						     ],
						     loadError: function(xhr,st,err) {
						    		jQuery("#"+subgrid_table_id).clearGridData();
						 	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
						     },
						     formatCell: function(rowid, cellname,value, iRow, iCol) {
					    	 	if (cellname == "mark") {
					    	 		
					    	 		var rowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					    	 		var string = removeHTMLTags(rowData["mark"]);
					    	 		
					    	 		
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
					    	 	value = trim(value);
					    	 	
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
							     	$.get("<lams:LAMSURL/>/gradebook/gradebook.do", {dispatch:"getLessonMarkAggregate", lessonID:"${lessonDetails.lessonID}", userID:row_id}, function(xml) {
								    	if (xml!=null) {
								    		jQuery("#userView").setCell(row_id, "mark", xml, "", "");
								    	} 
								    });
						     	}
						     },
						     errorCell: function(serverresponse, status) {
						     	alert("<fmt:message key="error.cellsave"/>");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"), "jqgridTooltip");
							 	fixPagerInCenter(subgrid_table_id+"_pager", 1);
							 },
							 subGrid : hasArchivedMarks,
							 subGridRowExpanded: function(subgrid_id, row_id) {
							    var subgrid_table_id = subgrid_id + "_t",
							   	    rowData = jQuery("#" + subgrid_id.substring(0, subgrid_id.lastIndexOf('_'))).getRowData(row_id),
							   	    activityID = rowData["id"].split("_")[0];
								jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
								jQuery("#"+subgrid_table_id).jqGrid({
										 guiStyle: "bootstrap",
										 iconSet: 'fontAwesome',
										 autoencode:false,
									     datatype: "xml",
									     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityArchiveGridData&lessonID=${lessonDetails.lessonID}&activityID="
										      + activityID + "&view=monUserView&userID=" + userID,
									     height: "100%",
									     autowidth:true,
									     cellEdit:false,
									     pager: false,
									     colNames: [
									    	"<fmt:message key="gradebook.columntitle.attempt"/>",
									    	"<fmt:message key="gradebook.columntitle.restart"/>",
									     	"<fmt:message key="gradebook.columntitle.lesson.mark"/>",
									     	"<fmt:message key="gradebook.columntitle.progress"/>",
									     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
									    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
									    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
									     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
									     	"<fmt:message key="gradebook.columntitle.mark"/>"
									     ],
									     colModel: [
									       	{name:'id', index:'id',  sortable:false, editable: false ,width:40, align:"right"},
									       	{name:'restart',index:'restart', sortable:false, editable: false, width:60,align:"left"},
											{name:'lessonMark',  index:'lessonMark', sortable:false, editable: false, width:50, align:"center" },
											{name:'status',  index:'status', sortable:false, editable:false, width:50, align:"center"},
											{name:'timeTaken',index:'timeTaken', sortable:false, editable: false, width:80, align:"center"},
										    {name:'startDate',index:'startDate', sortable:false, editable:false, search:false, width:85, align:"left"},
										    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
											{name:'feedback',  index:'feedback', sortable:false, editable: false, width:200, hidden:true},
											{name:'mark', index:'mark', sortable:false, editable: false, width:50, align:"center" }
									     ],
									     loadError: function(xhr,st,err) {
									    	jQuery("#"+subgrid_table_id).clearGridData();
									 	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
									     },
									     formatCell: function(rowid, cellname,value, iRow, iCol) {
								    	 	if (cellname == "mark") {
								    	 		
								    	 		var rowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
								    	 		var string = removeHTMLTags(rowData["mark"]);
								    	 		
								    	 		
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
										 gridComplete: function(){
										 	toolTip($(".jqgrow"), "jqgridTooltip");
										 }
								  	});
							}
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
					},
					gridComplete: function(){
						toolTip($(".jqgrow"), "jqgridTooltip");  // Allow tooltips for this grid	
				   	 	initializePortraitPopover('<lams:LAMSURL/>');
						// Load dates shown but hide straight away as all columns needed initially so that subgrid is displayed properly LDEV-4289
						processLessonDateFields( lessonDatesHidden );
					}
				}).navGrid("#userViewPager", {edit:false,add:false,del:false,search:false}); // applying refresh button
				jQuery("#userView").jqGrid('filterToolbar');

				// Creating activity view with sub learner view
				jQuery("#activityView").jqGrid({
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
					autoencode:false,
					caption: "<fmt:message key="gradebook.gridtitle.activitygrid"/>",
				    datatype: "xml",
				    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}",
				    height: "100%",
				    width: jqgridWidth,
				    shrinkToFit: false,
				    cellEdit: true,
				    pager: "activityViewPager",
				    rowList:[10,20,30,40,50,100],
				    	rowNum:10,
				    sortorder: "asc", 
				    sortname: "activityId", 
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
				    subGrid: true,
					subGridRowExpanded: function(subgrid_id, row_id) {
					   var subgrid_table_id;
					   var activityID = jQuery("#activityView").getRowData(row_id)["id"];
					   var groupID = jQuery("#activityView").getRowData(row_id)["groupId"];
					   subgrid_table_id = subgrid_id+"_t";
					   
					   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   jQuery("#"+subgrid_table_id).jqGrid({
 							 guiStyle: "bootstrap",
							 iconSet: 'fontAwesome',
							 autoencode:false,
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}&activityID=" + activityID + "&groupId=" + groupID,
						     height: "100%",
						     autowidth:true,
						     cellEdit:true,
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradebookData&lessonID=${lessonDetails.lessonID}&view=monActivityView&activityID=" + activityID,
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
						     	'activityURL'
						     ],
						     colModel:[
						     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
						     	{name:'marksAvailable',index:'marksAvailable', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
						     	{name:'rowName',index:'rowName', sortable:true, editable:false, formatter:userNameFormatterActivity},
						      	{name:'status', index:'status', sortable:false, editable:false, search:false, width:30, align:"center"},
						      	{name:'timeTaken', index:'timeTaken', sortable:true, editable: false, search:false, width:80, align:"center"},
							    {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:85, align:"left"},
							    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
						     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false, width:200, hidden:true},
						     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"},
						     	{name:'portraitId', index:'portraitId', width:0, hidden: true},
						     	{name:'activityURL', index:'activityURL', width:0, hidden: true}
						     ],
						     loadError: function(xhr,st,err) {
						    		jQuery("#"+subgrid_table_id).clearGridData();
						    		alert("<fmt:message key="gradebook.error.loaderror"/>");
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
					    	 	value = trim(value);
					    	 	
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
							     	$.get("<lams:LAMSURL/>/gradebook/gradebook.do", {dispatch:"getActivityMarkAverage", activityID:row_id}, function(xml) {
								    	if (xml!=null) {
								    		jQuery("#activityView").setCell(row_id, "avgMark", xml, "", "");
								    	} 
								    });
						     	}
						     },
						     errorCell: function(serverresponse, status) {
						     	alert("<fmt:message key="error.cellsave"/>");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"), "jqgridTooltip");	// applying tooltips for this grid
						   	 	initializePortraitPopover('<lams:LAMSURL/>');
						   	 	fixPagerInCenter(subgrid_table_id+"_pager", 1);
							 }
						 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}) // applying refresh button
						 jQuery("#"+subgrid_table_id).jqGrid('filterToolbar');

					},
					 gridComplete: function(){
					 	toolTip($(".jqgrow"), "jqgridTooltip");	// enable tooltips for grid
					 	fixPagerInCenter('activityViewPager', 0);
					 }	 
				}).navGrid("#activityViewPager", {edit:false,add:false,del:false,search:false}); // enable refresh button
				
				$("#export-grades-button").click(function() {
					var areaToBlock = "export-link-area";
					var exportExcelUrl = "<lams:WebAppURL/>/gradebookMonitoring.do?dispatch=exportExcelLessonGradebook&lessonID=${lessonDetails.lessonID}";
					var languageLabelWait = "<fmt:message key='gradebook.coursemonitor.wait'/>";
					// if exportSpan is hidden then icon only mode, use small font.
					blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait, $("#exportSpan:hidden").length > 0);
					
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

		        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

		});
		
	</script>
	
</lams:head>

<body class="stripes">

	<!--  The buttons are formatted and dispayed differently in the pop up vs monitoring version. Popup matches Course Gradebook, 
	-- Monitoring matches monitoring. So various button settings and the button codes are setup in advance and included where needed later.  -->
	
	<c:choose>
	<c:when test="${!isInTabs}">
		<c:set var="btnclass" value="btn btn-xs btn-default"/>
	</c:when>
	<c:otherwise>
		<c:set var="btnclass" value="btn btn-sm btn-default"/>
	</c:otherwise>
	</c:choose>
		
	<c:set var="tourDatesCode">
		<div id="tour-dates">
 		<div id="datesNotShown">
 			<a class="${btnclass}" href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.show.dates"/>" >
 			<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
			<fmt:message key="gradebook.monitor.show.dates" /></a>
			</span>
		</div>
		<div id="datesShown" style="display:none">
			<a class="${btnclass}" href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.hide.dates"/>" >
 			<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
			<fmt:message key="gradebook.monitor.hide.dates" /></a>
			</span>
		</div>
		</div>
	</c:set>
		
	<c:set var="lockLabelClass">${isInTabs?"lockLabel":""}</c:set>
	<c:set var="padlockCode">
		<div class="visible-xs-inline">
		<div id="padlockLocked" style="display:none">
			<span class="${lockLabelClass}">
			<i class="fa fa-lock"></i>
			<fmt:message key="label.marks"/>
			</span>
		</div>
		<div id="padlockUnlocked" style="display:none">
			<span class="${lockLabelClass}">
			<i class="fa fa-unlock"></i>
			<fmt:message key="label.marks"/>
			</span>
		</div>
		</div>
	</c:set>
			
			
	<c:set var="chartButtonCode">
		<div id="tour-mark-chart-button">
			<div id="markChartShown" style="display:none">
			<a href="javascript:toggleMarkChart()" class="${btnclass}" title="<fmt:message key='label.hide.marks.chart'/>" >
				<i class="fa fa-bar-chart"></i> <span class="hidden-xs">
				<fmt:message key="label.hide.marks.chart"/>
				</span>
			</a> 
		</div>
		<div id="markChartHidden">
			<a href="javascript:toggleMarkChart()" class="${btnclass}" title="<fmt:message key='label.show.marks.chart'/>" >
				<i class="fa fa-bar-chart"></i> <span class="hidden-xs">
				<fmt:message key="label.show.marks.chart"/>
				</span>
			</a> 
		</div>
		</div>
	</c:set>
	
	<c:if test="${usesWeights}">
		<c:set var="weightButtonCode">
			<div id="tour-weight-button">
				<div id="weightShown" style="display:none">
				<a href="javascript:toggleWeight()" class="${btnclass}" title="<fmt:message key='label.button.hide.weights'/>" >
					<i class="fa fa-balance-scale"></i> <span class="hidden-xs">
					<fmt:message key="label.button.hide.weights"/>
					</span>
				</a> 
				</div>
				<div id="weightHidden">
					<a href="javascript:toggleWeight()" class="${btnclass}" title="<fmt:message key='label.button.show.weights'/>" >
						<i class="fa fa-balance-scale"></i> <span class="hidden-xs">
						<fmt:message key="label.button.show.weights"/>
						</span>
					</a> 
				</div>
			</div>
		</c:set>
	</c:if>
	
	<c:choose>
	<c:when test="${!isInTabs}">
		<%-- replacement for Page type admin --%>
		<div class="row no-gutter no-margin">
		<div class="col-xs-12">
		<div class="container" id="content">

		<div class="panel panel-default panel-admin-page">
		<div class="panel-body panel-admin-body">

		<h4><fmt:message key="gradebook.title.lessonGradebook">
					<fmt:param>
						<c:out value="${lessonDetails.lessonName}" escapeXml="true"/>
					</fmt:param>
				</fmt:message></h4>

		<div class="gbTopButtonsContainer pull-right">
			${chartButtonCode}
			${weightButtonCode}
			${tourDatesCode}
			<a target="_blank" class="${btnclass}" title="<fmt:message key='button.help.tooltip'/>"
				   href="http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Lesson+Marking">
			<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span></a>
		</div>

		<div class="gbTopButtonsContainer pull-left" id="export-link-area">
			${padlockCode}
		
	</c:when>
	
	<c:otherwise>
	 	
		<div class="gbTopButtonsContainer pull-left">
			${padlockCode}
		</div>
		
		<div class="gbTopButtonsContainer pull-right" id="export-link-area">
	 	
	</c:otherwise>
	</c:choose>

			<div>
				<a href="#nogo" id="export-grades-button" class="${btnclass}" title="<fmt:message key='gradebook.export.excel'/>" >
					<i class="fa fa-download"></i><span id="exportSpan" class="hidden-xs">
					<fmt:message key="gradebook.export.excel" />
					</span>
				</a> 
			</div>
	 
	 		<div id="tour-release-marks">
			<div id="marksNotReleased" style="display:none">
				<a href="javascript:toggleRelease()" class="${btnclass}" 
					title="<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
					<i class="fa fa-share-alt "></i> <span class="hidden-xs">
					<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
					</span>
				</a>
			</div>
			<div id="marksReleased" style="display:none" class="tour-release-marks">
				<a href="javascript:toggleRelease()" class="${btnclass}" 
					title="<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />" >
					<i class="fa fa-share-alt "></i> <span class="hidden-xs">
					<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" /></span>
				</a> 
			</div>
			</div>

		<c:if test="${isInTabs}">
	 		${chartButtonCode}
	 		${weightButtonCode}
			${tourDatesCode}
		</c:if>
		
		</div> <!-- Closes buttons -->
		
			<div class="row">
				 <div class="col-xs-12">
				 <lams:WaitingSpinner id="markChartBusy"/>
 				 <div id="markChartDiv" class="markChartDiv" style="display:none"></div>
				</div>
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
						<c:if test="${weightCounter.index gt 0}"></div></c:if>
						<div class="row">
					</c:if>
					<div class="col-sm-4">${weightArray[0]}: ${weightArray[2]}</div>
				</c:forEach>
				</div> <%-- close off row started in the loop --%>
				</div>
			</div>	
			</div>	
			</c:if>
			
			<div class="grid-holder voffset20">
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<br />
				<br />
				
				<table id="activityView" class="scroll" ></table>
				<div id="activityViewPager" class="scroll" ></div>	
				
				<%-- not #tooltip as it conflicts with the learner progress tooltip --%>
				<div class="tooltip-lg" id="jqgridTooltip"></div> 
			</div>
	 
	<c:if test="not isInTabs">
 		</div>
 		</div>
 		</div>
		</div>
		</div>	
	</c:if>

</body>
</lams:html>
