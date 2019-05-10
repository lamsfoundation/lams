<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="questionResult" items="${assessmentResult.questionResults}" varStatus="i">
	<c:set var="question" value="${questionResult.questionDto}"/>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<span class="float-left space-right">Q${i.index+1})</span> 
				<c:out value="${questionResult.questionDto.title}" escapeXml="false"/>
			</h4> 
		</div>
	              
		<div class="panel-body">
			<div class="table-responsive">
				<table class="table table-striped">
					<tbody>
						<tr>
							<td>
								<%@ include file="userresponse.jsp"%>
												
								<!--<c:choose>
									<c:when test="${userAttempt.mcOptionsContent.correctOption}">
										<i class="fa fa-check-square"></i>
									</c:when>
									<c:otherwise>
										<i class="fa fa-minus-square"></i>
									</c:otherwise>
								</c:choose>-->
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:forEach>
