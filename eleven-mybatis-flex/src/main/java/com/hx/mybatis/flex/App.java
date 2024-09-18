package com.hx.mybatis.flex;

import com.mybatisflex.core.MybatisFlexBootstrap;

import javax.sql.DataSource;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        DataSource dataSource = null;

        MybatisFlexBootstrap bootstrap = MybatisFlexBootstrap.getInstance()
                .setDataSource(dataSource)
//                .setLogImpl(StdOutImpl.class)
//                .addMapper(AccountMapper.class)
                .start();
    }
}
