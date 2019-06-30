package android.app.printerapp.backend;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient
{
    //region Class variables
    private static Retrofit retrofit;
    //TODO: 09/03/18 Put server adress here, something like http://192.168.1.100/
     //private static String BASE_URL="http://swereaapi-dev.eu-central-1.elasticbeanstalk.com/api/";
    //private static String BASE_URL ="http://172.16.98.16/webapi/api/";
	
	private static String BASE_URL = "http://localhost:58638/";
    

    //endregion

    public static Retrofit getClient()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
