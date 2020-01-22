<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>
<c:set var="isAuthoringRestricted" value="${mode == 'teacher'}" />

<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/Sortable.js"></script>
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
	
$(document).ready(function(){
		
	    //init questions sorting
	    <c:if test="${not empty sessionMap.itemList}">
		    new Sortable($('#itemTable tbody')[0], {
			    animation: 150,
			    direction: 'vertical',
				store: {
					set: function (sortable) {
						//update all sequenceIds
						for (var i = 0; i < sortable.el.rows.length; i++) {
						 	var tr = sortable.el.rows[i];
						 	var input = $("input[name^=sequenceId]", tr);
						 	input.val(i);
						 	var displayOrder = $(".item-display-order", tr);
						 	displayOrder.text(i + 1 + ")");
						}
	
						//prepare SequenceIds parameter
						var serializedSequenceIds = "";
						$("[name^=sequenceId]").each(function() {
							serializedSequenceIds += "&" + this.name + "="  + this.value;
						});
	
						$.ajax({ 
						    url: '<c:url value="/authoring/cacheItemsOrder.do"/>',
							type: 'POST',
							data: {
								sessionMapID: "${sessionMapID}",
								sequenceIds: serializedSequenceIds
							}
						});
	
						//update names
						$("[name^=sequenceId]").each(function() {
							var newSequenceId = this.value;
							//update name of the hidden input
							this.name = "sequenceId" + newSequenceId;
						});
					}
				}
			});
		</c:if>
	});
</script>

<!-- Dropdown menu for choosing a question from question bank -->
<div class="panel panel-default voffset20">
	<div class="panel-heading panel-title">
		<div id="importExport" class="btn-group pull-right">
			<a href="#" onClick="javascript:importQTI();return false;" class="btn btn-default btn-xs loffset5">
				<fmt:message key="label.authoring.import.qti" /> 
			</a>
		</div> 
		<fmt:message key="label.questions"/>
	</div>
	<input type="hidden" name="itemCount" id="itemCount" value="${fn:length(sessionMap.itemList)}">
		
	<table id="itemTable" class="table table-condensed table-striped">
		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<tr>
				<td style="width:5%" class="item-display-order">
					${status.count})
				</td>
				<td style="padding-top:15px; padding-bottom:15px;">
					<input type="hidden" name="sequenceId${item.displayOrder}" value="${status.index}" class="item-sequence-id">
					
					<c:out value="${item.qbQuestion.name}" escapeXml="true"/>
					
				    <span class='pull-right alert-info btn-xs loffset5 roffset5'>
				    	v.&nbsp;${item.qbQuestion.version}
				    </span>
				    
				   	<span class='pull-right alert-info btn-xs'>
						<c:choose>
							<c:when test="${item.qbQuestion.type == 1}">
								<fmt:message key="label.type.multiple.choice" />
							</c:when>
							<c:when test="${item.qbQuestion.type == 3}">
								<fmt:message key="label.type.short.answer" />
							</c:when>
						</c:choose>
	       			</span>
				</td>
					
				<td align="center" style="width:5%">
					<c:set var="editItemUrl" >
						<c:url value='/authoring/editItem.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>		
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil"	title="<fmt:message key='label.edit' />"/></i>
					</a>
				</td>			
				
				<c:if test="${!isAuthoringRestricted}">
					<td align="center" style="width:5%">
						<i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
							onclick="removeItem(${status.index})"></i>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>
