package inc.lilin.crowd.order.core.service;

import inc.lilin.crowd.entity.vo.OrderProjectVO;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);
}
