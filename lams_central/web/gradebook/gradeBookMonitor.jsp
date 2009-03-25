<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>GradeBook Monitor</title>
	<lams:css/>
	
	<link rel="stylesheet" type="text/css" media="screen" href="<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/grid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<lams:LAMSURL />includes/javascript/jqgrid/themes/jqModal.css" />
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />includes/javascript/jqgrid/js/jquery.jqGrid.js" ></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />includes/javascript/jqgrid/js/jqModal.js" ></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />includes/javascript/jqgrid/js/jqDnR.js" ></script>
	
	<script type="text/javascript">
		
		jQuery(document).ready(function(){
  
			jQuery("#userView").jqGrid({
				caption: "Learner View",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&method=userView&lessonID=${lessonDetails.lessonID}",
			    height: "100%",
			    width: 990,
			    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
			    cellEdit: true,
			    sortorder: "asc", 
			    sortname: "fullName", 
			    pager: 'userViewPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradeBookData&lessonID=${lessonDetails.lessonID}&login=test1",
			    colNames:["", 'Name', 'Progress', 'Lesson FeedBack', 'Total Mark'],
			    colModel:[
			      {name:'login', index:'login', sortable:false, editable:false, hidden:true},
			      {name:'fullName',index:'fullName', sortable:true, editable:false},
			      {name:'status',index:'status', align:'center', width:30, sortable:false, editable:false},
			      {name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} },
			      {name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#userView").clearGridData();
			    	alert("There was an error loading the Learner View grid");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var userName = jQuery("#userView").getRowData(row_id)["login"];
				   subgrid_table_id = subgrid_id+"_t";
					 jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   jQuery("#"+subgrid_table_id).jqGrid({
					     datatype: "xml",
					     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&lessonID=${lessonDetails.lessonID}&method=userView&login=" + userName,
					     height: "100%",
					     width: 920,
					     cellEdit:true,
					     pager: subgrid_table_id + "_pager",
						 rowList:[5,10,20,30],
						 rowNum:10,
					     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradeBookData&lessonID=${lessonDetails.lessonID}&method=userView&login=" + userName,
					     colNames: ['Id','Activity','Progress','Outputs', 'Competences', 'Activity FeedBack', 'Mark'],
					     colModel: [
					       	{name:'activityId', width:10, index:'activityId', sortable:false, hidden:true},
							{name:'activityTitle', width:60, index:'activityTitle', sortable:false, editable: false},
							{name:'status', align:'center', width:30, index:'status', sortable:false, editable:false},
							{name:'output', width:250, index:'output', sortable:false, editable: false},
							{name:'competences', width:250, index:'competences', sortable:false, editable: false},
							{name:'feedback', width:250, index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}},
							{name:'mark', width:100, index:'mark', sortable:false, editable: true, editrules:{number:true} }
					     ],
					     loadError: function(xhr,st,err) {
					    	jQuery("#"+subgrid_table_id).clearGridData();
					    	alert("There was an error loading the Learner View grid");
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
						 imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images'
					  })
				 	}
				}).navGrid("#userViewPager", {edit:false,add:false,del:false,search:false});
				

				
				
				jQuery("#activityView").jqGrid({
					caption: "Activity View",
				    datatype: "xml",
				    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&method=activityView&lessonID=${lessonDetails.lessonID}",
				    height: "100%",
				    width: 990,
				    cellEdit: true,
				    pager: "activityViewPager",
				    rowList:[5,10,20,30],
			    	rowNum:10,
				    sortorder: "asc", 
				    sortname: "activityId", 
				    colNames:["", 'Activity Name', 'Competences', 'Average Mark'],
				    colModel:[
				      {name:'activityId', width:10, index:'activityId', sortable:false, hidden:true},
					  {name:'activityTitle', width:60, index:'activityTitle', sortable:false, editable: false},
				      {name:'competences', width:250, index:'competences', sortable:false, editable: false},
				      {name:'average',index:'average', sortable:false, editable:false}
				    ],
				    loadError: function(xhr,st,err) {
			    		jQuery("#activityView").clearGridData();
			    		alert("There was an error loading the Activity View grid");
			    	},
				    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
				    subGrid: true,
					subGridRowExpanded: function(subgrid_id, row_id) {
					   var subgrid_table_id;
					   var activityID = jQuery("#activityView").getRowData(row_id)["activityId"];
					   subgrid_table_id = subgrid_id+"_t";
					   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&method=activityView&lessonID=${lessonDetails.lessonID}&activityID=" + activityID,
						     height: "100%",
						     width: 920,
						     cellEdit:true,
						     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradeBookData&lessonID=${lessonDetails.lessonID}&method=activityView&activityID=" + activityID,
						     sortorder: "asc", 
							 sortname: "fullName", 
							 pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     colNames: ['','Full Name','Progress','Outputs', 'Activity Feedback', 'Mark'],
						     colModel:[
						     	{name:'login', index:'login', sortable:false, editable:false, hidden:true},
						     	{name:'fullName',index:'fullName', sortable:true, editable:false},
						      	{name:'status', align:'center', width:30, index:'status', sortable:false, editable:false},
						      	{name:'output', width:220, index:'output', sortable:false, editable: false},
						     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} },
						     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}}
						     ],
						     loadError: function(xhr,st,err) {
					    		jQuery("#"+subgrid_table_id).clearGridData();
					    		alert("There was an error loading the Activity View grid");
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
							 imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images'
						 })
					 }
				}).navGrid("#activityViewPager", {edit:false,add:false,del:false,search:false});
		});
		
		function launchPopup(url,title) {
			var wd = null;
			if(wd && wd.open && !wd.closed){
				wd.close();
			}
			wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
			wd.window.focus();
		}	
	</script>
	
</lams:head>

<body class="stripes" style="text-align:center">
	<div id="page">
		
		<div id="header-no-tabs"></div> <!--closes footer-->
		<div id="content" >
			<h1 class="no-tabs-below">GradeBook for ${lessonDetails.lessonName}</h1> 
			<div style="width:990px; margin-left:auto; margin-right:auto;">
			<br />
			<div style="width:990px; margin-left:auto; margin-right:auto;">
				<table id="userView" class="scroll" ></table>
				<div id="userViewPager" class="scroll" ></div>
				
				<br />
				<br />
				<br />
				
				<table id="activityView" class="scroll" ></table>
				<div id="activityViewPager" class="scroll" ></div>	
				
			</div>
		</div> <!-- Closes content -->
	
		<div id="footer"></div><!--closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>