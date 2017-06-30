package com.ivollo.scheduler.service;

import com.ivollo.scheduler.entity.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Service
public class JobDetailService {

    @Autowired
    private MongoOperations mongoOperations;

    public List<JobDetail> listJobs(){
       return mongoOperations.findAll(JobDetail.class);
    }

    public void addJob(JobDetail jobDetail){
        Criteria criteria = Criteria.where("jobName").is(jobDetail.getJobName());
        if(null == mongoOperations.findOne(Query.query(criteria),JobDetail.class)){
            mongoOperations.save(jobDetail);
        }
    }

    public void delJob(String jobName){
        Criteria criteria = Criteria.where("jobName").is(jobName);
        JobDetail jobDetail = mongoOperations.findOne(Query.query(criteria),JobDetail.class);
        if(null != jobDetail) {
            mongoOperations.remove(jobDetail);
        }
    }
}
