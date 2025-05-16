package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理するコントローラクラス
@Controller
public class LogoutController {
	// HttpSessionが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private HttpSession session;

	/**
	 * ログアウト処理
	 *
	 * /account/logout への GET リクエストを処理し、ログイン画面を返します。
	 * 
	 * @return ログイン画面のビュー名
	 */
	@GetMapping("/account/logout")
	public String accoutnLogout() {
		// セッションの無効化
		session.invalidate();
		return "redirect:/login";
	}
}
