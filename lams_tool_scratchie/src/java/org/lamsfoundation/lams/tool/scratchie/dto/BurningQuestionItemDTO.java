package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;

/**
 * Holds all non empty burning questions for the specified ScratchieItem.
 */
public class BurningQuestionItemDTO {
    private ScratchieItem scratchieItem;
    private List<BurningQuestionDTO> burningQuestionDtos;

    public ScratchieItem getScratchieItem() {
	return scratchieItem;
    }

    public void setScratchieItem(ScratchieItem scratchieItem) {
	this.scratchieItem = scratchieItem;
    }

    public List<BurningQuestionDTO> getBurningQuestionDtos() {
	return burningQuestionDtos;
    }

    public void setBurningQuestionDtos(List<BurningQuestionDTO> burningQuestionDtos) {
	this.burningQuestionDtos = burningQuestionDtos;
    }
}
