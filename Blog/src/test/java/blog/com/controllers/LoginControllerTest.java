package blog.com.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

import blog.com.models.entity.Account;
import blog.com.services.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	Account mockAccount = new Account("test", "test@test.com", "1234abcd");

	@BeforeEach
	public void prepareDate() {
		// ログインが成功： email "test@test.com"、 password "12345678" true
		when(accountService.createLogin("test@test.com", "1234abcd")).thenReturn(mockAccount);
		// ログインが失敗1： email "test@test.com"と等しい、 パスワードは1234 null
		when(accountService.createLogin("abc@test.com", "1234abcd")).thenReturn(null);
		// ログインが失敗2： email "abc@test.com"と等しい、 パスワードはどんな値でもいい null
		when(accountService.createLogin("test@test.com", "1234")).thenReturn(null);
		// ログインが失敗3： email "abc@test.com"と等しい、 パスワードはどんな値でもいい null
		when(accountService.createLogin("abc@test.com", "1234")).thenReturn(null);
	}

	// ログイン画面を正しく取得するテスト
	@Test
	public void testGetLoginPage_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/login");

		mockMvc.perform(request).andExpect(view().name("login.html"));
	}

	// ログインが成功した場合のテスト
	// ログインが成功したら「/blog/list」に遷移して、入力された値が渡されているかのテスト
	@Test
	public void testLogin_NewAccount_Succeed() throws Exception {
	    RequestBuilder request = MockMvcRequestBuilders.post("/login/process")
	        .param("email", "test@test.com")
	        .param("password", "1234abcd");

		    mockMvc.perform(request)
		        .andExpect(view().name("redirect:/blog/list"))
		        .andExpect(result -> {
		        	Object loginUser = result.getRequest().getSession().getAttribute("loginAc2countInfo");
		        	assertNotNull(loginUser);
		        });
	}

	// ログインが失敗した場合のテスト1
	@Test
	public void testLogin_ExistingUsername_Email_False() throws Exception {
	    RequestBuilder request = MockMvcRequestBuilders.post("/login/process")
	        .param("email", "abc@test.com")
	        .param("password", "1234");

	    mockMvc.perform(request)
	        .andExpect(model().attribute("error", "メールアドレスかパスワードが間違っています!"))
	        .andExpect(view().name("login.html"))
	        .andExpect(result -> {
	            Object loginUser = result.getRequest().getSession().getAttribute("loginAc2countInfo");
	            assertNull(loginUser);
	        });
	}

	// ログインが失敗した場合のテスト2
	@Test
	public void testLogin_ExistingUsername_Password_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/login/process").param("email", "test@test.com")
				.param("password", "1234");
		mockMvc.perform(request).andExpect(model().attribute("error", "メールアドレスかパスワードが間違っています!"))
				.andExpect(view().name("login.html"))
				.andExpect(result -> {
		            Object loginUser = result.getRequest().getSession().getAttribute("loginAc2countInfo");
		            assertNull(loginUser);
		        });
	}

	// ログインが失敗した場合のテスト3
	@Test
	public void testLogin_ExistingUsername_EmailAndPassword_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/login/process").param("email", "abc@test.com")
				.param("password", "1234");
		mockMvc.perform(request).andExpect(model().attribute("error", "メールアドレスかパスワードが間違っています!"))
				.andExpect(view().name("login.html"))
				.andExpect(result -> {
		            Object loginUser = result.getRequest().getSession().getAttribute("loginAc2countInfo");
		            assertNull(loginUser);
		        });
	}

}
