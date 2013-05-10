package pl.edu.agh.sigmobapp.json;

public class ChoosenAnswer {

	private Number questionId;
	private Number answerId;
	
	public Number getQuestionId(){
		return this.questionId;
	}
	public void setQuestionId(Number questionId){
		this.questionId = questionId;
	}
	
	public Number getAnswerId(){
		return this.answerId;
	}
	public void setAnswerId(Number answerId){
		this.answerId = answerId;
	}
}
