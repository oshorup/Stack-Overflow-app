package com.app.development.stackoverflowapi.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiClient {
//2.2/questions?page=1&pagesize=5&order=asc&sort=creation&site=stackoverflow
//2.2/questions?page=10&pagesize=5&order=desc&sort=activity&site=stackoverflow&key=
    @GET
    Call<QuestionList> getQuestion(@Url String  url);


}
