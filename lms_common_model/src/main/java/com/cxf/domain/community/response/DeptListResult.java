package com.cxf.domain.community.response;

import com.cxf.domain.community.Community;
import com.cxf.domain.community.Department;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeptListResult {
    private String communityId;
    private String communityName;
    private String communityManage;
    private List<Department> depts;

    public DeptListResult(Community community, List<Department> departments) {
        this.communityId = community.getId();
        this.communityName = community.getName();
        this.communityManage = community.getPrincipal();
        this.depts = departments;
    }
}
