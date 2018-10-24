package org.lamsfoundation.lams.tool.mc.dto;

/**
 * An output for a tool.
 */
public class ToolOutputDTO {

    private Integer userId;
    private Integer mark;

    public Integer getMark() {
	return mark;
    }

    public void setMark(Integer mark) {
	this.mark = mark;
    }
    
    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }
}

