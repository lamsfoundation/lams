<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="leaderDto" value="${sessionMap.leaderDto}"/>

<script type="text/javascript">
	$(document).ready(function(){
        $('[data-toggle="tooltip"]').bootstrapTooltip();
        
	   	// must display charts after screen is visible or cannot calculate widths.
		drawHistogram('chartDiv', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
					'<fmt:message key="label.marks"/>', '<fmt:message key="label.number.groups.in.mark.range"/>');
	});		
</script>

<c:choose>

<c:when test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:when>
	
<c:otherwise>
	
	<p><fmt:message key="label.graph.help"/></p>
	
	<div class="panel panel-default" >
      	<div class="panel-body">
            <div class="row">
                <div class="col-xs-12">
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
                                <c:out value="${leaderDto.numberGroupsLeaderFinished}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                                <fmt:message key="label.lowest.mark"/>
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.minMark}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.median.mark" />:
                            </td>
                            <td class="text-right">
                                -
                            </td>
                        </tr>
                    </table>    
                </div>
                <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
					<table class="table table-striped table-hover table-condensed">
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.average.mark" />:
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.avgMark}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                                <fmt:message key="label.highest.mark"/>
                            </td>
                            <td class="text-right">
                                <c:out value="${leaderDto.maxMark}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name" >
                                <fmt:message key="label.modes.mark" />:
                            </td>
                            <td class="text-right">
                                -
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div id="accordion-qb-stats" class="panel-group voffset20" role="tablist" aria-multiselectable="true">             
	<div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="heading-qb-stats">
            <span class="panel-title">
                <a class="" role="button" data-toggle="collapse" href="#qb-stats" aria-expanded="true" aria-controls="qb-stats">
                    <fmt:message key="label.qb.stats" />
                </a>
                <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="Item analysis is a technique that analyses the student answers to evaluate the effectiveness of questions in an exam."></i>
    		</span>
        </div>
        <div class="">
            <div aria-expanded="true" id="qb-stats" class="panel-body panel-collapse collapse in" role="tabpanel" aria-labelledby="heading-qb-stats" style="">
                <table class="table table-striped table-hover table-condensed">
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
                            <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.qb.difficulty.index.tooltip" />"></i>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.discrimination.index"/>&nbsp;
							<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.qb.discrimination.index.tooltip" />"></i>
                        </th>
                        <th scope="col" class="text-center">
                            <fmt:message key="label.qb.point.biserial"/>&nbsp;
							 <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.qb.point.biserial.tooltip" />"></i>
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
                                    <td class="text-center"><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.difficultyIndex}" /></td>
                                    <td class="text-center"><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.discriminationIndex}" /></td>
                                    <td class="text-center"><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.pointBiserial}" /></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>	
                </table>
            </div>
	    </div>
    </div>
	
</c:otherwise>
</c:choose>	
