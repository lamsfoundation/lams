<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>Question Bank Search</title>
	
	<lams:css />
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<style>
		#grid-holder {
		    padding-top: 15px;
		}
		.question-description-grid {
			min-height: 10px;
			max-height: 10px;
    		overflow-x: hidden;
    		margin-top: 4px;
		}
		
		#close-button .fa {
		    font-size: 16px;
		    vertical-align: super;
		}
		#main-panel {
			border: none; 
			box-shadow: none;
		}
		#import-button {
			display: none;
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
		
		#question-type {
			-moz-user-select: none; -webkit-user-select: none; -ms-user-select:none; user-select:none;-o-user-select:none;
		}
		
		/*----------STICKY FOOTER----------------*/
		html {
		    position: relative;
		    min-height: 100%;
		}
		body {
			margin-bottom: 44px;
		}
		footer {
			position: absolute;
		    bottom: 0;
		    width: 100%;
		    height: 44px;
		}
		footer > div {
			height: 44px;
		}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.highlight.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			
			jQuery("#questions-grid").jqGrid({
			   	multiselect: false,
				datatype: "json",
				url: "<c:url value="/searchQB/getPagedQuestions.do"/>?questionType=${questionType}",
				height: '100%',
				autowidth: true,
				shrinkToFit: true,
			    pager: 'questions-grid-pager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    viewrecords:true,
			    guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'questionUid',
					"Questions",
					"questionDescription"
				],
			   	colModel:[
			   		{name:'questionUid', index:'questionUid', width:0, hidden: true},
			   		{name:'questionName', index:'questionName', width:570, search: true, searchoptions: { clearSearch: false }, formatter:userNameFormatter},
			   		{name:'questionDescription', index:'questionDescription', width:0, hidden: true}
			   	],
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var questionUid = jQuery("#questions-grid").getCell(rowid, 'questionUid');

		  	        $("#question-detail-area").show().load(
		  	        	'<c:url value="/searchQB/displayQuestionDetails.do"/>',
		  	        	{
		  	        		questionUid: questionUid
		  	       		},
		  	       		function() {
		  	       			$("#import-button").show();
		  	       		}
		  	       	);
	  	  		},
		  	  	gridComplete: function () {
			  	  	//highlight search results
					if ($("#filter-questions").val()) {
						$('>tbody>tr.jqgrow>td:nth-child(2)', this).highlight($("#filter-questions").val());
					}
				},
			    loadError: function(xhr,st,err) {
			    	$("#questions-grid").clearGridData();
			    	$.jgrid.info_dialog.call("<fmt:message key="label.error"/>", "<fmt:message key="error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    	$("#import-button").hide();
			    	$("#question-detail-area").hide().html("");
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
	    	$("#import-button").on("click", function() {
	    		var qbQuestionUid = $("#selected-question-uid").val();
		    	
	    		parent.jQuery("#itemArea").load(
					"${param.returnUrl}",
					{
						qbQuestionUid: qbQuestionUid
					},
					function() {
	    	    		self.parent.refreshThickbox()
	    	    		self.parent.tb_remove();
					}
				);
	        });
		});

		//auxiliary formatter for jqGrid's question column
		function userNameFormatter (cellvalue, options, rowObject) {
	       	var questionDescription = rowObject[2] ? rowObject[2] : "";

	       	var text = cellvalue + "<div class='question-description-grid small'>";
	       	if (questionDescription.length > 0) {
	       		text += questionDescription;
			}
	        text += "</div>"
	        	
			return text;
		}

	    //search field handler
	    var timeoutHnd;
	    function doSearch(ev){
			//	var elem = ev.target||ev.srcElement;
	       	if(timeoutHnd)
	       		clearTimeout(timeoutHnd)
	       	timeoutHnd = setTimeout(gridSearch,500)
	    }
		function gridSearch(){
			$("#questions-grid").jqGrid(
				'setGridParam', 
				{
		           	postData: { searchString: $("#filter-questions").val() }
		       	}, 
				{ page: 1 }
			).trigger('reloadGrid');

		    $("#question-detail-area").hide("slow").html("");
		    $("#import-button").hide("fast");
	        //jQuery("#bigset").jqGrid('setGridParam',{url:"bigset.php?nm_mask="+nm_mask+"&cd_mask="+cd_mask,page:1}).trigger("reloadGrid");
	    }
	</script>
</lams:head>
<body>
	<div id="main-panel" class="panel panel-default">
		<div class="panel-heading panel-title">
			Search question bank
			
			<button type="button" id="close-button" class="close" data-dismiss="modal" aria-label="Close"
				onclick="javascript:self.parent.tb_remove();">
				<i class="fa fa-times" aria-hidden="true"></i>
			</button>
		</div>
			
		<div class="panel-body">
		
			<div id="search-widgets">
				<input type="text" id="filter-questions" class="form-control" placeholder="Contains text" 
					onkeydown="doSearch(arguments[0]||event)" />
		
				<span id="question-type" class="form-control loffset5">Type: Multiple choice</span>
			</div>

			<div id="grid-container" style="min-height: 300px;">
		 		<div class="grid-holder col-xs-12 col-sm-4">
		 			<table id="questions-grid" class="scroll"></table>
					<div id="questions-grid-pager" class="scroll"></div>
				</div>
				
				<div id="question-detail-area" class="col-xs-12 col-sm-8"></div>
			</div>
		
		</div>
	</div>
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5" style="display: inline;">
					<fmt:message key="label.cancel" />
				</a>
				<a id="import-button" class="btn btn-sm btn-default button-add-item" href="#nogo"
					title="Import question from the question bank">
					Import
				</a>
			</div>	
      	</div>
    </footer>
</body>
</lams:html>
