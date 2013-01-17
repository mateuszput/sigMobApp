
package pl.edu.agh.sigmobapp.json;

public class TaskShort{
   	private Number taskId;
   	private String type;

 	public Number getTaskId(){
		return this.taskId;
	}
	public void setTaskId(Number taskId){
		this.taskId = taskId;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
