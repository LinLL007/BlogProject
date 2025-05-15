package blog.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.com.modesls.dao.AccountDao;
import blog.com.modesls.entity.Account;

@Service
public class AccountService {
	@Autowired
	public AccountDao accountDao;

	// 登録処理
	// もしfindByEmail==nullだったら登録処理します
	// saveメソッドを呼び出し登録処理する
	// 保存ができたらtrue
	// そうでない場合、保存処理失敗 false
	public boolean createAccount(String accountName, String email, String password) {
		if (accountDao.findByEmail(email) == null) {
			accountDao.save(new Account(accountName, email, password));
			return true;
		} else {
			return false;
		}
	}

	// ログイン処理
	// ログインしている人の情報をコントローラークラスに渡す
	// 存在いない場null戻る
	public Account createLogin(String email, String password) {
		return accountDao.findByEmailAndPassword(email, password);
	}
}
