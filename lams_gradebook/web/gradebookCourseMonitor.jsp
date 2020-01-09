<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.courseMonitor"/></title>
	
	<lams:css />
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/portrait.js"></script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/blockexportbutton.js"></script>

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
				document.getElementById("datesNotShown").style.display="inline";
	        } else { 
		        grid.jqGrid('showCol','startDate');
		       	grid.jqGrid('showCol','finishDate');
				document.getElementById("datesShown").style.display="inline";
				document.getElementById("datesNotShown").style.display="none";
	        }
	        resizeJqgrid(grid);
 		}
		
		jQuery(document).ready(function(){

			var jqgridWidth = $(window).width() - 100;
			
			// Create the lesson view grid with sub grid for users	
			jQuery("#organisationGrid").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				autoencode:false,
				caption: "<fmt:message key="gradebook.gridtitle.lesson.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />gradebook/gradebook/getCourseGridData.do?view=monCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: jqgridWidth,
				shrinkToFit: false,
				cmTemplate: { title: false },
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
				multiselect: true,
				multiPageSelection : true,
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
			   	 	alert("<fmt:message key="gradebook.error.loaderror"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgrid_table_id = subgrid_id+"_t";
						lessonID = jQuery("#organisationGrid").getRowData(row_id)["id"];
				   
					jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					jQuery("#"+subgrid_table_id).jqGrid({
						 guiStyle: "bootstrap",
						 iconSet: 'fontAwesome',
						 autoencode:false,
					     datatype: "xml",
					     url: "<lams:LAMSURL />gradebook/gradebook/getUserGridData.do?view=monCourse&lessonID=" + lessonID,
					     height: "100%",
					     autowidth:true,
					     cmTemplate: { title: false },
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />gradebook/gradebookMonitoring/updateUserLessonGradebookData.do?<csrf:token/>&lessonID=" + lessonID,
					     sortorder: "asc", 
						 sortname: "rowName", 
						 pager: subgrid_table_id + "_pager",
					     rowList:[10,20,30,40,50,100],
						 rowNum:10,
					     colNames: [
					     	'',
					     	"<fmt:message key="gradebook.columntitle.learnerName"/>",
					     	"<fmt:message key="gradebook.columntitle.progress"/>", 
					     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
					    	"<fmt:message key="gradebook.columntitle.startDate"/>", 
					    	"<fmt:message key="gradebook.columntitle.completeDate"/>", 
					     	"<fmt:message key="gradebook.columntitle.lessonFeedback"/>", 
			    			"<fmt:message key="gradebook.columntitle.mark"/>",
			    			'portraitId'
					     ],
					     colModel:[
					     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'rowName',index:'rowName', sortable:true, editable:false, formatter:userNameFormatter},
					      	{name:'status', index:'status', sortable:false, editable:false, search:false, title:false, width:50, align:"center"},
					      	{name:'timeTaken', index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
						    {name:'startDate',index:'startDate', sortable:true, editable:false, search:false, width:85, align:"left"},
						    {name:'finishDate',index:'finishDate', sortable:false, editable:false, search:false, width:85, align:"left"},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false},
					     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"},
					     	{name:'portraitId', index:'portraitId', width:0, hidden: true}
					     ],
					     loadError: function(xhr,st,err) {
				    		jQuery("#"+subgrid_table_id).clearGridData();
				    		alert("<fmt:message key="gradebook.error.loaderror"/>");
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
						     	$.get("<lams:LAMSURL/>gradebook/gradebook/getAverageMarkForLesson.do", {lessonID:lessonID}, function(xml) {
							    	if (xml!=null) {
							    		jQuery("#organisationGrid").setCell(row_id, "avgMark", xml, "", "");
							    	} 
							    });
					     	}
					     	$("#userView").trigger("reloadGrid");
					     },
						 gridComplete: function(){
							fixArrowColumn(subgrid_id);
							processLessonDateFields( lessonDatesHidden, jQuery("#"+subgrid_table_id) );
							fixPagerInCenter(subgrid_table_id+"_pager",2);
							initializePortraitPopover('<lams:LAMSURL/>');
						 }	
					 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false})
					jQuery("#"+subgrid_table_id).jqGrid('filterToolbar');

					},
					gridComplete: function(){
						fixPagerInCenter('organisationGridPager', 0);
						processLessonDateFields( lessonDatesHidden, jQuery("#organisationGrid") ); // LDEV-4289 hide dates to start
						hideShowLessonCheckboxes(); 
					}	
			}).navGrid("#organisationGridPager", {edit:false,add:false,del:false,search:false})
			jQuery("#organisationGrid").jqGrid('filterToolbar');

			// Create the user view grid with sub grid for lessons
			jQuery("#userView").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				autoencode:false,
				caption: "<fmt:message key="gradebook.gridtitle.learner.view"/>",
			    datatype: "xml",
			    url: "<lams:LAMSURL />gradebook/gradebook/getUserGridData.do?view=listView&organisationID=${organisationID}",
			    height: "100%",
			    width: jqgridWidth,
			    shrinkToFit: false,
			    cmTemplate: { title: false },
			    sortorder: "asc", 
			    sortname: "rowName", 
			    pager: 'userViewPager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    colNames:[
					'',
					"<fmt:message key="gradebook.columntitle.learnerName"/>",
					'portraitId'
			    ],
			    colModel:[
					{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					{name:'rowName',index:'rowName', sortable:true, editable:false, formatter:userNameFormatter},
					{name:'portraitId', index:'portraitId', width:0, hidden: true}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#userView").clearGridData();
			    	alert("<fmt:message key="gradebook.error.loaderror"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgrid_table_id;
					var userID = jQuery("#userView").getRowData(row_id)["id"];
					subgrid_table_id = subgrid_id+"_t";
					jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					jQuery("#"+subgrid_table_id).jqGrid({
							 guiStyle: "bootstrap",
							 iconSet: 'fontAwesome',
							 autoencode:false,
						     datatype: "xml",
						     url: "<lams:LAMSURL />gradebook/gradebook/getCourseGridData.do?view=monUserView&organisationID=${organisationID}&userID=" + userID,
						     height: "100%",
						     autowidth:true,
						     cmTemplate: { title: false },
						     cellEdit:true,
						     pager: subgrid_table_id + "_pager",
						     rowList:[10,20,30,40,50,100],
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
					    	 	
					    	 	//modify cellurl setting to include lessonid
					    	 	var lessonID = jQuery("#"+subgrid_table_id).getRowData(rowid)["id"];
					    	 	$("#"+subgrid_table_id).setGridParam({cellurl: "<lams:LAMSURL />gradebook/gradebookMonitoring/updateUserLessonGradebookData.do?<csrf:token/>&lessonID=" + lessonID + "&id=" + userID});
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
						     	alert("<fmt:message key="error.cellsave"/>");
						     },
							 gridComplete: function(){
								processLessonDateFields( lessonDatesHidden, jQuery("#"+subgrid_table_id) );
								fixPagerInCenter(subgrid_table_id+"_pager",1);
							 }
					}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
				},
				gridComplete: function(){
					fixPagerInCenter('userViewPager',0);
					initializePortraitPopover('<lams:LAMSURL/>');
				}
			}).navGrid("#userViewPager", {edit:false,add:false,del:false,search:false}); // applying refresh button
			jQuery("#userView").jqGrid('filterToolbar');
			
			var languageLabelWait = "<fmt:message key='gradebook.coursemonitor.wait'/>";
			
			$("#export-course-button").click(function() {
				var areaToBlock = "export-link-area";
				var exportExcelUrl = "<lams:WebAppURL/>gradebookMonitoring/exportExcelCourseGradebook.do?<csrf:token/>&organisationID=${organisationID}";
				blockExportButton(areaToBlock, exportExcelUrl, languageLabelWait);
				
				return false;
			});
			
			$("#export-selected-lessons-button").click(function() {
				
				var ids = jQuery("#organisationGrid").getGridParam('selarrrow');
				// if at least one lesson selceted do export
				if(ids.length) {
					var lessonIds = "";
				    for (var i=0; i<ids.length; i++) {
				    	lessonIds += "&lessonID=" + ids[i];
				    }
					
					var areaToBlock = "select-lessons-area";
					var simplified = jQuery("#export-selected-simplified").prop('checked');
					simplified = "simplified="+simplified;
					var exportExcelUrl = "<lams:WebAppURL/>gradebookMonitoring/exportExcelSelectedLessons.do?<csrf:token/>&"+simplified+"&organisationID=${organisationID}" + lessonIds;
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
			if ( hideShowLessonCheckboxes() ) {
				document.getElementById("selectLessonsShown").style.display="inline";
				document.getElementById("selectLessonsNotShown").style.display="none";
			} else {
				document.getElementById("selectLessonsNotShown").style.display="inline";
				document.getElementById("selectLessonsShown").style.display="none";
			}
			return false;
		}
				
		// only show the selection checkboxes when the select lesson for export is shown
		// returns true if checkboxes are shown
		function hideShowLessonCheckboxes() {
			if ( $("#select-lessons-area").css('display') == 'none' ) {
				jQuery("#organisationGrid").jqGrid('hideCol', 'cb');
				return false;
			}
			jQuery("#organisationGrid").jqGrid('showCol', 'cb');
			return true;
		}

		// the location of the subgrids "look into here" arrow is thrown out by showing/hiding checkboxes
		// so show/hide the first column in the subgrid to match
		function fixArrowColumn(subgridName) {
			if ( $("#select-lessons-area").css('display') == 'none' ) {
				jQuery("#"+subgridName).parent().siblings("td:first").css( "display", "none" );
			} 		
		}
		

	  	function userNameFormatter (cellvalue, options, rowObject) {
	  		var index = rowObject.length - 1;
			return definePortraitPopover(rowObject[index].innerHTML, rowObject[0].innerHTML, cellvalue, cellvalue, true);
		}
		
	</script>

</lams:head>

<body class="stripes">

	<lams:Page type="admin">

		<h4>
			<fmt:message key="gradebook.title.courseMonitor">
				<fmt:param>
					<c:out value="${organisationName}" escapeXml="true"/>
				</fmt:param>
			</fmt:message>
		</h4>
		
		<c:set var="btnclass" value="btn btn-xs btn-default"/>

		<a target="_blank" class="${btnclass} pull-right loffset5" title="<fmt:message key='button.help.tooltip'/>"
		   href="http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Course+Monitor">
			<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span>
		</a>

		<div id="datesNotShown">
			<a class="${btnclass} pull-right btn-primary" href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.show.dates" />">
				<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
				<fmt:message key="gradebook.monitor.show.dates" /></span>
			</a>
		</div>

		<div id="datesShown" style="display:none">
			<a class="${btnclass} pull-right btn-primary" href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.hide.dates" />">
				<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
				<fmt:message key="gradebook.monitor.hide.dates" /></span>
			</a>
		</div>			
				

		<div id="export-link-area" class="gbTopButtonsContainer">
		<div>
			<a href="#nogo" id="export-course-button" class="${btnclass}" title="<fmt:message key="gradebook.export.excel" />">
				<i class="fa fa-download"></i><span class="hidden-xs">
				<fmt:message key="gradebook.export.excel" />
				</span>
			</a>
		</div>

		<div id="selectLessonsNotShown">
			<a class="${btnclass}" href="#nogo" onclick="return openSelectLessonsArea();" title="<fmt:message key="label.select.lessons.to.export" />" >
				<i class="fa fa-square-o"></i><span class="hidden-xs">
				<fmt:message key="label.select.lessons.to.export" />
				</span>
			</a>
		</div>
		<div id="selectLessonsShown" style="display:none">
			<a class="${btnclass}" href="#nogo" onclick="return openSelectLessonsArea();" title="<fmt:message key="label.hide.lessons.to.export " />" >
				<i class="fa fa-square-o"></i><span class="hidden-xs">
				<fmt:message key="label.hide.lessons.to.export" />
				</span>
			</a>
		</div>

		</div>

		<div id="select-lessons-area" class="voffset5 form-inline">
			<input class="${btnclass}" type="button" value="<fmt:message key="label.button.export"/>" id="export-selected-lessons-button" />
			<div class="checkbox input-sm loffset5"><label><input type="checkbox" id="export-selected-simplified">&nbsp;<fmt:message key="label.simplified.export"/></label></div>
			<div><fmt:message key="gradebook.export.desc"/></div>
		</div>

		<div class="grid-holder voffset20">
			<table id="organisationGrid" class="scroll"></table>
			<div id="organisationGridPager" class="scroll"></div>
			
			<br />
			<br />
			
			<table id="userView" class="scroll" ></table>
			<div id="userViewPager" class="scroll" ></div>
			
			<div class="tooltip-lg" id="tooltip"></div>
		</div>
		
		<div id="footer">
		</div> <!--Closes footer-->
	</lams:Page> <!-- Closes page -->
</body>
</lams:html>
