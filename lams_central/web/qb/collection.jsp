<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Question collections</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet">
	<style>
		#addCollectionDiv {
			margin-top: 10px;
			padding-top: 10px;
			border-top: black thin solid;
		}
		
		#addCollectionDiv input {
			width: 80%;
			margin-right: 10px;
			display: inline-block;
		}
		
		#addCollectionDiv button {
			float: right;
		}
		
		.row {
			margin-top: 10px;
		}
		
		.row > div:first-child, .row > div:last-child  {
			text-align: left;
			padding-left: 0;
		}
		
		.row > div {
			text-align: right;
		}
		
		.header-column {
			font-weight: bold;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			// create a grid for each collection
			$('.collection-grid').each(function(){
				var collectionGrid = $(this);
				
				collectionGrid.jqGrid({
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
					// data comes from data-collection-* attributes of <table> tag which is a base for the grid
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
				      // formatter gets just question uid and creates a button
				      {name:'stats', index:'stats', classes: "stats-cell", sortable:false, width: 10, align: "center", formatter: statsLinkFormatter}
				      ],
			        onSelectRow : function(id, status, event) {
					    var grid = $(this),
					    	// if no questions are selected, buttons to manipulate them get disabled
					    	buttons = grid.closest('.ui-jqgrid').siblings('.container-fluid').find('.questionButtons .btn'),
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
							// if no questions are selected, buttons to manipulate them get disabled
							buttons = grid.closest('.ui-jqgrid').siblings('.container-fluid').find('.questionButtons .btn'),
							// cell containing "(de)select all" button
							selectAllCell = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox > div'),
							included = grid.data('included');
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
						// remove checkbox next to search bar
						$('tr.ui-search-toolbar .cbox', gridView).remove();
						
						// do not select row when clicked on stats button
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
		
		// Creates a button to display question statistics
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";
		}
		
		// remove questions from a collection
		function removeCollectionQuestions(button) {
			var grid = $(button).closest('.container-fluid').siblings(".ui-jqgrid").find('.collection-grid'),	
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
		
		// add or copy questions to a collection
		function addCollectionQuestions(button, copy) {
			var grid = $(button).closest('.container-fluid').siblings(".ui-jqgrid").find('.collection-grid'),
				sourceCollectionUid = grid.data('collectionUid'),
				targetCollectionUid = $(button).closest('.container-fluid').find('.targetCollectionSelect').val(),
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
		
		// add a new collection
		function addCollection() {
			var name = $('#addCollectionDiv input').val().trim(),
				lower = name.toLowerCase();
			// check if a collection with same name already exists
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
		
		// remove a collection
		function removeCollection(button) {
			var grid = $(button).closest('.container-fluid').siblings(".ui-jqgrid").find('.collection-grid'),
				collectionUid = grid.data('collectionUid'),
				name = grid.data('collectionName');
		
			if (confirm('Are you sure you want to remove "' + name + '" collection?')) {
				$.ajax({
					'url'  : '<lams:LAMSURL />qb/collection/removeCollection.do',
					'type' : 'POST',
					'dataType' : 'text',
					'data' : {
						'collectionUid' : collectionUid
					},
					'cache' : false
				}).done(function(){
					document.location.reload();
				});
			}
		}
		
		// share a collection with authors of an organisation
		function shareCollection(button) {
			var grid = $(button).closest('.container-fluid').siblings(".ui-jqgrid").find('.collection-grid'),
				collectionUid = grid.data('collectionUid'),
				organisationId = $(button).closest('.container-fluid').find('.targetOrganisationSelect').val();
			
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/shareCollection.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : collectionUid,
					'organisationId': organisationId
				},
				'cache' : false
			}).done(function(){
				document.location.reload();
			});
		}
		
		// stop sharing a collection with authors of an organisation
		function unshareCollection(button, organisationId) {
			var grid = $(button).closest('.container-fluid').siblings(".ui-jqgrid").find('.collection-grid'),
				collectionUid = grid.data('collectionUid');
			
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/unshareCollection.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : collectionUid,
					'organisationId': organisationId
				},
				'cache' : false
			}).done(function(){
				document.location.reload();
			});
		}

		//create proper href for "Create question" button
		function initLinkHref(collectionUid) {
			var questionType = document.getElementById("question-type").selectedIndex + 1;
			$("#create-question-href").attr("href", 
					"<c:url value='/qb/edit/newQuestionInit.do'/>?questionType=" + questionType 
					+ "&collectionUid=" + collectionUid 
					+ "&KeepThis=true&TB_iframe=true&modal=true");
		};
		
	    //this method gets invoked after question has been edited and saved
		function refreshThickbox(){
			location.reload();
			//var newQuestionUid = $("#itemArea").html();
			//location.href = '<c:url value="/qb/stats/show.do" />?qbQuestionUid=' + newQuestionUid;
		};
	</script>
</lams:head>
<body class="stripes">
<lams:Page title="Question collections" type="admin">
	<c:forEach var="collection" items="${collections}" varStatus="status">
	
	<!-- TODO move to Collection jsp page -->
	<c:if test="${status.first}">
	<div class="btn-group-xs " style="height: 20px;">
	<!-- Dropdown menu for choosing a question type -->
	<div class="form-inline form-group pull-right">
		<select id="question-type" class="form-control input-sm">
			<option selected="selected"><fmt:message key="label.question.type.multiple.choice" /></option>
			<option><fmt:message key="label.question.type.matching.pairs" /></option>
			<option><fmt:message key="label.question.type.short.answer" /></option>
			<option><fmt:message key="label.question.type.numerical" /></option>
			<option><fmt:message key="label.question.type.true.false" /></option>
			<option><fmt:message key="label.question.type.essay" /></option>
			<option><fmt:message key="label.question.type.ordering" /></option>
			<option><fmt:message key="label.question.type.mark.hedging" /></option>
		</select>
		
		<a onclick="initLinkHref(${collection.uid});return false;" href="" class="btn btn-default btn-sm button-add-item thickbox" id="create-question-href">  
			<fmt:message key="label.create.question" />
		</a>
	</div>
	</div>
	</c:if>
	
		<div class="panel-body">

		
			<%-- jqGrid placeholder with some useful attributes --%>
			<table class="collection-grid" data-collection-uid="${collection.uid}" data-collection-name='<c:out value="${collection.name}" />' ></table>
			
			<div class="container-fluid">
			
				<div class="questionButtons row">
					<div class="col-xs-12 col-md-2">
						<%-- Do not allow removing questions from public collection --%>
						<c:if test="${not empty collection.userId}">
							<button class="btn btn-default" onClick="javascript:removeCollectionQuestions(this)" disabled="disabled">Remove questions</button>
						</c:if>
					</div>
					<div class="col-xs-12 col-md-2">
						<span>Transfer questions to </span>
					</div>
					<div class="col-xs-12 col-md-6">
						<select class="form-control targetCollectionSelect">
							<c:forEach var="target" items="${collections}">
								<c:if test="${not (target.uid eq collection.uid)}">
									<option value="${target.uid}"
									<%-- The default target collection for adding/copying questions is the public collection,
										 unless it is the public collection itself: then it is the private collection --%>
									<c:if test="${empty collection.userId ? target.personal : empty target.userId }">
										selected="selected"
									</c:if>
									>
										<c:out value="${target.name}" />
									</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div class="col-xs-12 col-md-2 button-group">
						<button class="btn btn-default" onClick="javascript:addCollectionQuestions(this, false)" disabled="disabled">Add</button>
						<button class="btn btn-default" onClick="javascript:addCollectionQuestions(this, true)" disabled="disabled">Copy</button>
					</div>
				</div>
				
				<%-- Do not display links for collection manipulation for public and private collections --%>
				<c:if test="${not empty collection.userId and not collection.personal}">
					<div class="row">
						<div class="col-xs-12 col-md-2">
							<button class="btn btn-default" onClick="javascript:removeCollection(this)">Remove collection</button>
						</div>
						<div class="col-xs-12 col-md-2">
							<span>Share collection with </span>
						</div>
						<div class="col-xs-12 col-md-6">
							<select class="form-control targetOrganisationSelect">
								<c:forEach var="target" items="${collection.shareableWithOrganisations}">
										<option value="${target.organisationId}">
											<c:out value="${target.name}" />
										</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-12 col-md-2">
							<button class="btn btn-default" onClick="javascript:shareCollection(this)">Share</button>
						</div>
					</div>
					
					<c:if test="${not empty collection.organisations}">
						<div class="row">
							<div class="col-xs-0 col-md-2"></div>
							<div class="col-xs-12 col-md-8 header-column">
								<span>Shared with organisations</span>
							</div>
							<div class="col-xs-0 col-md-2"></div>
						</div>
						<c:forEach var="organisation" items="${collection.organisations}">
							<div class="row">
								<div class="col-xs-0 col-md-2"></div>
								<div class="col-xs-0 col-md-8">
									<c:out value="${organisation.name}" />
								</div>
								<div class="col-xs-0 col-md-2">
									<button class="btn btn-default" onClick="javascript:unshareCollection(this, ${organisation.organisationId})">
										Unshare
									</button>
								</div>
							</div>
						</c:forEach>
					</c:if>
				</c:if>
			</div>
		</div>
	</c:forEach>
	<div id="addCollectionDiv">
		<input placeholder="Enter new collection name" class="form-control" />
		<button class="btn btn-primary" onClick="javascript:addCollection()">Add collection</button>
	</div>
	
	<!-- TODO move to Collection jsp page -->
	<!-- Dummy div for question save to work properly -->
	<div id="itemArea" class="hidden"></div>
</lams:Page>
</body>
</lams:html>