package inc.lilin.crowd.order.core.service;

import inc.lilin.crowd.database.AddressPOMapper;
import inc.lilin.crowd.database.OrderProjectPOMapper;
import inc.lilin.crowd.entity.po.AddressPO;
import inc.lilin.crowd.entity.vo.AddressVO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
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
}
