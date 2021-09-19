package inc.lilin.crowd.order.core.service;

import inc.lilin.crowd.database.OrderProjectPOMapper;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(projectId, returnId);
    }
}
