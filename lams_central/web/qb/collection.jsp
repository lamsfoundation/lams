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
		div.buttons {
			margin-top: 5px;
		}
		
		#addCollectionDiv {
			margin-top: 10px;
			padding-top: 10px;
			border-top: black thin solid;
		}
		
		#addCollectionDiv input {
			width: 80%;
			margin-right: 10px;
		}
		
		#addCollectionDiv button {
			float: right;
		}
		
		.form-control {
			display: inline;
		}
		
		.targetCollectionSelect {
			width: 300px;
		}
		
		.targetCollectionDiv {
			float: right;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			$('.collection-grid').each(function(){
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
					    	buttons = grid.closest('.ui-jqgrid').siblings('.buttons').find('.btn'),
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
									buttons.prop('disabled', false);
								}
							} else if (!status) {
								included.splice(index, 1);
								if (included.length === 0) {
									buttons.prop('disabled', true);
								}
							}
						}
					},
					gridComplete : function(){
						var grid = $(this),
							buttons = grid.closest('.ui-jqgrid').siblings('.buttons').find('.btn'),
							// cell containing "(de)select all" button
							selectAllCell = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox > div'),
							included = grid.data('included');
						// remove the default button provided by jqGrid1
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
														buttons.prop('disabled', false);
													} else {
														// on deselect all just change mode
														grid.data('excluded', null);
														grid.data('included', []);
														buttons.prop('disabled', true);
													}
												});
						grid.resetSelection();
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
				

				collectionGrid.data('included', []);
			});
		});
		
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";
		}
		
		function removeCollectionQuestions(button) {
			var grid = $(button).parent().siblings(".ui-jqgrid").find('.collection-grid'),	
				included = grid.data('included'),
				excluded = grid.data('excluded');
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/removeCollectionQuestions.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : grid.data('collectionUid'),
					'included'	    : included ? JSON.stringify(included) : null,
					'excluded'	    : excluded ? JSON.stringify(excluded) : null
				},
				'cache' : false
			}).done(function(){
				grid.trigger('reloadGrid');
			});
		}
		
		function addCollection() {
			var name = $('#addCollectionDiv input').val().trim(),
				lower = name.toLowerCase();
			$('.collection-grid').each(function(){
				if ($(this).data('collectionName').trim().toLowerCase() == lower) {
					alert('Collection with such name already exists');
					name = null;
					return false;
				}
			});
			if (name) {
				$.ajax({
					'url'  : '<lams:LAMSURL />qb/collection/addCollection.do',
					'type' : 'POST',
					'dataType' : 'text',
					'data' : {
						'name' : name
					},
					'cache' : false
				}).done(function(){
					document.location.reload();
				});
			}
		}
		
		function addCollectionQuestions(button, copy) {
			var grid = $(button).closest('.buttons').siblings(".ui-jqgrid").find('.collection-grid'),
				sourceCollectionUid = grid.data('collectionUid'),
				targetCollectionUid = $(button).closest('.panel-body').find('.targetCollectionSelect').val(),
				included = grid.data('included'),
				excluded = grid.data('excluded');
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/addCollectionQuestions.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'sourceCollectionUid' : sourceCollectionUid,
					'targetCollectionUid' : targetCollectionUid,
					'copy'			: copy,
					'included'	    : included ? JSON.stringify(included) : null,
					'excluded'	    : excluded ? JSON.stringify(excluded) : null
				},
				'cache' : false
			}).done(function(){
				$('.collection-grid[data-collection-uid="' + targetCollectionUid + '"]').trigger('reloadGrid');
			});
		}
	</script>
</lams:head>
<body class="stripes">
<lams:Page title="Question collections" type="admin">
	<c:forEach var="collection" items="${collections}">
		<div class="panel-body">
			<table class="collection-grid" data-collection-uid="${collection.uid}" data-collection-name='<c:out value="${collection.name}" />' ></table>
			<div class="buttons">
				<button class="btn btn-default" onClick="javascript:removeCollectionQuestions(this)" disabled="disabled">Remove questions</button>
				<div class="targetCollectionDiv">
					<span>Transfer questions to </span>
					<select class="form-control targetCollectionSelect">
						<c:forEach var="target" items="${collections}">
							<c:if test="${not (target.uid eq collection.uid)}">
								<option value="${target.uid}"
								<c:if test="${empty collection.userId ? target.personal : empty target.userId }">
									selected="selected"
								</c:if>
								><c:out value="${target.name}" /></option>
							</c:if>
						</c:forEach>
					</select>
					<button class="btn btn-default" onClick="javascript:addCollectionQuestions(this, false)" disabled="disabled">Add</button>
					<button class="btn btn-default" onClick="javascript:addCollectionQuestions(this, true)" disabled="disabled">Copy</button>
				</div>
			</div>
		</div>
	</c:forEach>
	<div id="addCollectionDiv">
		<input placeholder="Enter new collection name" class="form-control" />
		<button class="btn btn-primary" onClick="javascript:addCollection()">Add collection</button>
	</div>
</lams:Page>
</body>
</lams:html>