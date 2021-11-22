package com.common.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.common.entity.User;
import com.common.common.mapper.UserMapper;
import com.common.common.service.UserService;
import org.springframework.stereotype.Service;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.service.impl
 * @ClassName: UserServiceImpl
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-22 10:24
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-22 10:24
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
