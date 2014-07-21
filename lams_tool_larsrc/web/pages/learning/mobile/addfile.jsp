<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/mobileheader.jsp"%>
		
		<script language="JavaScript" type="text/JavaScript">
			$(document).bind("pageinit", function() { 
				$('#resourceItemForm').attr('data-ajax','true');
			});
		</script>
		
	</lams:head>
	<body>
	<div data-role="page" data-cache="false">
	
		<div data-role="header" data-theme="b" data-nobackbtn="true">
			<h1>
				<fmt:message key="label.learning.new.file" />
			</h1>
		</div>
		
		<html:form action="/learning/saveOrUpdateItem" method="post" styleId="resourceItemForm" enctype="multipart/form-data" >
			<html:hidden property="itemType" styleId="itemType" value="2" />
			<html:hidden property="mode" />
			<html:hidden property="sessionMapID" />

			<div data-role="content">
				<%@ include file="/common/messages.jsp"%>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.title.input" />
				</div>
				<html:text property="title" size="40" tabindex="1" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.file.input" />
				</div>
				<input type="file" name="file" size="25" />
	
				<div class="field-name space-top">
					<fmt:message key="label.learning.comment.or.instruction" />
				</div>
				
				<html:text tabindex="2" property="description" styleClass="text-area" maxlength="255"/>
				
				<div class="space-top">
					<html:checkbox property="openUrlNewWindow" styleId="open-file-in-new-window" styleClass="noBorder" />
					<label for="open-file-in-new-window">
						<fmt:message key="open.file.in.new.window" />
					</label>
				</div>
					
				<div class="big-space-top">
					<button onclick="document.getElementById('resourceItemForm').submit(); return false;" data-theme="b" >
						<fmt:message key="button.add" />
					</button>
					<a href="#nogo" data-role="button" data-theme="c" data-rel="back">
						<fmt:message key="button.cancel" />
					</a>
				</div>
				
			</div>
		</html:form>
	</div>
	</body>
</lams:html>
