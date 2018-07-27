<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
		
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
   		function formSubmit(){
   			$("#commonCartridgeItemForm").submit();
   		}
	</script>
</lams:head>
	
<body class="stripes">
	<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	
		<c:set var="title"><fmt:message key="label.authoring.basic.add.basiclti.tool" /></c:set>
		<lams:Page title="${title}" type="learner">

			<%@ include file="/common/messages.jsp"%>
			
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="1" />
			<html:hidden property="itemIndex" />
				
			<div class="form-group">
			    <label for="title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <html:text property="title" styleClass="form-control" styleId="title" tabindex="1"/>
			</div>
		
			<div class="form-group">
			    <label for="url">
			    	<fmt:message key="label.authoring.basic.bascilti.url" />
			    </label>
			    <html:text property="url" styleClass="form-control" styleId="url" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="key">
			    	<fmt:message key="label.authoring.basic.bascilti.key" />
			    </label>
			    <html:text property="key" styleClass="form-control" styleId="key" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="secret">
			    	<fmt:message key="label.authoring.basic.bascilti.secret" />
			    </label>
			    <html:text property="secret" styleClass="form-control" styleId="secret" tabindex="1"/>
			</div>
					
			<div class="form-group">
			    <label for="buttonText">
			    	<fmt:message key="label.authoring.basic.bascilti.button.text" />
			    </label>
			    <html:text property="buttonText" styleClass="form-control" styleId="buttonText" tabindex="1"/>
			</div>
		
			<div class="checkbox">
				<label for="openUrlNewWindow">
					<html:checkbox property="openUrlNewWindow" styleId="openUrlNewWindow"/>
					<fmt:message key="open.in.new.window" />
				</label>
			</div>
					
			<div class="form-group">
			    <label for="frameHeight">
			    	<fmt:message key="label.authoring.basic.bascilti.iframe.height" />
			    </label>
			    <html:text property="frameHeight" styleClass="form-control" styleId="frameHeight" tabindex="1"/>
			</div>
					
			<c:if test="${not empty formBean.customStr}">
				<div class="form-group">
				    <label for="customStr">
				    	<fmt:message key="label.authoring.basic.bascilti.custom.parameters" />
				    </label>
				    <lams:STRUTS-textarea rows="5" property="customStr" styleClass="form-control" styleId="customStr" tabindex="1"/>
				</div>						
			</c:if>

			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:formSubmit();" class="btn btn-sm btn-primary button-add-item">
					<fmt:message key="label.authoring.basic.add.url" /> 
				</a>
			</div>
					
		</lams:Page>

	</html:form>

</body>
</lams:html>
