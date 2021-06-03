package com.techbull.InterviewTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private CoordinatorLayout coordinator;
    private Toolbar toolbar;
    private AutoCompleteTextView searchText;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<SearchDto> searchDtoList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Boolean isLoading = false;
    private Boolean isVisibleProgressbar = false;
    private int searchDtoListCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        context = MainActivity.this;
        initView();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        init();
    }

    private void init(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        searchAdapter = new SearchAdapter(context,searchDtoList);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull  RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchDtoList.size() - 1) {
                        //bottom of list!
                        isLoading = true;
//                        loadData();

                    }
                }
            }
        });
        getApiKey();
    }

    private void getApiKey() {
        progressDialog.setTitle("ApiKey");
        progressDialog.setMessage("Loading...");
        TechBullApi api = TechBullConfig.getClient(context).create(TechBullApi.class);
        Call<JsonElement> call = api.getApiKey();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                showAlertDialogError(t.getMessage());
            }
        });
    }

    private void showAlertDialogError(String msg){
        if(!TextUtils.isEmpty(msg)){
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Error!!!");
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    private void initView() {
        coordinator = findViewById(R.id.coordinator);
        toolbar = findViewById(R.id.toolbar);
        searchText = findViewById(R.id.search_text);
        recyclerView = findViewById(R.id.recycler_view);
    }
}