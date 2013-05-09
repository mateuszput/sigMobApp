
package pl.edu.agh.sigmobapp.json;

import java.util.List;

// TODO: nie uzywana klasa?
public class Task{
   	private List<Answers> answers;
   	private String question;
   	private Number surveyId;

 	public List<Answers> getAnswers(){
		return this.answers;
	}
	public void setAnswers(List<Answers> answers){
		this.answers = answers;
	}
 	public String getQuestion(){
		return this.question;
	}
	public void setQuestion(String question){
		this.question = question;
	}
 	public Number getSurveyId(){
		return this.surveyId;
	}
	public void setSurveyId(Number surveyId){
		this.surveyId = surveyId;
	}
}
