<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div style="background-color: silver; width: 95%; height: 10px;">	
	<span>
		<img src="${tool}images/bar1.gif" width="${scribeSessionDTO.votePercentage}%" height="10" alt="progress bar">
	</span>
</div>

<p>	
	<fmt:message key="message.voteStatistics">
		<fmt:param value="${scribeSessionDTO.numberOfVotes}"></fmt:param>
		<fmt:param value="${scribeSessionDTO.numberOfLearners}"></fmt:param>
	</fmt:message>
	(${scribeSessionDTO.votePercentage}%)
</p>
