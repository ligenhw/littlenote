package cn.bestlang.littlenote.account.controller;


import cn.bestlang.littlenote.account.entity.Account;
import cn.bestlang.littlenote.account.model.AccountReq;
import cn.bestlang.littlenote.account.service.IAccountService;
import cn.bestlang.littlenote.context.UserContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  账户信息控制器
 * </p>
 *
 * @author gen
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping
    public void createAccount(@RequestBody AccountReq account) {
        Account entity = new Account();
        BeanUtils.copyProperties(account, entity);

        entity.setUserId(UserContext.getId());
        accountService.saveOrUpdate(entity);
    }
    
    @GetMapping
    public Page<Account> queryAccount(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Account> page = new Page<>(pageNum, pageSize);
        Page<Account> accountPage = accountService.page(page, Wrappers.<Account>lambdaQuery().eq(Account::getUserId, UserContext.getId()));

        return accountPage;
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Integer accountId) {
        accountService.removeById(accountId);
    }

}
