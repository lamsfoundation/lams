<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>Course GradeBook Learner</title>
	<lams:css />

	<jsp:include page="jqGridIncludes.jsp"></jsp:include>

	<script type="text/javascript">

		
		jQuery(document).ready(function(){
  
			jQuery("#organisationGrid").jqGrid({
				caption: "${organisationName}",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=lrnCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: 990,
			    imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
			    colNames:["", 'Lesson Name', 'Sub-Group', 'Progress', 'FeedBack', 'Start Date', 'Finish Date', 'Average Duration', 'Duration', 'Average Mark', 'Mark'],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowName',index:'rowName', sortable:true, editable:false},
			      {name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false},
			      {name:'status',index:'status', sortable:false, editable:false, search:false},
			      {name:'feedback',index:'feedback', sortable:false, editable:false, search:false},
			      {name:'startDate',index:'startDate', sortable:false, editable:false, hidden:true, search:false},
			      {name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:true, search:false},
			      {name:'averageTimeTaken',index:'averageTimeTaken', sortable:true, hidden:true, editable:false, search:false},
			      {name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false},
			      {name:'averageMark',index:'averageMark', sortable:true, editable:false, search:false},
			      {name:'mark',index:'mark', sortable:true, editable:false, search:false}
			    ],
			    loadError: function(xhr,st,err) {
			    	jQuery("#organisationGrid").clearGridData();
			    	alert("There was an error loading the Organisation grid");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
				   var subgrid_table_id;
				   var lessonID = jQuery("#organisationGrid").getRowData(row_id)["id"];
				   subgrid_table_id = subgrid_id+"_t";
					 jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					   	jQuery("#"+subgrid_table_id).jqGrid({
						     datatype: "xml",
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&view=lrnActivity&lessonID=" + lessonID,
						     height: "100%",
						     width: 920,
						     sortname: "id",
						     imgpath: '<lams:LAMSURL />includes/javascript/jqgrid/themes/basic/images',
						     pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     colNames: ['Id','Activity','Progress', 'Feedback', 'Average Duration', 'Duration', 'Average Mark', 'Mark'],
						     colModel: [
						       	{name:'id', width:10, index:'id', sortable:false, hidden:true, hidedlg:true},
								{name:'rowName', width:60, index:'rowName', sortable:false, editable: false},
								{name:'status', align:'center', width:30, index:'status', sortable:false, editable:false},
								{name:'feedback', width:250, index:'feedback', sortable:false, editable: false},
								{name:'averageTimeTaken',index:'averageTimeTaken', sortable:true, hidden:true, editable:false, search:false},
			      				{name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false},
			      				{name:'averageMark',index:'averageMark', sortable:true, editable:false, search:false},
								{name:'mark', width:100, index:'mark', sortable:true, editable: false}
						     ],
						     loadError: function(xhr,st,err) {
						    	jQuery("#"+subgrid_table_id).clearGridData();
						    	alert("There was an error loading the Learner View grid");
						     },
							 gridComplete: function(){
							 	toolTip($(".jqgrow"));
							 }
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
					  
					  	// Adding button for show/hiding collumn
					  	jQuery("#"+subgrid_table_id).navButtonAdd("#"+subgrid_table_id+"_pager",{
							caption: "",
							title: "Show/Hide Columns",
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
				
				<div class="tooltip" id="tooltip"></div>
			</div>
		</div> <!-- Closes content -->
		
		<div id="footer">
		</div> <!--Closes footer-->
	</div> <!-- Closes page -->
</body>
</lams:html>