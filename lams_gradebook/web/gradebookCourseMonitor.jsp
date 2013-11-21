<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.courseMonitor"/></title>
	
	<lams:css />
	<link type="text/css" href="includes/css/gradebook.css" rel="stylesheet" />
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="includes/javascript/blockexportbutton.js"></script>

	<script type="text/javascript">
	
		jQuery(document).ready(function(){

			// Create the lesson view grid with sub grid for users	
			jQuery("#organisationGrid").jqGrid({
				caption: "<fmt:message key="gradebook.gridtitle.lesson.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=monCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: 660,
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				colNames:[
					'', 
					"<fmt:message key="gradebook.columntitle.lessonName"/>", 
					"<fmt:message key="gradebook.columntitle.subGroup"/>", 
					"<fmt:message key="gradebook.columntitle.startDate"/>", 
					"<fmt:message key="gradebook.columntitle.averageTimeTaken"/>", 
			    	"<fmt:message key="gradebook.columntitle.averageMark"/>"
				],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowName',index:'rowName', sortable:true, editable:false},
			      {name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false, width:80},
			      {name:'startDate',index:'startDate', sortable:false, editable:false, search:false, width:80, align:"center"},
			      {name:'avgTimeTaken',index:'avgTimeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'avgMark',index:'avgMark', sortable:true, editable:false, search:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#organisationGrid").clearGridData();
			    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var lessonID = jQuery("#organisationGrid").getRowData(row_id)["id"];
				   subgrid_table_id = subgrid_id+"_t";
				   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
				   jQuery("#"+subgrid_table_id).jqGrid({
					     datatype: "xml",
					     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monCourse&lessonID=" + lessonID,
					     height: "100%",
					     autowidth:true,
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=" + lessonID,
					     sortorder: "asc", 
						 sortname: "rowName", 
						 pager: subgrid_table_id + "_pager",
						 rowList:[5,10,20,30],
						 rowNum:10,
					     colNames: [
					     	'',
					     	"<fmt:message key="gradebook.columntitle.learnerName"/>",
					     	"<fmt:message key="gradebook.columntitle.progress"/>", 
					     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
					     	"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
			    			"<fmt:message key="gradebook.columntitle.mark"/>"
					     ],
					     colModel:[
					     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'rowName',index:'rowName', sortable:true, editable:false},
					      	{name:'status', index:'status', sortable:false, editable:false, search:false, title:false, width:50, align:"center"},
					      	{name:'timeTaken', index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false},
					     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
					     ],
					     loadError: function(xhr,st,err) {
				    		jQuery("#"+subgrid_table_id).clearGridData();
				    		info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
				    	 },
				    	 formatCell: function(rowid, cellname,value, iRow, iCol) {
					    	 if (cellname == "mark") {
					    	 		
					    	 	var rowData = jQuery("#"+subgrid_table_id).getRowData(rowid);
					    	 	var string = removeHTMLTags(rowData["mark"]);
					    	 		
					    	 	if (string.indexOf("-") != -1){
					    	 		string = " ";
					    	 	}
					    	 		
					    	 	return string;
					    	 		
					    	 }
					     },
					     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
					     	
					     	// update the lesson average mark
					     	if (cellname == "mark") {
					     		// Update the average activity mark
						     	$.get("<lams:LAMSURL/>/gradebook/gradebook.do", {dispatch:"getLessonMarkAverage", lessonID:lessonID}, function(xml) {
							    	if (xml!=null) {
							    		jQuery("#organisationGrid").setCell(row_id, "avgMark", xml, "", "");
							    	} 
							    });
					     	}
					     	$("#userView").trigger("reloadGrid");
					     },
						 gridComplete: function(){
						 	toolTip($(".jqgrow"));	// enable tooltips for grid
						 }	
					 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false})
					 
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
					  jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
						caption: "",
						buttonimg:"<lams:LAMSURL />images/table_edit.png", 
						onClickButton: function(){
							jQuery("#"+subgrid_table_id).setColumns();
						}
					  });
					},
					gridComplete: function(){
						toolTip($(".jqgrow"));	// enable tooltips for grid
					}	
			}).navGrid("#organisationGridPager", {edit:false,add:false,del:false,search:false})
				
			jQuery("#organisationGrid").navButtonAdd("#organisationGridPager",{
				caption: "",
				buttonimg:"<lams:LAMSURL />images/find.png", 
				onClickButton: function(){
					jQuery("#organisationGrid").searchGrid({
						top:10, 
						left:10,
						sopt:['cn','bw','eq','ne','ew']
					});
				}
			});

			jQuery("#organisationGrid").navButtonAdd('#organisationGridPager',{
				caption: "",
				buttonimg:"<lams:LAMSURL />images/table_edit.png", 
				onClickButton: function(){
					jQuery("#organisationGrid").setColumns();
				}
			});
			
			// Create the user view grid with sub grid for lessons
			jQuery("#userView").jqGrid({
				caption: "<fmt:message key="gradebook.gridtitle.learner.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: 660,
			    sortorder: "asc", 
			    sortname: "rowName", 
			    pager: 'userViewPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
			    colNames:[
					'',
					"<fmt:message key="gradebook.columntitle.learnerName"/>",
					"<fmt:message key="gradebook.columntitle.progress"/>", 
					"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
					"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
			    	"<fmt:message key="gradebook.columntitle.mark"/>"
			    ],
			    colModel:[
					{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					{name:'rowName',index:'rowName', sortable:true, editable:false},
					{name:'status', index:'status', sortable:false, editable:false, search:false, title:false, width:50, align:"center", hidden:true},
					{name:'timeTaken', index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center", hidden:true},
					{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false, hidden:true},
					{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center", hidden:true}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#userView").clearGridData();
			    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var userID = jQuery("#userView").getRowData(row_id)["id"];
				   subgrid_table_id = subgrid_id+"_t";
					 jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   	jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=monUserView&organisationID=${organisationID}&userID=" + userID,
						     height: "100%",
						     autowidth:true,
						     cellEdit:true,
						     pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     cellurl: "", //will be updated dynamically
						     colNames: [
								'', 
								"<fmt:message key="gradebook.columntitle.lessonName"/>", 
								"<fmt:message key="gradebook.columntitle.subGroup"/>", 
								"<fmt:message key="gradebook.columntitle.progress"/>", 
								"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
								"<fmt:message key="gradebook.columntitle.startDate"/>", 
								"<fmt:message key="gradebook.columntitle.completeDate"/>", 
								"<fmt:message key="gradebook.columntitle.averageTimeTaken"/>", 
								"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
								"<fmt:message key="gradebook.columntitle.averageMark"/>", 
								"<fmt:message key="gradebook.columntitle.mark"/>"
						     ],
						     colModel: [
								{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
								{name:'rowName',index:'rowName', sortable:true, editable:false, width:150},
								{name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false, width:130},
								{name:'status',index:'status', sortable:false, editable:false, search:false, width:60, align:"center"},
								{name:'feedback',  index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, width:150},
								{name:'startDate',index:'startDate', sortable:false, editable:false, hidden:true, search:false},
								{name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:true, search:false},
								{name:'averageTimeTaken',index:'averageTimeTaken', sortable:true, hidden:true, editable:false, search:false, width:80, align:"center"},
								{name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false, width:80, align:"center"},
								{name:'averageMark',index:'averageMark', sortable:true, editable:false, hidden:true, search:false, width:50, align:"center"},
								{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:60, align:"center"}
						     ],
						     loadError: function(xhr,st,err) {
						    	jQuery("#"+subgrid_table_id).clearGridData();
						    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
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
						     				info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.markhigher"/>", "<fmt:message key="label.ok"/>");
						     				jQuery("#"+subgrid_table_id).restoreCell( iRow, iCol);
						     				throw("Mark must be lower than maximum mark");
						     			}
						     		}
					    	 	}
					    	 	
					    	 	//modify cellurl setting to include lessonid
					    	 	var lessonID = jQuery("#"+subgrid_table_id).getRowData(rowid)["id"];
					    	 	$("#"+subgrid_table_id).setGridParam({cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=" + lessonID + "&id=" + userID});
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
							     	var lessonID = jQuery("#"+subgrid_table_id).getRowData(rowid)["id"];
							     	$.get("<lams:LAMSURL/>/gradebook/gradebook.do", {dispatch:"getLessonMarkAggregate", lessonID:lessonID, userID:userID}, function(xml) {
								    	if (xml!=null) {
								    		jQuery("#userView").setCell(row_id, "mark", xml, "", "");
								    	} 
								    });							     	
						     	}
						     	$("#organisationGrid").trigger("reloadGrid");
						     },
						     errorCell: function(serverresponse, status) {
						     	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.cellsave"/>", "<fmt:message key="label.ok"/>");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"));
							 }
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
					  	// Adding button for show/hiding collumn
					  	jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
							caption: "",
							buttonimg:"<lams:LAMSURL />images/table_edit.png", 
							onClickButton: function(){
								jQuery("#"+subgrid_table_id).setColumns();
							}
						});
					},
					gridComplete: function(){
						toolTip($(".jqgrow"));  // allowing tooltips for this grid	
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
				
			// Allowing column editing for this grid
			jQuery("#userView").navButtonAdd('#userViewPager',{
				caption: "",
				buttonimg:"<lams:LAMSURL />images/table_edit.png", 
				onClickButton: function(){
					jQuery("#userView").setColumns();
				}
			});
			
			//initialize lesson list 
			jQuery("#lessons-jqgrid").jqGrid({
				datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=monCourse&organisationID=${organisationID}",
				colNames:['Id', '<fmt:message key="gradebook.columntitle.lessonName"/>'],
				colModel:[
					{name:'id',index:'id', width:35, sorttype:"int", hidden:true},
				   	{name:'rowName',index:'rowName', width:225, firstsortorder:'desc', sorttype: 'text'}
				],
				rowNum: 10000,
				sortname: 'id',
				multiselect: true,
				sortorder: "asc",
				height:'auto',
				ignoreCase: true
			});
			jQuery("#lessons-jqgrid").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: true, defaultSearch: 'cn'});

			var languageLabelWait = "<fmt:message key='gradebook.coursemonitor.wait'/>";
			
			$("#export-course-button").click(function() {
				var areaToBlock = "export-link-area";
				var exportExcelUrl = "<lams:WebAppURL/>/gradebookMonitoring.do?dispatch=exportExcelCourseGradebook&organisationID=${organisationID}";
				blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait);
				
				return false;
			});
			
			$("#export-selected-lessons-button").click(function() {
				
				var ids = jQuery("#lessons-jqgrid").getGridParam('selarrrow');
				// if at least one lesson selceted do export
				if(ids.length) {
					var lessonIds = "";
				    for (var i=0; i<ids.length; i++) {
				    	lessonIds += "&lessonID=" + ids[i];
				    }
					
					var areaToBlock = "select-lessons-area";
					var exportExcelUrl = "<lams:WebAppURL/>/gradebookMonitoring.do?dispatch=exportExcelSelectedLessons&organisationID=${organisationID}" + lessonIds;
					blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait);
				}
				
				return false;
			});
			
		});
		
		function openSelectLessonsArea() {
			$("#select-lessons-area").toggle("slow");
			return false;
		}

	</script>

</lams:head>

<body class="stripes" style="text-align: center">
	<div id="page">
	
		<div id="header-no-tabs"></div>
		
		<div id="content">
			<lams:help module="gradebook" page="Gradebook+Course+Monitor" style="no-tabs"/>
			<h1 class="no-tabs-below">
				<fmt:message key="gradebook.title.courseMonitor">
					<fmt:param>
						${organisationName}
					</fmt:param>
				</fmt:message>
			</h1>
			<br />
			
			<div id="export-link-area">
				<a href="#nogo" id="export-course-button">
					<fmt:message key="gradebook.export.excel.1" />
				</a> 
				<fmt:message key="gradebook.export.excel.2" />
			</div>
			
			<div>
				<a href="#nogo" onclick="return openSelectLessonsArea();" >
					<fmt:message key="label.select" />
				</a>
				<fmt:message key="label.lessons.to.export" />
			</div>
			
			<div id="select-lessons-area" >
				<table id="lessons-jqgrid" style="text-align: center;"></table>
				
				<input class="button" type="button" value="<fmt:message key="label.button.export"/>" id="export-selected-lessons-button" />			
			</div>
			
			<div class="grid-holder">
				<table id="organisationGrid" class="scroll"></table>
				<div id="organisationGridPager" class="scroll"></div>
				
				<br />
				<br />
				
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<div class="tooltip" id="tooltip"></div>
			</div>
		</div> <!-- Closes content -->
		
		<div id="footer">
		</div> <!--Closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>
