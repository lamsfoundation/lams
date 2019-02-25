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
			max-width: 97%;
    		overflow-x: hidden;
		}
		
		#close-button .fa {
		    font-size: 16px;
		    vertical-align: super;
		}
		#main-panel {
			border: none; 
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
			padding: 8px;
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
		  	       		}
		  	       	);
	  	  		},
	  	  	gridComplete: function () {
	  	      var filters, i, l, rules, rule, iCol, $this = $(this);
	  	      //if (this.p.search === true) {
	  	         // filters = $.parseJSON(this.p.postData.filters);
	  	      //    if (filters !== null && typeof filters.rules !== 'undefined' &&
	  	        //          filters.rules.length > 0) {
	  	        //      rules = filters.rules;
	  	        //      l = rules.length;
	  	                  if ($("#filter-questions").val()) {
	  	                      $('>tbody>tr.jqgrow>td:nth-child(2)', this).highlight($("#filter-questions").val());
	  	                  }
	  	       //       }
	  	       //   }
	  	     // }
	  	  },
			    loadError: function(xhr,st,err) {
			    	jQuery("#questions-grid").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    },
			    loadComplete: function () {
			   	 	
			    }
			})
			.navGrid("#questions-grid-pager", {edit:false,add:false,del:false,search:false});	

	        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
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

	    	$(document).on("click", '#import-qbquestion-button', function() {
	    		var qbQuestionUid = $(this).data("question-uid");
		    	
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
	        	timeoutHnd = setTimeout(gridReload,500)
	        }

	        function gridReload(){
		        $("#questions-grid").jqGrid(
					'setGridParam', 
					{
		            	postData: { searchString: $("#filter-questions").val() }
		        	}, 
					{ page: 1 }
				).trigger('reloadGrid');

		        $("#question-detail-area").hide("slow").html("");

		        
	        	//var nm_mask = jQuery("#item_nm").val();
	        	//var cd_mask = jQuery("#search_cd").val();
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
		
		<!-- 
		<a target="_blank" class="btn btn-xs btn-default pull-right loffset5" title="<fmt:message key='button.help.tooltip'/>"
		   href="http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Course+Monitor">
			<i class="fa fa-question-circle"></i> <span class="hidden-xs"><fmt:message key="button.help"/></span>
		</a>

		<div id="datesNotShown">
			<a class="btn btn-xs btn-default pull-right " href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.show.dates" />">
				<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
				<fmt:message key="gradebook.monitor.show.dates" /></span>
			</a>
		</div>

		<div id="datesShown" style="display:none">
			<a class="btn btn-xs btn-default pull-right " href="javascript:toggleLessonDates()" title="<fmt:message key="gradebook.monitor.hide.dates" />">
				<i class="fa fa-calendar-check-o"></i> <span class="hidden-xs">
				<fmt:message key="gradebook.monitor.hide.dates" /></span>
			</a>
		</div>			

		<div>
			<a href="#nogo" id="export-course-button" class="btn btn-xs btn-default" title="<fmt:message key="gradebook.export.excel" />">
				<i class="fa fa-download"></i><span class="hidden-xs">
				<fmt:message key="gradebook.export.excel" />
				</span>
			</a>
		</div>
		-->

		<div id="grid-container" style="min-height: 300px;">
	 		<div class="grid-holder col-xs-12 col-sm-4">
	 			<table id="questions-grid" class="scroll"></table>
				<div id="questions-grid-pager" class="scroll"></div>
			</div>
			
			<div id="question-detail-area" class="col-xs-12 col-sm-8"></div>
		</div>
		
		<!--
			<div class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					Close
				</a>
				 <a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					Import
				</a> 
				
			</div>
		-->
		
		
		</div>
		
	</div>
</body>
</lams:html>
