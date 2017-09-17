package com.taotao.manage.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.taotao.manage.pojo.BasePojo;
import com.taotao.manage.service.BaseService;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {
	
	/**
	 * 在spring 4.x版本之后，可以使用泛型依赖注入
	 */
	@Autowired
	private Mapper<T> mapper;
	
	private Class<T> clazz;
	
	public BaseServiceImpl() {
		//pt表示BaseServiceImpl<User> ；this表示BaseServiceImpl子类（UserServiceImpl）
		ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
		
		clazz = (Class<T>)pt.getActualTypeArguments()[0];
	}

	@Override
	public T queryById(Serializable id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<T> queryAll() {
		return mapper.selectAll();
	}

	@Override
	public List<T> queryByWhere(T t) {
		return mapper.select(t);
	}

	@Override
	public int queryCountByWhere(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public List<T> queryByPage(Integer page, Integer rows) {
		//设置分页
		PageHelper.startPage(page, rows);
		
		return mapper.select(null);
	}

	@Override
	public void saveSelective(T t) {
		if(t.getCreated() == null) {
			t.setCreated(new Date());
			t.setUpdated(t.getCreated());
		} else if(t.getUpdated() == null) {
			t.setUpdated(new Date());
		}
		mapper.insertSelective(t);
	}

	@Override
	public void updateSelective(T t) {
		if(t.getUpdated() == null) {
			t.setUpdated(new Date());
		}
		mapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public void deleteById(Serializable id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void deleteByIds(Serializable[] ids) {
		Example example = new Example(clazz);
		//创建查询对象
		Criteria criteria = example.createCriteria();
		//构造in条件
		criteria.andIn("id", Arrays.asList(ids));
		
		mapper.deleteByExample(example);
		
	}

}
