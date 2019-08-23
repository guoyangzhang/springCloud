package com.zhang.study.mapper;

import com.zhang.study.entity.TreeDemo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DragFileMapper {

    int deleteByPrimaryKey(String guid);

    int insert(TreeDemo record);

    int insertSelective(TreeDemo record);

    TreeDemo selectByPrimaryKey(String guid);

    int updateByPrimaryKeySelective(TreeDemo record);

    int updateByPrimaryKey(TreeDemo record);

    int insertListSelective(List<TreeDemo> list);

    int selectCountRootName(String rootName);

    int deleteRootName(String rootName);

    List<TreeDemo> selectList(TreeDemo treeDemo);

}