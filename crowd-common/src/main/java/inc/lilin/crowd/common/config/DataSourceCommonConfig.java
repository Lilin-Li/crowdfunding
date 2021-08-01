package inc.lilin.crowd.common.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"inc.lilin.crowd.*.database.mysql.mybatis"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceCommonConfig {

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory( DataSource dataSource) throws Exception {
        // 配置MapperConfig
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 当数据库集群时,配置多个数据源,通过设置不同的DatebaseId来区分数据源,同时sql语句中通过DatabaseId来指定匹配哪个数据源
        // configuration.setDatabaseId("Mysql-1");
        // 本地一级缓存作用域 = 每次SQL執行就清空
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        // 这个配置使全局的映射器启用或禁用缓存
        configuration.setCacheEnabled(false);
        // 允许 JDBC 支持生成的键，需要适合的驱动（如MySQL，SQL Server，Sybase ASE）。
        configuration.setUseGeneratedKeys(true);
        configuration.setMapUnderscoreToCamelCase(true);

        // 配置默认的执行器:
        // SIMPLE :> SimpleExecutor 执行器没有什么特别之处;
        // REUSE :> ReuseExecutor 执行器重用预处理语句,在一个Service方法中多次执行SQL字符串一致的操作时,会复用Statement及Connection,
        // 也就是说不需要再预编译Statement,不需要重新通过DataSource生成Connection及释放Connection,能大大提高操纵数据库效率;
        // BATCH :> BatchExecutor 执行器重用语句和批量更新
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        // 全局启用或禁用延迟加载，禁用时所有关联对象都会即时加载
        configuration.setLazyLoadingEnabled(false);
        // 设置SQL语句执行超时时间缺省值，具体SQL语句仍可以单独设置
        configuration.setDefaultStatementTimeout(5000);
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(configuration);
        // 设置MyBatis分页插件
        PageInterceptor pageInterceptor = this.initPageInterceptor();
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});

        // 匹配多个 MapperConfig.xml, 使用mappingLocation属性
        String[] locationPatterns = {
                "classpath*:*Mapper.xml"
        };
        sqlSessionFactoryBean.setMapperLocations(getResources(locationPatterns));
        return sqlSessionFactoryBean.getObject();
    }

    public PageInterceptor initPageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    private Resource[] getResources(String[] locationPatterns) throws Exception {
        ArrayList<Resource> resourceList = new ArrayList<>();

        for(String locationPattern:locationPatterns){
            Resource[] resource1 = new PathMatchingResourcePatternResolver().getResources(locationPattern);
            resourceList.addAll(Arrays.asList(resource1));
        }

        return resourceList.toArray(new Resource[resourceList.size()]);
    }
    @Bean("sqlSession")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlsesionFactory) {
        return new SqlSessionTemplate(sqlsesionFactory, ExecutorType.BATCH);
    }
}
