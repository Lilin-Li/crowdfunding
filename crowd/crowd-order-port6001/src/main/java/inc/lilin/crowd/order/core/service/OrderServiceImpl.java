package inc.lilin.crowd.order.core.service;

import inc.lilin.crowd.database.AddressPOMapper;
import inc.lilin.crowd.database.OrderPOMapper;
import inc.lilin.crowd.database.OrderProjectPOMapper;
import inc.lilin.crowd.database.ProjectPOMapper;
import inc.lilin.crowd.entity.po.AddressPO;
import inc.lilin.crowd.entity.po.OrderPO;
import inc.lilin.crowd.entity.po.OrderProjectPO;
import inc.lilin.crowd.entity.vo.AddressVO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
import inc.lilin.crowd.entity.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(projectId, returnId);
    }

    @Override
    public List<AddressVO> getAddressVO(Integer memberId) {
        return addressPOMapper.selectByMemberId(memberId);
    }

    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);

        // 儲存orderPO自動產生的主鍵是orderProjectPO需要用到的外來鍵
        orderPOMapper.insert(orderPO);

        // 從orderPO中獲取orderId
        Integer id = orderPO.getId();

        // 將orderId設定到orderProjectPO
        orderProjectPO.setOrderId(id);

        orderProjectPOMapper.insert(orderProjectPO);
    }

    @Override
    public void updateProjectMoney(Integer projectId, Double total) {
        projectPOMapper.updateMoneyByPrimaryKey(projectId, total);
    }
}
