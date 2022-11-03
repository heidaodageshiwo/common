package com.common.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.common.entity.User;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.mapper
 * @ClassName: UserMapper
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-18 14:04
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-18 14:04
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

  @Select("select * from user")
  List<User> getAll();

  @Select("select name,age from user")
  List<Map<String,Object>> getAllMap();
  @Select("<script>" +
          " select * from table " +
          " <if test=\"iccid !=null and iccid !=''\"> and iccid=${iccid}</if> " +
          " limit #{pageIndex},#{pageSize}" +
          "</script>")
  List<Map<String,Object>> getAllMapss(
          @Param("iccid")String iccid
          ,@Param("pageIndex") Integer pageIndex
          ,@Param("pageSize") Integer pageSize);
  @Select("<script>" +
          " select count(*) from table " +
          " <if test=\"iccid !=null and iccid !=''\"> and iccid=${iccid}</if> " +
          "</script>")
  List<Map<String,Object>> getAllMapsscount(
          @Param("iccid")String iccid);
}
