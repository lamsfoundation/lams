<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${itemList == null}">
	<c:set var="itemList" value="${sessionMap.itemList}"/>
</c:if>
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>
<c:set var="isAuthoringRestricted" value="${mode == 'teacher'}" />

<script lang="javascript">
	$(document).ready(function(){	
		//question bank div
		$('#question-bank-collapse').on('show.bs.collapse', function () {
			$('#question-bank-collapse.contains-nothing').load(
				"<lams:LAMSURL/>/searchQB/start.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${sessionMapID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})
	});

	var itemTargetDiv = "#itemArea";
	function removeItem(idx){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeItem.do"/>?<csrf:token/>";
			$(itemTargetDiv).load(
				url,
				{
					itemIndex: idx,
					sessionMapID: "${sessionMapID}"
				},
				function(){
					refreshThickbox();
				}
			);
		};
	}

	function initNewItemHref() {
		var itemTypeDropdown = document.getElementById("item-type");
		var itemType = itemTypeDropdown.value;
		
		var newItemInitHref = "<c:url value='/authoring/addItem.do'/>?sessionMapID=${sessionMapID}"
			+ "&questionType=" + itemType 
			+ "&KeepThis=true&TB_iframe=true&modal=true";
		$("#item-init-href").attr("href", newItemInitHref);
	};
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	}
	
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?limitType=mc&collectionChoice=true',
    			    'QuestionFile','width=500,height=240,scrollbars=yes');
    }
  	
  	// this method is called by QTI questionChoice.jsp 
    function saveQTI(formHTML, formName) {
    	var form = $($.parseHTML(formHTML));
    	// first, save questions in the QB
		$.ajax({
			url: '<lams:LAMSURL />imsqti/saveQTI.do?<csrf:token/>',
			data: form.serializeArray(),
			type: "POST",
			dataType: 'text',
			// the response is a comma-delimited list of QB question UIDs, for example 4,5,65 
			success: function(qbQuestionUids) {
				$.ajax({
					type: "POST",
					url: '<lams:WebAppURL />authoring/importQbQuestions.do',
					data: {
						// send QB question uids to MCQ authoring controller
						'qbQuestionUids' : qbQuestionUids,
						'sessionMapID'   : '${sessionMapID}'
					},
					dataType: 'html',
					success: function(response) {
						// question list gets refreshed
						$(itemTargetDiv).html(response);
						refreshThickbox();
					}
				});
			}
		});
    }
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="scratchie.title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="scratchie.title" cssClass="form-control"/>
</div>
<div class="form-group">
    <label for="scratchie.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="scratchie.instructions" value="${authoringForm.scratchie.instructions}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
</div>

<!-- Items -->
<div id="itemArea">
	<%@ include file="parts/itemlist.jsp"%>
</div>

<c:if test="${!isAuthoringRestricted}">
	<!-- Dropdown menu for choosing a question type -->
	<div id="add-question-div" class="form-inline form-group pull-right">
		<select id="item-type" class="form-control input-sm">
			<option selected="selected" value="1"><fmt:message key="label.type.multiple.choice" /></option>
			<option value="3"><fmt:message key="label.type.short.answer" /></option>
		</select>
		
		<a onclick="initNewItemHref();return false;" href="#nogo" class="btn btn-default btn-sm thickbox" id="item-init-href">  
			<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.another.scratchie" />"></i>
		</a>
	</div>
	
	<!-- Question Bank -->
	<div class="panel-group" id="question-bank-div" role="tablist" aria-multiselectable="true"> 
	    <div class="panel panel-default" >
	        <div class="panel-heading collapsable-icon-left" id="question-bank-heading">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#question-bank-collapse" aria-expanded="false" aria-controls="question-bank-collapse" >
		          		<fmt:message key="label.question.bank" />
		        	</a>
	      		</span>
	        </div>
	
			<div id="question-bank-collapse" class="panel-body panel-collapse collapse contains-nothing" role="tabpanel" aria-labelledby="question-bank-heading">
			</div>
		</div>
	</div>
</c:if>
