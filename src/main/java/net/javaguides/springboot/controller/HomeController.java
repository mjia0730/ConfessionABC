package net.javaguides.springboot.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.javaguides.springboot.model.FileUploadUtil;
import net.javaguides.springboot.model.Post;
import net.javaguides.springboot.model.SubmissionPost;
import net.javaguides.springboot.services.PostService;


@Controller
public class HomeController {

	@Autowired
	private PostService postService;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat formatterWithoutTime = new SimpleDateFormat("yyyy-MM-dd");
	

	public HomeController(PostService postService) {
		super();
		this.postService = postService;
	}
	
	@GetMapping(path= {"", "/search"})
	private String index(Model model, String keyword){
		if(keyword!=null){
			try {	//check see whether the keyword entered is a date with time or not
				model.addAttribute("posts", postService.findByDate(formatter.parse(keyword)));	
			}catch(ParseException e) {
				try {	//check see whether the keyword entered is a date without time or not
					model.addAttribute("posts", postService.findByDateWithoutTime(formatterWithoutTime.parse(keyword)));
				}catch(ParseException e1) {
					try {	//if the keyword is not date, check if the keyword is integer/long 
						model.addAttribute("posts", postService.findAll(keyword, Long.valueOf(keyword), Long.valueOf(keyword)));
			        }catch (NumberFormatException ex) { //if the keyword is neither date nor long, the keyword will be string
			        	model.addAttribute("posts", postService.findAll(keyword, null, null));
			        }
				}
			}
		
		}
		else	//if keyword is null, all posts will showed
			model.addAttribute("posts", postService.listAllPost());
		//list out all the IDs available that the user can replied to
	 	model.addAttribute("statusDropdown", postService.listAllPost());
		return "index.html";
	}
	

	@GetMapping(path= {"/adminHome", "/adminHomeSearch"})
	private String homePage(Model model, String keyword){
		if(keyword!=null){
			try {
				model.addAttribute("posts", postService.findByDate(formatter.parse(keyword)));
			}catch(ParseException e) {
				try {
					model.addAttribute("posts", postService.findByDateWithoutTime(formatterWithoutTime.parse(keyword)));
				}catch(ParseException e1) {
					try {
						model.addAttribute("posts", postService.findAll(keyword, Long.valueOf(keyword), Long.valueOf(keyword)));
			        }catch (NumberFormatException ex) {
			        	model.addAttribute("posts", postService.findAll(keyword, null, null));
			        }
				}
			}
		
		}
		else
			model.addAttribute("posts", postService.listAllPost());
		return "adminHome.html";
	}
	
	@GetMapping("/admin")
	private String listPendingPost(Model model) {
		//list all the pending posts
		model.addAttribute("posts", postService.listAllSubmittedPost());
		return "admin.html";
	}
	
	//Handler method which will handle http get request
	//view the original post of replyid on the replyId.html page
	@GetMapping("replyId/{replyId}")
	private String replypage(@PathVariable (value = "replyId") long replyId, Model model) {
		//find the original post of replyid by calling method and display its original post
		Post post = postService.get(replyId);		
		model.addAttribute("posts", post);
		//direct to the page of view reply id
		return "replyId.html";
	}
	
	@GetMapping("adminReplyId/{replyId}")
	private String adminReplypage(@PathVariable (value = "replyId") long replyId, Model model) {
		//find the original post of replyid by calling method and display its original post
		Post post = postService.get(replyId);		
		model.addAttribute("posts", post);
		//direct to the page of view reply id
		return "adminReplyId.html";
	}
	
	//Handler method which will handle http get request
	@GetMapping("/view")
	private String viewpage( Model model) {
		//call the view method to view the confession id and submission date and time after posting
		model.addAttribute("latest", postService.view());
		//return to the page of view confession post id
		return "view.html";
	}
	
	

	
	//handler method which will handle http post request
	@PostMapping("")
	public String postCreation(HttpServletRequest request, Model model,SubmissionPost submissionPost,
            @RequestParam("image") MultipartFile multipartFile) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		
		Long replyid;
		if(request.getParameter("replyId").equals(""))
			replyid = null;
		else
			replyid = Long.valueOf(request.getParameter("replyId"));
		String text = request.getParameter("text");
		
		//check if in 5 mins time did user repeatedly post same confession
		for (SubmissionPost item: postService.listAllSubmittedPost()) {
            if(text.equals(item.getText()))
            	return "redirect:/?errorRepeat";
        }
		
		//check for spam text
		String[] str = text.split(" "); //split the long text into array of words	 
		for(int i=0; i<str.length; i++) {
			int count=0;
			for(int j=0; j<str.length; j++) {
				if(j==i)
					continue;
				else {
					if(str[j].equals(str[i]))
						count++;
				}
				if(count>=3)
					return "redirect:/?errorSame";
			}
		}

				
		//check for bad tone
		String[] badTone = {"stupid","idiot","fuck", "nigga", "bastard", "dick head", "bitch", "asshole"};
		for(int i=0; i<badTone.length; i++) {
			if(text.toLowerCase().contains(badTone[i].toLowerCase()))
				return "redirect:/?errorBad";
		}
		
		//add photo file 
		//get the filename of the photo
		 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		//if there is no photo file uploaded, add the id, text, and replyid only into the queue
		 if(fileName.isEmpty()) {
			 submissionPost.setId();
			 Long id = submissionPost.getId();
			 postService.saveSubmissionPost(id,text,replyid);
		 }
		 
		//if there is a photo file uploaded, add the id, text, replyid, filename into the queue
		 else {
			 submissionPost.setId();
			 Long id = submissionPost.getId();
		     String uploadDir = "user-photos/" + id;
		 
		     //call the method in FileUploadUtil.java class to save file 
		     FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);        
		     postService.saveSubmissionPost(id,text,replyid,fileName);
		 }
		
		//return to the index page of success 
		return "redirect:/?success";
	}
	
	//delete from mysql database
	@GetMapping("/deletePosted/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		// call delete post method 
		this.postService.deletePosted(id);
		return "redirect:/adminHome";
	}
	
	//delete from the pending post queue
	@GetMapping("/deletePending/{id}")
	public String remove(@PathVariable (value = "id") long id) {
		
		// call remove pending post method 
		this.postService.remove(id);
		return "redirect:/admin";
	}
	
	//handler method which will handle http post request
	@GetMapping("/reply/{replyId}")
	public String reply(@PathVariable (value = "replyId") long replyId) {		
		// call get replyid method to find the reply id
		this.postService.get(replyId);	
		//redirect to the http of replyid
		return "redirect:/replyId/{replyId}";
	}
	
	
	
	

	
}
