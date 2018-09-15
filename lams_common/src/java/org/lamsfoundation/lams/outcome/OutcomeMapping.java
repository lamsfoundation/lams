package org.lamsfoundation.lams.outcome;

import java.io.Serializable;

public class OutcomeMapping implements Serializable {
    private static final long serialVersionUID = -2195345501533401085L;

    private Long mappingId;
    private Outcome outcome;
    private Long lessonId;
    private Long toolContentId;
    private Long itemId;

    public Long getMappingId() {
	return mappingId;
    }

    public void setMappingId(Long mappingId) {
	this.mappingId = mappingId;
    }

    public Outcome getOutcome() {
	return outcome;
    }

    public void setOutcome(Outcome outcome) {
	this.outcome = outcome;
    }

    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

}