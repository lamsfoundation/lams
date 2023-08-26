<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<lams:html>
<lams:head>
	    <%@ include file="/common/header.jsp" %>
	    <link type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">

		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
		<lams:JSImport src="includes/javascript/portrait.js" />

<script type="text/javascript">

	<c:set var="numOptions">${fn:length(question.options)}</c:set>
	<c:set var="filterOptions" value="{ sorter: false, filter: false}"/>
    <c:forEach var="option" items="${question.options}" varStatus="optStatus">
    	<c:choose>
    	<c:when test="${optStatus.first}">
	    	<c:set var="filterString">${optStatus.count}:${filterOptions}</c:set>
	    </c:when>
	    <c:otherwise>
	    	<c:set var="filterString">${filterString}, ${optStatus.count}:${filterOptions}</c:set>
	    </c:otherwise>
	    </c:choose>
    </c:forEach>
    <c:if test="${question.appendText || question.type==3}">
    	<c:choose> 
    	<c:when test="${numOptions gt 0}">
	    	<c:set var="filterString">${filterString}, ${numOptions+1}:${filterOptions}</c:set>
	    </c:when>
	    <c:otherwise>
	    	<c:set var="filterString">1:${filterOptions}</c:set>
	    </c:otherwise>
	    </c:choose>
    </c:if>
	
	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { ${filterString} }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.search"/></spring:escapeBody>' }, 
                filter_searchDelay: 700 
            }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
                container: $(this).find(".ts-pager"),
                output: '{startRow} to {endRow} ({totalRows})',
                cssPageDisplay: '.pagedisplay',
                cssPageSize: '.pagesize',
                cssDisabled: 'disabled',				ajaxUrl : "<c:url value='/monitoring/getAnswersJSON.do'/>?page={page}&size={size}&{sortList:column}&{filterList:fcol}&questionUid=${question.uid}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>';
							rows += definePortraitPopover(userData["portraitId"], userData["userId"],  userData["userName"],  userData["userName"]);
							rows += '</td>';

						    <c:forEach var="option" items="${question.options}">
								rows += '<td align="center" width="30px">';
								if ( $.inArray('${option.uid}',userData.choices) > -1 ) {
									rows += '<i class="fa fa-check text-success" title="<spring:escapeBody javaScriptEscape="true"><fmt:message key="message.learner.choose.answer"/></spring:escapeBody>">';
								}
								rows += '</td>';
						    </c:forEach>
								
							<c:if test="${question.appendText || question.type==3}">
							rows += '<td>';
							if ( userData["answerText"] ) {
								rows += userData["answerText"];
							} else {
								rows += '-';
							}
 
							rows += '</td>';
							</c:if>
															
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
					}
				}}).bind('pagerInitialized pagerComplete', function(event, options){
					initializePortraitPopover('${lams}');
	            })
		});
  	})
</script>
</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="title.chart.report"/></c:set>
	<c:set var="optionShortHeader"><%= SurveyConstants.OPTION_SHORT_HEADER %></c:set>
	<lams:Page type="monitor" title="${title}">

		<h4><fmt:message key="label.question"/></h4>

		<table  class="table table-condensed table-striped table-bordered">
		<tr>
			<th colspan="2" class="first"><c:out value="${question.description}" escapeXml="false"/></th>
		</tr>
		<c:forEach var="option" items="${question.options}" varStatus="optStatus">
			<tr>
				<td width="30%">
					${optionShortHeader}${optStatus.count}
				</td>
				<td>
					<c:out value="${option.description}" escapeXml="true"/>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${question.appendText ||question.type == 3}">
			<tr>
				<td colspan="2">
					<fmt:message key="label.open.response"/>
				</td>
			</tr>
		</c:if>
		</table>

		<h4><fmt:message key="label.answer"/></h4>

		<c:choose>
			<c:when test="${!(question.appendText || question.type==3)}">
				<c:set var="nameWidth">width="25%"</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="nameWidth"></c:set>
			</c:otherwise>
		</c:choose>
	
		<c:set var="numColumns" value="${fn:length(question.options)+1}"/>
		<c:if test="${question.appendText || question.type == 3}">
			<c:set var="numColumns" value="${numColumns+1}"/>
		</c:if>

		<lams:TSTable numColumns="${numColumns}" dataId='data-session-id="${toolSessionID}"'> 
			<th align="left" ${nameWidth}>
				<fmt:message key="label.learner"/>
			</th>
			<c:forEach var="option" items="${question.options}" varStatus="optStatus">
			<th>
				${optionShortHeader}${optStatus.count}
			</th>
			</c:forEach>
			<c:if test="${question.appendText || question.type == 3}">
			<th>
				<fmt:message key="label.open.response"/>
			</th>
			</c:if>
		</lams:TSTable>

		<a href="javascript:window.close();" class="btn btn-default btn-sm">
		<fmt:message key="button.close"/>
		</a>

	</lams:Page>
</body>
</lams:html>