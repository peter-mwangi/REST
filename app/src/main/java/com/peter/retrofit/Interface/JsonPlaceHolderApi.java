package com.peter.retrofit.Interface;

import com.peter.retrofit.model.Comments;
import com.peter.retrofit.model.Posts;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi
{
    @GET("posts")
    Call<List<Posts>> getPosts(
            @Query("userId") int[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order);

    @GET("posts")
    Call<List<Posts>> getPosts(

            @QueryMap Map<String,String> parameters
    );

    @GET("posts/{id}/comments")
    Call<List<Comments>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comments>> getComments(@Url String url);

    @POST("posts")
    Call<Posts> createPost(@Body Posts posts);

    @FormUrlEncoded
    @POST("posts")
    Call<Posts> createPost
            (
                    @Field("userId") int userId,
                    @Field("title") String title,
                    @Field("body") String text
            );

    @FormUrlEncoded
    @POST("posts")

    Call<Posts> createPost(@FieldMap Map<String,String> fields);

    @Headers({"Static-Header1: 123","StaticHeader2: 456"})
    @PUT("posts/{id}")
    Call<Posts> putPost(@Header ("Dynamic-Header") String header, @Path("id") int id, @Body Posts posts);

    @PATCH("posts/{id}")
    Call<Posts> patchPost(@HeaderMap Map<String, String> headers,@Path("id") int id, @Body Posts posts);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
