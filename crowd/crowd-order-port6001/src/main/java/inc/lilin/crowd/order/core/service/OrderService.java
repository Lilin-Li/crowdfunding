package inc.lilin.crowd.order.core.service;

import inc.lilin.crowd.entity.vo.AddressVO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVO(Integer memberId);

    void saveAddress(AddressVO addressVO);
}
