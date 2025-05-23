package blog.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.com.models.entity.Blog;

// データベースを操作し、データアクセスロジックをカプセル化する
@Repository
public interface BlogDao extends JpaRepository<Blog, Long> {
	// 保存処理と更新処理 insertとupdate
	Blog save(Blog blog);

	// SELECT * FROM blog ORDER BY view_count DESC
	// 用途：ブログの一覧を表示させる時に使用viewCountの降順で並び替えられています。
	List<Blog> findAllByOrderByViewCountDesc();

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
	// 用途：タイトルに指定されたキーワードを含むブログ記事を検索します。
	List<Blog> findByTitleContaining(String keyword);
	
	

}
