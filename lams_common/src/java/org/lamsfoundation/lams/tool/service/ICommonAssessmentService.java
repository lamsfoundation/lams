package org.lamsfoundation.lams.tool.service;

import java.util.Collection;

import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;

public interface ICommonAssessmentService {
    
    /**
     * Returns answers learners left for VSA questions in Assessment activity (together with according confidence
     * levels, if such option is turned on in Assessment). Currently only Assessment tool is capable of producing VSA
     * answers.
     * 
     * @param toolSessionId
     * @return
     */
    Collection<VsaAnswerDTO> getVsaAnswers(Long toolSessionId);

}
