package pl.edu.agh.sigmobapp.utils;

public class SurveyRadioTag {
	private Number answerId;
	private Number questionId;
	private Number surveyId;
	
	public SurveyRadioTag(Number surveyId, Number questionId, Number answerid){
		this.answerId = answerid;
		this.questionId = questionId;
		this.surveyId = surveyId;
	}
	
	public Number getAnswerId(){
		return this.answerId;
	}
	
	public Number getQuestionId(){
		return this.questionId;
	}
	
	public Number getSurveyId(){
		return this.surveyId;
	}
	
}
