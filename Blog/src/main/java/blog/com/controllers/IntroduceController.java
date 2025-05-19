package blog.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import blog.com.models.entity.Account;
import blog.com.services.BlogService;
import jakarta.servlet.http.HttpSession;

//フロントエンドからのリクエストを処理するコントローラクラス
@Controller
public class IntroduceController {
	// Session を使用できるように宣言
	// @Autowired は自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private HttpSession session;

	// BlogService を使用できるように宣言
	// @Autowired は自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private BlogService blogService;

	/**
	 * 自己紹介画面の表示
	 *
	 * /introduce への GET リクエストを処理し、自己紹介画面を返します。 Model model は、コントローラから HTML
	 * ページへデータを渡すために使います。
	 * 
	 * @return 自己紹介画面のビュー名
	 */
	@GetMapping("/introduce")
	public String getIntroducepage(Model model) {
		/**
		 * セッションからログインしているユーザー情報を取得し、account に格納。 account が null
		 * の場合、ログインしていないためログイン画面へリダイレクト。
		 */
		Account account = (Account) session.getAttribute("loginAccountInfo");
		if (account == null) {
			return "redirect:/login";
		} else {
			// 編集用にアカウント名とブログ情報を画面に渡す
			model.addAttribute("accountName", account.getAccountName());
			// 自己紹介画面 ページを表示します。
			return "introduce";
		}

	}
}
