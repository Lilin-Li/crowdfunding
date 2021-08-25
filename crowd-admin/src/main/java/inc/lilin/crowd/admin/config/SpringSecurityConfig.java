package inc.lilin.crowd.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // http.authorizeRequests().antMatchers("/","/**").permitAll();
        security
                .authorizeRequests() // 對請求進行授權
                // 登錄頁
                .antMatchers("/")
                .permitAll()
                // 靜態資源
                .antMatchers("/frontend/bootstrap/**")
                .permitAll()
                .antMatchers("/frontend/css/**")
                .permitAll()
                .antMatchers("/frontend/fonts/**")
                .permitAll()
                .antMatchers("/frontend/img/**")
                .permitAll()
                .antMatchers("/frontend/jquery/**")
                .permitAll()
                .antMatchers("/frontend/layer/**")
                .permitAll()
                .antMatchers("/frontend/script/**")
                .permitAll()
                .antMatchers("/frontend/ztree/**")
                .permitAll()
                // 其他請求
                .anyRequest()
                .authenticated()  // 需驗證後訪問
                .and()
                .formLogin()
                .loginPage("/") // 指定登錄頁面
                .loginProcessingUrl("/security/do/login.html") // 指定處理登錄請求的地址
                .defaultSuccessUrl("/admin-main")// 指定登錄成功后前往的地址
                .usernameParameter("loginAcct") // 賬號的請求參數名稱
                .passwordParameter("userPswd") // 密碼的請求參數名稱
                // HTTPS
                // .and()
                // .requiresChannel()
                // .antMatchers("/security/do/login.html").requiresSecure()   //啟動HTTPS
                .and()
                .logout() // 开启退出登录功能
                .logoutUrl("/admin/security/logout") // 指定退出登录地址
                .logoutSuccessUrl("/") // 指定退出成功以后前往的地址
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 臨時使用記憶體版登錄的模式測試程式碼
        // builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");
        // 正式功能中使用基於數據庫的認證
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

