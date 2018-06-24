package com.grabbddemoapp.data.network;

import com.grabbddemoapp.data.model.CommonResponse;
import com.grabbddemoapp.data.model.explore.ExploreResponse;
import com.grabbddemoapp.data.model.search.SearchRespone;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Developer: Sandy
 * <p>
 * The API interface for your application
 */
public interface ApiInterface {


    /**
     * Dummy sign in endpoint
     *
     * @param map the map of params to go along with reqquest
     * @return parsed common response object
     */
    @FormUrlEncoded
    @POST("/signIn")
    Call<CommonResponse> signIn(@FieldMap Map<String, String> map);


    @GET("/v2/venues/explore")
    Call<ExploreResponse> explore(@QueryMap Map<String, String> map);

    @GET("/v2/venues/search")
    Call<SearchRespone> search(@QueryMap Map<String, String> map);

}
