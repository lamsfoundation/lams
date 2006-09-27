package org.lamsfoundation.lams.tool.survey.dto;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.util.SurveyOptionComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;



public class AnswerDTO extends SurveyQuestion{

	private static final int SHORT_TITLE_LENGTH = 60;
	
	//***********************************************
	//DTO fields:
	//this is DTO field. For answer, which is user and session level. For question, which is content level.
	private SurveyAnswer answer;
	//Open text entry response percentage  if this question has open text entry.
	private double openResponse; 
	private String openResponseFormatStr;
	private int openResponseCount; 
	
	static Logger logger = Logger.getLogger(AnswerDTO.class.getName());
	public AnswerDTO(SurveyQuestion question){
		try {
			PropertyUtils.copyProperties(this, question);
		} catch (Exception e) {
			logger.error("Error occurs during creating AnswerDTO");
		}
		
		//clone options 
		Set<SurveyOption> optList = question.getOptions();
		if(optList != null){
			SortedSet<SurveyOption> newOptions = new TreeSet<SurveyOption>(new SurveyOptionComparator());
			for (SurveyOption option : optList) {
				SurveyOption newOption = (SurveyOption) option.clone();
				//clone does not copy the UID, here copy it back
				newOption.setUid(option.getUid());
				newOptions.add(newOption);
			}
			this.setOptions(newOptions);
		}
		
		String desc = this.getDescription();
		desc = desc.replaceAll("<(.|\n)*?>", "");
		this.setShortTitle(StringUtils.abbreviate(desc,SHORT_TITLE_LENGTH));

	}
	//****************************************************************
	// DTO fields
	//****************************************************************
	public SurveyAnswer getAnswer() {
		return answer;
	}
	public void setAnswer(SurveyAnswer answer) {
		this.answer = answer;
	}
	public double getOpenResponse() {
		return openResponse;
	}
	public void setOpenResponse(double openResponse) {
		this.openResponse = openResponse;
	}
	public int getOpenResponseCount() {
		return openResponseCount;
	}
	public void setOpenResponseCount(int openResponseCount) {
		this.openResponseCount = openResponseCount;
	}
	public String getOpenResponseFormatStr() {
		return openResponseFormatStr;
	}
	public void setOpenResponseFormatStr(String openResponseFormatStr) {
		this.openResponseFormatStr = openResponseFormatStr;
	}

}
