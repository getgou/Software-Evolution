
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartFile {

    @SerializedName("StlFileName")
    @Expose
    private String stlFileName;
    @SerializedName("PrtFileName")
    @Expose
    private String prtFileName;

    public String getStlFileName() {
        return stlFileName;
    }

    public void setStlFileName(String stlFileName) {
        this.stlFileName = stlFileName;
    }

    public String getPrtFileName() {
        return prtFileName;
    }

    public void setPrtFileName(String prtFileName) {
        this.prtFileName = prtFileName;
    }

}
