package blog.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.com.models.dao.AccountDao;
import blog.com.models.entity.Account;

// ビジネスロジック（業務処理）を担当するサービスクラス
@Service
public class AccountService {
	// AccountDaoが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	public AccountDao accountDao;

	/**
	 *  登録処理を行う。
	 * 指定されたメールアドレスが未登録の場合、新規アカウントを保存しtrueを返す。
	 * すでに登録済みの場合は登録せずfalseを返す。
	 */
	public boolean createAccount(String accountName, String email, String password) {
		if (accountDao.findByEmail(email) == null) {
			accountDao.save(new Account(accountName, email, password));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ログイン処理を行う。
	 * 指定されたメールアドレスとパスワードに一致するアカウント情報を取得し、
	 * 存在しない場合はnullを返す。
	 */
	public Account createLogin(String email, String password) {
		return accountDao.findByEmailAndPassword(email, password);
	}
}
