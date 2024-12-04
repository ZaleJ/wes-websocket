package com.quicktron.websocket.enums;

public interface RcsConstants {
    /**
     * 区别货架还是料箱
     */
    String QPRcsJobName = "QP";
    /**
     * 区别作业任务还是料箱的变化，其中料箱变化包含料箱位置变化和料箱出场等
     */
    String RcsJobStateTypeName = "JobStateEvent";
    String EVENT_TYPE_CONTAINER_UPDATE = "ContainerUpdateEvent";
    String EVENT_TYPE_ROBOT_UPDATE = "ChangeRobotReportEvent";
}
