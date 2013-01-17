
package pl.edu.agh.sigmobapp.json;

import java.util.List;

public class TasksList{
   	private List<TaskShort> tasks;

 	public List<TaskShort>  getTasks(){
		return this.tasks;
	}
	public void setTasks(List<TaskShort> tasks){
		this.tasks = tasks;
	}
}
