<!-- Wraps up both the sticky and non-sticky comments. Used for page refresh and changing the sort.-->

<div class="mb-2 float-end">
	<select id="sortMenu" name="sortMenu" class="form-control float-end w-auto">
		<option value='0'
			<c:if test='${sessionMap.sortBy == 0}'>selected</c:if>>
				<fmt:message key="label.newest.first" />
		</option>
		<option value='1'
				<c:if test='${sessionMap.sortBy == 1}'>selected</c:if>>
			<fmt:message key="label.top.comments" />
		</option>
	</select>
</div>

<!--  if sticky comments exists, they need to be displayed first, but do not clobber the non sticky threads! -->
<c:set var="nonsticky" value="${commentThread}" />
<c:set var="commentThread" value="${sticky}" />
<c:set var="noMorePages" value="true" />
<div id="stickyComments">
	<%@ include file="topicview.jsp"%>
</div>
<c:set var="commentThread" value="${nonsticky}" />
<c:set var="noMorePages" value="false" />

<div id="newcomments"></div>

<c:if test='${(empty commentThread) && (sessionMap.mode == "teacher")}'>
	<fmt:message key="label.no.comments" />
</c:if>

<div class="scroll">
	<c:if test="${! empty commentThread}">
		<%@ include file="topicview.jsp"%>
	</c:if>
</div>

<script>
	$('.scroll').jscroll({
		loadingHtml: '<img src="${loading_animation}" alt="${loading_words}" />${loading_words}',
		padding:30,
		autoTrigger:false,
		callback:scrollDoneCallback
	});
</script>