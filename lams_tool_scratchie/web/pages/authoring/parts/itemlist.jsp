<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>
<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<fmt:message key="message.qb.modified.update" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_VERSION_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.version" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.new" />';
			break;
	}
	if (qbMessage) {
		alert(qbMessage);
	}
</script>

<!-- Dropdown menu for choosing a question from question bank -->
<div class="panel panel-default voffset20">
	<div class="panel-heading panel-title">
		<div id="importExport" class="btn-group pull-right">
			<a href="#" onClick="javascript:importQTI();return false;" class="btn btn-default btn-xs">
				<fmt:message key="label.authoring.import.qti" /> 
			</a>
			<a href="#" onClick="javascript:exportQTI();return false;" class="btn btn-default btn-xs">
				<fmt:message key="label.authoring.export.qti" />
			</a>
		</div>

		<div class="roffset5 pull-right">
			<c:url var="tempUrl" value="">
				<c:param name="output">
					<c:url value='/authoring/importQbQuestion.do'/>?sessionMapID=${sessionMapID}
				</c:param>
			</c:url>
			<c:set var="returnUrl" value="${fn:substringAfter(tempUrl, '=')}" />
		
			<a href="<lams:LAMSURL/>/searchQB/start.do?returnUrl=${returnUrl}&toolContentId=${sessionMap.toolContentID}&KeepThis=true&TB_iframe=true&modal=true" class="btn btn-default btn-xs loffset5 thickbox"> 
				Import from question bank
			</a>
		</div> 
		
		<fmt:message key="label.questions"/>
		
	</div>
		<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(sessionMap.itemList)}">
		
		<table class="table table-condensed table-striped">
			<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
				<tr>
					<td style="padding-top:15px; padding-bottom:15px;">
						<c:out value="${item.qbQuestion.name}" escapeXml="true"/>			
					</td>
						
					<td class="arrows" style="width:5%">
						<c:if test="${not status.first}">
							<lams:Arrow state="up" title="<fmt:message key='label.authoring.basic.up'/>" onclick="return upItem(${status.index})"/>
						</c:if>
		
						<c:if test="${not status.last}">
							<lams:Arrow state="down" title="<fmt:message key='label.authoring.basic.down'/>" onclick="return downItem(${status.index})"/>
						</c:if>
					</td>
					
					<td align="center" style="width:5%">
						<c:set var="editItemUrl" >
							<c:url value='/authoring/editItem.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
						</c:set>		
						<a href="${editItemUrl}" class="thickbox"> 
							<i class="fa fa-pencil"	title="<fmt:message key='label.edit' />"/></i>
						</a>				
					
					<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
							onclick="removeItem(${status.index})"></i></td>
							
				</tr>
			</c:forEach>
		</table>
</div>
	
	
