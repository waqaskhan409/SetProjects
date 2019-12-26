package com.example.setprojects.interfaces;

import com.example.setprojects.model.Projects;
import com.example.setprojects.model.TestClas;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonApiHolder {
    @Multipart
    @POST("create_projects_data")
    Call<TestClas> createProjectData(
            @Part MultipartBody.Part image,
            @Part("district") RequestBody district,
            @Part("project_id_psdp") RequestBody psdp,
            @Part("lat") RequestBody lat,
            @Part("lng") RequestBody lng,
            @Part("projects_id") RequestBody project_id
    );

    @GET("get_projects_data")
    Call<List<Projects>> getDepartment();
}
