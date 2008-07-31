package org.lamsfoundation.lams.tool.daco.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionSummaryDTO implements Cloneable {
	private List<QuestionSummarySingleAnswerDTO> userSummary = new ArrayList<QuestionSummarySingleAnswerDTO>();
	private List<QuestionSummarySingleAnswerDTO> allSummary = new ArrayList<QuestionSummarySingleAnswerDTO>();
	private Long questionUid;

	public void addAllSummarySingleAnswer(int number, QuestionSummarySingleAnswerDTO singleAnswer) {
		while (number >= allSummary.size()) {
			allSummary.add(null);
		}
		allSummary.set(number, singleAnswer);
	}

	public QuestionSummarySingleAnswerDTO getAllSummarySingleAnswer(int number) {
		if (allSummary == null || number >= allSummary.size()) {
			return null;
		}
		return allSummary.get(number);
	}

	public void addUserSummarySingleAnswer(int number, QuestionSummarySingleAnswerDTO singleAnswer) {
		while (number >= userSummary.size()) {
			userSummary.add(null);
		}
		userSummary.set(number, singleAnswer);
	}

	public QuestionSummarySingleAnswerDTO getUserSummarySingleAnswer(int number) {
		if (userSummary == null || number >= userSummary.size()) {
			return null;
		}
		return userSummary.get(number);
	}

	public Long getQuestionUid() {
		return questionUid;
	}

	public void setQuestionUid(Long questionUid) {
		this.questionUid = questionUid;
	}

	public List<QuestionSummarySingleAnswerDTO> getUserSummary() {
		return userSummary;
	}

	public void setUserSummary(List<QuestionSummarySingleAnswerDTO> userSummary) {
		this.userSummary = userSummary;
	}

	public List<QuestionSummarySingleAnswerDTO> getAllSummary() {
		return allSummary;
	}

	public void setAllSummary(List<QuestionSummarySingleAnswerDTO> allSummary) {
		this.allSummary = allSummary;
	}

	@Override
	public Object clone() {
		QuestionSummaryDTO clone = null;
		try {
			clone = (QuestionSummaryDTO) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (userSummary != null) {
			List<QuestionSummarySingleAnswerDTO> list = new ArrayList<QuestionSummarySingleAnswerDTO>(userSummary.size());
			for (QuestionSummarySingleAnswerDTO singleAnswer : userSummary) {
				list.add((QuestionSummarySingleAnswerDTO) singleAnswer.clone());
			}
			Iterator<QuestionSummarySingleAnswerDTO> iter = userSummary.iterator();
			clone.userSummary = list;
		}
		return clone;
	}
}