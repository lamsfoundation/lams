<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />

<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-select.css" type="text/css"/>
	
<style>
	#grid-holder {
	    padding-top: 15px;
	}
	.question-description-grid {
		min-height: 10px;
		max-height: 12px;
   		overflow-x: hidden;
   		margin-top: 4px;
	}
	.question-title-grid {
	    overflow-x: hidden;
	}

	#import-button {
		margin-left: 5px;
	}
	#search-widgets {
		display: flex;
		margin-bottom: 15px;
	}
	#search-widgets .form-control {
		width: auto;
	}
		
	.question-description {
		font-size: smaller;
	}
		
	/* jqGrid padding */
	#grid-container .ui-jqgrid.ui-jqgrid-bootstrap tr.jqgrow>td {
		padding: 10px;
	}
		
	#grid-container {
		min-height: 300px;
	}
		
	/* padding of #grid-container's two columns */
	#grid-container > div {
		padding: 0;
	}
	@media (min-width: 768px){
		#question-detail-area {
		    padding-left: 20px !important;
		}
	}
	@media (max-width: 768px){
		#question-detail-area {
		    margin-top: 20px !important;
		}
	}
		
	#question-detail-area table {
	    margin-top: 15px;
	}
		
	/* hide "View 1 out of 10" label */
	#questions-grid-pager_left, #questions-grid-pager_right {
		display: none;
	}
		
	.highlight {
	    background-color: #FFFF88;
	}
		
	.disabled-span {
		-moz-user-select: none; -webkit-user-select: none; -ms-user-select:none; user-select:none;-o-user-select:none;
	}
		
	.dropdown-menu {
	    min-width: 90px;
	}
		
	/* jqGrid header */
	#grid-container .ui-jqgrid .ui-jqgrid-htable .ui-jqgrid-labels th div {
	    font-size: 14px;
   		margin: 6px;
	}
		
	/* question imported growl */
	div.growlUI h1, div.growlUI h2 {
		color: white;
		margin: 5px 5px 5px 0px;
		text-align: center;
		font-size: 18px;
	}
		
	#question-type {
		display: inline-flex;
	}
	
	#advanced-search {
		display: none;
		padding-bottom: 20px;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.highlight.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-select.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-search.js"></script>
<script type="text/javascript">
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
		    pager: 'questions-grid-pager',
		    rowList:[10,20,30,40,50,100],
		    rowNum:10,
		    guiStyle: "bootstrap",
			iconSet: 'fontAwesome',
		   	colNames:[
			   	'questionUid',
				'<fmt:message key="authoring.section.questions"/>',
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
	                	$.growlUI('<i class="fa fa-lg fa-ban" style="color:red"></i> <fmt:message key="label.question.not.added" />');
						
					} else {
						$("#itemArea").html(response);

						//invoke refreshThickbox() only in case parent page has this method 
						if (typeof refreshThickbox === "function") {
							refreshThickbox();
						}

						//show successfull notification
	                	$.growlUI('<i class="fa fa-lg fa-check" style="color:green"></i>&nbsp;<fmt:message key="label.question.successfully.imported" />');
					}
				}
			});
        });

		//handler for "Advanced search" button
    	$("#advanced-search-button").click(function() {
			$(this).toggleClass("btn-primary");
			$("#advanced-search").toggle('fast');
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
            style:"btn btn-sm btn-default",
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
            deselectAllText:"Deselect all",
            style:"btn btn-sm btn-default"
       	});
    	//workaround for $('#types-select-select').selectpicker('selectAll'); throwing exception 
    	$("#types-select option").prop("selected", "selected");
    	$('#types-select').selectpicker('refresh');
	});

	//load up question details area
    function loadQuestionDetailsArea(questionUid) {
        $("#question-detail-area").show().load(
  	       	'<c:url value="/searchQB/displayQuestionDetails.do"/>',
  	       	{
  	       		questionUid: questionUid,
  	       		isScratchie: ${isScratchie}
  	    	},
  	    	function() {
  	    	}
  	    );
	}
</script>

<div id="search-widgets" class="input-group-sm">
	<input type="text" id="filter-questions" class="form-control" placeholder="Contains text" 
		onkeydown="doSearch(arguments[0]||event)" />
		
	<a href="#nogo" class="btn btn-default btn-sm loffset10" id="advanced-search-button">  
		<i class="fa fa-lg fa-question-circle" aria-hidden="true" title="Advanced search"></i>
		Advanced search
	</a>
</div>
	
<div id="advanced-search">
	Collections: &nbsp;
	
	<select id="collections-select" class="selectpicker" multiple>
		<c:forEach var="collection" items="${userCollections}">
			<option value="${collection.uid}">
				<c:out value="${collection.name}" />
			</option>
		</c:forEach>
	</select>
	
	<c:choose>
		<c:when test="${!empty questionTypesAvailable}">
			<fmt:message key="label.question.type" />&nbsp;
		
			<select id="types-select" class="selectpicker" multiple>
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
		</c:when>
		
		<c:otherwise>
			<input type="hidden" id="types-select" value="${questionType}">
		</c:otherwise>
	</c:choose>
</div>

<div id="grid-container">
	<div class="grid-holder col-xs-12 col-sm-4">
		<table id="questions-grid" class="scroll"></table>
		<div id="questions-grid-pager" class="scroll"></div>
	</div>
				
	<div id="question-detail-area" class="col-xs-12 col-sm-8"></div>
</div>
