package org.lamsfoundation.lams.tool.daco.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionSummary implements Cloneable {
	public List<String[]> sum = new ArrayList<String[]>();

	public List<String[]> userSummary = new ArrayList<String[]>();
	public List<String[]> allSummary = new ArrayList<String[]>();
	public Long questionUid;

	public void addAllSummaryRow(int number, String[] summary) {
		if (allSummary == null) {
			allSummary = new ArrayList<String[]>();
		}
		while (number >= allSummary.size()) {
			allSummary.add(null);
		}
		allSummary.set(number, summary);
	}

	public String[] getAllSummaryRow(int number) {
		if (allSummary == null || number >= allSummary.size()) {
			return null;
		}
		return allSummary.get(number);
	}

	public void addUserSummaryRow(int number, String[] summary) {
		if (userSummary == null) {
			userSummary = new ArrayList<String[]>();
		}
		while (number >= userSummary.size()) {
			userSummary.add(null);
		}
		userSummary.set(number, summary);
	}

	public String[] getUserSummaryRow(int number) {
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

	public List<String[]> getUserSummary() {
		return userSummary;
	}

	public void setUserSummary(List<String[]> userSummary) {
		this.userSummary = userSummary;
	}

	public List<String[]> getAllSummary() {
		return allSummary;
	}

	public void setAllSummary(List<String[]> allSummary) {
		this.allSummary = allSummary;
	}

	@Override
	public Object clone() {
		QuestionSummary clone = null;
		try {
			clone = (QuestionSummary) super.clone();
		}
		catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (userSummary != null) {

			Iterator<String[]> iter = userSummary.iterator();
			List<String[]> list = new ArrayList<String[]>(userSummary.size());
			while (iter.hasNext()) {
				list.add(iter.next().clone());
			}
			clone.userSummary = list;
		}
		return clone;
	}

	public List<String[]> getSum() {
		return sum;
	}

	public void setSum(List<String[]> sum) {
		this.sum = sum;
	}
}