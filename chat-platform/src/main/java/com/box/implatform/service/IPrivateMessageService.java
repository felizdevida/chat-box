package com.box.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.box.imcommon.model.PrivateMessageInfo;
import com.box.implatform.entity.PrivateMessage;
import com.box.implatform.vo.PrivateMessageVO;

import java.util.List;


public interface IPrivateMessageService extends IService<PrivateMessage> {

    Long sendMessage(PrivateMessageVO vo);

    void recallMessage(Long id);

    List<PrivateMessageInfo> findHistoryMessage(Long friendId, Long page,Long size);

    void pullUnreadMessage();

}
