<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<div class="panel panel-default" style="background-color: #f9f8f8aa;">
<div class="panel-body">
	
	<div class="pull-right question-description" style="color: #969494">
		Version: ${question.version}
	</div>

	<div class="">
		<c:out value="${question.name}"/>
	</div>
	
	<div class="question-description">				
		<c:out value="${question.description}" escapeXml="false"/>
	</div>
 		
	<c:choose>
		<c:when test="${question.type == 1}">
		
				<table class="table table-striped table-hover table-condensed">
					<c:forEach var="option" items="${question.qbOptions}" varStatus="i">
						<tr>
							<td width="5px" style="    padding-right: 0;">
								<c:if test="${option.correct}">
									<i class="fa fa-check text-success"></i>
								</c:if>
							</td>
							
							<td width="10px">
								<span 
									<c:if test="${option.correct}">class="text-success"</c:if>>
									${i.index+1})
								</span>
							</td>
							
							<td>
								<c:if test="${option.correct}">
									<div class="text-success">
								</c:if>
								<c:out value="${option.name}" escapeXml="false"/>
								<c:if test="${option.correct}">
									</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>


				

		
</div></div>



		<div>
			<a id="import-qbquestion-button" class="btn btn-xs btn-default pull-right" data-question-uid="${question.uid}" title="<fmt:message key="gradebook.monitor.show.dates" />">
				<i class="fa fa-calendar-check-o"></i> 
				<span class="hidden-xs">
					Import
				</span>
			</a>
		</div>
