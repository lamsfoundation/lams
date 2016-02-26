<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<lams:html>
<lams:head>
	    <%@ include file="/common/header.jsp" %>
	    <link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">

		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>

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
			theme: 'blue',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "resizable", "filter" ],
            headers: { ${filterString} }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
                filter_searchDelay: 700 
            }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
				ajaxUrl : "<c:url value='/monitoring/getAnswersJSON.do'/>?page={page}&size={size}&{sortList:column}&{filterList:fcol}&questionUid=${question.uid}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>';
							rows += userData["userName"];
							rows += '</td>';

						    <c:forEach var="option" items="${question.options}">
								rows += '<td align="center" width="30px">';
								if ( $.inArray('${option.uid}',userData.choices) > -1 ) {
									rows += '<img src="${tool}/includes/images/tick_red.gif" title="<fmt:message key="message.learner.choose.answer"/>">';
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
				},					
			    container: $(this).next(".pager"),
			    output: '{startRow} to {endRow} ({totalRows})',
			    // css class names of pager arrows
			    cssNext: '.tablesorter-next', // next page arrow
				cssPrev: '.tablesorter-prev', // previous page arrow
				cssFirst: '.tablesorter-first', // go to first page arrow
				cssLast: '.tablesorter-last', // go to last page arrow
				cssGoto: '.gotoPage', // select dropdown to allow choosing a page
				cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
				cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option
				// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
				cssDisabled: 'disabled' // Note there is no period "." in front of this class name
			})
		});
  	})
</script>
</lams:head>
<body class="stripes">
		<div id="content">
		<h1>
			<fmt:message key="title.chart.report"/>
		</h1>

		<h2><fmt:message key="label.question"/></h2>

		<table  class="tablesorter-blue" cellspacing="0">
		<tr>
			<th colspan="2" class="first"><c:out value="${question.description}" escapeXml="false"/></th>
		</tr>
		<c:forEach var="option" items="${question.options}" varStatus="optStatus">
			<tr>
				<td  width="100px">
					<%= SurveyConstants.OPTION_SHORT_HEADER %>${optStatus.count}
				</td>
				<td>
					<c:out value="${option.description}" escapeXml="true"/>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${question.appendText ||question.type == 3}">
			<tr>
				<td  width="100px">
					<fmt:message key="label.open.response"/>
				</td>
				<td>&nbsp;</td>
			</tr>
		</c:if>
		</table>

		<h2><fmt:message key="label.answer"/></h2>

		<c:choose>
			<c:when test="${!(question.appendText || question.type==3)}">
				<c:set var="nameWidth">width="25%"</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="nameWidth"></c:set>
			</c:otherwise>
		</c:choose>

		<div class="tablesorter-holder">
		<table class="tablesorter" data-session-id="${toolSessionID}">
			<thead>
				<tr>
 					<th align="left" ${nameWidth}>
						<fmt:message key="label.learner"/>
					</th>
					<c:forEach var="option" items="${question.options}" varStatus="optStatus">
					<th>
						<%= SurveyConstants.OPTION_SHORT_HEADER %>${optStatus.count}
					</th>
					</c:forEach>
					<c:if test="${question.appendText || question.type == 3}">
					<th>
						<fmt:message key="label.open.response"/>
					</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		
		<!-- pager -->
		<div class="pager">
			<form>
				<img class="tablesorter-first"/>
				<img class="tablesorter-prev"/>
				<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				<img class="tablesorter-next"/>
				<img class="tablesorter-last"/>
				<select class="pagesize">
					<option selected="selected" value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
					<option value="100">100</option>
				</select>
			</form>
		</div>
		</div>
		
	</div>
</body>
</lams:html>
