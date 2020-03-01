package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.assignment.Models.Docs;
import com.example.assignment.Models.Model;
import com.example.assignment.Reterofit.NetworkClient;
import com.example.assignment.Reterofit.RequestService;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ArrayList<Docs> docs = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: ");

        final ProgressBar progressBar=findViewById(R.id.progress_bar);
        //progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        recyclerView=findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(savedInstanceState==null)
        {
            Retrofit retrofit = NetworkClient.getRetrofitClient();
            final RequestService requestService=retrofit.create(RequestService.class);
            Call<Model> call=requestService.requestGet();

            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    progressBar.setVisibility(View.INVISIBLE);

                    docs =new ArrayList<>((Arrays.asList(response.body().getResponse().getDocs())));

                    recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(),docs));
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        recyclerView.setAdapter(null);
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onResume()
    {
        if(recyclerView.getAdapter()==null)
        {
            recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(),docs));
        }
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }
}
