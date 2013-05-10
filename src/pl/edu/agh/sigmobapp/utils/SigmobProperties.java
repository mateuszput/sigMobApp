package pl.edu.agh.sigmobapp.utils;


public class SigmobProperties {
	private static final SigmobProperties sigmobProperties = new SigmobProperties();
	
	private String hostName;
	private String apiName;
	
	
    private SigmobProperties() {}
 
    public static SigmobProperties getInstance() {
        return sigmobProperties;
    }
    
    public void setHostName(String hostName){
    	this.hostName = hostName;
    }
    
    public String getHostName(){
    	return this.hostName;
    }
    
    public void setApiName(String apiName){
    	this.apiName = apiName;
    }
    
    public String getApiName(){
    	return this.apiName;
    }

    public String getHostAndApi(){
    	return this.hostName + this.apiName;
    }
    
}
