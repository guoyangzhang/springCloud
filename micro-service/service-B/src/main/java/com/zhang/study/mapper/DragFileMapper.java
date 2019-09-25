package com.zhang.study.mapper;

import com.zhang.study.entity.TreeDemo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    int deleteList(List<TreeDemo> list);

    List<TreeDemo> getCategory(String pid);


    TreeDemo queryTree (TreeDemo record);

}