package com.SpringWeb.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.SpringWeb.config.RootConfig;
import com.SpringWeb.config.SecurityConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		RootConfig.class,
		SecurityConfig.class
})

public class PasswordEncoderTests {

	@Autowired
	private PasswordEncoder pwEncoder;
	
	@Test
	public void testEncode() {
		String str1 = "1234";
		String str2 = "admin";
		String enStr1 = pwEncoder.encode(str1);
		String enStr2 = pwEncoder.encode(str2);
	}
}
