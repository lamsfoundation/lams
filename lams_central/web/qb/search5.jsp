<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<link rel="stylesheet"href="<lams:LAMSURL/>css/free.ui.jqgrid.min5.css">
<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-select.min5.css" />
<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
<link rel="stylesheet" href="<lams:LAMSURL/>css/components-responsive.css">

<style>
	.qb-search-widget .question-description-grid {
		min-height: 10px;
		max-height: 12px;
   		overflow-x: hidden;
   		margin-top: 4px;
	}
	
	.qb-search-widget .question-title-grid {
	    overflow-x: hidden;
	}
		
	.qb-search-widget .question-description {
		font-size: smaller;
	}
		
	.qb-search-widget #grid-container .ui-jqgrid.ui-jqgrid-bootstrap tr.jqgrow > td {
		padding: 10px;
	}
		
	@media (min-width: 768px){
		.qb-search-widget #question-detail-area {
		    padding-left: 20px !important;
		}
	}
	@media (max-width: 768px){
		.qb-search-widget #question-detail-area {
		    margin-top: 20px !important;
		}
	}
		
	/* jqGrid header */
	.qb-search-widget #grid-container .ui-jqgrid .ui-jqgrid-htable .ui-jqgrid-labels th div {
	    font-size: 14px;
   		margin: 6px;
	}
	
	.qb-search-widget .dropdown-item {
		cursor: pointer;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-select.min5.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-search.js"></script>
<script type="text/javascript">
	<%-- Add jqGrid internationalisation snippet --%>
	<%@ include file="/jqGrid.i18n.jsp"%>
	
	$.fn.selectpicker.Constructor.BootstrapVersion = '4';

	$(document).ready(function(){
			
		$("#questions-grid").jqGrid({
		   	multiselect: false,
			datatype: "json",
			url: "<c:url value="/searchQB/getPagedQuestions.do"/>",
			postData: { 
				questionTypes: "" + $("#types-select").val(), 
	        },
			height: '100%',
			autowidth: true,
			shrinkToFit: true,
			viewrecords : false,
		    pager: 'questions-grid-pager',
		    rowList:[10,20,30,40,50,100],
		    rowNum:10,
		    guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
		   	colNames:[
			   	'questionUid',
				'<fmt:message key="label.qb.collection.grid.title"/>',
				'questionDescription'
			],
		   	colModel:[
		   		{name:'questionUid', index:'questionUid', width:0, hidden: true},
		   		{name:'questionName', index:'questionName', width:570, search: true, searchoptions: { clearSearch: false }, formatter:questionNameFormatter},
		   		{name:'questionDescription', index:'questionDescription', width:0, hidden: true}
		   	],
			onSelectRow: function(rowid, e) {
			    //load up question details area
		   		var questionUid = jQuery("#questions-grid").getCell(rowid, 'questionUid');
		   		loadQuestionDetailsArea(questionUid);
			},
	  	  	gridComplete: gridSearchHighlight,
		    loadError: function(xhr,textStatus,errorThrown) {
		    	$("#questions-grid").clearGridData();
		    	$("#question-detail-area").hide().html("");

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

    	//handler for "Import" button
    	$(document).on("click", '#import-button', function() {
    		//don't import if button is disabled 
    		if ($(this).is('[disabled=disabled]')) {
    			return;
	    	}
	    	//disabling the button
    		$(this).attr("disabled", "disabled");

			$.ajax({
				url: "${param.returnUrl}",
				data: {
					qbQuestionUid: $("#selected-question-uid").val()
				},
				type: "POST",
				success: function(response, status, xhr) {
					if (response.isDuplicated) {
						//show not successfull notification
						$('#toast-import').toast('show').find('.toast-body').addClass('alert-danger').removeClass('alert-success')
										  .html('<i class="fa fa-lg fa-ban"></i>&nbsp;<fmt:message key="label.question.not.added" />');
					} else {
						$("#itemArea").html(response);

						//invoke refreshThickbox() only in case parent page has this method 
						if (typeof refreshThickbox === "function") {
							refreshThickbox();
						}

						//show successfull notification
						$('#toast-import').toast('show').find('.toast-body').addClass('alert-success').removeClass('alert-danger')
										  .html('<i class="fa fa-lg fa-check"></i>&nbsp;<fmt:message key="label.question.successfully.imported" />');
					}
				}
			});
        });

		//handler for "Advanced search" button
    	$("#advanced-search-button").click(function() {
			$(this).toggleClass("btn-secondary btn-outline-secondary");
			$("#advanced-search").toggleClass('d-flex');
        });

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
            <c:if test="${fn:length(userCollections) > 10}">liveSearch: true</c:if>
       	});
    	//workaround for $('#collections-select').selectpicker('selectAll'); throwing exception 
    	$("#collections-select option").prop("selected", "selected");
    	$('#collections-select').selectpicker('refresh');

		//handler for Types select
    	$('#types-select').selectpicker({ 
            iconBase: 'fa',
            tickIcon: 'fa-check',
            actionsBox:"true",
            selectedTextFormat:"count>2",
            countSelectedText : "{0} question types selected",
            noneSelectedText : "Display all question types",
            selectAllText:"Select all",
            deselectAllText:"Deselect all"
       	});
    	//workaround for $('#types-select-select').selectpicker('selectAll'); throwing exception 
    	$("#types-select option").prop("selected", "selected");
    	$('#types-select').selectpicker('refresh');
	});

	//load up question details area
    function loadQuestionDetailsArea(questionUid) {
        $("#question-detail-area").show().load(
  	       	'<c:url value="/searchQB/displayQuestionDetails5.do"/>',
  	       	{
  	       		questionUid: questionUid,
  	       		isScratchie: ${isScratchie}
  	    	},
  	    	function() {
  	    	}
  	    );
	}
</script>

<div class="qb-search-widget container-fluid">
	<div class="row align-items-center">
		<div class="col-8">
			<input type="text" id="filter-questions" class="form-control" placeholder="Contains text" 
				onkeydown="doSearch(arguments[0]||event)" />
		</div>
		<div class="col-4">
			<button href="#nogo" id="advanced-search-button" class="btn btn-outline-secondary ml-1" type="button" >  
				<i class="fa fa-lg fa-question-circle" aria-hidden="true" title="Advanced search"></i>
				Advanced search
			</button>
		</div>
	</div>
		
	<div id="advanced-search" class="row align-items-center mt-3 d-none">
		<div class="col">
			Collections: 
		</div>
		<div class="col">
			<select id="collections-select" class="selectpicker" multiple>
				<c:forEach var="collection" items="${userCollections}">
					<option value="${collection.uid}">
						<c:out value="${collection.name}" />
					</option>
				</c:forEach>
			</select>
		</div>
		
		<c:choose>
			<c:when test="${!empty questionTypesAvailable}">
				<div class="col">
					<fmt:message key="label.question.type" />
				</div>
				<div class="col">
					<select id="types-select" class="form-control selectpicker" multiple>
						<c:forTokens items="${questionTypesAvailable}" delims="," var="questionTypeIter">
							<option value="${questionTypeIter}">
								<c:choose>
									<c:when test="${questionTypeIter == 1}">
										<fmt:message key="label.question.type.multiple.choice" />
									</c:when>
									<c:when test="${questionTypeIter == 2}">
										<fmt:message key="label.question.type.matching.pairs" />
									</c:when>
									<c:when test="${questionTypeIter == 3}">
										<fmt:message key="label.question.type.short.answer" />
									</c:when>
									<c:when test="${questionTypeIter == 4}">
										<fmt:message key="label.question.type.numerical" />
									</c:when>
									<c:when test="${questionTypeIter == 5}">
										<fmt:message key="label.question.type.true.false" />
									</c:when>
									<c:when test="${questionTypeIter == 6}">
										<fmt:message key="label.question.type.essay" />
									</c:when>
									<c:when test="${questionTypeIter == 7}">
										<fmt:message key="label.question.type.ordering" />
									</c:when>
									<c:when test="${questionTypeIter == 8}">
										<fmt:message key="label.question.type.mark.hedging" />
									</c:when>
								</c:choose>
							</option>
						</c:forTokens>
					</select>
				</div>
			</c:when>
			
			<c:otherwise>
				<input type="hidden" id="types-select" value="${questionType}">
			</c:otherwise>
		</c:choose>
	</div>
	
	<div id="grid-container" class="row mt-3">
		<div class="grid-holder col-xs-12 col-sm-5">
			<table id="questions-grid" class="scroll"></table>
			<div id="questions-grid-pager" class="scroll"></div>
		</div>
					
		<div id="question-detail-area" class="col-xs-12 col-sm-7"></div>
	</div>	
	
	<div id="toast-import" class="toast toast-top" role="alert" aria-live="assertive" aria-atomic="true" data-delay="5000">
	  <div class="toast-body alert"></div>
	</div>
</div>