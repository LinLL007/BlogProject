package blog.com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.com.modesls.dao.BlogDao;
import blog.com.modesls.entity.Blog;

@Service
public class BlogService {
	@Autowired
	private BlogDao blogDao;
	
	// 商品一覧のチェック
	public List<Blog> selectAllBlogList(Long blogId){
		return blogDao.findAll();
	}
	
}
