package com.box.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.box.imcommon.model.GroupMessageInfo;
import com.box.implatform.entity.GroupMessage;
import com.box.implatform.vo.GroupMessageVO;

import java.util.List;


public interface IGroupMessageService extends IService<GroupMessage> {


    Long sendMessage(GroupMessageVO vo);

    void recallMessage(Long id);

    void pullUnreadMessage();

    List<GroupMessageInfo> findHistoryMessage(Long groupId, Long page, Long size);
}
