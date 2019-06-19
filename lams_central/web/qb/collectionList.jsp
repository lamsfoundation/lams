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
					beforeSelectRow: function(rowid, e) {
						// do not select rows at all
					    return false;
					},
				    loadError: function(xhr,st,err) {
				    	collectionGrid.clearGridData();
					   	alert("Error!");
				    	}
					}).jqGrid('filterToolbar');
			});
		});
		
		// Creates a button to display question statistics
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";
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
	</script>
</lams:head>
<body class="stripes">
<lams:Page title="Question collections" type="admin">
	<c:forEach var="collection" items="${collections}">
		<div class="panel-body">
			<%-- jqGrid placeholder with some useful attributes --%>
			<table class="collection-grid" data-collection-uid="${collection.uid}" data-collection-name='<c:out value="${collection.name}" />' ></table>
			
			<div class="container-fluid">
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
</lams:Page>
</body>
</lams:html>