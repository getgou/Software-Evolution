package android.app.printerapp.backend.models;

/**
 * Created by eminahromic on 2017-12-12.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralTest {

    @SerializedName("SupportRemoval")
    @Expose
    private Boolean supportRemoval;
    @SerializedName("WEDM")
    @Expose
    private Boolean wEDM;
    @SerializedName("wedmComment")
    @Expose
    private String wedmComment;
    @SerializedName("GeneralId")
    @Expose
    private Integer generalId;
    @SerializedName("Blasting")
    @Expose
    private Boolean blasting;
    @SerializedName("BlastingComments")
    @Expose
    private String blastingComments;
    @SerializedName("Type")
    @Expose
    private Integer type;

    public Boolean getSupportRemoval() {
        return supportRemoval;
    }

    public void setSupportRemoval(Boolean supportRemoval) {
        this.supportRemoval = supportRemoval;
    }

    public Boolean getWEDM() {
        return wEDM;
    }

    public void setWEDM(Boolean wEDM) {
        this.wEDM = wEDM;
    }

    public String getWedmComment() {
        return wedmComment;
    }

    public void setWedmComment(String wedmComment) {
        this.wedmComment = wedmComment;
    }

    public Integer getGeneralId() {
        return generalId;
    }

    public void setGeneralId(Integer generalId) {
        this.generalId = generalId;
    }

    public Boolean getBlasting() {
        return blasting;
    }

    public void setBlasting(Boolean blasting) {
        this.blasting = blasting;
    }

    public String getBlastingComments() {
        return blastingComments;
    }

    public void setBlastingComments(String blastingComments) {
        this.blastingComments = blastingComments;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
