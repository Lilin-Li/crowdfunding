package inc.lilin.crowd.common.database;


import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface AuthtpoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Authtpo record);

    Authtpo selectByPrimaryKey(Integer id);

    List<Authtpo> selectAll();

    int updateByPrimaryKey(Authtpo record);
}