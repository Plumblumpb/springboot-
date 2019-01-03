package com.plumblum.mapper;

import com.plumblum.dao.MemberDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Auther: cpb
 * @Date: 2018/8/2 11:28
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberDaoTest {
    @Resource
    private MemberDao userMapper;

    @Test
    public void userTest(){
        System.out.println(userMapper.findByID(37l));
    }
}
