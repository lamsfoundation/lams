<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.lessonMonitor"/></title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<link type="text/css" href="<lams:LAMSURL/>css/chart.css" rel="stylesheet" />
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
 	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />gradebook/includes/javascript/blockexportbutton.js"></script>
	
	<script type="text/javascript">
	
		var marksReleased = ${marksReleased};
		var graphLoaded = false;

		function toggleMarkChart() {
			if ( $("#markChartDiv").css("display") == "none" ) {
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
			} else {
				$("#markChartDiv").css("display", "none");
				$("#markChartShown").css("display", "none");
				$("#markChartHidden").css("display", "inline");
			}
		}

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
			    		
			    		$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.releasemarks.fail"/>", "<fmt:message key="label.ok"/>");
			    	}
			    });
		    }
		}
		
		function displayReleaseOption() {
			if (marksReleased) {
				document.getElementById("marksReleased").style.display="inline";
				document.getElementById("marksNotReleased").style.display="none";
			} else {
				document.getElementById("marksReleased").style.display="none";
				document.getElementById("marksNotReleased").style.display="inline";
			}
		}
		
		// Show/hide the dates for the start and end of the lesson. 
		function toggleLessonDates(){
		    var colModel = jQuery("#userView").jqGrid('getGridParam', 'colModel'), 
		    	i, 
		    	l = colModel.length,
		    	hidden = false;
		    
	        for (i = 0; ! hidden && i < l; i++) {
	            var colItem = colModel[i];
	            var cmName = colItem.name;
	            if (cmName == 'startDate' || cmName == 'finishDate') {
	            	hidden = colItem.hidden;
	            }
	        }
	            
            if ( hidden ) {
				jQuery("#userView").jqGrid('showCol','startDate');
				jQuery("#userView").jqGrid('showCol','finishDate');
				document.getElementById("datesShown").style.display="inline";
				document.getElementById("datesNotShown").style.display="none";

            } else { 
				jQuery("#userView").jqGrid('hideCol','startDate');
				jQuery("#userView").jqGrid('hideCol','finishDate');
				document.getElementById("datesShown").style.display="none";
				document.getElementById("datesNotShown").style.display="inline";
            }

            resizeJqgrid(jQuery("#userView"));

		}

        function resizeJqgrid(jqgrids) {
            jqgrids.each(function(index) {
                var gridId = $(this).attr('id');
                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
            });
        };

		jQuery(document).ready(function(){
  
			var jqgridWidth = $(window).width() - 100;
			displayReleaseOption();
			
			// Create the user view grid with sub grid for activities	
			jQuery("#userView").jqGrid({
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
			    rowList:[5,10,20,30],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=${lessonDetails.lessonID}",
			    colNames:["", 
			    	"<fmt:message key="gradebook.columntitle.name"/>", 
			    	"<fmt:message key="gradebook.columntitle.progress"/>", 
			    	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
			    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
			    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
			    	"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
			    	"<fmt:message key="gradebook.columntitle.mark"/>"
			    ],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowNamer',index:'rowName', sortable:true, editable:false, autoencode:true},
			      {name:'status',index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
			      {name:'timeTaken',index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'startDate',index:'startDate', sortable:true, editable:false, hidden:false, search:false, width:85, align:"left"},
			      {name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:false, search:false, width:85, align:"left"},
			      {name:'feedback',index:'feedback', sortable:true, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, search:false },
			      {name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#userView").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var userID = jQuery("#userView").getRowData(row_id)["id"];
				   subgrid_table_id = subgrid_id+"_t";
					 jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   	jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
						     height: "100%",
						     autowidth:true,
						     cellEdit:true,
						     pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
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
						    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
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
						     				$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.markhigher"/>", "<fmt:message key="label.ok"/>");
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
						     	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.cellsave"/>", "<fmt:message key="label.ok"/>");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"));
							 }
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
					},
					gridComplete: function(){
						toolTip($(".jqgrow"));  // Allow tooltips for this grid	
						// Load dates shown but hide straight away as all columns needed initially so that subgrid is displayed properly LDEV-4289
						toggleLessonDates();  
					}
				}).navGrid("#userViewPager", {edit:false,add:false,del:false,search:false}); // applying refresh button
				
				// Allowing search for this grid
				jQuery("#userView").navButtonAdd('#userViewPager',{
					caption: "",
					title: "Search Names",
					buttonimg:"<lams:LAMSURL />images/find.png", 
					onClickButton: function(){
						jQuery("#userView").searchGrid({
								top:10, 
								left:10,
								sopt:['cn','bw','eq','ne','ew']
							});
						}
				});
								
				// Creating activity view with sub learner view
				jQuery("#activityView").jqGrid({
					caption: "<fmt:message key="gradebook.gridtitle.activitygrid"/>",
				    datatype: "xml",
				    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}",
				    height: "100%",
				    width: jqgridWidth,
				    shrinkToFit: false,
				    cellEdit: true,
				    pager: "activityViewPager",
				    rowList:[5,10,20,30],
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
			    		$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    	},
				    subGrid: true,
					subGridRowExpanded: function(subgrid_id, row_id) {
					   var subgrid_table_id;
					   var activityID = jQuery("#activityView").getRowData(row_id)["id"];
					   var groupID = jQuery("#activityView").getRowData(row_id)["groupId"];
					   subgrid_table_id = subgrid_id+"_t";
					   
					   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}&activityID=" + activityID + "&groupId=" + groupID,
						     height: "100%",
						     autowidth:true,
						     cellEdit:true,
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradebookData&lessonID=${lessonDetails.lessonID}&view=monActivityView&activityID=" + activityID,
						     sortorder: "asc", 
							 sortname: "fullName", 
							 pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
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
						     	"<fmt:message key="gradebook.columntitle.mark"/>"
						     ],
						     colModel:[
						     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
						     	{name:'marksAvailable',index:'marksAvailable', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
						     	{name:'rowName',index:'rowName', sortable:true, editable:false},
						      	{name:'status', index:'status', sortable:false, editable:false, search:false, width:30, align:"center"},
						      	{name:'timeTaken', index:'timeTaken', sortable:true, editable: false, width:80, align:"center"},
							    {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:85, align:"left"},
							    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
						     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false, width:200, hidden:true},
						     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
						     ],
						     loadError: function(xhr,st,err) {
					    		jQuery("#"+subgrid_table_id).clearGridData();
					    		$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
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
						     				$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.markhigher"/>", "<fmt:message key="label.ok"/>");
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
						     	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.cellsave"/>", "<fmt:message key="label.ok"/>");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"));	// applying tooltips for this grid
							 }
						 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}) // applying refresh button
						 
						 // allowing search for this grid
						 jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
								caption: "",
								buttonimg:"<lams:LAMSURL />images/find.png", 
								onClickButton: function(){
									jQuery("#"+subgrid_table_id).searchGrid({
																		top:10, 
																		left:10,
																		sopt:['cn','bw','eq','ne','ew']
																	});
								}
						  });
						  
					 },
					 gridComplete: function(){
					 	toolTip($(".jqgrow"));	// enable tooltips for grid
					 }	 
				}).navGrid("#activityViewPager", {edit:false,add:false,del:false,search:false}); // enable refresh button
				
				$("#export-grades-button").click(function() {
					
					var areaToBlock = "export-link-area";
					var exportExcelUrl = "<lams:WebAppURL/>/gradebookMonitoring.do?dispatch=exportExcelLessonGradebook&lessonID=${lessonDetails.lessonID}";
					var languageLabelWait = "<fmt:message key='gradebook.coursemonitor.wait'/>";
					blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait);
					
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

		});
	</script>
	
</lams:head>

<body class="stripes">

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

		
		<a target="_blank" class="btn btn-xs btn-default pull-right loffset5" title="<fmt:message key='button.help.tooltip'/>"
			   href="http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Lesson+Marking">
		<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span></a>

		<c:set var="btnclass" value="btn btn-xs btn-default"/>
		<div class="gbTopButtonsContainer" id="export-link-area" >
	</c:when>
	<c:otherwise>
		<c:set var="btnclass" value="btn btn-sm btn-default"/>
	 	<div class="gbTopButtonsContainerInTab" id="export-link-area" >
	</c:otherwise>
	</c:choose>

			<div>
				<a href="#nogo" id="export-grades-button" class="${btnclass}">
					<fmt:message key="gradebook.export.excel" />
				</a> 
			</div>
	 
			<div id="marksNotReleased" style="display:none">
				<a href="javascript:toggleRelease()" class="${btnclass}">
					<fmt:message key="gradebook.monitor.releasemarks.1" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
				</a>
			</div>
			<div id="marksReleased" style="display:none">
				<a href="javascript:toggleRelease()" class="${btnclass}">
					<fmt:message key="gradebook.monitor.releasemarks.2" />&nbsp;<fmt:message key="gradebook.monitor.releasemarks.3" />
				</a> 
			</div>
	 	
 			<div id="markChartShown" style="display:none">
				<a href="javascript:toggleMarkChart()" class="${btnclass}">
					<fmt:message key="label.hide.marks.chart"/>
				</a> 
			</div>
			<div id="markChartHidden">
				<a href="javascript:toggleMarkChart()" class="${btnclass}">
					<fmt:message key="label.show.marks.chart"/>
				</a> 
			</div>

		<c:if test="${isInTabs}">
	 		<div id="datesNotShown">
				<a class="${btnclass}" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.show.dates" /></a>
			</div>
			<div id="datesShown" style="display:none">
				<a class="${btnclass}" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.hide.dates" /></a>
			</div>
		</c:if>
		
		</div> <!-- Closes buttons -->

		<!-- not in tabs? go next to the help button -->
		<c:if test="${!isInTabs}">
	 		<div id="datesNotShown">
				<a class="${btnclass} pull-right btn-primary" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.show.dates" /></a>
			</div>
			<div id="datesShown" style="display:none">
				<a class="${btnclass} pull-right btn-primary" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.hide.dates" /></a>
			</div>
		</c:if>
					
			<div class="row">
				 <div class="col-xs-12">
				 <lams:WaitingSpinner id="markChartBusy"/>
 				 <div id="markChartDiv" class="markChartDiv" style="display:none"></div>
				</div>
			</div>
		
			<div class="grid-holder voffset20">
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<br />
				<br />
				
				<table id="activityView" class="scroll" ></table>
				<div id="activityViewPager" class="scroll" ></div>	
				
				<div class="tooltip" id="tooltip"></div>
				
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
