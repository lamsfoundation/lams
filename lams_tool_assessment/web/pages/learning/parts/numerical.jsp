<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<tr>
		<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; ">
			<input type="text" name="question${status.index}" value="${question.answerString}" styleClass="noBorder" size="75"
	<%--						<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
	--%>					 
			/>	
		</td>
	</tr>
</table>		

<%@ include file="markandpenaltyarea.jsp"%>