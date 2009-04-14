<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.lessonMonitor"/></title>
	<lams:css/>
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	
	<script type="text/javascript">
	
		jQuery(document).ready(function(){
  
			// Create the user view grid with sub grid for activities	
			jQuery("#userView").jqGrid({
				caption: '<fmt:message key="gradebook.gridtitle.usergrid"/>',
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monUserView&lessonID=${lessonDetails.lessonID}",
			    height: "100%",
			    width: 700,
			    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
			    cellEdit: true,
			    viewrecords: true,
			    sortorder: "asc", 
			    sortname: "rowName", 
			    pager: 'userViewPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=${lessonDetails.lessonID}",
			    colNames:["", 
			    	'<fmt:message key="gradebook.columntitle.name"/>', 
			    	'<fmt:message key="gradebook.columntitle.progress"/>', 
			    	'<fmt:message key="gradebook.columntitle.timeTaken"/>', 
			    	'<fmt:message key="gradebook.columntitle.lessonFeedBack"/>', 
			    	'<fmt:message key="gradebook.columntitle.mark"/>'
			    ],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowName',index:'rowName', sortable:true, editable:false},
			      {name:'status',index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
			      {name:'timeTaken',index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, search:false },
			      {name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#userView").clearGridData();
			    	alert('<fmt:message key="gradebook.error.loaderror"/>');
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
						     width: 650,
						     cellEdit:true,
						     sortname: "id",
						     imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
						     pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradebookData&lessonID=${lessonDetails.lessonID}&view=monUserView&userID=" + userID,
						     colNames: [
						     	'',
						     	'<fmt:message key="gradebook.columntitle.activity"/>',
						     	'<fmt:message key="gradebook.columntitle.progress"/>',
						     	'<fmt:message key="gradebook.columntitle.outputs"/>', 
						     	'<fmt:message key="gradebook.columntitle.competences"/>', 
						     	'<fmt:message key="gradebook.columntitle.timeTaken"/>', 
						     	'<fmt:message key="gradebook.columntitle.activityFeedback"/>', 
						     	'<fmt:message key="gradebook.columntitle.mark"/>'
						     ],
						     colModel: [
						       	{name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
								{name:'rowName',  index:'rowName', sortable:false, editable: false},
								{name:'status',  index:'status', sortable:false, editable:false, width:50, align:"center"},
								{name:'output',  index:'output', sortable:false, editable: false, width:200},
								{name:'competences',  index:'competences', sortable:false, editable: false, hidden:true},
								{name:'timeTaken',index:'timeTaken', sortable:true, editable: false, width:80, align:"center"},
								{name:'feedback',  index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}, width:200},
								{name:'mark', index:'mark', sortable:true, editable: true, editrules:{number:true}, width:50, align:"center" }
						     ],
						     loadError: function(xhr,st,err) {
						    	jQuery("#"+subgrid_table_id).clearGridData();
						    	alert('<fmt:message key="gradebook.error.loaderror"/>');
						     },
						     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
						     	
						     	// Update the total lesson mark for the user
						     	if (cellname == "mark") {
							     	var ids = jQuery("#"+subgrid_table_id).getDataIDs()
							     	var totalMark = 0.0;
							     	for (var i=0; i < ids.length; i++) {
							     		var rowData = jQuery("#"+subgrid_table_id).getRowData(ids[i]);
							     		var userMark = rowData["mark"];
							     		
							     		if (userMark != "-") {
							     			totalMark += parseFloat(userMark);
							     		}
							     	}
							     	jQuery("#userView").setCell(row_id, "mark", totalMark, "", "");
						     	}
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
				
				
				// Creating activity view with sub learner view
				jQuery("#activityView").jqGrid({
					caption: '<fmt:message key="gradebook.gridtitle.activitygrid"/>',
				    datatype: "xml",
				    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}",
				    height: "100%",
				    width: 700,
				    cellEdit: true,
				    pager: "activityViewPager",
				    rowList:[5,10,20,30],
			    	rowNum:10,
				    sortorder: "asc", 
				    sortname: "activityId", 
				    colNames:[
				    	'', 
				    	'<fmt:message key="gradebook.columntitle.name"/>', 
				    	'<fmt:message key="gradebook.columntitle.averageTimeTaken"/>', 
				    	'<fmt:message key="gradebook.columntitle.competences"/>', 
				    	'<fmt:message key="gradebook.columntitle.averageMark"/>'
				    ],
				    colModel:[
				      {name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
					  {name:'rowName', index:'rowName', sortable:true, editable: false},
					  {name:'avgTimeTaken', index:'avgTimeTaken', sortable:true, editable: false, width:80, align:"center"},
					  {name:'competences', index:'competences', sortable:false, editable: false, hidden:true},
				      {name:'avgMark',index:'avgMark', sortable:true, editable:false, width:50, align:"center"}
				    ],
				    loadError: function(xhr,st,err) {
			    		jQuery("#activityView").clearGridData();
			    		alert('<fmt:message key="gradebook.error.loaderror"/>');
			    	},
				    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
				    subGrid: true,
					subGridRowExpanded: function(subgrid_id, row_id) {
					   var subgrid_table_id;
					   var activityID = jQuery("#activityView").getRowData(row_id)["id"];
					   subgrid_table_id = subgrid_id+"_t";
					   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&view=monActivityView&lessonID=${lessonDetails.lessonID}&activityID=" + activityID,
						     height: "100%",
						     width: 600,
						     cellEdit:true,
						     imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradebookData&lessonID=${lessonDetails.lessonID}&view=monActivityView&activityID=" + activityID,
						     sortorder: "asc", 
							 sortname: "fullName", 
							 pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     colNames: [
						     	'',
						     	'<fmt:message key="gradebook.columntitle.name"/>',
						     	'<fmt:message key="gradebook.columntitle.progress"/>', 
						     	'<fmt:message key="gradebook.columntitle.timeTaken"/>', 
						     	'<fmt:message key="gradebook.columntitle.outputs"/>', 
						     	'<fmt:message key="gradebook.columntitle.activityFeedback"/>', 
						     	'<fmt:message key="gradebook.columntitle.mark"/>'
						     ],
						     colModel:[
						     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
						     	{name:'rowName',index:'rowName', sortable:true, editable:false},
						      	{name:'status', align:'center', width:30, index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
						      	{name:'timeTaken', index:'timeTaken', sortable:true, editable: false, width:80, align:"center"},
						      	{name:'output', index:'output', sortable:false, editable: false, search:false, width:200},
						     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false, width:200},
						     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
						     ],
						     loadError: function(xhr,st,err) {
					    		jQuery("#"+subgrid_table_id).clearGridData();
					    		alert('<fmt:message key="gradebook.error.loaderror"/>');
					    	 },
						     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
						     	
						     	// update the activity average mark
						     	if (cellname == "mark") {
						     		var ids = jQuery("#"+subgrid_table_id).getDataIDs()
							     	var sumMarks = 0.0;
							     	var count = 0;
							     	for (var i = 0; i < ids.length; i++) {
							     		var rowData = jQuery("#"+subgrid_table_id).getRowData(ids[i]);
							     		var activityMark = rowData["mark"];
							     		
							     		if (activityMark != "-") {
							     			sumMarks += parseFloat(activityMark);
							     			count ++;
							     		}
							     	}						     	
							     	var average;
							     	if (count>0) {
							     		average = sumMarks / count;
							     	} else {
							     		average = value;
							     	}

							     	jQuery("#activityView").setCell(row_id, "average", average, "", "");
						     	}
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
						  
						  // Allowing column editing for this grid
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
				}).navGrid("#activityViewPager", {edit:false,add:false,del:false,search:false}); // enable refresh button
				
				// Enable show/hide columns
				jQuery("#activityView").navButtonAdd('#activityViewPager',{
					caption: "",
					title: '<fmt:message key="gradebook.function.window.showColumns"/>',
					buttonimg:"<lams:LAMSURL />images/table_edit.png", 
					onClickButton: function(){
						jQuery("#activityView").setColumns();
					}
				});
		});
	</script>
	
</lams:head>

<body class="stripes" style="text-align:center">
	<div id="page">
		
		<div id="header-no-tabs"></div> <!--closes footer-->
		<div id="content" >
			<h1 class="no-tabs-below">
				<fmt:message key="gradebook.title.lessonGradebook">
					<fmt:param>
						${lessonDetails.lessonName}
					</fmt:param>
				</fmt:message>
			</h1> 
			<br />
			<div style="width:700px; margin-left:auto; margin-right:auto;">
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<br />
				<br />
				
				<table id="activityView" class="scroll" ></table>
				<div id="activityViewPager" class="scroll" ></div>	
				
				<div class="tooltip" id="tooltip"></div>
				
			</div>
		</div> <!-- Closes content -->
	
		<div id="footer"></div><!--closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>
