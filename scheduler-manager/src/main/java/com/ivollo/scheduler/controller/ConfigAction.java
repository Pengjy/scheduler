package com.ivollo.scheduler.controller;

import com.ivollo.scheduler.constant.Operations;
import com.ivollo.scheduler.service.ConfigurationService;
import com.ivollo.scheduler.vo.ConfigVO;
import com.ivollo.scheduler.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
@Controller
@RequestMapping("/config")
public class ConfigAction {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MongoOperations mongoOperations;

    @RequestMapping("/")
    public String index(ModelAndView modelAndView){
        List<ConfigVO> configVOList = mongoOperations.findAll(ConfigVO.class);
        modelAndView.addObject("datas",configVOList);
        return "index";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Result<Boolean> add(ConfigVO configVO){
        configVO.setOperation(Operations.ADD);
        mongoOperations.save(configVO);

        configurationService.config(configVO);

        Result<Boolean> result = new Result<>();
        result.setCode(200);
        result.setData(true);
        return result;
    }

    @RequestMapping("/del")
    @ResponseBody
    public Result<Boolean> del(ConfigVO configVO){
        configVO.setOperation(Operations.DEL);
        Criteria criteria = Criteria.where("jobName").is(configVO.getJobName());
        ConfigVO configDBObj = mongoOperations.findOne(Query.query(criteria),ConfigVO.class);
        mongoOperations.remove(configDBObj);

        configurationService.config(configVO);

        Result<Boolean> result = new Result<>();
        result.setCode(200);
        result.setData(true);
        return result;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public Result<Boolean> modify(ConfigVO configVO){
        configVO.setOperation(Operations.MODIFY);

        Criteria criteria = Criteria.where("jobName").is(configVO.getJobName());
        ConfigVO configDBObj = mongoOperations.findOne(Query.query(criteria),ConfigVO.class);
        configDBObj.setCron(configVO.getCron());
        mongoOperations.save(configDBObj);

        configurationService.config(configVO);

        Result<Boolean> result = new Result<>();
        result.setCode(200);
        result.setData(true);
        return result;
    }
}
