package MBG;

import MBG.Test;
import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface TestMapper {
    int insert(Test record);

    List<Test> selectAll();
}