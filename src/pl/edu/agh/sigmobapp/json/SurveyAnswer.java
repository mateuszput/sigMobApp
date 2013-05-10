
package pl.edu.agh.sigmobapp.json;

import java.util.List;

public class SurveyAnswer{
   	private Number surveyId;
   	private List<ChoosenAnswer> chosenAnswers;
   	
 	public Number getSurveyId(){
		return this.surveyId;
	}
	public void setSurveyId(Number surveyId){
		this.surveyId = surveyId;
	}
	public List<ChoosenAnswer> getChosenAnswers(){
		return this.chosenAnswers;
	}
	public void setChosenAnswers(List<ChoosenAnswer> chosenAnswers){
		this.chosenAnswers = chosenAnswers;
	}
	
}
