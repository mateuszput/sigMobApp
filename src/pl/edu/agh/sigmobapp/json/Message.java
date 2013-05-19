package pl.edu.agh.sigmobapp.json;

public class Message {
	private Number messageId;
   	private String title;
   	private String body;
   	
	public Number getMessageId() {
		return messageId;
	}
	public void setMessageId(Number messageId) {
		this.messageId = messageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
