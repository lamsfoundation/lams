<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Option page for the Assessment Multiple Choice question. Creates the output ${containingDivName}assmcq${questionNumber}option${optionNumber} --%>

		<c:set scope="request" var="qnon">${containingDivName}assmcq${questionNumber}option${optionNumber}</c:set>

		<table class="table table-condensed table-no-border">
		<tr>
		<td width="75px">
			<span class="field-name"><label for="${qnon}">
				<fmt:message key="authoring.label.option.num"><fmt:param value="${optionNumber}"/></fmt:message>
			</label></span>
		</td>
		<td>
			<lams:CKEditor id="${qnon}" value="${optionText}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor> 
		</td>
		<td width="100px">
				<%@ include file="assessgradeselector.jsp"%>
		</td>
		<td class="arrows" width="40px">
			<!-- Don't display up icon if first line -->
			<c:if test="${optionNumber > 1}">
 				<lams:Arrow state="up" id="${qnon}UpButton" title="<fmt:message key='hint.option.up'/>" 
 					onclick="javascript:swapOptions(${questionNumber}, ${optionNumber}, ${optionNumber-1}, '${containingDivName}divassmcq${questionNumber}options', '${containingDivName}')"/>
 			</c:if>
			<!-- Don't display down icon if last line -->
			<c:set var="display" value="${optionCount > optionNumber ? '' : 'none'}"/>
			<lams:Arrow state="down" id="${qnon}DownButton" title="<fmt:message key='hint.option.down'/>" 
				onclick="javascript:swapOptions(${questionNumber}, ${optionNumber}, ${optionNumber+1}, '${containingDivName}divassmcq${questionNumber}options', '${containingDivName}')" display="${display}"/>
 		</td>
		<td width="20px" align="center">
			<i class="fa fa-times" id="${qnon}DeleteButton" title="<fmt:message key='hint.option.delete'/>" onclick="removeOption(${questionNumber}, ${optionNumber}, '${containingDivName}divassmcq${questionNumber}options', '${containingDivName}')">
		</td>
		</tr>
		</table>