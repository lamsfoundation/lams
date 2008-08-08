package org.lamsfoundation.lams.tool.daco.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionSummaryDTO implements Cloneable {
	private List<QuestionSummarySingleAnswerDTO> userSummary = new ArrayList<QuestionSummarySingleAnswerDTO>();
	private List<QuestionSummarySingleAnswerDTO> groupSummary = new ArrayList<QuestionSummarySingleAnswerDTO>();
	private Long questionUid;

	public void addGroupSummarySingleAnswer(int number, QuestionSummarySingleAnswerDTO singleAnswer) {
		while (number >= groupSummary.size()) {
			groupSummary.add(null);
		}
		groupSummary.set(number, singleAnswer);
	}

	public QuestionSummarySingleAnswerDTO getGroupSummarySingleAnswer(int number) {
		if (groupSummary == null || number >= groupSummary.size()) {
			return null;
		}
		return groupSummary.get(number);
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

	public List<QuestionSummarySingleAnswerDTO> getGroupSummary() {
		return groupSummary;
	}

	public void setGroupSummary(List<QuestionSummarySingleAnswerDTO> groupSummary) {
		this.groupSummary = groupSummary;
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