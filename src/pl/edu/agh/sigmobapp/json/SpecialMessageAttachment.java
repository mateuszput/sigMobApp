package pl.edu.agh.sigmobapp.json;

public class SpecialMessageAttachment {
	private String type;
	private Number fileId;
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public Number getFileId(){
		return this.fileId;
	}
	
	public void setFileId(Number fileId){
		this.fileId = fileId;
	}
	
}
