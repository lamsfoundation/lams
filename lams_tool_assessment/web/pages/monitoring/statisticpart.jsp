<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="leaderDto" value="${sessionMap.leaderDto}"/>
<c:set var="activityDto" value="${sessionMap.activityDto}"/>

<lams:css suffix="chart"/>

<script type="text/javascript" src="${lams}includes/javascript/chartjs/chart.umd.js"></script>
<lams:JSImport src="includes/javascript/chart.js" relative="true" />
<script type="text/javascript">
	$(document).ready(function(){
		$('[data-bs-toggle="tooltip"]').tooltip();
		
		$('#activity-evaluation').on('focus', function(){
			$(this).data('previousValue', this.value);
		}).on('change', function() {
			if (!this.value && !confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='warn.tool.output.change.none'/></spring:escapeBody>")) {
				$(this).val($(this).data('previousValue'));
				return;
			}
			$(this).data('previousValue', this.value);
			$.ajax({
				url: '<c:url value="/monitoring/setActivityEvaluation.do?sessionMapID=${sessionMapID}"/>',
				data: {
					activityEvaluation: this.value,
					"<csrf:tokenname/>":"<csrf:tokenvalue/>"
				},
				dataType: 'json',
				method: 'post',
				success: function (json) {
					if (json.success == "true") {
						alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.tool.output.has.been.changed'/></spring:escapeBody>");
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.operation.failed'/></spring:escapeBody>");
				}
			});
			
		});

	   	// must display charts after screen is visible or cannot calculate widths.
 		<c:choose>
		<c:when test="${sessionMap.assessment.useSelectLeaderToolOuput}">	
			drawHistogram('chartDivLeader${sessionMap.toolContentID}', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.marks"/></spring:escapeBody>', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.number.groups.in.mark.range"/></spring:escapeBody>');
		</c:when>
		<c:otherwise>
			drawHistogram('chartDivActivity${sessionMap.toolContentID}', 
				'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
				'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.marks"/></spring:escapeBody>', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.number.learners.in.mark.range"/></spring:escapeBody>');
			
			<c:forEach var="sessionDto" items="${sessionDtos}">
			drawHistogram('chartDivSession${sessionDto.sessionId}', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}"/>', 
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.marks"/></spring:escapeBody>', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.number.learners.in.mark.range"/></spring:escapeBody>');
			</c:forEach>	
		</c:otherwise>
		</c:choose>
	});		
</script>

<c:choose>
<c:when test="${empty sessionDtos}">
	<lams:Alert5 type="warn" id="no-edit">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert5>
</c:when>
	
<c:otherwise>
	<em>
		<fmt:message key="label.graph.help"/>
	</em>
	
	<c:choose>
	<c:when test="${sessionMap.assessment.useSelectLeaderToolOuput}">	
		<div class="lcard" >
       	<div class="card-body">
 		<div class="row">
			<div class="col-12"></div>
			<div id="chartDivLeader${sessionMap.toolContentID}" class="markChartDiv"></div>
		</div>
            
        <div class="row"> 
            <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
                <table class="table table-striped table-hover table-condensed">
                    <tr>
                        <td class="field-name">
                            <fmt:message key="label.number.groups.finished" />:
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.count}" />
                        </td>	 
                    </tr>
                    <tr>
                        <td class="field-name">
                            <fmt:message key="label.lowest.mark"/>
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.minString}" />
                        </td>
                    </tr>
                    <tr>
                        <td class="field-name">
                          <fmt:message key="label.median.mark"/> 
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.medianString}" />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
                <table class="table table-striped table-hover table-condensed">
                    <tr>
                        <td class="field-name">
                            <fmt:message key="label.monitoring.question.summary.average.mark" />:
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.averageString}" />
                        </td>
                    </tr>
                    <tr>
                        <td class="field-name">
                          <fmt:message key="label.highest.mark"/> 
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.maxString}" />
                        </td>
                    </tr>
                    <tr>
                        <td class="field-name">
                           <fmt:message key="label.modes.mark"/>
                        </td>
                        <td style="text-align:right;">
                             <c:out value="${leaderDto.modesString}" />
                        </td>
                    </tr>
                </table>
            </div>
        </div>	        

		</div>
		</div>
	</c:when>
		
	<c:otherwise>
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="card-subheader fs-4 mt-4 mb-2">
				<fmt:message key="label.activity.stats"/>
			</div>
		</c:if>
		
		<div class="lcard" >
       		<div class="card-body">
            <div class="row">
                 <div class="col-12" ></div>
                 <div id="chartDivActivity${sessionMap.toolContentID}" class="markChartDiv"></div>
            </div>

			<div class="row"> 
				<div class="col-md-6" style="padding: 0px 20px 0px 20px;">
					<table class="table table-striped table-hover table-condensed">
						<tr>
							<td class="field-name">
								<fmt:message key="label.number.learners.per.activity" />:
							</td>
							<td style="text-align:right;">
								 <c:out value="${activityDto.count}" />
							</td>	 
						</tr>
                        <tr>
                            <td class="field-name">
								<fmt:message key="label.lowest.mark"/>
                            </td>
                            <td style="text-align:right;">
                                 <c:out value="${activityDto.minString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                              <fmt:message key="label.median.mark"/> 
                            </td>
                            <td style="text-align:right;">
                                 <c:out value="${activityDto.medianString}" />
                            </td>
                        </tr>
					</table>
				</div>
				<div class="col-md-6" style="padding: 0px 20px 0px 20px;">
					<table class="table table-striped table-hover table-condensed">
                        <tr>
                            <td class="field-name">
								<fmt:message key="label.monitoring.question.summary.average.mark" />:
                            </td>
                            <td style="text-align:right;">
                                 <c:out value="${activityDto.averageString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                              <fmt:message key="label.highest.mark"/> 
                            </td>
                            <td style="text-align:right;">
                                 <c:out value="${activityDto.maxString}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="field-name">
                               <fmt:message key="label.modes.mark"/>
                            </td>
                            <td style="text-align:right;">
                                 <c:out value="${activityDto.modesString}" />
                            </td>
                        </tr>
                    </table>
				</div>
			</div>	
			</div>
		</div>
		
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="card-subheader fs-4 mt-4 mb-2">
				<fmt:message key="label.group.stats"/>
			</div>
			
			<c:forEach var="sessionDto" items="${sessionDtos}">
			    <div class="lcard" >
			        <div class="card-header" id="heading${sessionDto.sessionId}">
						<fmt:message key="monitoring.label.group" />: <c:out value="${sessionDto.sessionName}" />
			        </div>
			        
		       		<div class="card-body">    
					<div class="row">
						 <div class="col-12"></div>
		 				 <div id="chartDivSession${sessionDto.sessionId}" class="markChartDiv"></div>
				    </div>
                        
                    <div class="row"> 
                        <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
                            <table class="table table-striped table-hover table-condensed">
                                <tr>
                                    <td class="field-name">
                                        <fmt:message key="label.number.learners.per.session" />:
                                    </td>
                                    <td style="text-align:right;">
                                         <c:out value="${sessionDto.count}" />
                                    </td>	 
                                </tr>
                                <tr>
                                    <td class="field-name">
                                        <fmt:message key="label.lowest.mark"/>
                                    </td>
                                    <td style="text-align:right;">
                                         <c:out value="${sessionDto.minString}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="field-name">
                                      <fmt:message key="label.median.mark"/> 
                                    </td>
                                    <td style="text-align:right;">
                                        <c:out value="${sessionDto.medianString}" />
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="col-md-6" style="padding: 0px 20px 0px 20px;">
                            <table class="table table-striped table-hover table-condensed">
                                <tr>
                                    <td class="field-name">
                                        <fmt:message key="label.monitoring.question.summary.average.mark" />:
                                    </td>
                                    <td style="text-align:right;">
                                         <c:out value="${sessionDto.averageString}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="field-name">
                                      <fmt:message key="label.highest.mark"/> 
                                    </td>
                                    <td style="text-align:right;">
                                        <c:out value="${sessionDto.maxString}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="field-name">
                                       <fmt:message key="label.modes.mark"/>
                                    </td>
                                    <td style="text-align:right;">
                                         <c:out value="${sessionDto.modesString}" />
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>	

					</div>
				</div>
			</c:forEach>
		</c:if>
	</c:otherwise>
	</c:choose>		
	
	<div id="accordion-qb-stats" class="panel-group voffset20" role="tablist" aria-multiselectable="true"> 
	    <div class="lcard">
	        <div class="card-header" id="heading-qb-stats">
	        	<span class="card-title collapsable-icon-left">
					<button type="button" class="btn btn-secondary-darker no-shadow" data-bs-toggle="collapse" data-bs-target="#qb-stats" 
							aria-expanded="true" aria-controls="qb-stats">
		          		<fmt:message key="label.qb.stats" />
		          	</button>
		          	<lams:Popover>
		          		<fmt:message key="label.qb.stats.tooltip" />
		          	</lams:Popover>
	      		</span>
	        </div>
	
			<div id="qb-stats" class="card-body collapse show">
				<table class="table table-striped table-hover table-condensed">
					<tr>		
						<th scope="col"  class="text-left">
							#
						</th>
						<th scope="col"  class="text-left">
							<fmt:message key="label.monitoring.question.summary.question"/>
						</th>
						<th scope="col" class="text-center">
							<fmt:message key="label.qb.participant.count"/>
						</th>
						<th scope="col" class="text-center">
							<fmt:message key="label.qb.difficulty.index"/>&nbsp;
							<lams:Popover>
				          		<fmt:message key="label.qb.difficulty.index.tooltip" />
				          	</lams:Popover>
							
						</th>
						<th scope="col" class="text-center">
							<fmt:message key="label.qb.discrimination.index"/>&nbsp;
							<lams:Popover>
								<fmt:message key="label.qb.discrimination.index.tooltip" />
				          	</lams:Popover>
						</th>
						<th scope="col" class="text-center">
							<fmt:message key="label.qb.point.biserial"/>&nbsp;
							<lams:Popover>
								<fmt:message key="label.qb.point.biserial.tooltip" />
				          	</lams:Popover>
	
						</th>
					</tr>
						
					<c:forEach var="activityDTO" items="${qbStats}" varStatus="i">
						<tr>
							<td class="text-left">
								${i.index + 1}
							</td>
							<td class="text-left">
								<c:out value="${activityDTO.qbQuestion.name}" escapeXml="false"/>			
							</td>
							<td class="text-center">
								<c:out value="${activityDTO.participantCount}" />
							</td>
							<c:choose>
								<c:when test="${activityDTO.participantCount < 2}">
									<td class="text-center">-</td>
									<td class="text-center">-</td>
									<td class="text-center">-</td>
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
	</div>

</c:otherwise>
</c:choose>

<%@ include file="parts/toolOutput.jsp"%>
