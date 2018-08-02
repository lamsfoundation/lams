package org.lamsfoundation.lams.outcome;

public class OutcomeScaleItem {
    private Long itemId;
    private OutcomeScale scale;
    private Integer value;
    private String name;

    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

    public OutcomeScale getScale() {
	return scale;
    }

    public void setScale(OutcomeScale scale) {
	this.scale = scale;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}