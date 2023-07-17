<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css />
	<link href="<lams:LAMSURL/>tool/laasse10/includes/css/assessment.css" rel="stylesheet" type="text/css">
	<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-select.css" type="text/css"/>
	<style>
		#grid-container {
			min-height: 300px;
		}
		#search-widgets {
			display: flex;
		}
		.form-control {
			width: auto;
		}
	</style>

	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript">
		const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
		const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
	</script>
	<lams:JSImport src="includes/javascript/qb-question.js" />
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.highlight.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.blockUI.js"></script>
	<lams:JSImport src="includes/javascript/qb-search.js" />
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-select.js"></script>
    <script  type="text/javascript">
		$(document).ready(function(){
		    $("#referenceForm").validate({
		    	ignore: 'hidden',
   			    submitHandler: function(form) {
					$("#questionUids").val(idsOfSelectedRows.join(","));
   	   			    
	    	    	var options = { 
	    	    		target:  parent.jQuery('#itemArea'), 
	    		   		success: afterRatingSubmit  // post-submit callback
	    		    }; 				

	    			$('#referenceForm').ajaxSubmit(options);
	    		},
	    		invalidHandler: formValidationInvalidHandler,
				errorElement: "em",
				errorPlacement: formValidationErrorPlacement,
				highlight: formValidationHighlight,
				unhighlight: formValidationUnhighlight
		  	});

		    var idsOfSelectedRows = [<c:forEach var="asQuestion" items="${randomPoolQuestions}">"${asQuestion.qbQuestion.uid}",</c:forEach>],
				$grid = $("#questions-grid"),
		    	updateIdsOfSelectedRows = function (id, isSelected) {
		        var index = $.inArray(id, idsOfSelectedRows);
		        if (!isSelected && index >= 0) {
		            idsOfSelectedRows.splice(index, 1); // remove id from the list
		        } else if (index < 0) {
		            idsOfSelectedRows.push(id);
		        }
		    };
			
			$("#questions-grid").jqGrid({
			   	//multiselect: false,
				datatype: "json",
				url: "<c:url value="/authoring/getPagedRandomQuestions.do"/>",
				postData: { 
					sessionMapID:"${sessionMapID}"
		        },
				height: '100%',
				autowidth: true,
				shrinkToFit: true,
			    pager: 'questions-grid-pager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				multiselect: true,
				onSelectRow: updateIdsOfSelectedRows,
			    onSelectAll: function (aRowids, isSelected) {
				    //in case deselect all - simply clear uids array
			    	if (!isSelected) {
			    		questionUids = [];
			    		return;
					}

					// get all questionUids from DB
					var gridParams = $(this).jqGrid("getGridParam");
					$.ajax({ 
					    url: '<c:url value="/authoring/getAllQbQuestionUids.do"/>',
						type: 'POST',
						data: {
							sidx: gridParams.sortname,
							sord: gridParams.sortorder,
							collectionUids: "" + $("#collections-select").val(),
					       	searchString: $("#filter-questions").val() 
						}
					}).done(function(data) {
						//add all questionUids to idsOfSelectedRows array
						data.questionUids.forEach(function(entry) {
							updateIdsOfSelectedRows(entry, true);
						});
					});
			    },
			   	colNames:[
				   	'questionUid',
					'<fmt:message key="label.authoring.basic.list.header.question"/>',
					'questionDescription'
				],
			   	colModel:[
			   		{name:'questionUid', key: true, index:'questionUid', width:0, hidden: true},
			   		{name:'questionName', index:'questionName', width:570, search: true, searchoptions: { clearSearch: false }, formatter:questionNameFormatter},
			   		{name:'questionDescription', index:'questionDescription', width:0, hidden: true}
			   	],
		  	  	gridComplete: gridSearchHighlight,
			   	loadComplete: function() {
				   	//select rows that form random pool
			        var $this = $(this), i, count;
			        for (i = 0, count = idsOfSelectedRows.length; i < count; i++) {
			            $this.jqGrid('setSelection', idsOfSelectedRows[i], false);
			        }
			   	},
			    loadError: function(xhr,textStatus,errorThrown) {
			    	$("#questions-grid").clearGridData();
			    	//display warning message
			    	$(this).jqGrid("displayErrorMessage", "<fmt:message key="error.loaderror"/>");
			    }
			})
			.navGrid("#questions-grid-pager", {edit:false,add:false,del:false,search:false});	

	        //jqgrid autowidth
	        $(window).bind('resize', function() {
	            resizeJqgrid($(".ui-jqgrid-btable:visible"));
	        });
	    	function resizeJqgrid(jqgrids) {
	    		jqgrids.each(function(index) {
	    			var gridId = $(this).attr('id');
	    	    	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	    	    	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	    	    });
	    	};

			//handler for Collections select
	    	$('#collections-select').selectpicker({ 
	            iconBase: 'fa',
	            tickIcon: 'fa-check',
	            actionsBox:"true",
	            selectedTextFormat:"count>3",
	            countSelectedText : "{0} collections selected",
	            noneSelectedText : "Display all collections",
	            selectAllText:"Select all",
	            deselectAllText:"Deselect all",
	            style:"btn btn-sm btn-default",
	            <c:if test="${fn:length(userCollections) > 10}">liveSearch: true</c:if>
	       	});
	    	//workaround for $('#collections-select').selectpicker('selectAll'); throwing exception 
	    	$("#collections-select option").prop("selected", "selected");
	    	$('#collections-select').selectpicker('refresh');
		});
	</script>
</lams:head>
	
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			Add random question
		</div>
			
		<div class="panel-body">
		
			<h5 style="margin-top: 0px;">
				Select pool of random questions.
			</h5>
			
			<form:form action="saveOrUpdateRandomReference.do" id="referenceForm" method="post">
				<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
				<input type="hidden" name="isReferenceNew" value="${isReferenceNew}"/>
				<input type="hidden" name="questionUids" id="questionUids"/>
			</form:form>
			
			<div id="search-widgets" class="input-group-sm">
				<input type="text" id="filter-questions" class="form-control roffset10" placeholder="Contains text" 
					onkeydown="doSearch(arguments[0]||event)" />
					
				<span style="padding-top: 5px;">Collections: &nbsp;</span>
				
				<select id="collections-select" class="selectpicker" multiple>
					<c:forEach var="collection" items="${userCollections}">
						<option value="${collection.uid}">
							<c:out value="${collection.name}" />
						</option>
					</c:forEach>
				</select>
			</div>

			<div id="grid-container" class="voffset10">
		 		<div class="grid-holder">
		 			<table id="questions-grid" class="scroll"></table>
					<div id="questions-grid-pager" class="scroll"></div>
				</div>
			</div>

		</div>		
	</div>	
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#referenceForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>
      	</div>
    </footer>
</body>
</lams:html>