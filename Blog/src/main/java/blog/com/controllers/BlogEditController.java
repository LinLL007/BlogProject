package blog.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog.com.modesls.entity.Account;
import blog.com.modesls.entity.Blog;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理する
@Controller
public class BlogEditController {
	// BlogServiceが使えるように宣言
	@Autowired
	private BlogService blogService;

	// HttpSessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// 編集画面の表示
	@GetMapping("/blog/edit/{blogId}")
	public String getBlogEditPage(@PathVariable Long blogId, Model model) {
		// セッションからログインしている人の情報をaccountという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account==null、 ログイン画面にリダイレクトする
		if (account == null) {
			return "redirect:/login";
		} else {
			// 編集画面に表示させる情報を変数に格納 blog
			Blog blog = blogService.blogEditCheck(blogId);
			// もし、blog==nullだったら、商品一覧ページにリダイレクトする
			// そうでない場合、編集画面に編集する内容を渡す
			// 編集画面を表示
			if (blog == null) {
				return "redirect:/blog/list";
			} else {
				model.addAttribute("accountName", account.getAccountName());
				model.addAttribute("blog", blog);
				return "blog_edit";
			}
		}
	}

	// 更新処理をする
	@PostMapping("/blog/edit/process")
	public String blogUpdate(@RequestParam Long blogId, @RequestParam String title, @RequestParam String categoryName,
			@RequestParam MultipartFile blogImage, @RequestParam String content,
			@RequestParam("action") String action) {
		// セッションからログインしている人の情報をaccountという変数に格納
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、"update".equals(action)場合更新機能を実行、そうでない場合ログイン画面にリダイレクトする
		if ("update".equals(action)) {
			// もし、account == nullだったら、ログイン画面にリダイレクトする

			if (account == null) {
				return "redirect:/login";
			} else {
				// そうでない場合、
				/**
				 * 現在の日時情報を元に、ファイル名を作成しています。SimpleDateFormatクラスを使用して、日時のフォーマットを指定している。
				 * 具体的には、"yyyy-MM-dd-HH-mm-ss-"の形式でフォーマットされた文字列を取得している。
				 * その後、blogImageオブジェクトから元のファイル名を取得し、フォーマットされた日時文字列と連結して、fileName変数に代入
				 **/
				// ファイルの名前を取得
				String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
						+ blogImage.getOriginalFilename();
				// ファイルの保存
				try {
					Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// もし、blogUpdateの結果がtrueの場合は、ブログ一覧にリダイレクト
				// そうでない場合、商品編集画面にリダイレクトする
				if (blogService.blogUpdate(blogId, title, categoryName, fileName, content)) {
					return "redirect:/blog/list";
				} else {
					return "blog_register";
				}
			}
		} else if ("delete".equals(action)) {
			// もし、account == nullだったら、ログイン画面にリダイレクトする
			// そうでない場合、blogServiceでdeleteBlogメソッドを呼び出し、
			// blogIdについて削除する
			// 成功したブログの一覧ページにリダイレクト
			// 失敗場合編集画面にリダイレクトする
			if (account == null) {
				return "redirect:/login";
			} else {
				if (blogService.deleteBlog(blogId)) {
					return "redirect:/blog/list";
				} else {
					return "redirect:/blog/edit" + blogId;
				}
			}
		} else {
			return "redirect:/login";
		}
	}

}
