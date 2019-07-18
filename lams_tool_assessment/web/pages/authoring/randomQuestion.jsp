<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css />
	<link href="<lams:LAMSURL/>tool/laasse10/includes/css/assessment.css" rel="stylesheet" type="text/css">
	<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<style>
		#filter-questions {
			margin-bottom: 3px;
		}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript">
		const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
		const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-question.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.highlight.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-search.js"></script>
    <script  type="text/javascript">
		$(document).ready(function(){
		    $("#assessmentQuestionForm").validate({
		    	ignore: 'hidden',
   			    submitHandler: function(form) {
	    	    	var options = { 
	    	    		target:  parent.jQuery('#itemArea'), 
	    		   		success: afterRatingSubmit  // post-submit callback
	    		    }; 				

	    			$('#assessmentQuestionForm').ajaxSubmit(options);
	    		},
	    		invalidHandler: formValidationInvalidHandler,
				errorElement: "em",
				errorPlacement: formValidationErrorPlacement,
				success: formValidationSuccess,
				highlight: formValidationHighlight,
				unhighlight: formValidationUnhighlight
		  	});
			
			jQuery("#questions-grid").jqGrid({
			   	//multiselect: false,
				datatype: "json",
				url: "<c:url value="/authoring/getPagedRandomQuestions.do"/>",
				postData: { 
					sessionMapID:$("#sessionMapID").val(),
	           		collectionUids: "" 
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
				   	'',
					'<fmt:message key="label.authoring.basic.list.header.question"/>',
					'questionDescription'
				],
			   	colModel:[
			   		{name:'questionUid', index:'questionUid', width:0, hidden: true},
			   		{name: 'belongsToRandomPool', index: 'belongsToRandomPool', width:20, editable:true, edittype:'checkbox', editoptions: {value:"True:False"}, 
						formatter: "checkbox", formatoptions: {disabled : false}, sortable:false, search: false},
			   		{name:'questionName', index:'questionName', width:570, search: true, searchoptions: { clearSearch: false }, formatter:questionNameFormatter},
			   		{name:'questionDescription', index:'questionDescription', width:0, hidden: true}
			   	],
		  	  	gridComplete: gridSearchHighlight,
			   	loadComplete: function() {
					// storing belongToRandomPool change to DB
				    $(":checkbox").on("click", function(event){
					    var checkbox = $(this);
					    
					    var rowid = $(this).parent().parent().prop('id');
					    var qbQuestionUid = $("#questions-grid").getCell(rowid, 'questionUid');
						$.ajax({ 
					 	    url: '<c:url value="/authoring/toggleBelongToRandomPool.do"/>',
						    type: 'POST',
							data: {
								sessionMapID: $("#sessionMapID").val(),
								qbQuestionUid: qbQuestionUid,
								requestedToAdd: this.checked
							}, 
							function() {
							}
						}).done(function(data) { 
						    //postsjson = $.parseJSON(posts);
							$("#count-selected-questions").text(data.poolQuestionsCount);
						    
							//show successfull notification
							var notification = checkbox.is(":checked") ? "Added question to the random pool" : "Removed question from the random pool";
		                	$.growlUI('<i class="fa fa-lg fa-download"></i> ' + notification);

						});
				    	
				    });
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
			
			<form:form action="saveOrUpdateRandomQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm" 
				method="post" autocomplete="off">
				<form:hidden path="sessionMapID" id="sessionMapID"/>
				<form:hidden path="uid" />
				<form:hidden path="questionType" value="-1"/>
			</form:form>
			
			<div id="search-widgets" class="input-group-sm">
				<input type="text" id="filter-questions" class="form-control" placeholder="Contains text" 
					onkeydown="doSearch(arguments[0]||event)" />
			</div>

			<div id="grid-container" style="min-height: 300px;">
		 		<div class="grid-holder">
		 			<table id="questions-grid" class="scroll"></table>
					<div id="questions-grid-pager" class="scroll"></div>
				</div>
			</div>
			
			<div class="voffset10">
				<span class="alert-warning">
					Pool consists of <span id="count-selected-questions">${poolQuestionsCount}</span> question(s).
				</span>
			</div>

		</div>		
	</div>	
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>
      	</div>
    </footer>
</body>
</lams:html>
