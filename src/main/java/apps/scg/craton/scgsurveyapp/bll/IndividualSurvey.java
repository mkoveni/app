package apps.scg.craton.scgsurveyapp.bll;

import java.io.Serializable;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class IndividualSurvey implements Serializable{

    private int id;
    private String referenceNo;
    private boolean synced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
