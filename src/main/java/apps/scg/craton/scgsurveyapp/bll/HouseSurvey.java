package apps.scg.craton.scgsurveyapp.bll;

import java.io.Serializable;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class HouseSurvey implements Serializable{

    private String referenceNo;
    private double longitude;
    private double latitude;
    private int projectId;
    private boolean synced;

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
