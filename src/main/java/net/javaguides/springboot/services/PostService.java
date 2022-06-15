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

	
	Queue<SubmissionPost> saveSubmissionPost(String text, Long replyId) throws JsonProcessingException, FileNotFoundException, IOException, ParseException;
	void save();
	void remove(long id);
	Queue<SubmissionPost> listAllSubmittedPost();
	List<Post> listAllPost();
	Post get(long id);
	Queue<SubmissionPost> view();
	List<Post> findAll(String keyword, Long id, Long replyId);
	List<Post> findByDate(Date date);
	List<Post> findByDateWithoutTime(Date date);
	void deletePosted(long id);

}
