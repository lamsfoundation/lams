<%@ include file="/common/taglibs.jsp"%>         

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.burning.questions"/>
		</h3>
	</div>
</div>
<!-- End header -->              

<!-- Tables -->
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">

	<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
		<c:set var="burningQsCount" value="${fn:length(burningQuestionItemDto.burningQuestionDtos)}"/>
		<c:set var="item" value="${burningQuestionItemDto.scratchieItem}"/>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title panel-collapse">
					<c:choose>
						<c:when test="${burningQsCount != 0}">
							<a data-toggle="collapse" data-itemuid="${item.uid}" class="collapsed burning-question-title">
								Q${i.index+1}) <c:out value="${item.qbQuestion.name}" escapeXml="false"/>
							</a>
						</c:when>
						<c:otherwise>
							Q${i.index+1}) <c:out value="${item.qbQuestion.name}" escapeXml="false"/> 
						</c:otherwise>
					</c:choose>
					<span class="burning-question-description">
						<c:out value="${item.qbQuestion.description}" escapeXml="false"/> 
					</span>
                    <c:if test="${burningQsCount > 0}">
					   <span class="badge pull-right" style="margin-right: 4px">${burningQsCount}</span>
                    </c:if>    
				</h4>
			</div>
		
			<c:if test="${burningQsCount > 0}">
				<div id="collapse-${item.uid}" class="panel-collapse collapse">
				<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover">
						<tbody>
							<c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}">
								<tr>
									<td class="text-nowrap">
										<c:out value="${burningQuestionDto.sessionName}" escapeXml="false"/>
									</td>
									<td>
										${burningQuestionDto.escapedBurningQuestion}
									</td>
									<td class="text-nowrap">
										<span class="badge">${burningQuestionDto.likeCount}</span> &nbsp; <i class="fa fa-thumbs-o-up" style="color:darkblue"></i>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				</div>
				</div>
			</c:if>
			
		</div>
	</c:forEach>    
  
</div>
</div>
