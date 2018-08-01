<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
	</lams:head>
	<body>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>

		<script type="text/javascript"> 
			function submitHeading(){
				for (var i in CKEDITOR.instances) {
		       		CKEDITOR.instances[i].updateElement();
		   		}
					
				// after submit, it direct to itemlist.jsp, 
				// then refresh "basic tab" resource list and close this window.
			    $.ajax({ // create an AJAX call...
					data: $("#headingForm").serialize(), 
			       	type: $("#headingForm").attr('method'),
					url: $("#headingForm").attr('action'),
					success: function(data) {
					   $.ajaxSetup({ cache: true });
			           $('#itemListArea').html(data);
					}
			    });
			    
			}
		</script>
		
		<html:form action="/authoring" styleId="headingForm" method="post">
		
			<html:hidden property="dispatch" value="addOrUpdateHeading" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="headingIndex" />
		
			<c:set var="formBean"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"
				value="${sessionScope[formBean.sessionMapID]}" />
		
			<div class="panel panel-default add-file">
				<div class="panel-heading panel-title">
					<fmt:message key="label.authoring.basic.heading.add" />
				</div>
				
				<div class="panel-body">
		
					<c:set var="headingText" value="" />
					<c:if test="${not empty formBean.headingIndex}">
						<c:set var="headingText">${sessionMap.headings[formBean.headingIndex].headingText}</c:set>
					</c:if>
				
					<lams:CKEditor id="heading" value="${headingText}" contentFolderID="${sessionMap.contentFolderID}"/>
				
					<div class="voffset5 pull-right">
					    <a href="#" onclick="javascript:hideMessage()" class="btn btn-default btn-sm">
							<fmt:message key="button.cancel" /> </a>
						<a href="#" onclick="submitHeading()" class="btn btn-default btn-sm">
							<i class="fa fa-plus"></i>&nbsp;<fmt:message key="button.submit" /> </a>
		
						
					</div>
				</div>
			</div>
			
		</html:form>
	</body>
</lams:html>



