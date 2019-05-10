<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<div class="panel panel-default" style="background-color: #f9f8f8aa;">
<div class="panel-body">
	<input type="hidden" id="selected-question-uid" value="${question.uid}">
	
	<a id="import-button" class="btn btn-xs btn-default pull-right button-add-item" href="#nogo"
		title="Import question from the question bank">
		Import
	</a>
	
	<div class="pull-right">
		<c:choose>
			<c:when test="${fn:length(otherVersions) == 1}">
				<button class="btn btn-default btn-xs dropdown-toggle2" disabled="disabled">
				    Version ${question.version}
				</button>
			</c:when>

			<c:otherwise>
				<div class="dropdown">
					<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    	Version ${question.version}&nbsp;<span class="caret"></span>
					</button>
					
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
						<c:forEach items="${otherVersions}" var="otherVersion">
				    		<li <c:if test="${question.version == otherVersion.version}">class="disabled"</c:if>>
				    			<a href="#nogo" onclick="javascript:loadQuestionDetailsArea(${otherVersion.uid});">Version ${otherVersion.version}</a>
				    		</li>
				    	</c:forEach>
					</ul>
				</div>			
			</c:otherwise>
		</c:choose>
	</div>

	<div class="">
		<c:out value="${question.name}" escapeXml="false"/>
	</div>
	
	<div class="question-description">				
		<c:out value="${question.description}" escapeXml="false"/>
	</div>
 		
	<c:choose>
		<c:when test="${question.type == 1}">
			<table class="table table-striped table-hover table-condensed">
				<c:forEach var="option" items="${question.qbOptions}" varStatus="i">
					<tr>
						<td width="5px" style="padding-right: 0;">
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
</div>
</div>
