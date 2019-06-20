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
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable.css"> 
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable-lams.css"> 
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
			color: red !important;
		}
		
		#grid-question-create {
			display: inline-block;
			float: right;
			margin-right: 50px;
		}
		
		#collection-name:hover+span+i {
			visibility:visible
		}
		#collection-name+span+i {
			visibility:hidden
		}
		
		a.thickbox {
			color: black;
			padding-left: 8px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/x-editable.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var collectionGrid = $('#collection-grid');
			
			collectionGrid.jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				caption: "Questions",
			    datatype: "xml",
			 	// data comes from data-collection-* attributes of <table> tag which is a base for the grid
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
			    viewrecords: true,
			    recordpos: "left",
			    colNames:[
			    	"ID",
			    	"Name",
			    	"Used in<br>lessons",
			    	"Actions"
			    ],
			    colModel:[
			    	{name:'id', index:'uid', sortable:true, hidden:true, width: 10},
			    	{name:'name', index:'name', sortable:true, search:true, autoencode:true},
			    	{name: 'usage', index: 'usage', sortable:false, width: 10, align: "center"},
			      	// formatter gets just question uid and creates a button
			    	{name:'actions', index:'actions', classes: "stats-cell", sortable:false, width: 13, align: "center", formatter: actionsFormatter}
			    ],
				beforeSelectRow: function(rowid, e) {
					// do not select rows at all
				    return false;
				},
				loadComplete: function(data) {
					//init thickbox
					tb_init('a.thickbox');
			    },
			    loadError: function(xhr,st,err) {
			    	collectionGrid.clearGridData();
				   	alert("Error!");
			    }
			}).jqGrid('filterToolbar');

			//turn to inline mode for x-editable.js
			$.fn.editable.defaults.mode = 'inline';
			//enable renaming of lesson title  
			$('#collection-name').editable({
			    type: 'text',
			    pk: ${collection.uid},
			    url: "<lams:LAMSURL />qb/collection/changeCollectionName.do",
			    validate: function(value) {
				    //close editing area on validation failure
		            if (!value.trim()) {
		                $('.editable-open').editableContainer('hide', 'cancel');
		                return 'Can not be empty!';
		            }
		        },
			    //assume server response: 200 Ok {status: 'error', msg: 'field cannot be empty!'}
			    success: function(response, newValue) {
					if (response.created == 'false') {
						alert('Collection with such name already exists');
					}
			    }
		    //hide and show pencil on showing and hiding editing widget
			}).on('shown', function(e, editable) {
				$(this).nextAll('i.fa-pencil').hide();
			}).on('hidden', function(e, reason) {
				$(this).nextAll('i.fa-pencil').show();
			});
		});
		
		// Creates a button to display question statistics
		function actionsFormatter(cellvalue){
			var cellhtml = "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";

			cellhtml += "<a href='<c:url value='/qb/edit/editQuestion.do'/>?qbQuestionUid=" + cellvalue + "&KeepThis=true&TB_iframe=true' class='thickbox'>"; 
			cellhtml += 	"<i class='fa fa-pencil' title='<fmt:message key='label.edit' />'></i>";
			cellhtml += "</a>";

			return cellhtml;
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
		<button class="btn btn-default btn-sm" onClick="javascript:document.location.href='<lams:LAMSURL />qb/collection/show.do'">
			Collection management
		</button>
	</div>
	
	<h4 class="voffset20">
		<span id="collection-name">
			<c:out value="${collection.name}" />
		</span>
		<span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
		
		<c:if test="${collection.personal}">
			<span class="grid-collection-private small">
				<i class="fa fa-lock"></i> Private collection
			</span>
		</c:if>
			
		<div class="btn-group-xs pull-right">		
			<div class="btn-group-xs" style="display: flex;">
				<select id="question-type" class="form-control btn-xs" style="height: auto;">
					<option selected="selected"><fmt:message key="label.question.type.multiple.choice" /></option>
					<option><fmt:message key="label.question.type.matching.pairs" /></option>
					<option><fmt:message key="label.question.type.short.answer" /></option>
					<option><fmt:message key="label.question.type.numerical" /></option>
					<option><fmt:message key="label.question.type.true.false" /></option>
					<option><fmt:message key="label.question.type.essay" /></option>
					<option><fmt:message key="label.question.type.ordering" /></option>
					<option><fmt:message key="label.question.type.mark.hedging" /></option>
				</select>&nbsp;
					
				<a onclick="initLinkHref(${collection.uid});return false;" href=""
					class="btn btn-default thickbox" id="create-question-href">  
					<fmt:message key="label.create.question" />
				</a>
			</div>
		</div>
	</h4>
				
	<c:choose>
		<c:when test="${hasQuestions}">			
			<%-- jqGrid placeholder with some useful attributes --%>
			<div class="voffset20" >
				<table id="collection-grid" data-collection-uid="${collection.uid}">
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<div class="header-column">
				There are no questions in this collection
			</div>
		</c:otherwise>
	</c:choose>

		<div class="container-fluid">
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

	
	<!-- Dummy div for question save to work properly -->
	<div id="itemArea" class="hidden"></div>
</lams:Page>
</body>
</lams:html>