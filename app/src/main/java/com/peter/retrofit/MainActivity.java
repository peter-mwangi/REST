package com.peter.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.peter.retrofit.Interface.JsonPlaceHolderApi;
import com.peter.retrofit.model.Comments;
import com.peter.retrofit.model.Posts;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.post_text_view);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException
                    {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header( "Interceptor-Header","Peter")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPosts();

//        getComments();

//        createPosts();

//        updatePost();

//        deletePost();
    }



    private void deletePost()
    {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {

                    textViewResult.setText("Code: "+response.code());


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                textViewResult.setText(t.getMessage());

            }
        });
    }


    private void updatePost()
    {
        Posts posts = new Posts(2,null,"Hello There");

        Map<String, String> headers = new HashMap<>();
        headers.put("Header-Map1","Hello");
        headers.put("Header-Map2", "Wonderful");

        Call<Posts> call = jsonPlaceHolderApi.patchPost(headers,5,posts);

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response)
            {
                if (!response.isSuccessful())
                {
                    textViewResult.setText("Code: "+response.code());
                }

                String content = "";
                Posts putResponse = response.body();

                content +="Code: "+ response.code()+"\n";
                content +="ID: "+ putResponse.getId()+"\n";
                content += "User Id: "+putResponse.getUserId()+"\n";
                content += "Title: "+ putResponse.getTitle()+"\n";
                content += "Body: "+putResponse.getText()+"\n\n";

                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t)
            {
                textViewResult.setText(t.getMessage());
            }
        });
    }



    private void createPosts()
    {
       Posts posts = new Posts(1,"Hello This My First Post","Congratulations buddy, you are doing great");

       Map<String,String> fields = new HashMap<>();

       fields.put("userId", "1");
       fields.put("title", "Hello There");
       fields.put("body", "This is a map object");

       Call<Posts> call = jsonPlaceHolderApi.createPost(fields);

       call.enqueue(new Callback<Posts>()
       {
           @Override
           public void onResponse(Call<Posts> call, Response<Posts> response)
           {
               if (!response.isSuccessful())
               {
                   textViewResult.setText("Code: "+response.code());
                   return;
               }

               Posts postResponse = response.body();

               String content = "";

               content += "Code: "+response.code()+"\n";
               content += "ID: "+postResponse.getId()+"\n";
               content += "User Id: "+postResponse.getUserId()+"\n";
               content += "Title: "+postResponse.getTitle()+"\n";
               content += "Body: "+postResponse.getText()+"\n\n";

               textViewResult.append(content);

           }

           @Override
           public void onFailure(Call<Posts> call, Throwable t)
           {
               textViewResult.setText(t.getMessage());

           }
       });
    }



    private void getComments()
    {
        Call<List<Comments>> call = jsonPlaceHolderApi.getComments("posts/3/comments");
        call.enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response)
            {
                if (!response.isSuccessful())
                {
                    textViewResult.setText("Code: "+response.code());
                    return;
                }
                List<Comments> comments = response.body();

                for (Comments comment : comments)
                {
                    String content = "";

                    content += "ID: "+comment.getId()+"\n";
                    content +="Post ID: "+comment.getPostId()+"\n";
                    content +="Name: "+comment.getName()+"\n";
                    content +="Email: "+comment.getEmail()+"\n";
                    content +="Body: "+comment.getText()+"\n\n";

                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t)
            {
                textViewResult.setText(t.getMessage());

            }
        });
    }


    private void getPosts()
    {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        Call<List<Posts>> call = jsonPlaceHolderApi.getPosts(parameters);
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response)
            {
                if (!response.isSuccessful())
                {
                    textViewResult.setText("Code: "+response.code());
                    return;
                }

                List<Posts> posts = response.body();

                for (Posts post : posts)
                {
                    String content = "";
                    content +="ID: "+post.getId()+"\n";
                    content +="User Id: " + post.getUserId()+"\n";
                    content +="Title: " + post.getTitle()+"\n";
                    content +="Body: " + post.getText()+"\n\n";

                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable throwable)
            {
                textViewResult.setText(throwable.getMessage());

            }
        });

    }
}
