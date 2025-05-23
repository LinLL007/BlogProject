package blog.com.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.com.models.dao.BlogDao;
import blog.com.models.entity.Blog;

//ビジネスロジック（業務処理）を担当するサービスクラス
@Service
public class BlogService {
	// BlogDaoが使えるように宣言
	// @Autowiredは自動的に使いたいインスタンスを探して、変数に注入する
	@Autowired
	private BlogDao blogDao;

	/**
	 * すべてのブログ記事を取得する。 viewCountの降順で並び替えられています。
	 */
	public List<Blog> selectAllBlogList() {
		return blogDao.findAllByOrderByViewCountDesc();
	}

	/**
	 * タイトルにキーワードを含むブログ記事を検索する。
	 *
	 * @param keyword 検索に使用するキーワード
	 * @return キーワードを含むブログ記事のリスト
	 */
	public List<Blog> searchBlogsByTitle(String keyword) {
		return blogDao.findByTitleContaining(keyword);
	}

	/**
	 * ブログの登録処理を行う。 指定されたタイトルのブログが未登録の場合、新規ブログを保存しtrueを返す。
	 * すでに同じタイトルのブログが存在する場合は登録せずfalseを返す。
	 */
	public boolean createBlog(String title, String categoryName, String blogImage, String content, Long accountId) {
		if (blogDao.findByTitle(title) == null) {
			blogDao.save(new Blog(title, categoryName, blogImage, content, accountId));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 編集画面表示時のチェック処理を行う。 blogIdがnullの場合はnullを返し、 そうでなければ該当するブログ情報を取得して返す。
	 */
	public Blog blogEditCheck(Long blogId) {
		if (blogId == null) {
			return null;
		} else {
			return blogDao.findByBlogId(blogId);
		}
	}

	/**
	 * 更新処理のチェックのblogId もし、blogId==nullだったら、更新処理はしない falseを返す そうでない場合、更新処理をする
	 * もし、blog==nullだったら、更新処理はしない falseを返す そうでない場合、
	 * コントローラークラスからもらった、blogIdを使って、編集する前の、データを取得 変更するべきところだけ、セッターを使用してデータの更新をする
	 * trueを返す
	 */
	public boolean blogUpdate(Long blogId, String title, String categoryName, String blogImage, String content) {
		if (blogId == null) {
			return false;
		} else {
			Blog blog = blogDao.findByBlogId(blogId);
			if (blog == null) {
				return false;
			} else {
				blog.setTitle(title);
				blog.setCategoryName(categoryName);
				blog.setBlogImage(blogImage);
				blog.setContent(content);
				blogDao.save(blog);
				return true;
			}
		}
	}

	/**
	 * 指定されたブログIDに対応するブログを削除する。 ブログに関連する画像ファイルも存在すれば削除する。
	 * 
	 * @param blogId 削除対象のブログID
	 * @return 削除成功ならtrue、blogIdがnull、該当ブログなし、または例外発生時はfalseを返す
	 */
	public boolean deleteBlog(Long blogId) {
		if (blogId == null) {
			return false;
		} else {
			try {
				// blogIdで対象のBlogを探す
				Blog blog = blogDao.findByBlogId(blogId);
				if (blog == null) {
					return false;
				} else {
					// 画像ファイル名を取得
					String imageFileName = blog.getBlogImage();
					if (imageFileName != null && !imageFileName.isEmpty()) {
						// 画像のパスを作成
						 Path imagePath = Paths.get("upload", imageFileName);
						// 画像ファイルを削除
						Files.deleteIfExists(imagePath);
					}
					// blogをDBから削除
					blogDao.deleteById(blogId);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * 閲覧数を1増やして、データベースを更新する。
	 * 
	 * getViewCount()メソッドを呼び出し現在viewcountの値もらってその上で+1。
	 * その後、blogでsetViewCountメソッドを呼び出し、viewcountの値を再設定する。
	 * 最後blogDaoでsaveメソッドを呼び出し、データベース更新する。
	 */
	public void updateViewCount(Blog blog) {
		blog.setViewCount(blog.getViewCount() + 1);
		blogDao.save(blog);
	}

}
