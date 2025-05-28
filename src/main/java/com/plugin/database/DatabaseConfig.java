package com.plugin.database;

import java.util.List;

public class DatabaseConfig {
    private String type;
    private String host;
    private int port;
    private String dbName;
    private String user;
    private String password;
    private List<String> tables;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getTables() {
        return tables;
    }
    public void setTables(List<String> tables) {
        this.tables = tables;
    }
}
