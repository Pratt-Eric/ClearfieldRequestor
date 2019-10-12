/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.utils;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 *
 * @author P-ratt
 */
public class DBConnection {

    private PoolDataSource dataSource = null;

    public PoolDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PoolDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init(String contextName) {
        if (dataSource == null) {
            try {
                dataSource = PoolDataSourceFactory.getPoolDataSource();
                dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
                dataSource.setURL("jdbc:oracle:thin:@//localhost:1521/requestordata");
                dataSource.setUser("requestor");
                dataSource.setPassword("1qaz!QAZ2wsx@WSX");
                dataSource.setConnectionPoolName("requestor_Pool");
                dataSource.setInitialPoolSize(5);
                dataSource.setMinPoolSize(5);
                dataSource.setMaxPoolSize(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private DBConnection() {

    }

    public static DBConnection getInstance() {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder {

        private static final DBConnection INSTANCE = new DBConnection();
    }
}
