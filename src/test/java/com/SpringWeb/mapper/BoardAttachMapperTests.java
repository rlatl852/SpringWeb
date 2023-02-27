package com.SpringWeb.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.SpringWeb.config.RootConfig;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class BoardAttachMapperTests {

	@Autowired
	private BoardAttachMapper mapper;
	
	@Test
	public void testTgetOldFiles() {
		mapper.getOldFiles().forEach(boardAttachDTO -> {
			log.info(boardAttachDTO);
		});
	}
}
