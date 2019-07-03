<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="false"> 

<c:forEach var="sessionDto" items="${sessionDTOs}" varStatus="status">
	<c:set var="sessionUid" value="${sessionDto.sessionUid}"/>
	
	<c:if test="${reflect && sessionUid ne 0}">			
		<c:set var="viewReflectionsURL">
			<lams:WebAppURL/>/monitoring/ReflectionViewer.jsp?sessionUid=${sessionUid}
		</c:set>
	</c:if>

	<c:set var="buttonbar">
		<div class="pull-right">
			<c:if test="${not empty viewReflectionsURL}">
				<a href="javascript:launchPopup('${viewReflectionsURL}')" class="btn btn-default btn-sm">
					<fmt:message key="label.notebook.entries" />
				</a>
			</c:if>			
			&nbsp;
			<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${sessionDto.toolSessionId}&toolContentID=${toolContentID}" />
			<a class="fa fa-pie-chart text-primary btn btn-xs btn-primary" 
				title="<fmt:message key='label.tip.displayPieChart'/>"
				onclick="javascript:drawChart('pie', 'chartDiv${sessionDto.toolSessionId}', '${chartURL}')"></a>
			&nbsp;
			<a class="fa fa-bar-chart text-primary btn btn-xs btn-primary"
				title="<fmt:message key='label.tip.displayBarChart'/>" 
				onclick="javascript:drawChart('bar', 'chartDiv${sessionDto.toolSessionId}', '${chartURL}')"></a>
		</div>
	</c:set>						
		
	<div class="panel panel-default" >
       <div class="panel-heading " id="heading${sessionUid}">
  	    	<span class="panel-title  collapsable-icon-left">
  	    	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionUid}" 
				aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionUid}" >
				<c:out value="${sessionDto.sessionName}"/>
			</a>
			</span>  
			${buttonbar}
       </div>
       
       <div id="collapse${sessionUid}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionUid}">

	<table class="table table-condensed table-striped">							

		<tr>
			<th><fmt:message key="label.nomination"/></th>
			<th style="width: 90px;"><fmt:message key="label.total.votes"/></th>
		</tr>
												
		<c:forEach var="currentNomination" items="${sessionDto.nominations}">
			<c:set var="questionUid" scope="request" value="${currentNomination.questionUid}"/>
			<tr>
				<td  valign=top class="align-left">
					<!-- Cannot escape as it was entered in a CKeditor -->
					${currentNomination.nomination}
				</td>
						
				<td  valign=top class="align-left">				  	 		
					<c:set var="viewVotesURL">
						<lams:WebAppURL/>monitoring/getVoteNomination.do?questionUid=${questionUid}&sessionUid=${sessionUid}
					</c:set>
					<a href="javascript:launchInstructionsPopup('${viewVotesURL}')">
						<c:out value="${currentNomination.numberOfVotes}"/>  
					</a>
					&nbsp;(<fmt:formatNumber type="number" maxFractionDigits="2" value="${currentNomination.percentageOfVotes}" /><fmt:message key="label.percent"/>) 
				</td>			
			</tr>	
		</c:forEach>	
		
		<c:if test="${allowText eq true}">
			<tr>
				<td  valign=top class="align-left">
					<fmt:message key='label.open.vote'/>
				</td>
						
				<td  valign=top class="align-left">				  	 		
					<c:set var="viewVotesURL">
						<lams:WebAppURL/>monitoring/OtherTextNominationViewer.jsp?&toolContentUID=${toolContentID}&sessionUid=${sessionUid}
					</c:set>
					<a href="javascript:launchInstructionsPopup('${viewVotesURL}')">
						<c:out value="${sessionDto.openTextNumberOfVotes}"/>  
					</a>
					&nbsp(<fmt:formatNumber type="number" maxFractionDigits="2" value="${sessionDto.openTextPercentageOfVotes}" />  <fmt:message key="label.percent"/>) 
				</td>			
			</tr>	
		</c:if>
	</table>

 	<p id="chartDiv${sessionDto.toolSessionId}" style="height: 220px; display: none;"></p>
 
	</div> <!-- end collapse area  -->
	</div> <!-- end collapse panel  -->
 	${ ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>				

</div> <!--  end panel group -->

