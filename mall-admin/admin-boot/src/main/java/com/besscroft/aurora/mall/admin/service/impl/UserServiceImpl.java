package com.besscroft.aurora.mall.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.aurora.mall.admin.api.AuthFeignClient;
import com.besscroft.aurora.mall.admin.domain.param.AdminParam;
import com.besscroft.aurora.mall.admin.mapper.AuthRoleMapper;
import com.besscroft.aurora.mall.admin.mapper.AuthUserMapper;
import com.besscroft.aurora.mall.admin.service.MenuService;
import com.besscroft.aurora.mall.admin.service.UserService;
import com.besscroft.aurora.mall.common.constant.AuthConstants;
import com.besscroft.aurora.mall.common.domain.AuthUserExcelDto;
import com.besscroft.aurora.mall.common.domain.UserDto;
import com.besscroft.aurora.mall.common.entity.AuthRole;
import com.besscroft.aurora.mall.common.entity.AuthUser;
import com.besscroft.aurora.mall.admin.converter.UserConverterMapper;
import com.besscroft.aurora.mall.common.result.AjaxResult;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements UserService {

    private final AuthFeignClient authFeignClient;
    private final AuthRoleMapper authRoleMapper;
    private final MenuService menuService;
    private final HttpServletRequest request;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public AjaxResult login(String username, String password) {
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            log.error("?????????????????????????????????");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstants.ADMIN_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        AjaxResult accessToken = authFeignClient.getAccessToken(params);
        log.info("accessToken:{}", accessToken);
        redisTemplate.opsForValue().set(AuthConstants.ADMIN_CLIENT_ID + ":token:user:" + username, accessToken.get("access_token").toString());
        return accessToken;
    }

    @Override
    public boolean logout(Long adminId) {
        AuthUser user = this.baseMapper.selectById(adminId);
        redisTemplate.delete(AuthConstants.ADMIN_CLIENT_ID + ":token:user:" + user.getUsername());
        redisTemplate.boundHashOps("admin").delete("user:tree:" + adminId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setLoginTime(LocalDateTime loginTime, Long id) {
        return this.baseMapper.updateLoginTime(loginTime, id) > 0;
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        AuthUser authUser = this.baseMapper.selectAuthUserByUsername(username);
        if (authUser != null) {
            List<AuthRole> authRoles = authRoleMapper.selectAuthRoleListByAdminId(authUser.getId());
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(authUser, userDto);
            if (CollUtil.isNotEmpty(authRoles)) {
                List<String> roleStrList = authRoles.stream().map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
                userDto.setRoles(roleStrList);
            }
            return userDto;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(AdminParam adminParam) {
        AuthUser authUser = AuthUser.builder().build();
        BeanUtils.copyProperties(adminParam, authUser);
        // ???????????????????????????
        authUser.setCreateTime(LocalDateTime.now());
        // ??????????????????
        authUser.setStatus(1);
        // ?????????????????????
        authUser.setPassword(new BCryptPasswordEncoder().encode(adminParam.getPassword()));
        // ??????????????????
        return this.baseMapper.insert(authUser) > 0;
    }

    @Override
    public AuthUser getCurrentAdmin() {
        String header = request.getHeader(AuthConstants.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(header)) {
            log.error("???????????????token????????????");
        }
        UserDto userDto = JSONUtil.toBean(header, UserDto.class);
        return this.baseMapper.selectById(userDto.getId());
    }

    @Override
    public List<AuthRole> getRoleList(Long adminId) {
        return authRoleMapper.selectAuthRoleListByAdminId(adminId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getUserInfo() {
        AuthUser currentAdmin = getCurrentAdmin();
        Map<String, Object> data = menuService.getTreeListById(currentAdmin.getId());
        data.put("username", currentAdmin.getNickName());
        data.put("icon", currentAdmin.getIcon());
        List<AuthRole> roleList = getRoleList(currentAdmin.getId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(AuthRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        // ??????????????????
        setLoginTime(LocalDateTime.now(), currentAdmin.getId());
        return data;
    }

    @Override
    public List<AuthUser> getUserPageList(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<AuthUser> users = this.baseMapper.selectUserListByPage(keyword);
        users.forEach(user -> user.setPassword(""));
        return users;
    }

    @Override
    public AuthUser getUserById(Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(AuthUser authUser) {
        return this.baseMapper.updateUser(authUser) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeSwitch(boolean flag, Long id) {
        int status;
        if (flag) {
            status = 1;
        } else {
            status = 0;
        }
        return this.baseMapper.changeSwitch(status, id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delUser(Long id) {
        return this.baseMapper.delUser(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(AuthUser authUser) {
        // ???????????????????????????
        authUser.setCreateTime(LocalDateTime.now());
        // ?????????????????????????????????????????????
        authUser.setLoginTime(LocalDateTime.now());
        // ????????????
        authUser.setPassword(new BCryptPasswordEncoder().encode(authUser.getPassword()));
        // ??????????????????
        authUser.setDel(1);
        return this.baseMapper.insertUser(authUser) > 0;
    }

    @Override
    public List<AuthUser> getUserAllList() {
        return this.baseMapper.getAllList();
    }

    @Override
    public void export(List<Long> ids, HttpServletResponse response) {
        List<AuthUser> userList = this.baseMapper.selectBatchIds(ids);
        if (CollUtil.isNotEmpty(userList)) {
            List<AuthUserExcelDto> excelDtos = UserConverterMapper.INSTANCE.authUserToAuthUserExcelListDto(userList);
            excelDtos.forEach(excelDto -> {
                String status = excelDto.getStatus();
                switch (status) {
                    case "0":
                        excelDto.setStatus("??????");
                        break;
                    case "1":
                        excelDto.setStatus("??????");
                        break;
                }
                String del = excelDto.getDel();
                switch (del) {
                    case "0":
                        excelDto.setDel("?????????");
                        break;
                    case "1":
                        excelDto.setDel("????????????");
                        break;
                }
            });
            try {
                // ???????????? ????????????????????? swagger ?????????????????????????????????????????????????????? postman
                response.setContentType("application/vnd.ms-excel");
                // ???????????????????????????
                response.setCharacterEncoding("utf-8");
                // ?????? URLEncoder.encode ???????????????????????? ????????? easyexcel ????????????
                String fileName = URLEncoder.encode("????????????", "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                EasyExcel.write(response.getOutputStream(), AuthUserExcelDto.class).autoCloseStream(true).sheet("????????????").doWrite(excelDtos);
            } catch (IOException e) {
                log.error("excel ????????????.", e);
            }
        }
    }

}
