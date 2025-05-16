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

import blog.com.models.entity.Account;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理するコントローラクラス
@Controller
public class BlogRegisterController {
	// HttpSessionが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private HttpSession session;

	// BlogServiceが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private BlogService blogService;

	
	/**
	 * ブログ登録画面の表示
	 *
	 * @GetMapping("/blog/register"): ブラウザから HTTP GET リクエストで、
	 * /blog/registerにアクセスされたときに、このメソッドが実行されます。
	 * 
	 * Model model: コントローラから ページへデータを渡すために使います
	 */
	@GetMapping("/blog/register")
	public String getBlogRegisterPage(Model model) {
		/**
		 * セッションからログインしているユーザー情報を取得し、account に格納。
		 *　account が null の場合、ログインしていないためログイン画面へリダイレクト。
		 */
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			// ログインしている場合、ユーザー名をモデルにセットして登録画面を表示
			model.addAttribute("accountName", account.getAccountName());
			return "blog_register";
		}
	}

	
	/**
	 * ブログの登録処理
	 * 
	 * @PostMapping("/blog/register/process"):
	 * ブログ登録画面から送信されたデータを受け取り、ブログの更新処理を行う。
	 *
	 *@RequestParam：HTTPリクエストのパラメータを受け取るアノテーションです。
	 *
	 *@RequestParam String title：
	 * フォームから送信されたタイトルを受け取ります。
	 * 
	 *@RequestParam String categoryName：
	 * カテゴリ名を受け取ります。
	 * 
	 * @RequestParam MultipartFile blogImage：
	 * アップロードされた画像ファイルを受け取ります。
	 * MultipartFile：アップロードされたファイルを処理するためのインターフェースです。
	 *
	 * @RequestParam String content：
	 * ブログの本文を受け取ります。
	 */
	@PostMapping("/blog/register/process")
	// @RequestParam:URLやフォームのパラメータを受け取る
	public String blogRegisterProcess(@RequestParam String title, @RequestParam String categoryName,
			@RequestParam MultipartFile blogImage, @RequestParam String content) {
		/**
		 * セッションからログインしているユーザー情報を取得し、account に格納。
		 *　account が null の場合、ログインしていないためログイン画面へリダイレクト。
		 */
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			 /**
	         * 現在の日時をもとにファイル名を生成します。
	         * たとえば "2025-05-15-14-30-00-元のファイル名" のようになります。
	         * blogImage から取得したファイル名を、日時の文字列と連結します。
	         */
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ blogImage.getOriginalFilename();
			// 画像ファイルを保存します
			try {
				Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ブログを登録し、成功したら一覧画面へリダイレクト、失敗したらブログ登録画面にとどまります
			if (blogService.createBlog(title, categoryName, fileName, content, account.getAccountId())) {
				return "redirect:/blog/list";
			} else {
				return "blog_register";
			}
		}

	}
}
