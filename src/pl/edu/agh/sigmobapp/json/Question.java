package pl.edu.agh.sigmobapp.json;

import java.util.List;

public class Question {
	private Number questionId;
   	private String question;
   	private List<Answer> answers;
   	
   	public Number getQuestionId(){
		return this.questionId;
	}
	public void setQuestionId(Number questionId){
		this.questionId = questionId;
	}
	public String getQuestion(){
		return this.question;
	}
	public void setQuestion(String question){
		this.question = question;
	}
	
   	public List<Answer> getAnswers(){
		return this.answers;
	}
	public void setAnswers(List<Answer> answers){
		this.answers = answers;
	}
 	
	              
}
