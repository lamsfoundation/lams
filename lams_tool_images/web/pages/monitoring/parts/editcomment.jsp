<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.monitoring.title" />
		</title>
		
		<%@ include file="/common/headerWithoutPrototype.jsp"%>
	</lams:head>
	
	<body class="stripes">
	
		<div style="margin: 10% auto auto; width: 600px;">
		<div id="content" >
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/monitoring/saveComment" method="post" styleId="imageCommentForm">
				<html:hidden property="sessionMapID" />
				<html:hidden property="commentUid" />
	
				<h2 class="no-space-left">
					<fmt:message key="label.monitoring.updatecomment.update.comment" />
				</h2>
				
				<div class="space-top" >
					<span class="field-name">
						<fmt:message key="label.monitoring.updatecomment.posted.by" />:
					</span>
					${comment.createBy.loginName}					
				</div>					 
				
				<div class="space-top" >
					<span class="field-name">
						<fmt:message key="label.monitoring.updatecomment.posted.on" />:
					</span>
					${comment.createDate}
				</div> 
	
				<div class="field-name space-top" >
					<fmt:message key="label.monitoring.updatecomment.comment" />
				</div>
				<lams:STRUTS-textarea property="comment" rows="3" cols="50" />
			</html:form>

			<br><br>
	
			<lams:ImgButtonWrapper>
				<a href="#" onclick="self.parent.tb_remove();" class="button right-buttons space-left">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#" onclick="document.imageCommentForm.submit();" class="button right-buttons space-left">
					<fmt:message key="label.monitoring.updatecomment.save" /> 
				</a>
			</lams:ImgButtonWrapper>
		
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->

		</div>	
	</body>
</lams:html>
