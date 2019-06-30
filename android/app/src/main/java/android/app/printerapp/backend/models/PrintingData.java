
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrintingData {

    @SerializedName("printingInfoID")
    @Expose
    private Integer printingInfoID;
    @SerializedName("buildStatus")
    @Expose
    private String buildStatus;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("printingDate")
    @Expose
    private String printingDate;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("typeOfmachine")
    @Expose
    private String typeOfmachine;
    @SerializedName("powderWeightStart")
    @Expose
    private Double powderWeightStart;
    @SerializedName("powderweightEnd")
    @Expose
    private Double powderweightEnd;
    @SerializedName("weightPowderWaste")
    @Expose
    private Double weightPowderWaste;
    @SerializedName("powerUsed")
    @Expose
    private Double powerUsed;
    @SerializedName("platformMaterial")
    @Expose
    private String platformMaterial;
    @SerializedName("platformWeight")
    @Expose
    private Double platformWeight;
    @SerializedName("printedTime")
    @Expose
    private String printedTime;
    @SerializedName("powderCondition")
    @Expose
    private String powderCondition;
    @SerializedName("numberLayers")
    @Expose
    private Integer numberLayers;
    @SerializedName("dpcFactor")
    @Expose
    private Integer dpcFactor;
    @SerializedName("minExposureTime")
    @Expose
    private String minExposureTime;
    @SerializedName("comments")
    @Expose
    private Object comments;
    @SerializedName("buildId")
    @Expose
    private Integer buildId;

    public Integer getPrintingInfoID() {
        return printingInfoID;
    }

    public void setPrintingInfoID(Integer printingInfoID) {
        this.printingInfoID = printingInfoID;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrintingDate() {
        return printingDate;
    }

    public void setPrintingDate(String printingDate) {
        this.printingDate = printingDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTypeOfmachine() {
        return typeOfmachine;
    }

    public void setTypeOfmachine(String typeOfmachine) {
        this.typeOfmachine = typeOfmachine;
    }

    public Double getPowderWeightStart() {
        return powderWeightStart;
    }

    public void setPowderWeightStart(Double powderWeightStart) {
        this.powderWeightStart = powderWeightStart;
    }

    public Double getPowderweightEnd() {
        return powderweightEnd;
    }

    public void setPowderweightEnd(Double powderweightEnd) {
        this.powderweightEnd = powderweightEnd;
    }

    public Double getWeightPowderWaste() {
        return weightPowderWaste;
    }

    public void setWeightPowderWaste(Double weightPowderWaste) {
        this.weightPowderWaste = weightPowderWaste;
    }

    public Double getPowerUsed() {
        return powerUsed;
    }

    public void setPowerUsed(Double powerUsed) {
        this.powerUsed = powerUsed;
    }

    public String getPlatformMaterial() {
        return platformMaterial;
    }

    public void setPlatformMaterial(String platformMaterial) {
        this.platformMaterial = platformMaterial;
    }

    public Double getPlatformWeight() {
        return platformWeight;
    }

    public void setPlatformWeight(Double platformWeight) {
        this.platformWeight = platformWeight;
    }

    public String getPrintedTime() {
        return printedTime;
    }

    public void setPrintedTime(String printedTime) {
        this.printedTime = printedTime;
    }

    public String getPowderCondition() {
        return powderCondition;
    }

    public void setPowderCondition(String powderCondition) {
        this.powderCondition = powderCondition;
    }

    public Integer getNumberLayers() {
        return numberLayers;
    }

    public void setNumberLayers(Integer numberLayers) {
        this.numberLayers = numberLayers;
    }

    public Integer getDpcFactor() {
        return dpcFactor;
    }

    public void setDpcFactor(Integer dpcFactor) {
        this.dpcFactor = dpcFactor;
    }

    public String getMinExposureTime() {
        return minExposureTime;
    }

    public void setMinExposureTime(String minExposureTime) {
        this.minExposureTime = minExposureTime;
    }

    public Object getComments() {
        return comments;
    }

    public void setComments(Object comments) {
        this.comments = comments;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

}
