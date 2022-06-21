package net.javaguides.springboot.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;


import net.javaguides.springboot.model.Post;
import net.javaguides.springboot.model.SubmissionPost;

public interface PostService {

	//save pending post with image into queue
	Queue<SubmissionPost> saveSubmissionPost(Long id,String text, Long replyId, String photo) throws JsonProcessingException, FileNotFoundException, IOException, ParseException;
	
	//save pending post with no image into queue
	Queue<SubmissionPost> saveSubmissionPost(Long id,String text, Long replyId) throws JsonProcessingException, FileNotFoundException, IOException, ParseException;
	
	//save pending post into database 
	void save();
	
	//remove pending post from queue
	void remove(long id);
	
	//list all pending posts in queue
	Queue<SubmissionPost> listAllSubmittedPost();
	
	//list all posts in database
	List<Post> listAllPost();
	
	//return the original post that are replied
	Post get(long id);
	
	//for the user to view the id and date when the user submitted a new post
	Queue<SubmissionPost> view();
	
	//list posts with particular keyword in text, id or replyId
	List<Post> findAll(String keyword, Long id, Long replyId);
	
	//list posts with particular date and time
	List<Post> findByDate(Date date);
	
	//list posts with particular date
	List<Post> findByDateWithoutTime(Date date);
	
	//delete post
	void deletePosted(long id);

}
