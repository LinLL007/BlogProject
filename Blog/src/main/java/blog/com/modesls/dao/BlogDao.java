package blog.com.modesls.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.com.modesls.entity.Blog;

// データベースを操作し、データアクセスロジックをカプセル化する
@Repository
public interface BlogDao extends JpaRepository<Blog, Long> {
	// 保存処理と更新処理 insertとupdate
	Blog save(Blog blog);

	// SELECT * FROM blog
	// 用途：ブログの一覧を表示させる時に使用
	List<Blog> findAll();

	// SELECT * FROM blog WHERE title=?
	// 用途：ブログの登録チェックに使用（同じブログが登録されないようにする）
	Blog findByTitle(String title);

	// SELECT * FROM blog WHERE product_id=?
	// 用途：編集画面と検索時に画面を表示する際に使用
	Blog findByBlogId(Long blogId);

	// DLETE FROM blog WHERE blog_id=?
	// 用途：削除使用します。
	void deleteByBlogId(Long blogId);

	// SELECT * FROM blog WHERE title LIKE '%keyword%';
	// 用途：検索使用します。
	List<Blog> findByTitleContaining(String keyword);
}
