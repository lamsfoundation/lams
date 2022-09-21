<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.qb.collection.management" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<style>
		#add-collection-div {
			height: 50px;
		}
		
		#add-collection-button {
			float: right;
		}
		
		.ui-jqgrid-title {
			display: inline-block;
			width: 100%;
			height: 25px;
		}
		
		.edit-button {
			position: absolute;
			right: 50px;
			font-size: 12px !important;
		}
				
		.grid-question-count {
			margin-left: 10px;
		}
		
		.grid-collection-private {
			margin-left: 10px;
			color: red;
			font-size: smaller;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			<%-- Add jqGrid internationalisation snippet --%>
			<%@ include file="/jqGrid.i18n.jsp"%>
			
			// create a grid for each collection
			$('.collection-grid').each(function(){
				var collectionGrid = $(this);
				
				collectionGrid.jqGrid({
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
					// data comes from data-collection-* attributes of <table> tag which is a base for the grid
					caption: collectionGrid.data('collectionTitle'),
				    datatype: "xml",
				    url: "<lams:LAMSURL />qb/collection/getCollectionGridData.do?view=list&collectionUid=" + collectionGrid.data('collectionUid'),
				    height: "100%",
				    autowidth:true,
					shrinkToFit: true,
					viewrecords: true,
				    cellEdit: false,
				    cmTemplate: { title: false, search: false },
				    sortorder: "asc", 
				    sortname: "name", 
				    pager: true,
				    rowList:[10,20,30,40,50,100],
				    rowNum: 10,
				    colNames:[
				    	'<fmt:message key="label.qb.collection.grid.id" />',
				    	'<fmt:message key="label.qb.collection.grid.name" />',
				    	"questionType",
				    	"questionVersion",
				    	'<fmt:message key="label.qb.collection.grid.outcomes" />',
				    	// this column is hidden, so data coming from controller can be the same as for single collection view
				    	'usage',
				    	'<fmt:message key="label.qb.collection.grid.stats" />'
				    ],
				    colModel:[
				    	{name:'id', index:'question_id', sortable:true,  width: 10},
				    	// formatter gets just question uid and creates a link
				    	{name:'name', index:'name', sortable:true, search:true, autoencode:true, formatter: nameLinkFormatter},
				    	{name:'questionType', index:'questionType', width:0, hidden: true},
				   		{name:'questionVersion', index:'questionVersion', width:0, hidden: true},
				    	{name: 'learningOutcomes', index: 'learningOutcomes', width:0, hidden: true},
				    	{name: 'usage', index: 'usage', hidden: true},
				    	// formatter gets just question uid and creates a button
				    	{name:'stats', index:'stats', classes: "stats-cell", sortable:false, width: 10, align: "center", formatter: statsLinkFormatter}
				    ],
					beforeSelectRow: function(rowid, e) {
						// do not select rows at all
					    return false;
					},
				    loadError: function(xhr,st,err) {
				    	collectionGrid.clearGridData();
					   	alert('<fmt:message key="label.qb.collection.grid.error" />');
				    }
				}).jqGrid('filterToolbar');
			});
		});
		
		// Creates a button to display question statistics
		function statsLinkFormatter(cellvalue){
			return "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";
		}
		
		// Creates a link to display question statistics
		function nameLinkFormatter(cellValue, options) {
			return cellValue ? "<a target='_blank' title='Show stats' href='<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + options.rowId + "'>"
					+ cellValue  + "</a>" : "";
		}
		
		// add a new collection
		function addCollection() {
			// get collection name from a pop up
			var name = prompt('<fmt:message key="label.qb.collection.name.prompt" />'),
				lower = name.toLowerCase();
			// check if a collection with same name already exists
			$('.collection-grid').each(function(){
				if (String($(this).data('collectionName')).trim().toLowerCase() == lower) {
					alert('<fmt:message key="label.qb.collection.name.duplicate.error" />');
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
						'name' : name,
						"<csrf:tokenname/>" : "<csrf:tokenvalue/>"
					},
					'cache' : false
				}).done(function(){
					document.location.reload();
				});
			}
		}
	</script>
</lams:head>
<body class="stripes">
<fmt:message key="label.qb.collection.management" var="label.qb.collection.management" />
<lams:Page title="${label.qb.collection.management}" type="admin">
	<%-- This option can be switched off in appadmin --%>
	<c:if test="${createCollectionAllowed}">
		<div id="add-collection-div">
			<button id="add-collection-button" class="btn btn-primary" onClick="javascript:addCollection()">
				<fmt:message key="label.qb.collection.add" />
			</button>
		</div>
	</c:if>
	
	<c:forEach var="collection" items="${collections}">
		<div class="panel-body">
			<%-- Build collection title with its name, question count, optional "private" flag and edit button --%>
			<c:set var="collectionTitle">
				<a href="<lams:LAMSURL />qb/collection/showOne.do?collectionUid=${collection.uid}">
					<c:choose>
						<c:when test="${empty collection.userId and not collection.personal}">
							<fmt:message key="label.qb.collection.public.name" />
						</c:when>
						<c:otherwise>
							<c:out value="${collection.name}" />
						</c:otherwise>
					</c:choose>
					
					<span class="grid-question-count">(${questionCount[collection.uid]}&nbsp;<fmt:message key="label.qb.collection.questions" />)</span>
					<c:if test="${collection.personal}">
						<span class="grid-collection-private"><i class="fa fa-lock"></i> <fmt:message key="label.qb.collection.private" /></span>
					</c:if>
				</a>
				<button class="btn btn-primary btn-xs edit-button"
						onClick="javascript:document.location.href=`<lams:LAMSURL />qb/collection/showOne.do?collectionUid=${collection.uid}`">
					<fmt:message key="label.qb.collection.edit" />
				</button>
			</c:set>
				
			<%-- jqGrid placeholder with some useful attributes --%>
			<table class="collection-grid" data-collection-uid="${collection.uid}"
			 	   data-collection-name='<c:out value="${collection.name}" />' data-collection-title='${collectionTitle}'>
			</table>
		</div>
	</c:forEach>
</lams:Page>
</body>
</lams:html>