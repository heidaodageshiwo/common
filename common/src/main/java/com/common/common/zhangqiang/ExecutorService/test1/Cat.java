package com.common.common.zhangqiang.ExecutorService.test1;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ExecutorService
 * @ClassName: Cat
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-17  09:06
 * @UpdateDate: 2022-10-17  09:06
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Data
@Service
public class Cat {
    private String catName;
    public Cat setCatName(String name) {
        this.catName = name;
        return this;
    }
}