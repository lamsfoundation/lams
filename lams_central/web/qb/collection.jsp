<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Question collections</title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<style>
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			$('.collectionGrid').each(function(){
				var collectionGrid = $(this);
				
				collectionGrid.jqGrid({
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
					caption: collectionGrid.data('collectionName'),
				    datatype: "xml",
				    url: "<lams:LAMSURL />qb/collection/getCollectionGridData.do?collectionUid=" + collectionGrid.data('collectionUid'),
				    height: "100%",
				    autowidth:true,
					shrinkToFit: true,
				    cellEdit: false,
				    cmTemplate: { title: false, search: false },
				    sortorder: "asc", 
				    sortname: "name", 
				    multiselect 	   : true,
				    multiPageSelection : true,
				    pager: true,
				    rowList:[10,20,30,40,50,100],
				    rowNum: 10,
				    colNames:[
				    	"ID",
				    	"Name",
				    	"Stats"
				    ],
				    colModel:[
				      {name:'id', index:'uid', sortable:true,  width: 10},
				      {name:'name', index:'name', sortable:true, search:true, autoencode:true},
				      {name:'stats', index:'stats', classes: "stats-cell", sortable:false, width: 10, align: "center", formatter: statsLinkFormatter}
				      ],
			        onSelectRow : function(id, status, event) {
					    var grid = $(this),
					   		included = grid.data('included'),
							excluded = grid.data('excluded'),
							selectAllChecked = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox .cbox').prop('checked');
						if (selectAllChecked) {
							var index = excluded.indexOf(+id);
							// if row is deselected, add it to excluded array
							if (index < 0) {
								if (!status) {
									excluded.push(+id);
								}
							} else if (status) {
								excluded.splice(index, 1);
							}
						} else {
							var index = included.indexOf(+id);
							// if row is selected, add it to included array
							if (index < 0) {
								if (status) {
									included.push(+id);
								}
							} else if (!status) {
								included.splice(index, 1);
							}
						}
					},
					gridComplete : function(){
						var grid = $(this),
							included = grid.data('included'),
							// cell containing "(de)select all" button
							selectAllCell = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox > div');
						// remove the default button provided by jqGrid
						$('.cbox', selectAllCell).remove();
						// create own button which follows own rules
						var selectAllCheckbox = $('<input type="checkbox" class="cbox" />')
												.prop('checked', included === null)
												.prependTo(selectAllCell)
												.change(function(){
													// start with deselecting every question on current page
													grid.resetSelection();
													if ($(this).prop('checked')){
														// on select all change mode and select all on current page
														grid.data('included', null);
														grid.data('excluded', []);
														$('[role="row"]', grid).each(function(){
															grid.jqGrid('setSelection', +$(this).attr('id'), false);
														});
													} else {
														// on deselect all just change mode
														grid.data('excluded', null);
														grid.data('included', []);
													}
												});
						grid.resetSelection();
						if (selectAllCheckbox.prop('checked')) {
							var excluded = grid.data('excluded');
							// go through each loaded row
							$('[role="row"]', grid).each(function(){
								var id = +$(this).attr('id'),
									selected = $(this).hasClass('success');
								// if row is not selected and is not excluded, select it
								if (!selected && (!excluded || !excluded.includes(id))) {
									// select without triggering onSelectRow
									grid.jqGrid('setSelection', id, false);
								}
							}); 
						} else {
							// go through each loaded row
							$('[role="row"]', grid).each(function(){
								var id = +$(this).attr('id'),
									selected = $(this).hasClass('success');
								// if row is not selected and is included, select it
								if (!selected && included.includes(id)) {
									// select without triggering onSelectRow
									grid.jqGrid('setSelection', id, false);
								}
							});
						}
					},
					loadComplete : function(){
						var grid = $(this),
							gridView = grid.closest('.ui-jqgrid-view');
						$('tr.ui-search-toolbar .cbox', gridView).remove();
						
						$('.stats-cell', gridView).click(function(event){
							event.stopImmediatePropagation();
						});
					},
				    loadError: function(xhr,st,err) {
				    	collectionGrid.clearGridData();
					   	alert("Error!");
				    	}
					}).jqGrid('filterToolbar');
				

				collectionGrid.data('excluded', null)
							  .data('included', []);
			});
		});
		
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue + "\", \"_blank\")' title='Show stats'></i>";
			// return "<a target='_blank' title='Show stats' href='javascript:document.location.href=\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue + "\"'><i class='fa fa-bar-chart'></i></a>";
		}
	</script>
</lams:head>
<body class="stripes">
<lams:Page title="Question collections" type="admin">
	<c:forEach var="collection" items="${collections}">
		<div class="panel-body">
			<table class="collectionGrid" data-collection-uid="${collection.uid}" data-collection-name='<c:out value="${collection.name}" />' ></table>
		</div>
	</c:forEach>
</lams:Page>
</body>
</lams:html>