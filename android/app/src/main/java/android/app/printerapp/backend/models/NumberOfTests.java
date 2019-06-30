package android.app.printerapp.backend.models;

/**
 * Created by eminahromic on 2017-12-12.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberOfTests {

    @SerializedName("generalTests")
    @Expose
    private Integer generalTests;
    @SerializedName("agingTests")
    @Expose
    private Integer agingTests;
    @SerializedName("stressRelievingTests")
    @Expose
    private Integer stressRelievingTests;
    @SerializedName("hardeningTests")
    @Expose
    private Integer hardeningTests;
    @SerializedName("tamperingTests")
    @Expose
    private Integer tamperingTests;
    @SerializedName("solutionTreatmenTest")
    @Expose
    private Integer solutionTreatmenTest;

    public Integer getGeneralTests() {
        return generalTests;
    }

    public void setGeneralTests(Integer generalTests) {
        this.generalTests = generalTests;
    }

    public Integer getAgingTests() {
        return agingTests;
    }

    public void setAgingTests(Integer agingTests) {
        this.agingTests = agingTests;
    }

    public Integer getStressRelievingTests() {
        return stressRelievingTests;
    }

    public void setStressRelievingTests(Integer stressRelievingTests) {
        this.stressRelievingTests = stressRelievingTests;
    }

    public Integer getHardeningTests() {
        return hardeningTests;
    }

    public void setHardeningTests(Integer hardeningTests) {
        this.hardeningTests = hardeningTests;
    }

    public Integer getTamperingTests() {
        return tamperingTests;
    }

    public void setTamperingTests(Integer tamperingTests) {
        this.tamperingTests = tamperingTests;
    }

    public Integer getSolutionTreatmenTest() {
        return solutionTreatmenTest;
    }

    public void setSolutionTreatmenTest(Integer solutionTreatmenTest) {
        this.solutionTreatmenTest = solutionTreatmenTest;
    }
}