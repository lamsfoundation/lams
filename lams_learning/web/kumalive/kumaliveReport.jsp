<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet" />
	<style type="text/css">
		#downloadFrame {
			display: none;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>

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
			    rowNum			   : 10,
				colNames : [
					'#',
					'<fmt:message key="label.kumalive.report.full.name"/>'
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
			    onSelectRow : function(){
				    // disable "Export selected" button when no Kumalives are selected
			    	var noRowsSelected = $(this).jqGrid('getGridParam','selarrrow').length == 0;
			    	$('#exportSelected').prop('disabled', noRowsSelected);
				},
				gridComplete : function(){
					var noRecords = $("#organisationGrid").jqGrid('getGridParam','reccount') == 0;
					$('#exportAll').prop('disabled', noRecords);
				},
			    loadError : function(xhr,st,err) {
			    	$("#organisationGrid").clearGridData();
			    	$.jgrid.info_dialog('<fmt:message key="error.title"/>', 
	    					'<fmt:message key="message.kumalive.report.load.error"/>',
	    					'<fmt:message key="button.kumalive.ok"/>');
			    },
			    // subgrid for learners who replied to questions during Kumalive
			    subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var kumaliveId = row_id,
						user_subgrid_table_id = subgrid_id + "_user";
				   $("#" + subgrid_id).html("<table id='" + user_subgrid_table_id + "' class='scroll'></table><div id='"
						 + user_subgrid_table_id + "_pager' class='scroll' ></div>");

				   // fetch rubrics first so we can build column model
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
				                url 		: "<lams:LAMSURL />learning/kumalive.do?method=getReportKumaliveData&kumaliveId="
					                			+ kumaliveId,
				                colNames 	: ['<fmt:message key="label.kumalive.report.full.name"/>'].concat(columnNames),
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
						    		$.jgrid.info_dialog('<fmt:message key="error.title"/>', 
								    					'<fmt:message key="message.kumalive.report.load.error"/>',
								    					'<fmt:message key="button.kumalive.ok"/>');
						    	},
						    	// subgrid of scores (marks) for the given learner
						    	subGrid: true,
								subGridRowExpanded: function(subgrid_id, row_id) {
									var userId = row_id,
										score_subgrid_table_id = subgrid_id + "_score";
									$("#" + subgrid_id).html("<table id='" + score_subgrid_table_id + "' class='scroll'></table><div id='"
										  + score_subgrid_table_id + "_pager' class='scroll' ></div>");
									  
						            $("#" + score_subgrid_table_id).jqGrid({
						                datatype 	: 'json',
						                url 		: "<lams:LAMSURL />learning/kumalive.do?method=getReportUserData&kumaliveId="
							                			+ kumaliveId + "&userId=" + userId,
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
								    		$.jgrid.info_dialog('<fmt:message key="error.title"/>', 
							    					'<fmt:message key="message.kumalive.report.load.error"/>',
							    					'<fmt:message key="button.kumalive.ok"/>');
								    	}
						            });
								}
				            });
				        }
				    });
				}
			});
		});

		function exportAll(){
			$('#downloadFrame').attr('src',
				'<lams:LAMSURL />learning/kumalive.do?method=exportKumalives&organisationID=${param.organisationID}');
		}

		function exportSelected(){
			$('#downloadFrame').attr('src',
				'<lams:LAMSURL />learning/kumalive.do?method=exportKumalives&kumaliveIds='
				// return an array of IDs of rows (Kumalives) selected on all pages
				+ JSON.stringify($("#organisationGrid").jqGrid('getGridParam','selarrrow')));
		}
	</script>
	
</lams:head>
<body class="stripes">

<lams:Page type="admin">
	<div id="toolbar" class="buttons btn-group-sm" style="text-align: right">
		<button id="exportAll" class="btn btn-default" onClick="javascript:exportAll()">
			<i class="fa fa-download"></i> 
			<span><fmt:message key="button.kumalive.report.export.all"/></span>
		</button>
		<button id="exportSelected" class="btn btn-default" disabled="disabled" onClick="javascript:exportSelected()">
			<i class="fa fa-download"></i> 
			<span><fmt:message key="button.kumalive.report.export.selected"/></span>
		</button>
	</div>
	<div class="grid-holder voffset10">
		<table id="organisationGrid"></table>
		<div class="tooltip-lg" id="tooltip"></div>
	</div>
</lams:Page>

<iframe id="downloadFrame"></iframe>
</body>
</lams:html>