package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.com.models.entity.Account;
import blog.com.services.AccountService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理するコントローラクラス
@Controller
public class LoginController {
	// AccountServiceが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private AccountService accountService;

	// HttpSessionが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private HttpSession session;

	/**
	 * ログイン画面の表示
	 *
	 * /login への GET リクエストを処理し、ログイン画面を返します。
	 * 
	 * @return ログイン画面のビュー名
	 */
	@GetMapping("/login")
	public String getLoginPage() {
		return "/login";
	}

	/**
	 * ログインの処理
	 * 
	 * @RequestParam：HTTPリクエストのパラメータを受け取るアノテーションです。
	 * @RequestParam HTTPリクエストのパラメータを受け取るアノテーションです。
	 * @RequestParam email    ログインに使用するメールアドレス
	 * @RequestParam password ログインに使用するパスワード
	 * Model model コントローラからビューへデータを渡すためのModelオブジェクト
	 */
	@PostMapping("/login/process")
	public String loginProcess(@RequestParam String email, @RequestParam String password, Model model) {
		/**
		 * accountServiceでcreateLoginメソッドを呼び出し、
		 * email と password を使ってログイン認証を行い、
		 * 認証に成功すればアカウント情報を session に保存してブログ一覧画面へリダイレクト、
		 * 失敗すればエラーメッセージをモデルにセットしてログイン画面を再表示する。
		 */
		Account account = accountService.createLogin(email, password);

		if (account == null) {
			model.addAttribute("error", "メールアドレスかパスワードが間違っています!");
			return "login";
		} else {
			session.setAttribute("loginAccountInfo", account);
			return "redirect:/blog/list";
		}
	}
}
