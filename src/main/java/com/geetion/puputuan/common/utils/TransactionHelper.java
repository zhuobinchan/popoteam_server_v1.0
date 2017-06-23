package com.geetion.puputuan.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by yoan on 16/3/7.
 */
@Component
public class TransactionHelper {

    @Autowired
    private DataSourceTransactionManager manager;

    public TransactionStatus start(){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
        def.setTimeout(10000);
        return manager.getTransaction(def);
    }

    public void commit(TransactionStatus status){
        manager.commit(status);
    }

    public void rollback(TransactionStatus status){
        manager.rollback(status);
    }
}
