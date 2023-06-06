package com.maxipago;

import com.maxipago.enums.ReportsPeriodEnum;

import javax.xml.bind.annotation.XmlElement;

public class FilterOptions {
    @XmlElement
    public String transactionId;

    @XmlElement
    public String referenceNumber;

    @XmlElement
    public String orderId;

    @XmlElement
    public ReportsPeriodEnum period;

    @XmlElement
    public Integer pageSize;

    @XmlElement
    public String startDate;

    @XmlElement
    public String endDate;

    @XmlElement
    public String startTime;

    @XmlElement
    public String endTime;

    @XmlElement
    public String orderByName;

    @XmlElement
    public String orderByDirection;

    public FilterOptions setPageToken(String pageToken) {
        this.pageToken = pageToken;
        return this;
    }

    public FilterOptions setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    @XmlElement
    public String pageToken;

    @XmlElement
    public Integer pageNumber;

    public FilterOptions setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public FilterOptions setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public FilterOptions setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public FilterOptions setPeriod(ReportsPeriodEnum period) {
        this.period = period;
        return this;
    }

    public FilterOptions setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public FilterOptions setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public FilterOptions setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public FilterOptions setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public FilterOptions setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public FilterOptions setOrderByName(String orderByName) {
        this.orderByName = orderByName;
        return this;
    }

    public FilterOptions setOrderByDirection(String orderByDirection) {
        this.orderByDirection = orderByDirection;
        return this;
    }
}
