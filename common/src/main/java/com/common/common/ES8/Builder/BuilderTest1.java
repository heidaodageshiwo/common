package com.common.common.ES8.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8.Builder
 * @ClassName: BuilderTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-26  08:32
 * @UpdateDate: 2022-09-26  08:32
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class BuilderTest1 {
    //假设这里有比较多的属性

    private String name;
    private Integer age;
    private Integer sex;
    private String address;

}