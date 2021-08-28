package inc.lilin.crowd.common.database;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberPO record);

    MemberPO selectByPrimaryKey(Integer id);

    List<MemberPO> selectAll();

    int updateByPrimaryKey(MemberPO record);
}