package net.javaguides.springboot.configure;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //marks the class as a source of bean definitions.
public class MvcConfig implements WebMvcConfigurer{

	//MVC = model-view-controller
	//WebMvcConfigurer = will set up the basic support we need for an MVC project, 
	//such as registering controllers and mappings, type converters, validation support, message converters and exception handling
	
	//This handler is invoked for requests that match one of the specified URL path patterns.
	//such as "/static/**" or "/css/{filename:\\w+\\.css}" 
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", registry);
    }
	
    //To display images in browsers, we need to expose the directory containing the uploaded files so the web browsers can access. 
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
         
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
         
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
}
