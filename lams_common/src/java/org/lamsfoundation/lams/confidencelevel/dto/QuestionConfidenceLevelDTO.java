package org.lamsfoundation.lams.confidencelevel.dto;

import java.util.Collection;

public class QuestionConfidenceLevelDTO {

    //common properties
    private Long questionUid;
    private Collection<ConfidenceLevelDTO> confidenceLevelDtos;

    public QuestionConfidenceLevelDTO() {
    }

    public Long getQuestionUid() {
	return questionUid;
    }

    public void setQuestionUid(Long itemId) {
	this.questionUid = itemId;
    }

    public Collection<ConfidenceLevelDTO> getConfidenceLevelDtos() {
	return confidenceLevelDtos;
    }

    public void setConfidenceLevelDtos(Collection<ConfidenceLevelDTO> criteriaDtos) {
	this.confidenceLevelDtos = criteriaDtos;
    }
}
