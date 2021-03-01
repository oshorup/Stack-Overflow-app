package com.app.development.stackoverflowapi.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.development.stackoverflowapi.Model.ApiClient;
import com.app.development.stackoverflowapi.Model.ApiService;
import com.app.development.stackoverflowapi.Model.EachQuestion;
import com.app.development.stackoverflowapi.Model.QuestionList;
import com.app.development.stackoverflowapi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    SwipeRefreshLayout rootlayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int page_size;
    ProgressDialog progressDialog;

    FloatingActionButton google_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        page_size = sharedPreferences.getInt("page_size", 1);

        rootlayout = findViewById(R.id.root);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Questions, please wait..");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new Adapter(this,recyclerView);
        recyclerView.setAdapter(adapter);

        google_search=findViewById(R.id.google_search);

        Log.e("page_size_inside_Create",page_size+"");
        if(isConnectionAvailable(this)){
            loadData("", page_size + "");
        }else {
            Toast.makeText(this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }

        rootlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showDialogForTagInput();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rootlayout.setRefreshing(false);
                    }
                }, 500);

            }
        });

        google_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGoogleIntent = new Intent(MainActivity.this,WebViewActivity.class);
                openGoogleIntent.putExtra("URL","https://www.google.com/");
                startActivity(openGoogleIntent);
            }
        });

    }


    private void loadData(String Tags, String PageNo) {
        progressDialog.show();
        page_size = sharedPreferences.getInt("page_size", 1);
        Log.e("page_size_inside_loadDa",page_size+"");
        String URL;
        if (Tags.equals("") && PageNo.equals("")) {
            Log.e("location","00");
            URL = "2.2/questions?page=" + page_size + "&pagesize=100&order=desc&sort=votes&site=stackoverflow&key= Insert Your API Key here";
        } else if (Tags.equals("")) {
            Log.e("location","01");
            URL = "2.2/questions?page=" + PageNo + "&pagesize=100&order=desc&sort=votes&site=stackoverflow&key= Insert Your API Key here";
        } else if (PageNo.equals("")) {
            Log.e("location","10");
            URL = "2.2/questions?page=" + page_size + "&pagesize=100&order=desc&sort=votes&tagged=" + Tags + "&site=stackoverflow&key= Insert Your API Key here";
        } else {
            Log.e("location","11");
            URL = "2.2/questions?page=" + PageNo + "&pagesize=100&order=desc&sort=votes&tagged=" + Tags + "&site=stackoverflow&key= Insert Your API Key here";
        }

        final ApiClient apiClient = ApiService.getRetrofit().create(ApiClient.class);
        Call<QuestionList> questionListCall = apiClient.getQuestion(URL);
        questionListCall.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.isSuccessful()) {
                    List<EachQuestion> questions= response.body().getItems();

                    if (questions.size() != 0) {
                        editor = sharedPreferences.edit();
                        Log.e("page_size_after_loadDa",sharedPreferences.getInt("page_size",1)+"");
                        editor.putInt("page_size", sharedPreferences.getInt("page_size", 1) + 1);
                        editor.apply();
                        adapter.updateList(questions);

                    } else {
                        Log.e("page_size_failed_loadDa",sharedPreferences.getInt("page_size",1)+"");
                        showSnackBar("Unable to fetch new questions. You may have entered invalid TAG combination or page number");
                        editor = sharedPreferences.edit();
                        editor.putInt("page_size", 1);
                        editor.apply();
                    }
                } else {
                    Log.e("page_size_failed_loadD2",sharedPreferences.getInt("page_size",1)+"");
                    editor = sharedPreferences.edit();
                    editor.putInt("page_size", 1);
                    editor.apply();
                    showSnackBar("Page number's limit exceeded. Unable to fetch new questions.");
                    Log.e("error",response.message());
                }
            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("OnFailure",t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putInt("page_size", sharedPreferences.getInt("page_size", 1));
        editor.apply();
        Log.e("page_size_inside_Pause",page_size+"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor = sharedPreferences.edit();
        editor.putInt("page_size", sharedPreferences.getInt("page_size", 1));
        editor.apply();
        Log.e("page_size_inside_Stop",page_size+"");

    }

    private void showDialogForTagInput() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View view = getLayoutInflater().inflate(R.layout.dialog, null);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlide;
        dialog.setView(view);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editTextTags = view.findViewById(R.id.editText_TAG);
                String Tags = editTextTags.getText().toString().trim();
                EditText editTextPageNo = view.findViewById(R.id.editText_page_no);
                String pageNo = editTextPageNo.getText().toString().trim();
                if(isConnectionAvailable(MainActivity.this)){
                    loadData(Tags, pageNo);
                }else {
                    Toast.makeText(MainActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(rootlayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Fetch again", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForTagInput();
            }
        });
        snackbar.show();
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.question_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
