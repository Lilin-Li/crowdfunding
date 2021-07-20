package inc.lilin.crowd;

import inc.lilin.crowd.admin.AdminApplication;
import inc.lilin.crowd.admin.domain.TestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdminApplication.class)
class DatabaseTest {

    @Autowired
    TestMapper dataSource;

    @Test
    void contextLoads() throws Exception{
        dataSource.insert(new inc.lilin.crowd.admin.domain.Test("aa"));
    }
}
