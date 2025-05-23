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

import blog.com.models.entity.Account;
import blog.com.models.entity.Blog;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理するコントローラクラス
@Controller
public class BlogEditController {
	// BlogServiceが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private BlogService blogService;

	// HttpSessionが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private HttpSession session;

	/**
	 * 編集画面の表示
	 *
	 * @GetMapping("/blog/edit/{blogId}"): ブラウザから HTTP GET リクエストで、
	 * /blog/edit/{blogId}にアクセスされたときに、このメソッドが実行されます。
	 * {blogId}:パス変数です。具体的にブログのIDについて変化。
	 * 
	 * @PathVariable:
	 * 上の@GetMapping("/blog/edit/{blogId}")で URL から blogId の値を取得します。
	 * パス変数名とメソッド引数名が同じ場合、@PathVariable("blogId") のように書かなくても省略可能です。
	 * 
	 * Model model: コントローラから ページへデータを渡すために使います
	 */
	@GetMapping("/blog/edit/{blogId}")
	public String getBlogEditPage(@PathVariable Long blogId, Model model) {
	       
		/**
		 * セッションからログインしているユーザー情報を取得し、account に格納。
		 *　account が null の場合、ログインしていないためログイン画面へリダイレクト。
		 */
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			// 編集画面に表示するための情報を blog 変数に格納
			Blog blog = blogService.blogEditCheck(blogId);
			// blog が nullの場合は、ブログ一覧ページにリダイレクト
			if (blog == null) {
				return "redirect:/blog/list";
			} else {
				// blogがnullでない場合、blogServiceでupdateViewCountメソッドを呼び出し閲覧数を更新する
				blogService.updateViewCount(blog);
				// 編集用にアカウント名とブログ情報を画面に渡す
				model.addAttribute("accountName", account.getAccountName());
				model.addAttribute("blog", blog);
				// 編集画面を表示
				return "blog_edit";
			}
		}
	}

	/**
	 * 更新処理を行うメソッド
	 * 
	 * @PostMapping("/blog/edit/process"):
	 * ブログ編集画面から送信されたデータを受け取り、ブログの更新処理を行う。
	 *
	 * @RequestParam：HTTPリクエストのパラメータを受け取るアノテーションです。
	 * 
	 * @RequestParam Long blogId：
	 * ブログのID（どのブログを更新するか）を取得します。
	 * 
	 * @RequestParam String title：
	 * フォームから送信されたタイトルを受け取ります。
	 * 
	 * @RequestParam String categoryName：
	 * カテゴリ名を受け取ります。
	 * 
	 * @RequestParam MultipartFile blogImage：
	 * MultipartFile：アップロードされたファイルを処理するためのインターフェースです。
	 * アップロードされた画像ファイルを受け取ります。
	 * 
	 * @RequestParam String content：
	 * ブログの本文を受け取ります。
	 * 
	 * @RequestParam("action") String action：
	 * フォームの「action」という名前の値を受け取ります
	 */
	@PostMapping("/blog/edit/process")
	public String blogUpdate(@RequestParam Long blogId, @RequestParam String title, @RequestParam String categoryName,
			@RequestParam MultipartFile blogImage, @RequestParam String content,
			@RequestParam("action") String action) {
		/**
		 * セッションからログインしているユーザー情報を取得し、account に格納。
		 *　account が null の場合、ログインしていないためログイン画面へリダイレクト。
		 */
		Account account = (Account) session.getAttribute("loginAccountInfo");
		// もし "update" が指定された場合は、更新処理を実行します。
		// それ以外の場合（"delete" や不正な値）は別の処理を行います。
		if ("update".equals(action)) {
			// ログインしていない場合、ログイン画面にリダイレクトします。
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
					Path uploadDir = Path.of("upload");
					if (!Files.exists(uploadDir)) {
						Files.createDirectories(uploadDir);
					}
					  Files.copy(blogImage.getInputStream(), uploadDir.resolve(fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}

				/** 
				 * blogServiceでblogUpdateメソッドを呼び出し、
				 * 更新処理を実行し、成功すれば一覧画面にリダイレクト、
				 * 失敗した場合はブログ登録画面に戻します。
		         */ 
				if (blogService.blogUpdate(blogId, title, categoryName, fileName, content)) {
					return "redirect:/blog/list";
				} else {
					return "blog_register";
				}
			}
			
		} else if ("delete".equals(action)) {
			// もし "delete" が指定された場合は、削除処理を実行します。
			// それ以外の場合（不正な値）は別の処理を行います。
			
			// ログインしていない場合、ログイン画面にリダイレクトします。
			if (account == null) {
				return "redirect:/login";
			} else {
				/**
				 * そうでない場合、blogServiceでdeleteBlogメソッドを呼び出し、
		         * blogIdについて削除処理を実行します。
		         * 成功した場合は一覧画面にリダイレクト、
		         * 失敗した場合は再度編集画面にリダイレクトします。
		         */
				if (blogService.deleteBlog(blogId)) {
					return "redirect:/blog/list";
				} else {
					return "redirect:/blog/edit" + blogId;
				}
			}
		} else {
			// "update" でも "delete" でもない場合、不正な操作とみなしログイン画面へ
			return "redirect:/login";
		}
	}

}
