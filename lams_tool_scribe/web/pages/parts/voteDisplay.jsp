<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div style="background-color: silver; width: 100%; height: 10px;">	
	<span>
		<img src="${tool}images/bar1.gif" width="${scribeSessionDTO.votePercentage}%" height="10" alt="progress bar">
	</span>
</div>

<p>	
	${scribeSessionDTO.numberOfVotes} out of
	${scribeSessionDTO.numberOfLearners} agree (${scribeSessionDTO.votePercentage}%)
</p>
