package inc.lilin.crowd.database;

import inc.lilin.crowd.entity.po.AddressPO;
import inc.lilin.crowd.entity.vo.AddressVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此class由 MybatisGenerator產生，任何修改都會在下一次產生器產生code時被覆蓋
 */
public interface AddressPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AddressPO record);

    AddressPO selectByPrimaryKey(Integer id);

    List<AddressPO> selectAll();

    int updateByPrimaryKey(AddressPO record);

    List<AddressVO> selectByMemberId(@Param("memberId") Integer memberId);
}