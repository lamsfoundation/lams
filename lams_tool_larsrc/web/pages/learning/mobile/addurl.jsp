<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/mobileheader.jsp"%>
	</lams:head>
	<body>
	<div data-role="page" data-cache="false">
	
		<div data-role="header" data-theme="b" data-nobackbtn="true">
			<h1>
				<fmt:message key="label.learning.new.url" />
			</h1>
		</div>
	
		<html:form action="/learning/saveOrUpdateItem" method="post" styleId="resourceItemForm">
			<html:hidden property="itemType" styleId="itemType" value="1" />
			<html:hidden property="mode" />
			<html:hidden property="sessionMapID" />
			
			<div data-role="content">
				<%@ include file="/common/messages.jsp"%>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.title.input" />
				</div>
	
				<html:text property="title" size="40" tabindex="1" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.url.input" />
				</div>
	
				<html:text property="url" size="40" tabindex="2" />
	
	
				<html:checkbox property="openUrlNewWindow" tabindex="3" styleId="openUrlNewWindow" styleClass="noBorder">
				</html:checkbox>
				<label for="openUrlNewWindow">
					<fmt:message key="open.in.new.window" />
				</label>
	
				<div class="field-name space-top">
					<fmt:message key="label.learning.comment.or.instruction" />
				</div>
	
				<html:text tabindex="2" property="description" styleClass="text-area" maxlength="255" />
				
				<div class="big-space-top">
					<button type="submit" data-theme="b" >
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
