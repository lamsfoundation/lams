<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="leaderDto" value="${sessionMap.leaderDto}"/>

<script type="text/javascript">
	$(document).ready(function(){
        $('[data-bs-toggle="tooltip"]').tooltip();
        
	   	// must display charts after screen is visible or cannot calculate widths.
		drawHistogram('chartDiv', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.marks"/></spring:escapeBody>', 
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.number.groups.in.mark.range"/></spring:escapeBody>'
		);
	});		
</script>

<c:choose>
<c:when test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:when>
	
<c:otherwise>
	<div class="lcard" >
      	<div class="card-body">
			<em class="p-2">
				<fmt:message key="label.graph.help"/>
			</em>
			
            <div class="row">
                <div class="col-12">
                    <div id="chartDiv" class="markChartDiv"></div>
                </div>    
            </div>
            
            <div class="row">
                <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
					<table class="table table-striped table-hover table-condensed">
                        <tr>
                            <td class="field-name">
                                <fmt:message key="label.number.groups.finished" />:
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.count}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                                <fmt:message key="label.lowest.mark"/>
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.minString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.median.mark" />
                            </td>
                            <td class="text-right">
                               <c:out value="${leaderDto.medianString}" />
                            </td>
                        </tr>
                    </table>    
                </div>
                
                <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
					<table class="table table-striped table-hover table-condensed">
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.average.mark" />
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.averageString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                                <fmt:message key="label.highest.mark"/>
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.maxString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.modes.mark" />
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.modesString}" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
		</div>
	</div>

	<div class="lcard" >
        <div class="card-header collapsable-icon-left" id="heading-qb-stats">
            <span class="card-title">
                <button type="button" class="btn btn-secondary-darker no-shadow" data-bs-toggle="collapse" data-bs-target="#qb-stats" aria-expanded="true" aria-controls="qb-stats">
                    <fmt:message key="label.qb.stats" />
                </button>
                <i class="fa fa-question-circle" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="right" title="Item analysis is a technique that analyses the student answers to evaluate the effectiveness of questions in an exam."></i>
    		</span>
        </div>
        
		<div aria-expanded="true" id="qb-stats" class="card-body panel-collapse collapse show">
                <table class="table table-striped table-condensed">
                    <tr>
                        <th scope="col" class="text-left">
                            #
                        </th>
                        <th scope="col" class="text-left">
                            <fmt:message key="label.question"/>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.participant.count"/>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.difficulty.index"/>&nbsp;
                            <i class="fa fa-question-circle" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="right" title="<fmt:message key="label.qb.difficulty.index.tooltip" />"></i>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.discrimination.index"/>&nbsp;
							<i class="fa fa-question-circle" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="right" title="<fmt:message key="label.qb.discrimination.index.tooltip" />"></i>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.point.biserial"/>&nbsp;
							 <i class="fa fa-question-circle" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="right" title="<fmt:message key="label.qb.point.biserial.tooltip" />"></i>
                        </th>
                    </tr>

                    <c:forEach var="activityDTO" items="${qbStats}" varStatus="i">
                        <tr>
                            <td class="text-left">
                                ${i.count}
                            </td>
                            <td class="text-left">
                                <c:out value="${activityDTO.qbQuestion.name}" escapeXml="false"/>			
                            </td>
                            <td  class="text-center">
                                <c:out value="${activityDTO.participantCount}" />
                            </td>
                            <c:choose>
                                <c:when test="${activityDTO.participantCount < 2}">
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </c:when>
                                <c:otherwise>
                                  	<td class="text-center ${activityDTO.difficultyIndex < 0.3 or activityDTO.difficultyIndex > 0.7 ? 'bg-danger' : 'bg-success'}">
										<fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.difficultyIndex}" />
									</td>
									<td class="text-center ${activityDTO.discriminationIndex < -0.2 ? 'bg-danger' : (activityDTO.discriminationIndex < 0.2 ? 'bg-warning' : 'bg-success')}">
										<fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.discriminationIndex}" />
									</td>
									<td class="text-center ${activityDTO.pointBiserial < -0.2 ? 'bg-danger' : (activityDTO.pointBiserial< 0.2 ? 'bg-warning' : 'bg-success')}">
										<fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.pointBiserial}" />
									</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>	
                </table>
		</div>
    </div>
</c:otherwise>
</c:choose>	
