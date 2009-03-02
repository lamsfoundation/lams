<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<tr>
		<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; ">
			<lams:STRUTS-textarea property="question${status.index}" rows="7" cols="60" value="${question.answerString}" disabled="${finishedLock}"/>
		</td>
	</tr>
</table>		
