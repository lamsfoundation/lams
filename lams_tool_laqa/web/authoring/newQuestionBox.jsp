<%@ include file="/common/taglibs.jsp"%>
<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.add.new.question" /></div>
</div>

<div class="panel-body">
<html:form action="/authoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="dispatch" value="${empty formBean.editableQuestionIndex ? 'addSingleQuestion' : 'saveSingleQuestion'}" />
	<html:hidden property="toolContentID" />
	<html:hidden property="httpSessionID" />
	<html:hidden property="contentFolderID" />
	<html:hidden property="editableQuestionIndex" />
	<html:hidden property="editQuestionBoxRequest" value="${not empty formBean.editableQuestionIndex}" />
	
	<lams:CKEditor id="newQuestion" value="${formBean.editableQuestionText}"
				   contentFolderID="${formBean.contentFolderID}"/>

			<div class="checkbox">
				<label> <html:checkbox property="required" value="1"
						styleId="required" />&nbsp;<fmt:message key="label.required.desc" />
				</label>
			</div>

			<a data-toggle="collapse" data-target="#extra" href="#extra"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.other.options"/></a>
<div id="extra" class="panel-body collapse" >  	        
	<div class="form-group form-inline">
	  	<label for="minWordsLimit">
	    	<fmt:message key="label.minimum.number.words" >
				<fmt:param> </fmt:param>
			</fmt:message>:
		</label>
			<input type="number" class="form-control input-sm" id="minWordsLimit" name="minWordsLimit" value="${formBean.minWordsLimit}" min="0"/>
	</div>
	
	<div class="form-group">
	    <label for="feedback"><fmt:message key="label.feedback" /></label>
		<lams:CKEditor id="feedback" value="${formBean.feedback}"
				   contentFolderID="${formBean.contentFolderID}"/>
	</div>
</div>	
	<div class="voffset5 pull-right">
		<a href="#" onclick="hideMessage()" class="btn btn-default btn-xs loffset5"> <fmt:message key="label.cancel" /> </a>
		<a href="#" onclick="submitMessage()" class="btn btn-default btn-xs"> <fmt:message key="label.save.question" /></a>
	</div>
</html:form>
</div>
</div>