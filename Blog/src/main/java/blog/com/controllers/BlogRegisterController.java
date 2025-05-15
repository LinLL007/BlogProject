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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blog.com.modesls.entity.Account;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理する
@Controller
public class BlogRegisterController {
	// Sessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// BlogServiceが使えるように宣言
	@Autowired
	private BlogService blogService;

	// ブログ登録画面の表示
	@GetMapping("/blog/register")
	public String getBlogRegisterPage(Model model) {
		// セッションからログインしている人の情報をaccountという変数に渡す
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account == null、 ログイン画面にリダイレクトする
		// そうでない場合、ログインしている人の名前を画面に渡す
		// ブログ登録のhtmlを表示させる
		if (account == null) {
			return "redirect:/login";
		} else {
			model.addAttribute("accountName", account.getAccountName());
			return "blog_register";
		}
	}

	// ブログの登録処理
	@PostMapping("/blog/register/process")
	//@RequestParam:URLやフォームのパラメータを受け取る
	public String blogRegisterProcess(@RequestParam String title, @RequestParam String categoryName,
			@RequestParam MultipartFile blogImage,@RequestParam  String content) {
		// セッションからログインしている人の情報をaccountという変数に渡す
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし、account == null、 ログイン画面にリダイレクトする
		// そうでない場合、画像のファイル名を取得
		// 画像のアップロード
		// もし、同じファイルの名前がなかったら保存
		// ブログの一覧画像にリダイレクトする
		// そうでない場合、ブログ登録画面にとどまります
		/**
		 * 現在の日時情報を元に、ファイル名を作成しています。SimpleDateFormatクラスを使用して、日時のフォーマットを指定している。
		 * 具体的には、"yyyy-MM-dd-HH-mm-ss-"の形式でフォーマットされた文字列を取得している。
		 * その後、blogImageオブジェクトから元のファイル名を取得し、フォーマットされた日時文字列と連結して、fileName変数に代入
		 **/
		if (account == null) {
			return "redirect:/login";
		} else {
			// ファイルの名前を取得
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ blogImage.getOriginalFilename();
			// ファイルの保存作業
			try {
				Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (blogService.createBlog(title, categoryName, fileName, content, account.getAccountId())) {
				return "redirect:/blog/list";

			} else {
				return "blog_register";
			}
		}

	}
}
