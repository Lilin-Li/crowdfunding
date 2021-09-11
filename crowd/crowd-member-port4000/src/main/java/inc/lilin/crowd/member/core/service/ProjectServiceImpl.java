package inc.lilin.crowd.member.core.service;

import inc.lilin.crowd.database.ProjectPOMapper;
import inc.lilin.crowd.entity.vo.PortalTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    ProjectPOMapper projectPOMapper;

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }
}
