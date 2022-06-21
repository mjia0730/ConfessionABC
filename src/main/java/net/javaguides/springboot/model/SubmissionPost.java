package net.javaguides.springboot.model;

import java.beans.Transient;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//pending posts
public class SubmissionPost {
	private Long id;
	private String text;
	private Long replyId;
	private Date date;	
	private Long count= Long.valueOf(0);
	
	public SubmissionPost(String text, Long replyId) {
		super();
		this.text = text;
		this.replyId = replyId;
	}
	
	@Column(nullable = true, length = 64)
    private String photos;
	
	
	public SubmissionPost(Long id, String text, Long replyId) {
		super();
		this.id = id;
		this.text = text;
		this.replyId = replyId;
	}

	public SubmissionPost(Long id, String text, Long replyId, String photos) {
		super();
		this.id = id;
		this.text = text;
		this.replyId = replyId;
		this.photos = photos;
	}

	@Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + photos;
    }

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	
	
	public SubmissionPost() {
		super();
	}


	public Long getId() {
		return id;
	}

	public SubmissionPost(Long id, Date date) {
		super();
		this.id = id;
		this.date = date;
	}

	public SubmissionPost(Long id) {
		super();
		this.id = id;
	}

	public void setId() throws FileNotFoundException, IOException, ParseException {
		/*
		ObjectMapper objectMapper = new ObjectMapper();
		long count;
		File file = new File("posts.json");
		JsonNode str = objectMapper.readTree(file);
		if(str.isEmpty()) {
			count = 1;
		}
		else {
		count = str.size()+1;
		}		
		*/
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader("posts.json");
		//Read JSON file
        Object obj = jsonParser.parse(reader);

        JSONArray List = (JSONArray) obj;
        count = Long.valueOf(List.size());
        if(!List.isEmpty()) {
	        String[] post = List.get(0).toString().split(",");
	        for(int i=0; i<post.length; i++) {
	        	System.out.println(post[i]);
	        	if(post[i].contains("id")) {
	        		this.id = Long.valueOf(post[i].substring(5)) + count;
	        		break;
	        	}
	        }
        }
        else
        	this.id = Long.valueOf(1);
	}	
		
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
