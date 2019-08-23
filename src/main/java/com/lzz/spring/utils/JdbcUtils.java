package com.lzz.spring.utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {

    /**
     * JDBC工具类
     */

        private static String driverClassName;
        private static String URL;
        private static String username;
        private static String password;
        private static boolean autoCommit;

        /**
         * 声明一个 Connection类型的静态属性，用来缓存一个已经存在的连接对象
         */
        private static Connection conn;

        static {
            config();
        }

        /**
         * 开头配置自己的数据库信息
         */
        private static void config() {
            /*
             * 获取驱动
             */
            driverClassName = "com.mysql.jdbc.Driver";
            /*
             * 获取URL
             */
            URL = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf8";
            /*
             * 获取用户名
             */
            username = "root";
            /*
             * 获取密码
             */
            password = "123";
            /*
             * 设置是否自动提交，一般为false不用改
             */
            autoCommit = false;

        }

        /**
         * 载入数据库驱动类
         */
        private static boolean load() {
            try {
                Class.forName(driverClassName);
                return true;
            } catch (ClassNotFoundException e) {
                System.out.println("驱动类 " + driverClassName + " 加载失败");
            }
            return false;
        }

        /**
         * 检查缓存的连接是否不可用 ，不可用返回 true
         */
        private static boolean invalid() {
            if (conn != null) {
                try {
                    //isValid方法是判断Connection是否有效,如果连接尚未关闭并且仍然有效，则返回 true
                    if (conn.isClosed() || !conn.isValid(3)) {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //conn不为null且没有关闭且isValid 返回 true，说明是可以使用的 ( 返回false )
                return false;
            } else {
                return true;
            }
        }

        /**
         * 建立数据库连接
         */
        public static Connection connect() {
            if (invalid()) {
                load();
                try {
                    conn = DriverManager.getConnection(URL, username, password);
                } catch (SQLException e) {
                    System.out.println("建立 " + conn + " 数据库连接失败 , " + e.getMessage());
                }
            }
            return conn;
        }

        /**
         * 设置是否自动提交事务
         **/
        public static void transaction() {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                System.out.println("设置事务的提交方式为 : " + (autoCommit ? "自动提交" : "手动提交") + " 时失败: " + e.getMessage());
            }
        }

    public static String getParamNameListAndReplace(List<String> paramNameList, String sql) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sql.length(); i++){
            if (sql.charAt(i) == '#'){
                int b = i+2;
                int e = sql.substring(b).indexOf("}");
                paramNameList.add(sql.substring(b, b+e));
                i = b+e;
                sb.append("?");
            }
            else sb.append(sql.charAt(i));
        }
        return sb.toString();
    }


        /**
         * 提交事务
         */
        private static void commit(Connection c) {
            if (c != null && !autoCommit) {
                try {
                    c.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 回滚事务
         */
        private static void rollback(Connection c) {
            if (c != null && !autoCommit) {
                try {
                    c.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 释放资源
         **/
        public static void release(Object cloaseable) {
            if (cloaseable != null) {
                if (cloaseable instanceof ResultSet) {
                    ResultSet rs = (ResultSet) cloaseable;
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (cloaseable instanceof Statement) {
                    Statement st = (Statement) cloaseable;
                    try {
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (cloaseable instanceof Connection) {
                    Connection c = (Connection) cloaseable;
                    try {
                        c.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }



        public static void insert(String sql, Object args[]){
            try {
                Connection conn = JdbcUtils.connect();
                PreparedStatement stat = conn.prepareStatement(sql);
                System.out.println(sql);
                for (int i = 0; i < args.length; i++) {
                    stat.setString(i+1, args[i].toString());
                    System.out.println(args[i]);
                }
                stat.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static Object queryAll(String sql, Object args[], Class<?> cls){
            try {
                Connection conn = JdbcUtils.connect();
                QueryRunner qr = new QueryRunner();
                System.out.println(cls.getName());
                return qr.query(conn, sql, new BeanListHandler<>(cls), args);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

}
