<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title>Course Gradebook Learner</title>
	
	<lams:css />
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>

	<script type="text/javascript">
		
		$(document).ready(function(){
  
			// for the ipad, we seem to need to force the grid to a sensible size to start
			$("#organisationGrid").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				autoencode:false,
				caption: "${organisationName}",
			    datatype: "xml",
			    url: "<lams:LAMSURL />gradebook/gradebook/getCourseGridData.do?view=lrnCourse&organisationID=${organisationID}",
				height: 'auto',
				width: $(window).width() - 100,
				shrinkToFit: false,
				cmTemplate: { title: false },
			    sortorder: "asc", 
			    sortname: "id", 
			    pager: 'organisationGridPager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    colNames:[
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
			    	"<fmt:message key="gradebook.columntitle.mark"/>"],
			    colModel:[
			    	{name:'id', index:'id', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			    	{name:'rowName',index:'rowName', sortable:true, editable:false},
			    	{name:'subGroup',index:'subGroup', sortable:false, editable:false, search:false},
			    	{name:'status',index:'status', sortable:false, editable:false, search:false, width:50, align:"center"},
			    	{name:'startDate',index:'startDate', sortable:false, editable:false, hidden:true, search:false},
			    	{name:'finishDate',index:'finishDate', sortable:false, editable:false, hidden:true, search:false},
			    	{name:'feedback',index:'feedback', sortable:false, editable:false, search:false, width:200}, 
			    	{name:'medianTimeTaken',index:'medianTimeTaken', sortable:true, hidden:true, editable:false, search:false, width:80, align:"center"},
			    	{name:'timeTaken',index:'timeTaken', sortable:true, editable:false, hidden:true, search:false, width:80, align:"center"},
			    	{name:'averageMark',index:'averageMark', sortable:true, editable:false, search:false, width:50, align:"center"},
			    	{name:'mark',index:'mark', sortable:true, editable:false, search:false, width:50, align:"center"}
			    ],
 			    loadError: function(xhr,st,err) {
			    	$("#organisationGrid").jqGrid('clearGridData');
			    	alert('<fmt:message key="label.error"/>\n\n<fmt:message key="gradebook.error.loaderror"/>');
			    },
 			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgrid_table_id;
					var lessonID = $("#organisationGrid").getRowData(row_id)["id"];
					subgrid_table_id = subgrid_id+"_t";
					$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					$("#"+subgrid_table_id).jqGrid({
							 guiStyle: "bootstrap",
							 iconSet: 'fontAwesome',
							 autoencode:false,
						     datatype: "xml",
						     url: "<lams:LAMSURL />gradebook/gradebook/getActivityGridData.do?view=lrnActivity&lessonID=" + lessonID,
						     height: "100%",
						     autowidth:true,
						     cmTemplate: { title: false },
						     pager: subgrid_table_id + "_pager",
						     rowList:[10,20,30,40,50,100],
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
						    	$("#"+subgrid_table_id).jqGrid('clearGridData');
						    	alert('<fmt:message key="label.error"/>\n\n<fmt:message key="gradebook.error.loaderror"/>');
						     },
 							 gridComplete: function(){
							 	fixPagerInCenter(subgrid_table_id+"_pager", 1);
							 }
					  	}).navGrid("#"+subgrid_table_id+"_pager", {edit:false,add:false,del:false,search:false}); // applying refresh button
				},
				gridComplete: function(){
					fixPagerInCenter('organisationGridPager', 0);
				}	
			}).navGrid("#organisationGridPager", {edit:false,add:false,del:false,search:false});

			jQuery("#organisationGrid").jqGrid('filterToolbar');	

	        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
	        $(window).bind('resize', function() {
	            resizeJqgrid($(".ui-jqgrid-btable:visible"));
	        });

	        //resize jqGrid on openning of bootstrap collapsible
	        $('div[id^="collapse"]').on('shown.bs.collapse', function () {
	            resizeJqgrid($(".ui-jqgrid-btable:visible", this));
	        })

	        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

		});
	</script>

</lams:head>

<body class="stripes">

	<lams:Page type="admin">

		<a target="_blank" class="btn btn-sm btn-default pull-right" title="<fmt:message key='button.help.tooltip'/>"
		   href="http://wiki.lamsfoundation.org/display/lamsdocs/My+Grades">
		<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span></a>

		<h4><fmt:message key="gradebook.title.myGradebook">
				<fmt:param>
					<c:out value="${fullName}" escapeXml="true"/>
				</fmt:param>
			</fmt:message>
		</h4>

 		<div class="grid-holder">
 			<table id="organisationGrid" class="scroll"></table>
			<div id="organisationGridPager" class="scroll"></div>
 			<div class="tooltip-lg" id="tooltip"></div>
		</div>
 		<div id="footer">
		</div> <!--Closes footer-->
		
	</lams:Page>

</body>
</lams:html>
