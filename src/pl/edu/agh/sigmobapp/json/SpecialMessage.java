package pl.edu.agh.sigmobapp.json;

import java.util.List;

public class SpecialMessage {
	private String title;
	private String body;
	private List<SpecialMessageAttachment> attachments;

	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getBody(){
		return this.body;
	}
	public void setBody(String body){
		this.body = body;
	}
	
	public List<SpecialMessageAttachment> getAttachments(){
		return this.attachments;
	}
	public void setAttachments(List<SpecialMessageAttachment> attachments){
		this.attachments = attachments;
	}
}
