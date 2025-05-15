package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.com.modesls.entity.Account;
import blog.com.services.AccountService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	// AccountServiceが使えるように宣言
	@Autowired
	private AccountService accountService;

	// Sessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// ログイン画面の表示
	@GetMapping("/login")
	public String GetLoginPage() {
		return "/login.html";
	}

	// ログインの処理
	@PostMapping("/login/process")
	public String LoginProcess(@RequestParam String email, @RequestParam String password, Model model) {
		// createLoginメソッドを呼び出し、結果はaccountに渡す
		Account account = accountService.createLogin(email, password);
		// accountはnullの場、エラー文出す、ログイン画面にとどまります
		// そうでない場合は、sessionにログイン情報に保存
		// ブログ一覧画面にリダイレクトする
		if (account == null) {
			model.addAttribute("error", "メールアドレスかパスワードか間違え!");
			return "login.html";
		} else {
			session.setAttribute("loginAccountInfo", account);
			return "redirect:/blog/list";
		}
	}
}
