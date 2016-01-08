<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<%@ attribute name="allowCommentsVariableName" required="false" rtexprvalue="true"%>
<%@ attribute name="allowCommentLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="likeDislikeVariableName" required="false" rtexprvalue="true"%>
<%@ attribute name="likeOnlyCommentLabelKey" required="false" rtexprvalue="true"%>
<%@ attribute name="likeDislikeLabelKey" required="false" rtexprvalue="true"%>

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

<p class="small-space-top">
	<html:checkbox property="${allowCommentsVariableName}" value="1"
		styleClass="noBorder" styleId="${allowCommentsVariableName}">
		&nbsp;<fmt:message key="${allowCommentLabelKey}" />
	</html:checkbox>
</p>
	<div style="padding-left: 20px" id="likedislike">
	<p>
		<html:radio property="${likeDislikeVariableName}" value="false" /><fmt:message key="${likeOnlyCommentLabelKey}" /> &nbsp;
		<html:radio property="${likeDislikeVariableName}" value="true" /><fmt:message key="${likeDislikeLabelKey}" /> 
	</p>
	</div>

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


