package com.liy.event;

import com.liy.entity.FriendLink;
import com.liy.exception.BusinessException;
import com.liy.entity.Resource;
import com.liy.mapper.ResourceMapper;
import com.liy.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataListener {

    private final ResourceMapper resourceMapper;

    private final EmailService emailService;


    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleDataSyncEvent(DataEvent event) {
        // 检查 event 是否为 null
        if (event == null || event.getData() == null) {
            log.error("Event and Event data must not be null.");
            return;
        }

        try {
            switch (event.getEventEnum()) {
                case RESOURCE_ADD:
                    if (event.getData() instanceof Resource) {
                        resourceMapper.insert((Resource) event.getData());
                    } else {
                        // 记录日志或抛出异常，表示数据类型不匹配
                        throw new BusinessException("Data for RESOURCE_ADD must be an instance of Resource.");
                    }
                    break;
                case EMAIL_SEND:
                    if (event.getData() instanceof FriendLink) {
                        emailService.sendFriendEmail((FriendLink) event.getData());
                    } else {
                        // 记录日志或抛出异常，表示数据类型不匹配
                        throw new BusinessException("Data for RESOURCE_ADD must be an instance of Resource.");
                    }
                    break;
                default:
                    // 记录日志，表示遇到了未处理的事件类型
                    log.warn("Unhandled event type: " + event.getEventEnum());
                    break;
            }
        } catch (Exception e) {
            // 捕获并处理可能的异常，例如记录日志
            log.error("Error handling event: " + event.getEventEnum(), e);
        }
    }
}
