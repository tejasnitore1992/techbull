package com.techbull.InterviewTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private int pageNo = 1;
    private String searchTextStr = "batman";
    private boolean isPageLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchDtoList.size() - 1) {
                        pageNo = pageNo + 1;
                        isPageLoading = true;
                        getMovieList();

                    }

            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())) {
                    searchTextStr = "batman";
                }else {
                    searchTextStr = s.toString();
                }
                pageNo = 1;
                isPageLoading = false;
                getMovieList();
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getMovieList();
    }


    private void getMovieList() {
        progressDialog.setTitle("Movie Title");
        progressDialog.setMessage("Loading...");
        TechBullApi api = TechBullConfig.getClient(context).create(TechBullApi.class);
        Call<JsonElement> call = api.searchMovieShowName(searchTextStr,TechBullConfig.APIKEY,""+pageNo);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonObject jsonObject = response.body().getAsJsonObject();
                if(jsonObject.has("Search")){
                    JsonArray jsonArray = jsonObject.getAsJsonArray("Search");
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SearchDto>>(){}.getType();
                    List<SearchDto> temList = gson.fromJson(jsonArray,type);

                    if(!isPageLoading){
                        searchDtoList.clear();
                    }
                    if(temList != null){
                        searchDtoList.addAll(temList);
                    }
                    if(searchAdapter != null)
                        searchAdapter.notifyDataSetChanged();
                    if(temList.isEmpty()){
                        Toast toast = Toast.makeText(context,"Complete list load",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
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