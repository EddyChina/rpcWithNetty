package com.eddy.rpc.client.task;

import java.io.Serializable;
import java.util.Objects;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.task.TaskInfomation
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 6:44 PM
 * ***********************************************
 */
public class HiveSqlRequestTask implements Serializable {

    private String taskId; // 任务ID
    private String committer; // 任务提交人
    private String hiveSql; // 提交查询hive sql
    private Long taskTime; // 任务提交时间
    private int priority; // 任务优先级
    private boolean cancel;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCommitter() {
        return committer;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }

    public String getHiveSql() {
        return hiveSql;
    }

    public void setHiveSql(String hiveSql) {
        this.hiveSql = hiveSql;
    }

    public Long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Long taskTime) {
        this.taskTime = taskTime;
    }

    @Override
    public String toString() {
        return "TaskInformation{" +
                "taskId='" + taskId + '\'' +
                ", committer='" + committer + '\'' +
                ", hiveSql='" + hiveSql + '\'' +
                ", taskTime=" + taskTime +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HiveSqlRequestTask that = (HiveSqlRequestTask) o;

        if (cancel != that.cancel) return false;
        return taskId.equals(that.taskId);
    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + (cancel ? 1 : 0);
        return result;
    }
}
