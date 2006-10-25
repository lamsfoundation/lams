<%@ include file="/common/taglibs.jsp"%>

<div id="content">
<h1>
	${chatDTO.title}
</h1>

	<html:form action="/learning" method="post">
	
					<p class="small-space-top"><lams:out value="${chatDTO.reflectInstructions}"/>
					</p>			
			
					<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area"></html:textarea>
			
			
			<div align="right" class="space-bottom-top">
					<html:hidden property="dispatch" value="submitReflection" />
					<html:hidden property="chatUserUID" />
					<html:submit styleClass="button">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
		 </div>
	
		 
		 
	</html:form>
</div>

