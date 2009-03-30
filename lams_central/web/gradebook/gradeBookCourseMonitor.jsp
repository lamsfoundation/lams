<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>Course GradeBook Monitor</title>
	<lams:css />

	<link rel="stylesheet" type="text/css" media="screen" href="<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/grid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<lams:LAMSURL />includes/javascript/jqgrid/themes/jqModal.css" />
	<script language="JavaScript" type="text/javascript" src="../includes/javascript/jquery-latest.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="../includes/javascript/jqgrid/js/jquery.jqGrid.js"></script>
	<script language="JavaScript" type="text/javascript" src="../includes/javascript/jqgrid/js/jqModal.js"></script>
	<script language="JavaScript" type="text/javascript" src="../includes/javascript/jqgrid/js/jqDnR.js"></script>

	<script type="text/javascript">
	
		
		jQuery(document).ready(function(){
  
			jQuery("#organisationGrid").jqGrid({
				caption: "${organisationName}",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&method=monitorView&organisationID=${organisationID}",
			    height: "100%",
			    width: 990,
			    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
			    cellEdit: true,
			    sortorder: "asc", 
			    sortname: "lessonName", 
			    pager: 'organisationGridPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradeBookData&lessonID=${lessonDetails.lessonID}&login=test1",
			    colNames:["", 'Lesson Name', 'Sub-Group', 'Description', 'Average Mark'],
			    colModel:[
			      {name:'lessonID', index:'lessonID', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'lessonName',index:'lessonName', sortable:true, editable:false},
			      {name:'subGroup',index:'subGroup', sortable:true, editable:false, search:false},
			      {name:'description',index:'description', sortable:false, editable:false, search:false},
			      {name:'mark',index:'mark', sortable:true, editable:false, search:false}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#organisationGrid").clearGridData();
			    	alert("There was an error loading the Organisation grid");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var lessonID = jQuery("#organisationGrid").getRowData(row_id)["lessonID"];
				   subgrid_table_id = subgrid_id+"_t";
				   jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
				   jQuery("#"+subgrid_table_id).jqGrid({
					     datatype: "xml",
					     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getUserGridData&method=courseMonitorView&lessonID=" + lessonID,
					     height: "100%",
					     width: 920,
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradeBookData&lessonID=" + lessonID,
					     sortorder: "asc", 
						 sortname: "fullName", 
						 pager: subgrid_table_id + "_pager",
						 rowList:[5,10,20,30],
						 rowNum:10,
					     colNames: ['','Full Name','Progress', 'Lesson Feedback', 'Lesson Mark'],
					     colModel:[
					     	{name:'login', index:'login', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'fullName',index:'fullName', sortable:true, editable:false},
					      	{name:'status', align:'center', width:30, index:'status', sortable:false, editable:false, search:false},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false},
					     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false}
					     ],
					     loadError: function(xhr,st,err) {
				    		jQuery("#"+subgrid_table_id).clearGridData();
				    		alert("There was an error loading the User View grid");
				    	 },
					     afterSaveCell: function(rowid, cellname,value, iRow, iCol) {
					     	
					     	// update the lesson average mark
					     	if (cellname == "mark") {
					     		var ids = jQuery("#"+subgrid_table_id).getDataIDs()
						     	var sumMarks = 0.0;
						     	var count = 0;
						     	for (var i = 0; i < ids.length; i++) {
						     		var rowData = jQuery("#"+subgrid_table_id).getRowData(ids[i]);
						     		var lessonMark = rowData["mark"];
						     		
						     		if (lessonMark != "-") {
						     			sumMarks += parseFloat(lessonMark);
						     			count ++;
						     		}
						     	}						     	
						     	var average;
						     	if (count>0) {
						     		average = sumMarks / count;
						     	} else {
						     		average = value;
								}

						     	jQuery("#organisationGrid").setCell(row_id, "mark", average, "", "");
					     	}
					     },
						 imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images'
					 }).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false})
					 
					 jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
							caption: "",
							buttonimg:"<lams:LAMSURL />images/find.png", 
							onClickButton: function(){
								jQuery("#"+subgrid_table_id).searchGrid({
									Find: "Find", 
									Clear: "Clear", 
									top:10, 
									left:10,
									sopt:['cn','bw','eq','ne','ew']
								});
							}
					  });
					  jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
						caption: "",
						title: "Show/Hide Columns",
						buttonimg:"<lams:LAMSURL />images/table_edit.png", 
						onClickButton: function(){
							jQuery("#"+subgrid_table_id).setColumns();
						}
					  });
					}
				}).navGrid("#organisationGridPager", {edit:false,add:false,del:false,search:false})
				
			jQuery("#organisationGrid").navButtonAdd("#organisationGridPager",{
				caption: "",
				buttonimg:"<lams:LAMSURL />images/find.png", 
				onClickButton: function(){
					jQuery("#organisationGrid").searchGrid({
						Find: "Find", 
						Clear: "Clear", 
						top:10, 
						left:10,
						sopt:['cn','bw','eq','ne','ew']
					});
				}
			});

			jQuery("#organisationGrid").navButtonAdd('#organisationGridPager',{
				caption: "",
				title: "Show/Hide Columns",
				buttonimg:"<lams:LAMSURL />images/table_edit.png", 
				onClickButton: function(){
					jQuery("#organisationGrid").setColumns();
				}
			});	
		});
		
		
		
		function launchPopup(url,title) {
			var wd = null;
			if(wd && wd.open && !wd.closed){
				wd.close();
			}
			wd = window.open(url,title,'resizable,width=1220,height=600,scrollbars');
			wd.window.focus();
		}	
	</script>

</lams:head>

<body class="stripes" style="text-align: center">
	<div id="page">
	
		<div id="header-no-tabs"></div>
		
		<div id="content">
			<h1 class="no-tabs-below">Course GradeBook for ${organisationName}</h1>
			<br />
			<div style="width: 990px; margin-left: auto; margin-right: auto;">
				<table id="organisationGrid" class="scroll"></table>
				<div id="organisationGridPager" class="scroll"></div>
			</div>
		</div> <!-- Closes content -->
		
		<div id="footer">
		</div> <!--Closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>