
package pl.edu.agh.sigmobapp.json;

public class TaskShort{
   	private Number id;
   	private String type;

 	public Number getId(){
		return this.id;
	}
	public void setId(Number taskId){
		this.id = taskId;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
