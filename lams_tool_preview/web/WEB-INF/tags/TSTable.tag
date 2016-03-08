<%-- Tablesorter Table and Pager --%>
<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>

<%@ attribute name="numColumns" required="true" rtexprvalue="true"%>
<%@ attribute name="sessionId" required="false" rtexprvalue="true"%>
<%@ attribute name="tableClass" required="false" rtexprvalue="true"%>
<%@ attribute name="pagerClass" required="false" rtexprvalue="true"%>
<%@ attribute name="test" required="false" rtexprvalue="true"%>

<c:if test="${empty tableClass}">
	<c:set var="tableClass" value="tablesorter" />
</c:if>

<c:if test="${empty pagerClass}">
	<c:set var="pagerClass" value="ts-pager" />
</c:if>

<table class="${tableClass}" data-session-id="${sessionId}">
	<thead>
		<tr>
			<jsp:doBody />
		</tr>
	</thead>
	<tfoot>
		<tr>
	    	<th colspan="${numColumns}" class="${pagerClass} form-horizontal">
	        <button type="button" class="btn first"><i class="fa fa-step-backward"></i></button>
	        <button type="button" class="btn prev"><i class="fa fa-backward"></i></button>
	        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
	        <button type="button" class="btn next"><i class="fa fa-forward"></i></button>
	        <button type="button" class="btn last"><i class="fa fa-step-forward"></i></button>
	        <select class="pagesize input-mini" title="Select page size">
	      		<c:if test="${not empty test}"><option value="2">2</option></c:if>
	      		<option selected="selected" value="10">10</option>
	      		<option value="20">20</option>
	      		<option value="30">30</option>
	      		<option value="40">40</option>
	      		<option value="50">50</option>
	      		<option value="100">100</option>
	        </select>
	      </th>
	    </tr>
	</tfoot>
	<tbody>
	</tbody>
</table>

	