package com.signetz.sso.service;

import com.duangframework.db.convetor.KvItem;
import com.duangframework.generate.curd.CurdService;
import com.duangframework.mvc.annotation.Service;
import com.duangframework.mvc.dto.PageDto;
import com.duangframework.mvc.dto.SearchListDto;

import java.util.List;

@Service
public class SSOLoginService extends CurdService<Object> {

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public Object findById(String id) {
        System.out.println("################:  模拟登录" + id);
        return id;
    }

    @Override
    public Object findByKey(List<String> fieldList, KvItem... kvItems) {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public PageDto<Object> search(SearchListDto searchListDto) {
        return null;
    }

    @Override
    public List<Object> findAllByKey(List<String> fieldList, KvItem... kvItems) {
        return null;
    }
}
