package inc.lilin.crowd.admintest;

import inc.lilin.crowd.admin.MyAdminApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@SpringBootTest(classes = MyAdminApplication.class)
class DatabaseTest {

    @Autowired
    DataSource dataSource;


    @Transactional
    @Test
    void connectionTest() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            log.debug("已連線到資料庫:" + connection.getCatalog());
        }
    }
}
