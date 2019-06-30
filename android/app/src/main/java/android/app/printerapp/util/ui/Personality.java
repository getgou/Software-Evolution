package android.app.printerapp.util.ui;


import java.io.Serializable;

public class Personality implements Serializable
{
    //region Class variables and constructor
    private String id;
    private int typeOfData;

    public Personality(String id, int typeOfData)
    {
        this.id = id;
        this.typeOfData = typeOfData;
    }
    //endregion

    //region Getters
    public String getId()
    {
        return id;
    }

    public int getTypeOfData()
    {
        return typeOfData;
    }

    //endregion


} //End of class
