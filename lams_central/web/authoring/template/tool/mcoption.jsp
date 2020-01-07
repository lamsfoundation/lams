<%@ include file="/common/taglibs.jsp"%>
<%-- Generic Q&A option page. Expects an input of questionNumber, optionNumber & contentFolderID, and creates a radio button question${questionNumber}correct 
and a text field field named question${questionNumber}option${optionNumber} --%>

		<c:set var="qnon">question${questionNumber}option${optionNumber}</c:set>
		
		<table class="table table-condensed table-no-border">
		<tr>
		<td>
			<span class="field-name">
				<fmt:message key="authoring.label.option.num"><fmt:param value="${optionNumber}"/></fmt:message>
			</span>
			<lams:CKEditor id="${qnon}" value="${optionText}" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor> 
		</td>
		<td width="60px">
				<input type="radio" name="question${questionNumber}correct" value="${optionNumber}" id="${qnon}correct" ${optionCorrect?"checked":""}/> 
		</td>
		<td class="arrows" width="40px">
			<!-- Don't display up icon if first line -->
			<c:if test="${optionNumber > 1}">
 				<lams:Arrow state="up" id="${qnon}UpButton" title="<fmt:message key='hint.option.up'/>" 
 					onclick="javascript:swapOptions(${questionNumber}, ${optionNumber}, ${optionNumber-1})"/>
 			</c:if>
			<!-- Don't display down icon if last line -->
			<c:set var="display" value="${optionCount > optionNumber ? '' : 'none'}"/>
			<lams:Arrow state="down" id="${qnon}DownButton" title="<fmt:message key='hint.option.down'/>" 
				onclick="javascript:swapOptions(${questionNumber}, ${optionNumber}, ${optionNumber+1})" display="${display}"/>
 		</td>
		<td width="20px" align="center">
			<i class="fa fa-times" id="${qnon}DeleteButton" title="<fmt:message key='hint.option.delete'/>" onclick="removeOption(${questionNumber}, ${optionNumber});">
		</td>
		</tr>
		</table>