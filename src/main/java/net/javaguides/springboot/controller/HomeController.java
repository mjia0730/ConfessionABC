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
		model.addAttribute("posts", postService.listAllSubmittedPost());
		return "admin.html";
	}
	
	//Handler method wchich will handle http get request
	//view the original post of replyid on the replyId.html page
	@GetMapping("replyId/{replyId}")
	private String replypage(@PathVariable (value = "replyId") long replyId, Model model) {
		//find the original post of replyid by calling method and display its original post
		Post post = postService.get(replyId);		
		model.addAttribute("posts", post);
		//direct to the page of view reply id
		return "replyId.html";
	}
	
	//Handler method wchich will handle http get request
	@GetMapping("/view")
	private String viewpage( Model model) {
		//call the method of view confession id and submission date and time
		model.addAttribute("latest", postService.view());
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
            	return "redirect:/create?error";
        }
		
		//check for spam text
		String[] str = text.split(" ");
		java.util.Stack<String> subStr =  new java.util.Stack<>();
		for(int i=1; i<str.length; i++) {
			subStr.add(str[i]);
		}
		if(subStr.size()>1) {
			while(!subStr.isEmpty()) {
				if(subStr.peek().equals(str[0]))
					subStr.pop();
				else
					break;
			}
			if(subStr.isEmpty())
				return "redirect:/?error";
		}
				
		//check for bad tone
		String[] badTone = {"stupid","idiot","fuck", "nigga", "bastard", "dick head", "bitch", "asshole"};
		for(int i=0; i<badTone.length; i++) {
			if(text.toLowerCase().contains(badTone[i].toLowerCase()))
				return "redirect:/?error";
		}
		
		//add photo
		 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		 if(fileName.isEmpty()) {
			 submissionPost.setId();
			 Long id = submissionPost.getId();
			 postService.saveSubmissionPost(id,text,replyid);
		 }
		 
		 else {
			 submissionPost.setId();
			 Long id = submissionPost.getId();
		     String uploadDir = "user-photos/" + id;
		 
		     FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		         
		        
		     postService.saveSubmissionPost(id,text,replyid,fileName);
		 }
		
		return "redirect:/?success";
	}
	
	@GetMapping("/deletePosted/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		// call delete post method 
		this.postService.deletePosted(id);
		return "redirect:/adminHome";
	}
	
	@GetMapping("/deletePending/{id}")
	public String remove(@PathVariable (value = "id") long id) {
		
		// call remove pending post method 
		this.postService.remove(id);
		return "redirect:/admin";
	}
	
	@GetMapping("/reply/{replyId}")
	public String reply(@PathVariable (value = "replyId") long replyId) {		
		// call remove pending post method 
		this.postService.get(replyId);		
		return "redirect:/replyId/{replyId}";
	}
	
	
	
	

	
}
