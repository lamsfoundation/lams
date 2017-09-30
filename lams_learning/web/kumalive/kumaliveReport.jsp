<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<lams:css/>
	<link type="text/css" href="css/ui.jqgrid.css" rel="stylesheet" />
	<style type="text/css">
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.jqgrid.src.js"></script>

	<script type="text/javascript">
		$(document).ready(function(){
			// Create the organisation view grid with sub grid for users	
			$("#organisationGrid").jqGrid({
			    datatype		   : "json",
			    url				   : "<lams:LAMSURL />learning/kumalive.do?method=getReportOrganisationData&organisationID=${param.organisationID}",
			    height			   : "100%",
			    // use new theme
			    guiStyle 		   : "bootstrap",
			    iconSet 		   : 'fontAwesome',
			    autowidth		   : true,
			    shrinkToFit 	   : true,
			    // allow selection of Kumalives for export
			    multiselect 	   : true,
			    multiPageSelection : true,
			    // it gets ordered by 
			    sortorder		   : "asc", 
			    sortname		   : "id", 
			    pager			   : true,
			    rowNum			   :10,
				colNames : [
					'#',
					'Name'
				],
			    colModel : [
					{
					   'name'  : 'order', 
					   'index' : 'order',
					   'title' : false
					},
			        {
					   'name'  : 'name', 
					   'index' : 'name',
					   'title' : false
					}
			    ],
			    loadError : function(xhr,st,err) {
			    	$("#organisationGrid").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var kumaliveId = row_id,
						user_subgrid_table_id = subgrid_id + "_user";
				   $("#" + subgrid_id).html("<table id='" + user_subgrid_table_id + "' class='scroll'></table><div id='"
						 + user_subgrid_table_id + "_pager' class='scroll' ></div>");

				   $.ajax({
						url: "<lams:LAMSURL />learning/kumalive.do",
					    data: {
						    	'method' : 'getReportKumaliveRubrics',
						        'kumaliveId' : kumaliveId
						      },
					    dataType: "json",
						success: function(result) {
				            var columnNames  = [],
				            	columnModel = [];
						    $.each(result, function(){
							    columnNames.push(this[1]);
							    columnModel.push({
								    'name'    : 'rubric' + this[0],
								    'index'   : 'rubric' + this[0],
								    'sortable': false,
								    'title'   : false
								  });
							});

				            $("#" + user_subgrid_table_id).jqGrid({
				                datatype 	: 'json',
				                url 		: "<lams:LAMSURL />learning/kumalive.do?method=getReportKumaliveData&kumaliveId=" + kumaliveId,
				                colNames 	: ['Name'].concat(columnNames),
				                colModel 	: [
								      		   {
												'name'  : 'name', 
												'index' : 'name',
												'title' : false
											   }
									    	  ].concat(columnModel),
							    height      : "100%",
							    guiStyle 	: "bootstrap",
							    iconSet 	: 'fontAwesome',
							    autowidth   : true,
							    shrinkToFit : true,
							    sortorder 	: "asc", 
								sortname 	: "name", 
								loadError 	: function(xhr,st,err) {
						    		$("#" + user_subgrid_table_id).clearGridData();
						    		$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
						    	},
						    	subGrid: true,
								subGridRowExpanded: function(subgrid_id, row_id) {
									var userId = row_id,
										score_subgrid_table_id = subgrid_id + "_score";
									$("#" + subgrid_id).html("<table id='" + score_subgrid_table_id + "' class='scroll'></table><div id='"
										  + score_subgrid_table_id + "_pager' class='scroll' ></div>");
									  
						            $("#" + score_subgrid_table_id).jqGrid({
						                datatype 	: 'json',
						                url 		: "<lams:LAMSURL />learning/kumalive.do?method=getReportUserData&kumaliveId=" + kumaliveId + "&userId=" + userId,
						                colNames 	: ['#'].concat(columnNames),
						                colModel 	: [
										      		   {
														'name'     : 'order', 
														'index'    : 'order',
														'sortable' : false,
														'title'    : false
													   }
											    	  ].concat(columnModel),
									    height      : "100%",
									    guiStyle 	: "bootstrap",
									    iconSet 	: 'fontAwesome',
									    autowidth   : true,
									    shrinkToFit : true,
										loadError 	: function(xhr,st,err) {
								    		$("#" + score_subgrid_table_id).clearGridData();
								    		$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
								    	}
						            });
								}
				            });
				        },
				        error: function(x, e) {
				            alert(x.readyState + " "+ x.status +" "+ e.msg);   
				        }
				    });
				}
			});
		});
	</script>
	
</lams:head>
<body class="stripes">

<lams:Page type="admin">
	<div class="grid-holder voffset20">
		<table id="organisationGrid"></table>
		<div class="tooltip-lg" id="tooltip"></div>
	</div>
</lams:Page>

</body>
</lams:html>