package blog.com.modesls.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//データベースのテーブルをマッピングする
@Entity
public class Account {
	// account_id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;

	// account_name
	private String accountName;

	// email
	private String email;

	// password
	private String password;

	// 空のコンストラクタ
	public Account() {

	}

	// コンストラクタ
	public Account(String accountName, String email, String password) {
		this.accountName = accountName;
		this.email = email;
		this.password = password;
	}

	// get set
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
