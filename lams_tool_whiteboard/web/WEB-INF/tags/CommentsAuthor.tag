<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="allowCommentsVariableName" required="false" rtexprvalue="true"%>
<%@ attribute name="allowCommentLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="likeDislikeVariableName" required="false" rtexprvalue="true"%>
<%@ attribute name="likeOnlyCommentLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="likeDislikeLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="allowAnonymousVariableName" required="false" rtexprvalue="true"%>
<%@ attribute name="allowAnonymousLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="commentPanelHeaderKey" required="false" rtexprvalue="true"%>


<c:if test="${empty allowCommentsVariableName}">
	<c:set var="allowCommentsVariableName" value="allowComments" />
</c:if>
<c:if test="${empty allowCommentLabelKey}">
	<c:set var="allowCommentLabelKey" value="advanced.allow.comments" />
</c:if>
<c:if test="${empty likeDislikeVariableName}">
	<c:set var="likeDislikeVariableName" value="commentsLikeAndDislike" />
</c:if>
<c:if test="${empty likeOnlyCommentLabelKey}">
	<c:set var="likeOnlyCommentLabelKey" value="advanced.comments.like.only" />
</c:if>
<c:if test="${empty likeDislikeLabelKey}">
	<c:set var="likeDislikeLabelKey" value="advanced.comments.like.and.dislike" />
</c:if>
<c:if test="${empty allowAnonymousVariableName}">
	<c:set var="allowAnonymousVariableName" value="allowAnonymous" />
</c:if>
<c:if test="${empty allowAnonymousLabelKey}">
	<c:set var="allowAnonymousLabelKey" value="advanced.enable.anonymous.posts" />
</c:if>
<c:if test="${empty commentPanelHeaderKey}">
	<c:set var="commentPanelHeaderKey" value="advanced.comment.header" />
</c:if>

<lams:SimplePanel titleKey="advanced.comment.header">

<div class="checkbox">
	<label>
	<form:checkbox path="${allowCommentsVariableName}" value="1" id="${allowCommentsVariableName}" />
	<fmt:message key="${allowCommentLabelKey}"/>
	</label>
</div>

<div id="likedislike">
<div class="loffset20">
		<form:radiobutton path="${likeDislikeVariableName}" value="false" />&nbsp;<fmt:message key="${likeOnlyCommentLabelKey}" /> &nbsp;
		<form:radiobutton path="${likeDislikeVariableName}" value="true" />&nbsp;<fmt:message key="${likeDislikeLabelKey}" /> 
</div>
<div class="checkbox">
	<label>
	<form:checkbox path="${allowAnonymousVariableName}" styleId="${allowAnonymousVariableName}" />
	<fmt:message key="${allowAnonymousLabelKey}"/>
	</label>
</div>
</div>

</lams:SimplePanel>

<script>
jQuery( document ).ready(function( $ ) {
	if (! $("#${allowCommentsVariableName}").is(':checked')) {
		$("#likedislike").hide();
	}
	$("#${allowCommentsVariableName}").click(function() {
		$("#likedislike").toggle('slow');
	});
});
</script>


