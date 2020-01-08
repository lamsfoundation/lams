<%@ include file="/common/taglibs.jsp"%>
<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.add.new.question" /></div>
</div>

<div class="panel-body">
	<c:set var="csrfToken"><csrf:token/></c:set>
	<form:form action="${empty newQuestionForm.editableQuestionIndex ? 'addSingleQuestion.do' : 'saveSingleQuestion.do'}?${csrfToken}" modelAttribute="newQuestionForm" id="newQuestionForm" method="POST">
	<form:hidden path="toolContentID" />
	<form:hidden path="httpSessionID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="editableQuestionIndex" />
	<form:hidden path="editQuestionBoxRequest" value="${not empty newQuestionForm.editableQuestionIndex}" />
	
	<lams:CKEditor id="newQuestion" value="${newQuestionForm.editableQuestionText}"
				   contentFolderID="${newQuestionForm.contentFolderID}"/>

			<div class="checkbox">
				<label> <form:checkbox path="required" value="1"
						id="required" />&nbsp;<fmt:message key="label.required.desc" />
				</label>
			</div>

			<a data-toggle="collapse" data-target="#extra" href="#xta"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.other.options"/></a>
<div id="extra" class="panel-body collapse" >  	        
	<div class="form-group form-inline">
	  	<label for="minWordsLimit">
	    	<fmt:message key="label.minimum.number.words" >
				<fmt:param> </fmt:param>
			</fmt:message>:
		</label>
			<input type="number" class="form-control input-sm" id="minWordsLimit" name="minWordsLimit" value="${newQuestionForm.minWordsLimit}" min="0"/>
	</div>
	
	<div class="form-group">
	    <label for="feedback"><fmt:message key="label.feedback" /></label>
		<lams:CKEditor id="feedback" value="${newQuestionForm.feedback}"
				   contentFolderID="${newQuestionForm.contentFolderID}"/>
	</div>
</div>	
	<div class="voffset5 pull-right">
		<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="label.cancel" /> </a>
		<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs"> <fmt:message key="label.save.question" /></a>
	</div>
</form:form>
</div>
</div>
