package com.taotao.manage.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

	//根据id查询
	public T queryById(Serializable id);
	//查询所有
	public List<T> queryAll();
	//根据条件查询
	public List<T> queryByWhere(T t);
	//根据条件查询总数
	public int queryCountByWhere(T t);
	//根据分页查询
	public List<T> queryByPage(Integer page, Integer rows);
	//选择性新增
	public void saveSelective(T t);
	//选择性更新
	public void updateSelective(T t);
	//根据id删除
	public void deleteById(Serializable id);
	//批量删除
	public void deleteByIds(Serializable[] ids);
}
