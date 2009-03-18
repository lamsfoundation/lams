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
  
			jQuery("#users").jqGrid({
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=getLessonGradeBookData&lessonID=${lessonDetails.lessonID}",
			    height: "100%",
			    width: 707,
			    cellEdit: true,
			    sortorder: "asc", 
			    sortname: "lastName", 
			    pager: 'pager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradeBookData&lessonID=${lessonDetails.lessonID}&login=test1",
			    colNames:["", 'Last Name','First Name', 'Progress', 'Lesson FeedBack', 'Total Mark'],
			    colModel:[
			      {name:'login', index:'login', sortable:false, editable:false, hidden:true},
			      {name:'lastName',index:'lastName', sortable:true, editable:false},
			      {name:'firstName',index:'firstName', sortable:true, editable:false},
			      {name:'status',index:'status', sortable:false, editable:false},
			      {name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} },
			      {name:'totalMark',index:'totalMark', sortable:true, editable:true, editrules:{number:true}}
			    ],
			    imgpath: 'themes/basic/images',
			    caption: "Learners",
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var userName = jQuery("#users").getRowData(row_id)["login"];
				   subgrid_table_id = subgrid_id+"_t";
					 jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
					   jQuery("#"+subgrid_table_id).jqGrid({
					     datatype: "xml",
					     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getLessonGradeBookDataForUser&lessonID=${lessonDetails.lessonID}&login=" + userName,
					     height: "100%",
					     width: 650,
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserActivityGradeBookData&lessonID=${lessonDetails.lessonID}&login=" + userName,
					     ExpandColumn: "Outputs",
					     colNames: ['Id','Activity','Status','Outputs', 'Competences', 'Activity FeedBack', 'Mark'],
					     colModel: [
					       	{name:'activityId', width:10, index:'activityId', sortable:false, hidden:true},
							{name:'activityTitle', width:60, index:'activityTitle', sortable:false, editable: false},
							{name:'status', width:100, index:'status', sortable:false, editable:false},
							{name:'output', width:250, index:'output', sortable:false, editable: false},
							{name:'competences', width:250, index:'competences', sortable:false, editable: false},
							{name:'feedback', width:250, index:'feedback', sortable:false, editable: true, edittype:'textarea', editoptions:{rows:'4',cols:'20'}},
							{name:'mark', width:100, index:'mark', sortable:false, editable: true, editrules:{number:true} }
					                   ],
					     editurl: "server.php",
					     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
					     	var ids = jQuery("#"+subgrid_table_id).getDataIDs()
					     	var totalMark = 0.0;
					     	for (var i=0; i < ids.length; i++) {
					     		var rowData = jQuery("#"+subgrid_table_id).getRowData(ids[i]);
					     		var activityMark = rowData["mark"];
					     		
					     		if (activityMark != "-") {
					     			totalMark += parseFloat(activityMark);
					     		}
					     	}
					     	jQuery("#users").setCell(row_id, "totalMark", totalMark, "", "");
					     },
						 imgpath: 'themes/basic/images'
					  })
				 	}
				})
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
			<div style="width:707px; margin-left:auto; margin-right:auto;">
			<br />
			<div style="width:707px; margin-left:auto; margin-right:auto;">
				<table id="users" class="scroll" ></table>
				<div id="pager" class="scroll" ></div>
			</div>
		</div> <!-- Closes content -->
	
		<div id="footer"></div><!--closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>