package net.javaguides.springboot.repositories;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.model.Post;

//Do not implement this interface.
//Spring data will create an implementation of it with @Repository
@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	//Custom query
		@Query(value = "select * from posts p where p.id like %:id% or p.reply_id like %:replyId% or p.text like %:keyword%", nativeQuery = true)
		List<Post> findBy(@Param("keyword") String keyword, @Param("id")Long id, @Param("replyId") Long replyId);
		
		List<Post> findByDate(Date date);
		
		@Query(value= "select * from posts p where date(p.date) <= :date", nativeQuery=true)
		List<Post> findByDateWithoutTime(@Param("date") Date date);
		
		@Query(value= "select * from posts p where p.reply_id like %:replyId%", nativeQuery=true)
		List<Post> findByReplyId(@Param("replyId")Long replyId);
	
}
