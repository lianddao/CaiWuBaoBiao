package com.hzsh.config;

//import com.baomidou.mybatisplus.core.injector.ISqlInjector;
//import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;


@Configuration
public class MybatisPlusConfig {
    public static  ThreadLocal<String> myTableName = new ThreadLocal<>();


//    /**
//     * SQL执行效率插件
//     */
//    @Bean
//    @Profile({"dev","test"})// 设置 dev test 环境开启
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

    /**
     * 乐观锁插件
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // ------- 动态表名 解析处理拦截器 start -----------
//        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
//        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
//        tableNameHandlerMap.put("T_USER", new ITableNameHandler() {
//            //返回替换后的表名
//            @Override
//            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
//                System.out.println("------------------------dynamicTableName-hello--------------------------------");
//                return myTableName.get();
//            }
//        });
//        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
//        //新建 SQL 解析处理拦截器 列表
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        //表动态名 解析处理拦截器 加入列表
//        sqlParserList.add(dynamicTableNameParser);
//        // ------- 表动态名 解析处理拦截器 end -----------
//
//        //配合 分页拦截器 使用 SQL 解析处理拦截器
//        System.out.println("setSqlParserList-----------");
//        paginationInterceptor.setSqlParserList(sqlParserList);

        return  paginationInterceptor;
    }

//    /*
//     * 逻辑删除实现
//     * 3.1.1开始不需要注册LogicSqlInjector，3.1.1以下版本要手动注册才生效
//     */
//    @Bean
//    public ISqlInjector sqlInjector(){
//        return new LogicSqlInjector();
//    }
}
