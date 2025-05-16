package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.com.services.AccountService;

//フロントエンドからのリクエストを処理する
@Controller
public class RegisterController {
	// AccountServiceが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private AccountService accountService;

	/**
	 * 登録画面の表示
	 *
	 * /register への GET リクエストを処理し、登録画面を返します。
	 * 
	 * @return 録画面のビュー名
	 */
	@GetMapping("/register")
	public String getRegisterPage() {
		return "register.html";
	}

	/**
	 * 登録処理
	 * @RequestParam：HTTPリクエストのパラメータを受け取るアノテーションです。
	 * @RequestParam String accountName　登録するユーザー名
	 * @RequestParam String email　登録するメールアドレス
	 * @RequestParam String password　登録するパスワード
	 * Model model コントローラからビューへデータを渡すためのModelオブジェクト
	 */
	@PostMapping("/register/process")
	public String registerProcess(@RequestParam String accountName, @RequestParam String email,
			@RequestParam String password, Model model) {
		// createAccountメソッドを呼び出し、true場合login画面に遷移
		// そうでない場合、エラー文出す、registerにとどまります
		if (accountService.createAccount(accountName, email, password)) {
			return "login.html";
		} else {
			model.addAttribute("error", "メールアドレスは既に登録されています!");
			return "register";
		}
	}

}
