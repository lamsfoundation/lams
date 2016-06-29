<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>Course Gradebook Learner</title>
	
	<lams:css />
	<link type="text/css" href="includes/css/gradebook.css" rel="stylesheet" />
	
	<style>
		#content {width:680px; }
	</style>

	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>

	<script type="text/javascript">

		
		jQuery(document).ready(function(){
  
			jQuery("#organisationGrid").jqGrid({
				caption: "${organisationName}",
			    datatype: "xml",
			    url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getCourseGridData&view=lrnCourse&organisationID=${organisationID}",
			    height: "100%",
			    width: 600,
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[5,10,20,30],
			    rowNum:10,
			    colNames:[
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
			    	"<fmt:message key="gradebook.columntitle.mark"/>"],
			    colModel:[
			      {name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'rowName',index:'rowName', sortable:true, editable:false},
			      {name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false},
			      {name:'status',index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
			      {name:'feedback',index:'feedback', sortable:false, editable:false, search:false, width:200},
			      {name:'startDate',index:'startDate', sortable:false, editable:false, hidden:true, search:false},
			      {name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:true, search:false},
			      {name:'medianTimeTaken',index:'medianTimeTaken', sortable:true, hidden:true, editable:false, search:false, width:80, align:"center"},
			      {name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false, width:80, align:"center"},
			      {name:'averageMark',index:'averageMark', sortable:true, editable:false, search:false, width:50, align:"center"},
			      {name:'mark',index:'mark', sortable:true, editable:false, search:false, width:50, align:"center"}
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
						     url: "<lams:LAMSURL />/gradebook/gradebook.do?dispatch=getActivityGridData&view=lrnActivity&lessonID=" + lessonID,
						     height: "100%",
						     autowidth:true,
						     pager: subgrid_table_id + "_pager",
							 rowList:[5,10,20,30],
							 rowNum:10,
						     colNames: [
						     	'',
						     	"<fmt:message key="gradebook.columntitle.activity"/>",
						     	"<fmt:message key="gradebook.columntitle.progress"/>", 
						     	"<fmt:message key="gradebook.columntitle.activityFeedback"/>", 
						     	"<fmt:message key="gradebook.columntitle.averageTimeTaken"/>", 
						     	"<fmt:message key="gradebook.columntitle.timeTaken"/>", 
						     	"<fmt:message key="gradebook.columntitle.averageMark"/>", 
						     	"<fmt:message key="gradebook.columntitle.mark"/>"
						     ],
						     colModel: [
						       	{name:'id', index:'id', sortable:false, hidden:true, hidedlg:true},
								{name:'rowName', index:'rowName', sortable:false, editable: false, width:100},
								{name:'status', index:'status', sortable:false, editable:false, width:50, align:"center"},
								{name:'feedback', index:'feedback', sortable:false, editable: false, hidden:true},
								{name:'medianTimeTaken',index:'medianTimeTaken', sortable:true, hidden:true, editable:false, search:false, width:80, align:"center"},
			      				{name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false, width:80, align:"center"},
			      				{name:'averageMark',index:'averageMark', sortable:true, editable:false, search:false, width:50, align:"center"},
								{name:'mark', width:100, index:'mark', sortable:true, editable: false, width:50, align:"center"}
						     ],
						     loadError: function(xhr,st,err) {
						    	jQuery("#"+subgrid_table_id).clearGridData();
						    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
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
			<lams:help module="gradebook" page="My+Grades" style="no-tabs"/>
			<h1 class="no-tabs-below">
				<fmt:message key="gradebook.title.myGradebook">
					<fmt:param>
						<c:out value="${fullName}" escapeXml="true"/>
					</fmt:param>
				</fmt:message>
			</h1>
			<br />
			<div style="width: 600px; margin-left: 20px; margin-right: 20px">
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
