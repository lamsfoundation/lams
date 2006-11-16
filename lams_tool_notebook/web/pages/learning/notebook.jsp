<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		${notebookDTO.title}
	</h1>

	<c:if test="${mode == 'learner' || mode == 'author'}">
	
		<html:form action="/learning" method="post">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<p>
				${notebookDTO.instructions}
			</p>

			<c:set var="lrnForm"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				
			<c:choose>
				<c:when test="${contentEditable}">
					<c:choose>
						<c:when test="${notebookDTO.allowRichEditor}">
							<lams:FCKEditor id="entryText" value="${lrnForm.entryText}"
								toolbarSet="Default-Learner">
							</lams:FCKEditor>
						</c:when>

						<c:otherwise>
							<html:textarea cols="60" rows="8" property="entryText"
								styleClass="text-area"></html:textarea>
						</c:otherwise>
					</c:choose>
				</c:when>

				<c:otherwise>
					${lrnForm.entryText}
				</c:otherwise>
			</c:choose>

			<div class="space-bottom-top align-right">
					<html:submit styleClass="button">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
			</div>
		</html:form>
	</c:if>
</div>
