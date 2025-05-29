package blog.com.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import blog.com.services.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@BeforeEach
	public void prepareDate() {
		// 登録できる場合 "test", "test@test.com" "1234abcd" true
		lenient().when(accountService.createAccount("test", "test@test.com", "1234abcd")).thenReturn(true);
		// ログインが失敗： email "test@test.com"と等しい、 ユーザ名とパスワードはどんな値でもいい false
		lenient().when(accountService.createAccount("Taro", "test@test.com", "1234abcd")).thenReturn(false);
	}

	// 登録画面が正常表示できるテスト
	@Test
	public void testGetRegisterPage_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/register");

		mockMvc.perform(request).andExpect(view().name("register.html"));
	}

	// ユーザーの登録が成功するかのテスト
	@Test
	public void testRegisterProcess_True() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("accountName", "test")
				.param("email", "test@test.com")
				.param("password", "1234abcd");

		mockMvc.perform(request).andExpect(view().name("login.html"));
		verify(accountService, times(1)).createAccount("test", "test@test.com", "1234abcd");
	}
	
	// ユーザーの登録が失敗するかのテスト
	@Test
	public void testRegisterProcess_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("accountName", "Taro")
				.param("email", "test@test.com")
				.param("password", "1234abcd");

		mockMvc.perform(request).andExpect(view().name("register.html"));
		verify(accountService, times(1)).createAccount("Taro", "test@test.com", "1234abcd");
	}

}
