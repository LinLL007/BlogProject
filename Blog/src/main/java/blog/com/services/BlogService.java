package blog.com.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.com.modesls.dao.BlogDao;
import blog.com.modesls.entity.Blog;

//ビジネスロジック（処理の中身）を担当する
@Service
public class BlogService {
	// BlogDaoが使えるように宣言
	@Autowired
	private BlogDao blogDao;

	// ブログ一覧のチェック
	public List<Blog> selectAllBlogList() {
		return blogDao.findAll();
	}

	// ブログ一覧の検索
	public List<Blog> searchBlogsByTitle(String keyword) {
	    return blogDao.findByTitleContaining(keyword);
	}

	
	
	// ブログの登録処理チェック
	// もし、findByTitle==null,保存処理 true
	// そうでない場合 false
	public boolean createBlog(String title, String categoryName, String blogImage, String content, Long accountId) {
		if (blogDao.findByTitle(title) == null) {
			blogDao.save(new Blog(title, categoryName, blogImage, content, accountId));
			return true;
		} else {
			return false;
		}
	}

	// 編集画面を表示するときのチェック
	// もし、blogId == null
	// そうでない場合
	// findByBlogIdの情報をコントローラークラスに渡す
	public Blog blogEditCheck(Long blogId) {
		if (blogId == null) {
			return null;
		} else {
			return blogDao.findByBlogId(blogId);
		}
	}

	// 更新処理のチェックのblogId
	// もし、blogId==nullだったら、更新処理はしない falseを返す
	// そうでない場合、更新処理をする
	// もし、blog==nullだったら、更新処理はしない falseを返す
	// そうでない場合、
	// コントローラークラスからもらった、blogIdを使って、編集する前の、データを取得
	// 変更するべきところだけ、セッターを使用してデータの更新をする
	// trueを返す
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

	// 削除処理のチェック
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
						Path imagePath = Paths.get("src/main/resources/static/blog-img", imageFileName);
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

}
