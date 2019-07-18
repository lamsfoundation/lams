<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	
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
		.ui-jqgrid.ui-jqgrid-bootstrap tr.jqgrow>td {
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
		.ui-jqgrid .ui-jqgrid-htable .ui-jqgrid-labels th div {
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
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.highlight.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-search.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			jQuery("#questions-grid").jqGrid({
			   	multiselect: false,
				datatype: "json",
				url: "<c:url value="/searchQB/getPagedQuestions.do"/>",
				postData: { 
	           		questionType: ${questionType} 
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
		    	
		    	var qbQuestionUid = $("#selected-question-uid").val();
		    	var divToReplace = ${param.isFullJspRequested == true} ? parent.jQuery("#itemArea") : jQuery("#itemArea");
		    	divToReplace.load(
					"${param.returnUrl}",
					{
						qbQuestionUid: qbQuestionUid
					},
					function() {
						//invoke refreshThickbox() only in case parent page has this method 
						if (typeof self.parent.refreshThickbox === "function") {
							self.parent.refreshThickbox();
						} else if (typeof refreshThickbox === "function") {
							refreshThickbox();
						}

						//show successfull notification
	                	$.growlUI('<i class="fa fa-lg fa-download"></i> <fmt:message key="label.question.successfully.imported" />');
					}
				);
	        });
		});

		//load up question details area
	    function loadQuestionDetailsArea(questionUid) {
  	        $("#question-detail-area").show().load(
	  	       	'<c:url value="/searchQB/displayQuestionDetails.do"/>',
	  	       	{
	  	       		questionUid: questionUid
	  	    	},
	  	    	function() {
	  	    	}
	  	    );
		}
	</script>

			<div id="search-widgets" class="input-group-sm">
				<input type="text" id="filter-questions" class="form-control" placeholder="Contains text" 
					onkeydown="doSearch(arguments[0]||event)" />
		
				<c:if test="${!empty questionTypesAvailable}">
					<div class="loffset10 input-group-sm">
						<fmt:message key="label.question.type" />
						<select id="question-type" onchange="gridSearch();" class="form-control">
							<c:forTokens items="${questionTypesAvailable}" delims="," var="questionTypeIter">
		  						<option value="${questionTypeIter}"
		  							<c:if test="${questionTypeIter == questionType}">selected="selected"</c:if>>
		  							
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
				</c:if>
			</div>

			<div id="grid-container">
		 		<div class="grid-holder col-xs-12 col-sm-4">
		 			<table id="questions-grid" class="scroll"></table>
					<div id="questions-grid-pager" class="scroll"></div>
				</div>
				
				<div id="question-detail-area" class="col-xs-12 col-sm-8"></div>
			</div>