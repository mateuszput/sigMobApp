
package pl.edu.agh.sigmobapp.json;

import java.util.List;

public class Survey{
	private List<Question> questions;
   	private Number surveyId;
   	private String name;
   	
 	public List<Question> getQuestions(){
		return this.questions;
	}
	public void setQuestions(List<Question> questions){
		this.questions = questions;
	}
 	public Number getSurveyId(){
		return this.surveyId;
	}
	public void setSurveyId(Number surveyId){
		this.surveyId = surveyId;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}
