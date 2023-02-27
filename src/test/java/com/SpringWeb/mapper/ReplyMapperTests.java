package com.SpringWeb.mapper;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.SpringWeb.config.RootConfig;
import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.ReplyDTO;

import lombok.Setter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class ReplyMapperTests {

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	@Test
	public void getReplyList() {
		Criteria cri = new Criteria();
		List<ReplyDTO> replies = mapper.getReplyList(17L);
	}
	
	@Test
	@Ignore
	public void testInsert() {
		ReplyDTO vo = new ReplyDTO();
		for(int i=0; i<=5; i++) {
			vo.setBno(17);
			vo.setReply("댓글 테스트 : " + i);
			vo.setReplyer("Replyer " + i);
			mapper.insert(vo);
		}
	}
	
	@Test
	@Ignore
	public void testDelete() {
		mapper.delete(3L);
	}
	
	@Test
	@Ignore
	public void testUpdate() {
		ReplyDTO vo = new ReplyDTO();
		vo.setRno(4);
		vo.setReply("수정 테스트");
		mapper.update(vo);
	}
}
