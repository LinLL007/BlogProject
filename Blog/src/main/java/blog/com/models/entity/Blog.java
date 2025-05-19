package blog.com.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//データベースのテーブルをマッピングする
@Entity
public class Blog {
	// account_id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long blogId;

	// title
	private String title;

	// category_name
	private String categoryName;

	// blogImage
	private String blogImage;

	// content
	private String content;

	// accountId
	private Long accountId;
	
	// viewCount
	private int viewCount = 0;

	// 空のコンストラクタ
	public Blog() {

	}

	// コンストラクタ
	public Blog(String title, String categoryName, String blogImage, String content, Long accountId) {
		this.title = title;
		this.categoryName = categoryName;
		this.blogImage = blogImage;
		this.content = content;
		this.accountId = accountId;
	}

	// set get
	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBlogImage() {
		return blogImage;
	}

	public void setBlogImage(String blogImage) {
		this.blogImage = blogImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

}
