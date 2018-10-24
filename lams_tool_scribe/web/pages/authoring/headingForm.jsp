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
		
		<form:form action="authoring/addOrUpdateHeading.do" modelAttribute="authoringForm" id="headingForm" method="post">
		
			<form:hidden path="sessionMapID" />
			<form:hidden path="headingIndex" />
		
			<c:set var="sessionMap"
				value="${sessionScope[authoringForm.sessionMapID]}" />
		
			<div class="panel panel-default add-file">
				<div class="panel-heading panel-title">
					<fmt:message key="label.authoring.basic.heading.add" />
				</div>
				
				<div class="panel-body">
		
					<c:set var="headingText" value="" />
					<c:if test="${not empty authoringForm.headingIndex}">
						<c:set var="headingText">${sessionMap.headings[authoringForm.headingIndex].headingText}</c:set>
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
			
		</form:form>
	</body>
</lams:html>



