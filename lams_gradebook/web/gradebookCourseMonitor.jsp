<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
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
	
		var lessonDatesHidden = true;
		
		// Show/hide the dates for the start and end of the lesson. 
		function toggleLessonDates() {
			lessonDatesHidden = !lessonDatesHidden;
			jQuery(".ui-jqgrid-btable:visible").each(function(index) {
				var gridId = $(this).attr('id');
				processLessonDateFields(lessonDatesHidden, jQuery('#' + gridId));
            });
		}
		
		function processLessonDateFields( hide, grid ) {

 			if ( hide ) {
 				grid.jqGrid('hideCol','startDate');
 				grid.jqGrid('hideCol','finishDate');
				document.getElementById("datesShown").style.display="none";
				document.getElementById("datesNotShown").style.display="block";
	        } else { 
	        	grid.jqGrid('showCol','startDate');
	        	grid.jqGrid('showCol','finishDate');
				document.getElementById("datesShown").style.display="block";
				document.getElementById("datesNotShown").style.display="none";
	        }
	        resizeJqgrid(grid);
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
			
			// Create the lesson view grid with sub grid for users	
			jQuery("#organisationGrid").jqGrid({
				caption: "<fmt:message key="gradebook.gridtitle.lesson.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=monCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: jqgridWidth,
				shrinkToFit: false,
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
			      {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'avgTimeTaken',index:'avgTimeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'avgMark',index:'avgMark', sortable:true, editable:false, search:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#organisationGrid").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
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
					    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
					    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
					     	"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
			    			"<fmt:message key="gradebook.columntitle.mark"/>"
					     ],
					     colModel:[
					     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'rowName',index:'rowName', sortable:true, editable:false},
					      	{name:'status', index:'status', sortable:false, editable:false, search:false, title:false, width:50, align:"center"},
					      	{name:'timeTaken', index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
						    {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:85, align:"left"},
						    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false},
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
						     	$.get("<lams:LAMSURL/>/gradebook/gradebook.do", {dispatch:"getAverageMarkForLesson", lessonID:lessonID}, function(xml) {
							    	if (xml!=null) {
							    		jQuery("#organisationGrid").setCell(row_id, "avgMark", xml, "", "");
							    	} 
							    });
					     	}
					     	$("#userView").trigger("reloadGrid");
					     },
						 gridComplete: function(){
							processLessonDateFields( lessonDatesHidden, jQuery("#"+subgrid_table_id) );
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
					},
					gridComplete: function(){
						toolTip($(".jqgrow"));	// enable tooltips for grid
						processLessonDateFields( lessonDatesHidden, jQuery("#organisationGrid") ); // LDEV-4289 hide dates to start
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

			// Create the user view grid with sub grid for lessons
			jQuery("#userView").jqGrid({
				caption: "<fmt:message key="gradebook.gridtitle.learner.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=listView&organisationID=${organisationID}",
			    height: "100%",
			    width: jqgridWidth,
			    shrinkToFit: false,
			    sortorder: "asc", 
			    sortname: "rowName", 
			    pager: 'userViewPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
			    colNames:[
					'',
					"<fmt:message key="gradebook.columntitle.learnerName"/>"
			    ],
			    colModel:[
					{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					{name:'rowName',index:'rowName', sortable:true, editable:false}
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
								"<fmt:message key="gradebook.columntitle.startDate"/>", 
								"<fmt:message key="gradebook.columntitle.completeDate"/>", 
								"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
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
								{name:'startDate',index:'startDate', sortable:false, editable:false, search:false, width:85, align:"left"},
							    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
								{name:'feedback',  index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, width:150},
								{name:'medianTimeTaken',index:'medianTimeTaken', sortable:true, hidden:true, editable:false, search:false, width:80, align:"center"},
								{name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false, width:80, align:"center"},
								{name:'averageMark',index:'averageMark', sortable:true, editable:false, hidden:true, search:false, width:50, align:"center"},
								{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:60, align:"center"}
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
						     	}
						     	$("#organisationGrid").trigger("reloadGrid");
						     },
						     errorCell: function(serverresponse, status) {
						     	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.cellsave"/>", "<fmt:message key="label.ok"/>");
						     },
							 gridComplete: function(){
								processLessonDateFields( lessonDatesHidden, jQuery("#"+subgrid_table_id) );
							 	toolTip($(".jqgrow"));
							 }
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
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
			
			//initialize lesson list 
			jQuery("#lessons-jqgrid").jqGrid({
				datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=listView&organisationID=${organisationID}",
				colNames:['Id', '<fmt:message key="gradebook.columntitle.lessonName"/>'],
				colModel:[
					{name:'id',index:'id', width:35, sorttype:"int", hidden:true},
				   	{name:'rowName',index:'rowName', width:325, firstsortorder:'desc', sorttype: 'text'}
				],
				rowNum: 10000,
				sortname: 'id',
				multiselect: true,
				sortorder: "asc",
				height:'auto',
				ignoreCase: true
			});
			jQuery("#lessons-jqgrid").jqGrid('filterToolbar');

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
					var simplified = jQuery("#export-selected-simplified").prop('checked');
					simplified = "&simplified="+simplified;
					var exportExcelUrl = "<lams:WebAppURL/>/gradebookMonitoring.do?dispatch=exportExcelSelectedLessons"+simplified+"&organisationID=${organisationID}" + lessonIds;
					blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait);
				}
				
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
		
		function openSelectLessonsArea() {
			$("#select-lessons-area").toggle();
			return false;
		}
		
	</script>

</lams:head>

<body class="stripes">

	<lams:Page type="admin">

		<a target="_blank" class="btn btn-sm btn-default pull-right" title="<fmt:message key='button.help.tooltip'/>"
		   href="http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Course+Monitor">
			<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span>
		</a>

		<h4>
			<fmt:message key="gradebook.title.courseMonitor">
				<fmt:param>
					<c:out value="${organisationName}" escapeXml="true"/>
				</fmt:param>
			</fmt:message>
		</h4>
			
			<div id="export-link-area">
				<a href="#nogo" id="export-course-button" class="btn btn-xs btn-default">
					<fmt:message key="gradebook.export.excel" />
				</a>
			</div>
			
			<div class="voffset5">
				<a class="btn btn-xs btn-default" href="#nogo" onclick="return openSelectLessonsArea();" >
					<fmt:message key="label.select.lessons.to.export" />
				</a>
			</div>
			
			<div id="select-lessons-area" class="text-center">
				<table id="lessons-jqgrid" class="center-block"></table>
				<div class="checkbox input-sm"><label><input type="checkbox" id="export-selected-simplified"><fmt:message key="label.simplified.export"/></label></div>
				<input class="btn btn-sm btn-default" type="button" value="<fmt:message key="label.button.export"/>" id="export-selected-lessons-button" />			
			</div>

			<div id="datesNotShown">
				<a class="pull-right label label-primary" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.show.dates" /></a>
			</div>

			<div id="datesShown" style="display:none">
				<a class="pull-right label label-primary" href="javascript:toggleLessonDates()"><fmt:message key="gradebook.monitor.hide.dates" /></a>
			</div>			
					
			<div class="grid-holder voffset20">
				<table id="organisationGrid" class="scroll"></table>
				<div id="organisationGridPager" class="scroll"></div>
				
				<br />
				<br />
				
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<div class="tooltip" id="tooltip"></div>
			</div>
		
		<div id="footer">
		</div> <!--Closes footer-->
	</lams:Page> <!-- Closes page -->
</body>
</lams:html>
