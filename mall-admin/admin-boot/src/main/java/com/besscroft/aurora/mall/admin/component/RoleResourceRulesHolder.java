package com.besscroft.aurora.mall.admin.component;

import com.besscroft.aurora.mall.admin.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleResourceRulesHolder {

    @Autowired
    private ResourceService resourceService;

    @PostConstruct
    public void initRoleResourceMap() {
        resourceService.initRoleResourceMap();
    }
}
