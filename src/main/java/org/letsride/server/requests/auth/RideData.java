package org.letsride.server.requests.auth;

import org.letsride.server.models.DataPoint;

import java.util.Date;
import java.util.List;

public class RideData {
    private List<DataPoint> dataPoints;
    private Date startDate;
    private Date endDate;

    public RideData() {}

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
