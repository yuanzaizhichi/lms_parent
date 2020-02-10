package com.cxf.community.controller;

import com.alibaba.fastjson.JSON;
import com.cxf.domain.community.Community;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityControllerTest {

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void save() throws Exception {
        Community community = new Community();
        community.setName("羽毛球协会");
        community.setManagerId("yumaoqiu");
        community.setVersion("1");
        community.setCommunityAddress("广州大学华软软件学院");
        community.setPrincipal("张三");

        String s = JSON.toJSONString(community);
        System.out.println(s);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/community")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void deleteById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/community/1224601529758306304"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void update() throws Exception {
        Community community = new Community();
        community.setName("蓝球协会2");
        community.setManagerId("lanqiu");
        community.setVersion("2");
        community.setCommunityAddress("广州大学华软软件学院");
        community.setPrincipal("李四");

        String s = JSON.toJSONString(community);
        System.out.println(s);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/community/1224609189509582848")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(s))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/community/55"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
//        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/community"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}