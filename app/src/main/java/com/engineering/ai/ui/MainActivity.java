package com.engineering.ai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.engineering.ai.adapter.UserAdapter;
import com.engineering.ai.model.ResponseData;
import com.engineering.ai.model.User;
import com.engineering.ai.model.UserData;
import com.engineering.ai.network.APIClient;
import com.engineering.ai.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<User> Users = new ArrayList<>();
    UserAdapter adapter;
    ApiInterface apiInterface;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager llm;
    boolean hasMore = true;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int offsetCurr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity);
        rv = findViewById(R.id.userData);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        adapter = new UserAdapter(Users, getApplicationContext(), width);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);


        //api call
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        if (hasMore)
            callAPi(offsetCurr);


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = rv.getChildCount();
                totalItemCount = llm.getItemCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    if (hasMore) {
                        offsetCurr = offsetCurr + 10;
                        callAPi(offsetCurr);
                    }

                    loading = true;
                }
            }
        });


    }

    private void callAPi(int offset) {
        Call<ResponseData> call = apiInterface.getUsers(offset, 10);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                List<UserData> userData = response.body().getData().getUsers();
                hasMore = response.body().getData().getHasMore();
                for (UserData user : userData) {
                    ArrayList<String> listdata = new ArrayList<String>();
                    Users.add(new User(user.getName(), user.getImage(), user.getItems()));
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("failure", "fail");
            }
        });
    }
}
