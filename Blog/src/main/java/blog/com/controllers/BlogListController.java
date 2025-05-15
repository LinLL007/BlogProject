package blog.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.com.modesls.entity.Account;
import blog.com.modesls.entity.Blog;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理する
@Controller
public class BlogListController {
	// Sessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// BlogServiceが使えるように宣言
	@Autowired
	private BlogService blogService;

	/**
     * ブログ一覧画面を表示する
     * 
     * @GetMapping("/blog/list")：
     * ブラウザから HTTP GET リクエストで /blog/list にアクセスされたときに、このメソッドが実行されます。
     */
	@GetMapping("/blog/list")
	 /**
     * @RequestParam：
     * HTTP リクエストのパラメータ "keyword" を受け取ります。
     * name = "keyword" は、パラメータ名を指定します。
     * required = false は、"keyword" が存在しなくてもエラーにならないようにします。
     * Model model は、コントローラから HTML ページへデータを渡すために使います。
     */
	public String blogList(@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		// blogList という名前の Blog エンティティのリストを定義
		List<Blog> blogList;
		/*
		 * keywordがnullでなく、空文字でもない場合、
		 * blogServiceのsearchBlogsByTitleメソッドを呼び出してkeywordを渡し、検索結果のblogListを取得します。
		 * また、keywordをモデルに"keyword"という名前でセットし、 ページで表示できるようにします。
		 */
		if (keyword != null && !keyword.isEmpty()) {
			blogList = blogService.searchBlogsByTitle(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			// blogService の selectAllBlogList メソッドを呼び出して、 すべてのブログ情報を取得します。
			blogList = blogService.selectAllBlogList();
		}
		// blogListをモデルに"blogList"という名前でセットし、 ページで表示できるようにします。
		model.addAttribute("blogList", blogList);
		// 処理が完了したら blog_list ページを表示します。
		return "blog_list";
	}

}
