package net.javaguides.springboot.model;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
	
	@Id
	private Long id;
	private String text;
	private Long replyId;
	private Date date = new Date();

	@Column(nullable = true, length = 64)
    private String photos;
	

	public Post(Long id, String text, Long replyId, Date date, String photos) {
		super();
		this.id = id;
		this.text = text;
		this.replyId = replyId;
		this.date = date;
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



	public Post(String text, Long replyId) {
		super();
		this.text = text;
		this.replyId = replyId;
	}




	public Post() {
		
	}
	
	public Post(Long id, String text, Long replyId, Date date) {
		super();
		this.id = id;
		this.text = text;
		this.replyId = replyId;
		this.date = date;
	}

	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

