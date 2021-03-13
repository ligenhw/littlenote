package cn.bestlang.littlenote.account.service.impl;

import cn.bestlang.littlenote.account.entity.Account;
import cn.bestlang.littlenote.account.mapper.AccountMapper;
import cn.bestlang.littlenote.account.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gen
 * @since 2021-03-12
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
