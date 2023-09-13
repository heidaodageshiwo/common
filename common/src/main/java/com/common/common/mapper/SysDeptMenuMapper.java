package com.common.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.common.entity.SysDeptMenu;

/**
 * @PackageName com.lnsoft.bygl.mapper
 * @ClassName SysDeptMenuMapper
 * @Description TODO
 * @Author zhangqiang
 * @Date 2023/2/28 8:48
 * @Version 1.0
 **/
public interface SysDeptMenuMapper extends BaseMapper<SysDeptMenu> {


   /* select count(1) from iot_center_sys.sys_dept; -- 1879
<select id="selectDeptList" parameterType="SysDept" resultMap="SysDeptResult">
        <include refid="selectDeptVo"/>
    where d.del_flag = '0'
		<if test="deptId != null and deptId != 0">
    AND dept_id = #{deptId}
		</if>
        <if test="parentId != null and parentId != 0">
    AND parent_id = #{parentId}
		</if>
		<if test="deptName != null and deptName != ''">
    AND dept_name like concat('%', #{deptName}, '%')
		</if>
		<if test="status != null and status != ''">
    AND status = #{status}
		</if>
		<!-- 数据范围过滤 -->
    ${params.dataScope}
    order by d.parent_id, d.order_num
            </select>


 select distinct  b.device_identifier,a.app_id as appId,a.device_name as appName,a.product_name, a.client_id,'admin' as lastUpdatedBy ,'2021-09-24T08:44:26' as lastUpdatedDate  from (
 
 SELECT * FROM  view_db.all_device_details  WHERE device_oid IN (SELECT device_oid FROM iot_center_sys.sys_dept_device where dept_id in(  SELECT dept_id FROM iot_center_sys.sys_dept  WHERE dept_id = ( SELECT dept_id FROM iot_center_sys.sys_dept WHERE org_no = '374012517' )  OR find_in_set( ( SELECT dept_id FROM iot_center_sys.sys_dept WHERE org_no = '374012517' ), ancestors )) )

) a 

left join (select device_identifier, client_id from iot_center_yygl.zutai_device_channel where root_identifier = device_identifier ) b on a.client_id = b.client_id
 
 left join iot_center.city_tag c on c.city_tag=a.city_tag   where c.city = '济南' and  a.parent_device_oid is null  
 
 
 
 SELECT device_oid FROM iot_center_sys.sys_dept_device
 
 
 SELECT * FROM iot_center_sys.sys_dept_device;
 
 
 
 SELECT * FROM  view_db.all_device_details   WHERE device_oid='1059' 
*/

}
