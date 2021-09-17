package inc.lilin.crowd.project.service;

import inc.lilin.crowd.entity.vo.DetailProjectVO;
import inc.lilin.crowd.entity.vo.ProjectVO;

public interface ProjectService {

	void saveProject(ProjectVO projectVO, Integer memberId);
	DetailProjectVO getDetailProjectVO(Integer projectId);

}
