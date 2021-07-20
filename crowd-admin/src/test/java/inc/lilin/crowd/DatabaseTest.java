package inc.lilin.crowd;

import inc.lilin.crowd.admin.AdminApplication;
import inc.lilin.crowd.admin.domain.test.TestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = AdminApplication.class)
class DatabaseTest {

    @Autowired
    TestMapper dataSource;

    @Transactional
    @Test
    void contextLoads() throws Exception{
        dataSource.insert(new inc.lilin.crowd.admin.domain.test.Test("aa"));
    }
}
