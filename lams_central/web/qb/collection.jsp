<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<c:set var="hasQuestions" value="${questionCount > 0}" />

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title>Collection</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<style>
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
		
		.grid-question-count {
			margin-left: 10px;
		}
		
		.grid-collection-private {
			margin-left: 10px;
			color: red;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var collectionGrid = $('#collection-grid');
			
			collectionGrid.jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				// data comes from data-collection-* attributes of <table> tag which is a base for the grid
				caption: collectionGrid.data('collectionTitle'),
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
		
		// Creates a button to display question statistics
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";
		}
		
		// remove a collection
		function removeCollection() {
			var grid = $('#collection-grid'),
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
					document.location.href = '<lams:LAMSURL />qb/collection/show.do';
				});
			}
		}
		
		// share a collection with authors of an organisation
		function shareCollection() {
			var grid =  $('#collection-grid'),
				collectionUid = grid.data('collectionUid'),
				organisationId = $('#targetOrganisationSelect').val();
			
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
		function unshareCollection(organisationId) {
			var grid =  $('#collection-grid'),
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
<lams:Page title="Collection" type="admin">
	<button class="btn btn-default" onClick="javascript:document.location.href='<lams:LAMSURL />qb/collection/show.do'">Collection management</button>
	<div class="panel-body">
		<c:choose>
			<c:when test="${hasQuestions}">
				<c:set var="collectionTitle">
					<c:out value="${collection.name}" />
					<span class="grid-question-count">(${questionCount} questions)</span>
					<c:if test="${collection.personal}">
						<span class="grid-collection-private"><i class="fa fa-lock"></i> Private</span>
					</c:if>
				</c:set>
				<%-- jqGrid placeholder with some useful attributes --%>
				<table id="collection-grid" data-collection-uid="${collection.uid}"
				 	   data-collection-title='${collectionTitle}' data-collection-name="<c:out value='${collection.name}' />">
				</table>
			</c:when>
			<c:otherwise>
				There are no questions in this collection
			</c:otherwise>
		</c:choose>

		
		<%-- Do not display links for collection manipulation for public and private collections --%>
		<c:if test="${not empty collection.userId and not collection.personal}">
			<div class="container-fluid">
				<div class="row">
					<c:if test="${not hasQuestions}">
						<div class="col-xs-12 col-md-2">
							<button class="btn btn-default" onClick="javascript:removeCollection()">Remove collection</button>
						</div>
					</c:if>
					<c:if test="${not empty availableOrganisations}">
						<div class="col-xs-12 col-md-2">
							<span>Share collection with </span>
						</div>
						<div class="col-xs-12 col-md-6">
							<select id="targetOrganisationSelect" class="form-control">
								<c:forEach var="target" items="${availableOrganisations}">
										<option value="${target.organisationId}">
											<c:out value="${target.name}" />
										</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-12 col-md-2">
							<button class="btn btn-default" onClick="javascript:shareCollection()">Share</button>
						</div>
					</c:if>
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
								<button class="btn btn-default" onClick="javascript:unshareCollection(${organisation.organisationId})">
									Unshare
								</button>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</c:if>
		
	</div>
</lams:Page>
</body>
</lams:html>