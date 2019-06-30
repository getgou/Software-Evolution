package android.app.printerapp.util.ui;

import android.app.Application;



public class MyApplication extends Application
{
    //region Class variables
    private String token;
    private String qrcode;
    private String role;
    private String partTestId;
    //endregion

    // region Getters
    public String getToken() {
        return token;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getRole() {
        return role;
    }

    public String getPartTestId() { return partTestId; }
    //endregion

    //region Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPartTestId(String partTestId) { this.partTestId = partTestId; }
    //endregion
}
