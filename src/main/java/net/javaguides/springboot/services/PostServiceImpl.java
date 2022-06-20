package net.javaguides.springboot.services;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.javaguides.springboot.model.Post;
import net.javaguides.springboot.model.SubmissionPost;
import net.javaguides.springboot.repositories.PostRepository;


@Service
public class PostServiceImpl implements PostService{
	
	  // Create ObjectMapper object.
    ObjectMapper mapper = new ObjectMapper();
    

	private Queue<SubmissionPost> submittedQueue = new LinkedList<>();
	private Queue<SubmissionPost> latest = new LinkedList<>();
	private Queue<SubmissionPost> jsonfile = new LinkedList<>();
	
	private PostRepository postRepository;
	
	
	
	Runnable task =() -> save();
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	
	public PostServiceImpl(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}
	
	
	
	
	@Override
	public Queue<SubmissionPost> saveSubmissionPost(Long id,String text, Long replyId,String photo) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		SubmissionPost submittedPost = new SubmissionPost(id,text, replyId,photo);
		
		submittedPost.setDate(new Date());
		submittedQueue.add(submittedPost);
		jsonfile.add(submittedPost);

		
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		   String json = mapper.writeValueAsString(jsonfile);
		   try {
	            FileWriter file = new FileWriter("posts.json");
	            file.write(json.toString());
	            file.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		   
		if(submittedQueue.size()<=5)
			executor.schedule(task, 1, TimeUnit.MINUTES);
		else if(submittedQueue.size()<=10)
			executor.schedule(task, 2, TimeUnit.MINUTES);
		else
			executor.schedule(task, 3, TimeUnit.MINUTES);
		return submittedQueue;	
		 
	}
	
	@Override
	public Queue<SubmissionPost> saveSubmissionPost(Long id,String text, Long replyId) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		SubmissionPost submittedPost = new SubmissionPost(id,text, replyId);
		
		submittedPost.setDate(new Date());
		submittedQueue.add(submittedPost);
		jsonfile.add(submittedPost);

		
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		   String json = mapper.writeValueAsString(jsonfile);
		   try {
	            FileWriter file = new FileWriter("posts.json");
	            file.write(json.toString());
	            file.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		   
		if(submittedQueue.size()<=5)
			executor.schedule(task, 1, TimeUnit.MINUTES);
		else if(submittedQueue.size()<=10)
			executor.schedule(task, 2, TimeUnit.MINUTES);
		else
			executor.schedule(task, 3, TimeUnit.MINUTES);
		return submittedQueue;	
		 
	}

	@Override
	public List<Post> findAll(String keyword, Long id, Long replyId){
		List<Post> list = postRepository.findBy(keyword, id, replyId);
		Collections.reverse(list);
		return list;
	}


	@Override
	public List<Post> findByDate(Date date) {
		// TODO Auto-generated method stub
		List<Post> list = postRepository.findByDate(date);
		Collections.reverse(list);
		return list;
	}


	@Override
	public List<Post> findByDateWithoutTime(Date date) {
		// TODO Auto-generated method stub
		List<Post> list = postRepository.findByDateWithoutTime(date);
		Collections.reverse(list);
		return list;
	}
	

	@Override
	public Queue<SubmissionPost> listAllSubmittedPost() {
		// TODO Auto-generated method stub
		return submittedQueue;
	}

	@Scheduled(fixedRate = 60000)
	@Override
	public void save() {
		// TODO Auto-generated method stub
		while(!submittedQueue.isEmpty()) {
			SubmissionPost submittedPost = submittedQueue.poll();
			Post post = new Post(submittedPost.getId(), submittedPost.getText(),submittedPost.getReplyId(),submittedPost.getDate(),submittedPost.getPhotos());
			postRepository.save(post);
		}
	}


	@Override
	public List<Post> listAllPost() {
		// TODO Auto-generated method stub
		return postRepository.findAll(Sort.by(Sort.Direction.DESC, "Id"));
	}


	@Override
	public Post get(long id) {
		Optional<Post> optional = postRepository.findById(id);
		Post post = null;
		if (optional.isPresent()) {
			post = optional.get();
		} else {
			throw new RuntimeException(" Post not found for id : " + id);
		}
		return post;
	}


	
	@Override
	public void deletePosted(long id) {
		while(!postRepository.findByReplyId(id).isEmpty()) {
			deletePosted(postRepository.findByReplyId(id).get(0).getId());
		}
		postRepository.deleteById(id);
	}
 

	@Override
	public void remove(long id) {
		  Queue<SubmissionPost> ref = new LinkedList<>();
		  int s = submittedQueue.size();
		    int cnt = 0;
		 // Finding the value to be removed
		    while (!submittedQueue.isEmpty() && submittedQueue.peek().getId()!= id) {
		        ref.add(submittedQueue.peek());
		        submittedQueue.remove();
		        cnt++;
		    }
		    // If element is not found
		    if (submittedQueue.isEmpty()) {
		        System.out.print("element not found!!" +"\n");
		        while (!ref.isEmpty()) {
		 
		            // Pushing all the elements back into q
		        	submittedQueue.add(ref.peek());
		            ref.remove();
		        }
		    }
		    // If element is found
		    else {
		    	submittedQueue.remove();
		        while (!ref.isEmpty()) {
		 
		            // Pushing all the elements back into q
		        	submittedQueue.add(ref.peek());
		            ref.remove();
		        }
		        int k = s - cnt - 1;
		        while (k-- >0) {
		 
		            // Pushing elements from front of q to its back
		            SubmissionPost p = submittedQueue.peek();
		            submittedQueue.remove();
		            submittedQueue.add(p);
		        }
		    }
		}




	@Override
	public Queue<SubmissionPost> view() {
		Queue<SubmissionPost> temp = new LinkedList<>();
		while(latest.size()!=0) {
			latest.remove();
		}
		if(submittedQueue.isEmpty()) {
			return null;
		}
		else {
			while(submittedQueue.size()!=1) {
				temp.add(submittedQueue.remove());
			}
		    	latest.add(submittedQueue.peek());
		}
		while(temp.size()!=0) {
		submittedQueue.add(temp.remove());
		}
		    return latest;
		
	}
	
	 





}
		    
	
	
	
	


