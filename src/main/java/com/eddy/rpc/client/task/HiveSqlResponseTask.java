package com.eddy.rpc.client.task;

import java.io.Serializable;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.task.HiveSqlResponseTask
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 9:06 PM
 * ***********************************************
 */
public class HiveSqlResponseTask implements Serializable {
    private String responseId;
    private String responseData;
    private Long responseTime;

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    private HiveSqlRequestTask requestTask;

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public HiveSqlRequestTask getRequestTask() {
        return requestTask;
    }

    public void setRequestTask(HiveSqlRequestTask requestTask) {
        this.requestTask = requestTask;
    }

    @Override
    public String toString() {
        return "HiveSqlResponseTask{" +
                "responseId='" + responseId + '\'' +
                ", responseData='" + responseData + '\'' +
                ", responseTime=" + responseTime +
                ", requestTask=" + requestTask +
                '}';
    }
}
