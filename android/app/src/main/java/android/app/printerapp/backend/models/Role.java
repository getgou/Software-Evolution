
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Role {

    @SerializedName("users")
    @Expose
    private List<Object> users = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public List<Object> getUsers() {
        return users;
    }

    public void setUsers(List<Object> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
