package android.app.printerapp.backend.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eminahromic on 2017-12-12.
 */


public class UpdateGeneral {

    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("supportRemoval")
    @Expose
    private Boolean supportRemoval;
    @SerializedName("wedm")
    @Expose
    private Boolean wEDM;
    @SerializedName("wedmComments")
    @Expose
    private String wedmComment;
    @SerializedName("blasting")
    @Expose
    private Boolean blasting;
    @SerializedName("blastingComments")
    @Expose
    private String blastingComments;

    public String getQrcode() {return qrcode;}

    public void setQrcodeId(String qrcode) { this.qrcode = qrcode;}

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
}