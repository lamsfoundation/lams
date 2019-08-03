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
			var url = "<c:url value="/authoring/removeItem.do"/>";
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
	function upItem(idx){
		var url = "<c:url value="/authoring/upItem.do"/>";
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
	}
	function downItem(idx){
		var url = "<c:url value="/authoring/downItem.do"/>";
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
	}
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
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
	<div id="add-question-div" class="form-inline form-group pull-right">
		<c:set var="addItemUrl" >
			<c:url value='/authoring/addItem.do'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&modal=true
		</c:set>
		<a href="${addItemUrl}" class="btn btn-default btn-sm thickbox">
			<i class="fa fa-lg fa-plus-circle" aria-hidden="true" title="<fmt:message key="label.authoring.basic.add.another.scratchie" />"></i>
			&nbsp;
			<fmt:message key="label.authoring.basic.add.another.scratchie" />
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
