package android.app.printerapp.backend;


import android.app.printerapp.backend.models.AgingTreatmentTest;
import android.app.printerapp.backend.models.BuildFiles;
import android.app.printerapp.backend.models.CreateAgingTest;
import android.app.printerapp.backend.models.CreateHardeningTest;
import android.app.printerapp.backend.models.CreateSolutionTest;
import android.app.printerapp.backend.models.CreateStressRelieving;
import android.app.printerapp.backend.models.CreateTamperingTest;
import android.app.printerapp.backend.models.GeneralTest;
import android.app.printerapp.backend.models.HardeningTest;
import android.app.printerapp.backend.models.NumberOfTests;
import android.app.printerapp.backend.models.PartFile;
import android.app.printerapp.backend.models.PrintingData;
import android.app.printerapp.backend.models.SolutionTreatmentTest;
import android.app.printerapp.backend.models.StressRelieving;
import android.app.printerapp.backend.models.TamperingTest;
import android.app.printerapp.backend.models.Token;
import android.app.printerapp.backend.models.UpdateAgingTreatmentTest;
import android.app.printerapp.backend.models.UpdateGeneral;
import android.app.printerapp.backend.models.UpdateHardening;
import android.app.printerapp.backend.models.UpdateSolutionTreatment;
import android.app.printerapp.backend.models.UpdateStressRelieving;
import android.app.printerapp.backend.models.UpdateTampering;
import android.app.printerapp.backend.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface endpointDB
{
    //region Getting a Token
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("token")
    Call<Token> getToken(@Field("grant_type") String grantType, @Field("username") String username, @Field("password") String password);
    //endregion

    //region Requesting Printing Data by Build QRCode
    @Headers("Content-Type: application/json")
    @GET("api/PrintingInfo/GetPrintingInfo")
    Call<PrintingData> getPrintingDataByBuildQRCode(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Requesting Printing Data by Part QR Code
    @Headers("Content-Type: application/json")
    @GET("api/PrintingInfo/GetPrintingInfoByPartQRCode")
    Call<PrintingData> getPrintingDataByPartQRCode(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Modifying Printing Data by Build QR Code or Part QRCode
    @Headers("Content-Type: application/json")
    @PUT("api/PrintingInfo/UpdatePrintingInfo")
    Call<PrintingData> updatePrintingData(@Query("qrcode") String buildID, @Body PrintingData data, @Header("Authorization") String token);
    //endregion

    //region Requesting Files corresponding to the build
    @Headers("Content-Type: application/json")
    @GET("api/Build/GetBuild")
    Call<BuildFiles> getBuildFiles(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Requesting if a QR Code exists
    @Headers("Content-Type: application/json")
    @GET("api/{type}/{method}")
    Call<Boolean> doesPartIDExists(@Path("type") String type, @Path("method") String method, @Query("qrcode")String partID, @Header("Authorization") String token);
    //endregion

    // region Requesting Roles for a given user
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/Account/GetInfoLoggedInUser")
    Call<User> getUserRole(@Header("Authorization") String token);
    //endregion

    //region Requesting names of file associated to a given part
    @Headers("Content-Type: application/json")
    @GET("api/Part")
    Call<List<PartFile>> getPartFiles(@Query("qrcode") String partID, @Header("Authorization") String token);
    //endregion

    //region Requesting Stress Relieving test
    @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetStressTest")
    Call<List<StressRelieving>> getStressRelievingTest(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Modifying Stress Relieving Test Data
    //region Requesting if a QR Code exists
    @Headers("Content-Type: application/json")
    @PUT("api/PartTest/UpdatePartTest")
    Call<String> updateStressRelievingData(@Body UpdateStressRelieving data, @Header("Authorization") String token);

    //region Requesting Hardening test
    @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetHardeningTest")
    Call<List<HardeningTest>> getHardeningTest(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Modifying Hardening Test Data
    @Headers("Content-Type: application/json")
    @PUT("api/PartTest/UpdatePartTest")
    Call<String> updateHardeningData(@Body UpdateHardening data, @Header("Authorization") String token);
    //endregion

    //region Requesting General test
    @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetGeneralTest")
    Call<GeneralTest> getGeneralTest(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

    //region Modifying General Test Data
    @Headers("Content-Type: application/json")
    @PUT("api/PartTest/UpdateGeneralTest")
    Call<String> updateGeneralData(@Body UpdateGeneral data, @Header("Authorization") String token);
    //endregion

    //region Requesting Tampering test
    @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetTamperingTest")
    Call<List<TamperingTest>> getTamperingTest(@Query("qrcode") String buildID, @Header("Authorization") String token);

    //region Modifying Tampering Test Data
    @Headers("Content-Type: application/json")
    @PUT("api/PartTest/UpdatePartTest")
    Call<String> updateTamperingData(@Body UpdateTampering data, @Header("Authorization") String token);
    //endregion

    //region Requesting Aging Treatment test
    @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetAgingTest")
    Call<List<AgingTreatmentTest>> getAgingTreatmentTest(@Query("qrcode") String buildID, @Header("Authorization") String token);

    //region Modifying Aging Treatment Test Data
    @Headers("Content-Type: application/json")
    @PUT("api/PartTest/UpdatePartTest")
    Call<String> updateAgingTreatmentData(@Body UpdateAgingTreatmentTest data, @Header("Authorization") String token);
    //endregion

    //region Requesting Solution Treatment Test Data
    // @Headers("Content-Type: application/json")
    @GET("api/PartTest/GetSolutionTest")
    Call<List<SolutionTreatmentTest>> getSolutionTreatmentTest(@Query("qrcode") String buildID, @Header("Authorization") String token);
    //endregion

     //region Modifying Solution Treatment Test Data
     @Headers("Content-Type: application/json")
     @PUT("api/PartTest/UpdatePartTest")
     Call<String> updateSolutionTreatmentData(@Body UpdateSolutionTreatment data, @Header("Authorization") String token);
    //endregion

    //region Requesting Number of Tests
    @Headers("Content-Type: application/json")
     @GET("api/PartTest/GetNumberOfTests")
     Call<NumberOfTests> getNumberOfPartTests(@Query("qrcode") String buildID, @Header("Authorization") String token);
     //endregion

    //region Deleting part Tests
    @Headers("Content-Type: application/json")
    @DELETE("api/PartTest/DeletePartTest")
    Call<String> deletePartTest(@Query("partTestId") int partTestId, @Header("Authorization") String token);
     //endregion

     //region Deleting Str
     // ess Relieving Tests
    @Headers("Content-Type: application/json")
    @DELETE("api/PartTest/DeleteStressTest")
     Call<String> deleteStressTest(@Query("partTestId") int partTestId, @Header("Authorization") String token);
    //endregion

    //region Create New Stress Test
    @Headers("Content-Type: application/json")
    @POST("api/PartTest/CreateStressTest")
    Call<String> createStressTest(@Body CreateStressRelieving data, @Header("Authorization") String token);
    //endregion

    //region Create New Aging Treatment Test
    @Headers("Content-Type: application/json")
    @POST("api/PartTest/CreateAgingTest")
    Call<String> createAgingTest(@Body CreateAgingTest data, @Header("Authorization") String token);
    //endregion

    //region Create New Hardening Test
    @Headers("Content-Type: application/json")
    @POST("api/PartTest/CreateHardeningTest")
    Call<String> createHardeningTest(@Body CreateHardeningTest data, @Header("Authorization") String token);
    //endregion

    //region Create New Solution Treatment Test
    @Headers("Content-Type: application/json")
    @POST("api/PartTest/CreateSolutionTest")
    Call<String> createSolutionTest(@Body CreateSolutionTest data, @Header("Authorization") String token);
    //endregion

    //region Create New Tampering Test
    @Headers("Content-Type: application/json")
    @POST("api/PartTest/CreateTamperingTest")
    Call<String> createTamperingTest(@Body CreateTamperingTest data, @Header("Authorization") String token);
    //endregion
}
