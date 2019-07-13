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
			font-size: smaller;
		}
		
		#grid-question-create {
			display: inline-block;
			float: right;
			margin-right: 50px;
		}
		
		#collection-name-row {
			min-height: 20px; 
			margin-top: 20px;
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
		
		select {
			height: auto !important;
			width: auto !important;
		}
		
		.all-learning-outcomes {
			display: none;
		}
		
		.all-learning-outcomes-show {
			cursor: pointer;
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
			var collectionGrid = $('#collection-grid'),	
				isPublicCollection = ${empty collection.userId};
			
			collectionGrid.jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				caption: "Questions",
			    datatype: "xml",
			    url: "<lams:LAMSURL />qb/collection/getCollectionGridData.do?view=single&collectionUid=${collection.uid}",
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
			    viewrecords: true,
			    recordpos: "left",
			    hidegrid: false,
			    colNames:[
			    	"ID",
			    	"Name",
			    	"questionType",
			    	"questionVersion",
			    	"Learning Outcomes",
			    	"Used in<br>lessons",
			    	"Actions",
			    	"hasVersions"
			    ],
			    colModel:[
			    	{name:'id', index:'uid', sortable:true, hidden:false, width: 10},
			    	{name:'name', index:'name', sortable:true, search:true, autoencode:true, formatter: questionNameFormatter},
			    	{name:'questionType', index:'questionType', width:0, hidden: true},
			    	{name:'questionVersion', index:'questionVersion', width:0, hidden: true},
			    	{name: 'learningOutcomes', index: 'learningOutcomes', sortable:true, search:true, autoencode: true, width: 25, formatter: learningOutcomeFormatter},
			    	{name: 'usage', index: 'usage', sortable:false, width: 10, align: "center"},
			      	// formatter gets just question uid and creates a button
			    	{name:'actions', index:'actions', classes: "stats-cell", sortable:false, width: 13, align: "center", formatter: actionsFormatter},
					{name:'hasVersions', index:'hasVersions', width:0, hidden: true}
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
			    },
				subGrid : true,
				subGridOptions: {
				    hasSubgrid: function (options) {
				        return options.data.hasVersions == 'true';
				    }
				 },
				subGridRowExpanded: function(subgrid_id, row_id) {
				    var subgrid_table_id = subgrid_id + "_t",
				   	    rowData = jQuery("#" + subgrid_id.substring(0, subgrid_id.lastIndexOf('_'))).getRowData(row_id),
				   	    questionId = rowData["id"].split("_")[0];
					jQuery("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll archive'></table><div id='"+subgrid_table_id+"_pager' class='scroll' ></div>");
					jQuery("#"+subgrid_table_id).jqGrid({
							 guiStyle: "bootstrap",
							 iconSet: 'fontAwesome',
							 autoencode:false,
							 autowidth: true,
						     datatype: "xml",
						     url: "<lams:LAMSURL />qb/collection/getQuestionVersionGridData.do?view=version&qbQuestionId=" + questionId,
						     height: "100%",
						     cmTemplate: { title: false },
						     cellEdit:false,
						     pager: false,
						     colNames: [
						    	"ID",
						    	"Name",
						    	"questionType",
						    	"questionVersion",
						    	"Learning Outcomes",
						    	"Used in<br>lessons",
						    	"Actions"
						     ],
						     colModel: [
						    	{name:'id', index:'question_id', sortable:false, hidden:true, width: 10},
						    	{name:'name', index:'name', sortable:false, search:false, autoencode:true, formatter: questionNameFormatter},
						    	{name:'questionType', index:'questionType', width:0, hidden: true},
						    	{name:'questionVersion', index:'questionVersion', width:0, hidden: true},
						    	{name: 'learningOutcomes', index: 'learningOutcomes', width:0, hidden: true},
						    	{name: 'usage', index: 'usage', sortable:false, width: 9, align: "center"},
						      	// formatter gets just question uid and creates a button
						    	{name:'actions', index:'actions', classes: "stats-cell", sortable:false, width: 12, align: "center", formatter: actionsFormatter}
						     ],
						     loadError: function(xhr,st,err) {
						    	jQuery("#"+subgrid_table_id).clearGridData();
						    	alert("Error!");
						     }
					  	});
					}
			}).jqGrid('filterToolbar');

			if (!isPublicCollection) {
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
			}
		});
		
		function learningOutcomeFormatter(cellvalue) {
			if (!cellvalue) {
				return '';
			}
			
			var outcomes = cellvalue.split('<br>'),
				cellhtml = '',
				firstOutcome = null;
			outcomes.forEach(function(outcome){
				cellhtml += '<span class="alert-info btn-xs">' + outcome + '</span><br>';
				if (!firstOutcome) {
					firstOutcome = cellhtml;
				}
			});
			
			// if there is only one outcome, just show it
			// if there is more, show the first one and a button to show all
			return outcomes.length === 1 ? cellhtml :
				   	firstOutcome + '<a class="all-learning-outcomes-show" onClick="javascript:showLearningOutcomes(this)">'
					+ '(show all)</a><div class="all-learning-outcomes">' + cellhtml + '</div>';
		}
		
		function showLearningOutcomes(button) {
			var cell = $(button).closest('td'),
				fullText = $('.all-learning-outcomes', cell).html();
			cell.html(fullText);
		}
		
		// auxiliary formatter for jqGrid's question statistics column
		function actionsFormatter(cellvalue){
			var cellhtml = "<i class='fa fa-bar-chart' onClick='javascript:window.open(\"<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" + cellvalue 
					+ "\", \"_blank\")' title='Show stats'></i>";

			cellhtml += "<a  title='<fmt:message key='label.edit' />' href='<c:url value='/qb/edit/editQuestion.do'/>?qbQuestionUid=" 
						+ cellvalue + "&collectionUid=${collection.uid}&KeepThis=true&TB_iframe=true&modal=true' class='thickbox'>"; 
			cellhtml += 	"<i class='fa fa-pencil'></i>";
			cellhtml += "</a>";

			return cellhtml;
		}
		
		//auxiliary formatter for jqGrid's question column
		function questionNameFormatter (cellvalue, options, rowObject) {
	       	var questionTypeInt = rowObject[2].textContent;
	       	var questionType;
	       	switch (questionTypeInt) {
	        case '1':
	        	questionType = "<fmt:message key="label.question.type.multiple.choice" />";
	          	break;
	        case '2':
	        	questionType = "<fmt:message key="label.question.type.matching.pairs" />";
	          	break;
	        case '3':
	        	questionType = "<fmt:message key="label.question.type.short.answer" />";
	          	break;
	        case '4':
	        	questionType = "<fmt:message key="label.question.type.numerical" />";
	          	break;
	        case '5':
	        	questionType = "<fmt:message key="label.question.type.true.false" />";
	          	break;
	        case '6':
	        	questionType = "<fmt:message key="label.question.type.essay" />";
	          	break;
	        case '7':
	        	questionType = "<fmt:message key="label.question.type.ordering" />";
	          	break;
	        case '8':
	        	questionType = "<fmt:message key="label.question.type.mark.hedging" />";
	          	break;
	      	}

	       	var questionVersion = rowObject[3].textContent;

	       	var text = cellvalue ? "<a target='_blank' title='Show stats' href='<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=" 
	       						  + options.rowId + "'>" + cellvalue  + "</a>" : "";
	        text += "<span class='pull-right alert-info btn-xs loffset5'>";
	       	text += "v. " + questionVersion;
	        text += "</span>";
	        if (questionType) {
		       	text += "<span class='pull-right alert-info btn-xs'>";
		       	text += questionType;
		        text += "</span>";
	        }
	        	
			return text;
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
		function shareCollection(organisationId) {
			var grid =  $('#collection-grid');
			
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/shareCollection.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : ${collection.uid},
					'organisationId': organisationId
				},
				'cache' : false
			}).done(function(){
				document.location.reload();
			});
		}
		
		// stop sharing a collection with authors of an organisation
		function unshareCollection(organisationId) {
			var grid =  $('#collection-grid');
			
			$.ajax({
				'url'  : '<lams:LAMSURL />qb/collection/unshareCollection.do',
				'type' : 'POST',
				'dataType' : 'text',
				'data' : {
					'collectionUid' : ${collection.uid},
					'organisationId': organisationId
				},
				'cache' : false
			}).done(function(){
				document.location.reload();
			});
		}

	    function importQTI(){
	    	window.open('<lams:LAMSURL/>questions/questionFile.jsp',
				'QuestionFile','width=500,height=240,scrollbars=yes');
	    }
		
	    function saveQTI(formHTML, formName) {
	    	var form = $($.parseHTML(formHTML));
			$.ajax({
				type: "POST",
				url: '<c:url value="/imsqti/saveQTI.do" />?contentFolderID=${contentFolderID}&collectionUid=${collection.uid}',
				data: form.serializeArray(),
				success: function() {
					location.reload();
				}
			});
	    }

	    function exportQTI(){
	    	var frame = document.getElementById("downloadFileDummyIframe");
	    	frame.src = '<c:url value="/imsqti/exportCollectionAsQTI.do" />?collectionUid=${collection.uid}';
	    }
		
		function exportQuestionsXml(){   
		    var reqIDVar = new Date();
			location.href="<c:url value='/xmlQuestions/exportQuestionsXml.do'/>?collectionUid=${collection.uid}&reqID="+reqIDVar.getTime();
		}
		
		//create proper href for "Create question" button
		function initLinkHref() {
			var questionType = document.getElementById("question-type").selectedIndex + 1;
			$("#create-question-href").attr("href", 
					"<c:url value='/qb/edit/initNewQuestion.do'/>?questionType=" + questionType 
					+ "&collectionUid=${collection.uid}" 
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
			<i class="fa fa-angle-double-left"></i>
			Collection management
		</button>
	</div>
	
	<div id="collection-name-row" class="row h4">
		
		<div class="col-xs-12 col-sm-8">
			<span id="collection-name">
				<strong>
					<c:out value="${collection.name}" />
				</strong>
			</span>
			<c:if test="${not empty collection.userId}">
				<span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
			</c:if>
			
			<c:if test="${collection.personal}">
				<span class="grid-collection-private small">
					<i class="fa fa-lock"></i> Private collection
				</span>
			</c:if>
		</div>
		
		<div class=" col-xs-12 col-sm-4">

			<%-- Do not display button for public and private collections --%>
			<c:if test="${not empty collection.userId and not collection.personal}">
				<div class="btn-group-xs pull-right loffset10">
					<c:if test="${not hasQuestions}">
						<button class="btn btn-default" onClick="javascript:removeCollection()" title="Remove collection">
							<i class="fa fa-trash"></i>
							Remove collection
						</button>
					</c:if>
				</div>
			</c:if>
		
			<div class="btn-group btn-group-xs pull-right loffset10" role="group">
				<a class="btn btn-default btn-xs disabled" aria-disabled="true">XML</a>
			
				<a class="btn btn-default btn-xs loffset5 thickbox" title="<fmt:message key="label.import.xml" />"
					href="<c:url value='/xmlQuestions/initImportQuestionsXml.do'/>?collectionUid=${collection.uid}&KeepThis=true&TB_iframe=true&modal=true" >  
					<i class="fa fa-upload"></i>
				</a>
				
				<c:if test="${isQtiExportEnabled && hasQuestions}">
					<a onclick="javascript:exportQuestionsXml();" class="btn btn-default btn-xs" title="<fmt:message key="label.export.xml" />">  
						<i class="fa fa-download"></i>
					</a>
				</c:if>
			</div>
			
			<div class="btn-group btn-group-xs loffset10 pull-right" role="group">
				<a class="btn btn-default btn-xs disabled" aria-disabled="true">QTI</a>
			
				<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-default" title="<fmt:message key='label.import.qti'/>">
					<i class="fa fa-upload"></i>
				</a>
				
				<c:if test="${isQtiExportEnabled && hasQuestions}">
					<a href="#nogo" onClick="javascript:exportQTI()" class="btn btn-default" title="<fmt:message key='label.export.qti'/>">
						<i class="fa fa-download"></i>
					</a>
				</c:if>
			</div>
				
			<div class="btn-group-xs pull-right" style="display: flex;">
				<select id="question-type" class="form-control btn-xs">
					<option selected="selected"><fmt:message key="label.question.type.multiple.choice" /></option>
					<option><fmt:message key="label.question.type.matching.pairs" /></option>
					<option><fmt:message key="label.question.type.short.answer" /></option>
					<option><fmt:message key="label.question.type.numerical" /></option>
					<option><fmt:message key="label.question.type.true.false" /></option>
					<option><fmt:message key="label.question.type.essay" /></option>
					<option><fmt:message key="label.question.type.ordering" /></option>
					<option><fmt:message key="label.question.type.mark.hedging" /></option>
				</select>&nbsp;
						
				<a onclick="initLinkHref();return false;" href="" class="btn btn-default thickbox" id="create-question-href">  
					<i class="fa fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.create.question" />"></i>
				</a>
			</div>
			
		</div>
	</div>
				
	<c:choose>
		<c:when test="${hasQuestions}">			
			<%-- jqGrid placeholder with some useful attributes --%>
			<div class="voffset20" >
				<table id="collection-grid"></table>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-warning">
				There are no questions in this collection
			</div>
		</c:otherwise>
	</c:choose>

	<%-- Do not display links for collection manipulation for public and private collections --%>
	<c:if test="${not empty collection.userId and not collection.personal
		and (not empty collection.organisations or not empty availableOrganisations)}">
		<div class="panel panel-default voffset20">
			<div class="panel-heading">
				Share collection with organisations
			</div>
			<div class="panel-body">
				<table class="table table-striped table-condensed table-responsive" style="max-width: 400px;">
					<tbody>
					
						<%-- Already shared organisations--%>
						<c:forEach var="organisation" items="${collection.organisations}">
							<tr  class="info">
								<td>
									<c:out value="${organisation.name}" />&nbsp;
									<span class="label label-primary">Shared</span>
								</td>
								<td width="30px;">
									<button class="btn btn-default btn-xs" onClick="javascript:unshareCollection(${organisation.organisationId})">
										Unshare
									</button>
								</td>
							</tr>
						</c:forEach>
							
						<%-- Not yet shared ones --%>
						<c:forEach var="organisation" items="${availableOrganisations}">
							<tr>
								<td>
									<c:out value="${organisation.name}" />
								</td>
								<td width="30px;" style="text-align: center;">
									<button class="btn btn-default btn-xs" onClick="javascript:shareCollection(${organisation.organisationId})">
										Share
									</button>
								</td>
							</tr>						
						</c:forEach>
					</tbody>
				</table>
			
			</div>
		</div>
	</c:if>

	
	<!-- Dummy div for question save to work properly -->
	<div id="itemArea" class="hidden"></div>
	<!-- Dummy iframe for exporting QTI packages -->
	<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
</lams:Page>
</body>
</lams:html>