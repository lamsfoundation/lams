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

<div class="checkbox">
	<label>
	<html:checkbox property="${allowCommentsVariableName}" value="1"
		styleId="${allowCommentsVariableName}">
	</html:checkbox>
	<fmt:message key="${allowCommentLabelKey}"/>
	</label>
</div>
<div class="loffset20" id="likedislike">
		<html:radio property="${likeDislikeVariableName}" value="false" />&nbsp;<fmt:message key="${likeOnlyCommentLabelKey}" /> &nbsp;
		<html:radio property="${likeDislikeVariableName}" value="true" />&nbsp;<fmt:message key="${likeDislikeLabelKey}" /> 
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


