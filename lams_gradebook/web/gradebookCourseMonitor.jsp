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

	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>

	<script type="text/javascript">
		
		jQuery(document).ready(function(){
  
			jQuery("#organisationGrid").jqGrid({
				caption: "${organisationName}",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=monCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: 700,
			    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
				colNames:[
					'', 
					'<fmt:message key="gradebook.columntitle.lessonName"/>', 
					'<fmt:message key="gradebook.columntitle.subGroup"/>', 
					'<fmt:message key="gradebook.columntitle.startDate"/>', 
					'<fmt:message key="gradebook.columntitle.averageTimeTaken"/>', 
			    	'<fmt:message key="gradebook.columntitle.averageMark"/>'
				],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowName',index:'rowName', sortable:true, editable:false},
			      {name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false},
			      {name:'startDate',index:'startDate', sortable:false, editable:false, search:false, width:80, align:"center"},
			      {name:'avgTimeTaken',index:'avgTimeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
			      {name:'avgMark',index:'avgMark', sortable:true, editable:false, search:false, width:50, align:"center"}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#organisationGrid").clearGridData();
			    	alert('<fmt:message key="gradebook.error.loaderror"/>');
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
					     width: 650,
					     imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
					     cellEdit:true,
					     cellurl: "<lams:LAMSURL />/gradebook/gradebookMonitoring.do?dispatch=updateUserLessonGradebookData&lessonID=" + lessonID,
					     sortorder: "asc", 
						 sortname: "rowName", 
						 pager: subgrid_table_id + "_pager",
						 rowList:[5,10,20,30],
						 rowNum:10,
					     colNames: [
					     	'',
					     	'<fmt:message key="gradebook.columntitle.learnerName"/>',
					     	'<fmt:message key="gradebook.columntitle.progress"/>', 
					     	'<fmt:message key="gradebook.columntitle.timeTaken"/>', 
					     	'<fmt:message key="gradebook.columntitle.lessonFeedback"/>', 
			    			'<fmt:message key="gradebook.columntitle.mark"/>'
					     ],
					     colModel:[
					     	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
					     	{name:'rowName',index:'rowName', sortable:true, editable:false},
					      	{name:'status', index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
					      	{name:'timeTaken', index:'timeTaken', sortable:true, editable:false, search:false, width:80, align:"center"},
					     	{name:'feedback',index:'feedback', sortable:false, editable:true, edittype:'textarea', editoptions:{rows:'4',cols:'20'} , search:false},
					     	{name:'mark',index:'mark', sortable:true, editable:true, editrules:{number:true}, search:false, width:50, align:"center"}
					     ],
					     loadError: function(xhr,st,err) {
				    		jQuery("#"+subgrid_table_id).clearGridData();
				    		alert('<fmt:message key="gradebook.error.loaderror"/>');
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
		});
	</script>

</lams:head>

<body class="stripes" style="text-align: center">
	<div id="page">
	
		<div id="header-no-tabs"></div>
		
		<div id="content">
			<h1 class="no-tabs-below">
				<fmt:message key="gradebook.title.courseMonitor">
					<fmt:param>
						${organisationName}
					</fmt:param>
				</fmt:message>
			</h1>
			<br />
			<div style="width: 700px; margin-left: auto; margin-right: auto;">
				<table id="organisationGrid" class="scroll"></table>
				<div id="organisationGridPager" class="scroll"></div>
				
				<div class="tooltip" id="tooltip"></div>
			</div>
		</div> <!-- Closes content -->
		
		<div id="footer">
		</div> <!--Closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>
