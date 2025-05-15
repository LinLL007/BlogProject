package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理する
@Controller
public class LogoutController {
	// HttpSessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// ログアウト処理
	@GetMapping("/account/logout")
	public String accoutnLogout() {
		// セッションの無効化
		session.invalidate();
		return "redirect:/login";
	}
}
