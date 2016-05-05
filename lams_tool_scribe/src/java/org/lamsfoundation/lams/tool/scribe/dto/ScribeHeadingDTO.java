package org.lamsfoundation.lams.tool.scribe.dto;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;

public class ScribeHeadingDTO implements Comparable<ScribeHeadingDTO> {

    private static Logger logger = Logger.getLogger(ScribeDTO.class);

    private Long uid;

    private String headingText;

    private Integer displayOrder;

    public ScribeHeadingDTO(ScribeHeading heading) {
	this.uid = heading.getUid();
	this.headingText = heading.getHeadingText();
	this.displayOrder = heading.getDisplayOrder();
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public String getHeadingText() {
	return headingText;
    }

    public void setHeadingText(String headingText) {
	this.headingText = headingText;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    @Override
    public int compareTo(ScribeHeadingDTO o) {

	int returnValue = this.displayOrder.compareTo(o.displayOrder);

	if (returnValue == 0) {
	    logger.debug("compareTo: Different scribeHeadings have equal displayOrder");
	    returnValue = this.uid.compareTo(o.uid);
	}

	return returnValue;
    }

}
