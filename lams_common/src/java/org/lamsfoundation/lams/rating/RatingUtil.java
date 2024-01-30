package org.lamsfoundation.lams.rating;

public class RatingUtil {
    
    public static String constructRatingTagDisabled(String averageRating, String averageRatingLabel) {
	int averageRatingFloor = (int) Math.floor(Double.parseDouble(averageRating));
	String dataRating = String.valueOf(averageRatingFloor) + (Double.parseDouble(averageRating) % 1 >= 0.5 ? ".5" : "");
	
	String starString = 
		"<div class='extra-controls-inner'>" +
			"<div class='starability-holder'>" +
//				<c:if test="${not hideCriteriaTitle}">
//					<legend class="text-muted fw-bold">
//						${criteriaDto.ratingCriteria.title}
//					</legend>
//				</c:if>
				"<div class='starability starability-result' data-rating='" + dataRating + "'>" +
				"</div>" +
					
				"<div class='starability-caption'>" +
					averageRatingLabel +
				"</div>" +
			"</div>" +
		"</div>";
	return starString;
    }

}
