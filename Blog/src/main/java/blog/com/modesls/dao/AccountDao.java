package blog.com.modesls.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.com.modesls.entity.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
	// 保存処理と更新処理 insertとupdate
	Account save(Account account);
	
	// SELECT * FROM account WHERE email = ?
	// 用途：アカウントの登録処理をする時に、同じメールアドレスがあったらば登録させないようにする
	Account findByEmail(String email);
	
	// SELECT * FROM account WHERE admin_email=? AND password=?
	// 用途：ログイン処理に使用。入力したメールアドレスとパスワードが一致してる時だけデータを取得する
	Account findByEmailAndPassword(String email,String password);
}
