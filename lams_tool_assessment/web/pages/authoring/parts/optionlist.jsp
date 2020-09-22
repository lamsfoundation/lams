<%@ include file="/common/taglibs.jsp"%>
<div id="optionArea">
	<input type="hidden" name="optionCount" id="optionCount" value="${fn:length(optionList)}">
	<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
	
	<table class="table table-condensed table-striped">
		<c:forEach var="option" items="${optionList}" varStatus="status">
			<tr>
				<td>
					<c:choose>
						<c:when test="${questionType == 1}">
							<%@ include file="option.jsp"%>
						</c:when>
						<c:when test="${questionType == 2}">
							<%@ include file="matchingpairoption.jsp"%>
						</c:when>
						<c:when test="${questionType == 3}">
							<%@ include file="shortansweroption.jsp"%>
						</c:when>					
						<c:when test="${questionType == 4}">
							<%@ include file="numericaloption.jsp"%>
						</c:when>
						<c:when test="${questionType == 7}">
							<%@ include file="orderingoption.jsp"%>
						</c:when>
						<c:when test="${questionType == 8}">
							<%@ include file="optionhedgingmark.jsp"%>
						</c:when>
					</c:choose>	
				</td>
				
				<td class="arrows">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" titleKey="label.authoring.basic.up" 
		 						onclick="javascript:upOption(${status.index})"/>
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.authoring.basic.down" 
								onclick="javascript:downOption(${status.index})"/>
		 			</c:if>
				</td>			

				<c:if test="${!isAuthoringRestricted}">
					<td width="30px">
						<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.delete" />"
							onclick="javascript:removeOption(${status.index})"></i>
					</td>
				</c:if>
				
			</tr>
		</c:forEach>
	</table>
</div>