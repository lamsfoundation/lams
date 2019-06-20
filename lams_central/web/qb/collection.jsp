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
	<link type="text/css" href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet">
	<style>
		#edit-collection-button {
			float: right;
		}
		
		.row {
			margin-top: 10px;
		}
				
		.middle-cell {
			padding-top: 6px;
			display: inline-block;
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
		
		#grid-question-create {
			display: inline-block;
			float: right;
			margin-right: 50px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var collectionGrid = $('#collection-grid');
			
			collectionGrid.jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				// data comes from data-collection-* attributes of <table> tag which is a base for the grid
				caption: collectionGrid.data('collectionTitle'),
			    datatype: "xml",
			    url: "<lams:LAMSURL />qb/collection/getCollectionGridData.do?showUsage=true&collectionUid=" + collectionGrid.data('collectionUid'),
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
			    	"Used in<br>lessons",
			    	"Stats"
			    ],
			    colModel:[
			      {name:'id', index:'uid', sortable:true,  width: 10},
			      {name:'name', index:'name', sortable:true, search:true, autoencode:true},
			      {name: 'usage', index: 'usage', sortable:false, width: 10, align: "center"},
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
			if (confirm('Are you sure you want to remove "${collection.name}" collection?')) {
				$.ajax({
					'url'  : '<lams:LAMSURL />qb/collection/removeCollection.do',
					'type' : 'POST',
					'dataType' : 'text',
					'data' : {
						'collectionUid' : ${collection.uid}
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
		
		// add a new collection
		function changeCollectionName() {
			var name = prompt("New collection name");
			if (name) {
				$.ajax({
					'url'  : '<lams:LAMSURL />qb/collection/changeCollectionName.do',
					'type' : 'POST',
					'dataType' : 'text',
					'data' : {
						'collectionUid' : ${collection.uid},
						'name' : name
					},
					'cache' : false
				}).done(function(created){
					if (created == 'true') {
						document.location.reload();
					} else {
						alert('Collection with such name already exists');
					}
				});
			}
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
		};
	</script>
</lams:head>
<body class="stripes">
<lams:Page title="Collection" type="admin">
	<div>
		<button class="btn btn-default" onClick="javascript:document.location.href='<lams:LAMSURL />qb/collection/show.do'">
			Collection management
		</button>
		<button id="edit-collection-button" class="btn btn-primary" onClick="javascript:changeCollectionName()">Change name</button>
	</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${hasQuestions}">
				<%-- Build collection title with its name, question count, optional "private" flag and edit button --%>
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
				<div class="header-column">
					There are no questions in this collection
				</div>
			</c:otherwise>
		</c:choose>

		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-0 col-md-2"></div>
				<div class="col-xs-12 col-md-2 middle-cell">
					<span>Create question</span>
				</div>
				<div class="col-xs-12 col-md-6">
					<select id="question-type" class="form-control">
						<option selected="selected"><fmt:message key="label.question.type.multiple.choice" /></option>
						<option><fmt:message key="label.question.type.matching.pairs" /></option>
						<option><fmt:message key="label.question.type.short.answer" /></option>
						<option><fmt:message key="label.question.type.numerical" /></option>
						<option><fmt:message key="label.question.type.true.false" /></option>
						<option><fmt:message key="label.question.type.essay" /></option>
						<option><fmt:message key="label.question.type.ordering" /></option>
						<option><fmt:message key="label.question.type.mark.hedging" /></option>
					</select>
				</div>
				<div class="col-xs-12 col-md-2">
					<a onclick="initLinkHref(${collection.uid});return false;" 
						href=""
						class="btn btn-default thickbox" id="create-question-href">  
						<fmt:message key="label.create.question" />
					</a>
				</div>
			</div>

		<%-- Do not display links for collection manipulation for public and private collections --%>
			<c:if test="${not empty collection.userId and not collection.personal}">
				<div class="row">
					<div class="col-xs-12 col-md-2">
						<c:if test="${not hasQuestions}">
							<button class="btn btn-default" onClick="javascript:removeCollection()">Remove collection</button>
						</c:if>
					</div>
					<c:if test="${not empty availableOrganisations}">
						<div class="col-xs-12 col-md-2 middle-cell">
							<span>Share collection with</span>
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
						<div class="col-xs-0 col-md-4"></div>
						<div class="col-xs-12 col-md-8 header-column">
							<span>Shared with organisations</span>
						</div>
						<div class="col-xs-0 col-md-2"></div>
					</div>
					<c:forEach var="organisation" items="${collection.organisations}">
						<div class="row">
							<div class="col-xs-0 col-md-4"></div>
							<div class="col-xs-0 col-md-6 middle-cell">
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
			</c:if>
		</div>
	</div>
	
	<!-- Dummy div for question save to work properly -->
	<div id="itemArea" class="hidden"></div>
</lams:Page>
</body>
</lams:html>