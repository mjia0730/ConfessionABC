package net.javaguides.springboot.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SubmissionPost {
	private Long id;
	private String text;
	private Long replyId;
	private Date date;		
	
	public SubmissionPost(String text, Long replyId) {
		super();
		this.text = text;
		this.replyId = replyId;
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
	
		this.id = count;
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
