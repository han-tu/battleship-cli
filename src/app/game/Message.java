package app.game;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * Class Message sebagai objek yang akan dikirimkan melalui socket
	 */
	private static final long serialVersionUID = 123;
	private String sender;			// Message Sender
	private String receiver;		// Message Receiver
	private String text;			// Message Text
	private String req;
    
    /*
     * Request Type List:
     * 
     * 
     * 
     */

    public Message() {
    	
    }
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getReceiver() {
    	return receiver;
    }
    
    public void setReceiver(String receiver) {
    	this.receiver = receiver;
    }

	public void setRequest(String req) {
		this.req = req;
	}
	
	public String getRequest() {
		return this.req;
	}
}
