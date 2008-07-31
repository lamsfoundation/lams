/**
 * 
 */
package org.lamsfoundation.lams.tool.daco.dto;

public class QuestionSummarySingleAnswerDTO {
	private String answer;
	private String sum;
	private String average;
	private String count;

	public QuestionSummarySingleAnswerDTO(String answer, String sum, String average, String count) {
		this.answer = answer;
		this.sum = sum;
		this.average = average;
		this.count = count;
	}

	public QuestionSummarySingleAnswerDTO() {

	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
}