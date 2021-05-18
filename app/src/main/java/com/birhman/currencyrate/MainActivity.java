package com.birhman.currencyrate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.birhman.currencyrate.adapter.CurrencyAdapter;
import com.birhman.currencyrate.api.ApiCallBack;
import com.birhman.currencyrate.api.ApiManager;
import com.birhman.currencyrate.api.ApiResponseListener;
import com.birhman.currencyrate.listener.DeleteListener;
import com.birhman.currencyrate.local.AppConstant;
import com.birhman.currencyrate.local.SavedPrefManager;
import com.birhman.currencyrate.local.SqliteHelperDatabase;
import com.birhman.currencyrate.model.CurrencyResponse;
import com.birhman.currencyrate.model.MyCustomModel;
import com.birhman.currencyrate.model.RatesModel;
import com.birhman.currencyrate.utils.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements DeleteListener, View.OnClickListener {
    private Toolbar toolbar;
    private Context context;
    private AVLoadingIndicatorView indicatorView;
    RatesModel listOfHomeData;
    SqliteHelperDatabase helperDatabase;
    List<MyCustomModel> customEntry = new ArrayList<>();
    CurrencyAdapter adapter;
    RecyclerView recyclerView;
    AppCompatEditText editText;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        helperDatabase = new SqliteHelperDatabase(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(-1);
        toolbar.setSubtitleTextColor(-1);
        setSupportActionBar(toolbar);
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rl_quiz);
        editText = findViewById(R.id.edt_value);
        RelativeLayout relativeLayoutButton = findViewById(R.id.rl_button);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        indicatorView = findViewById(R.id.avi);
        indicatorView.smoothToHide();

        relativeLayoutButton.setOnClickListener(this::onClick);

        adapter = new CurrencyAdapter(customEntry, R.layout.home_item, context);
        recyclerView.setAdapter(adapter);

        checkDataExits();
    }

    private void checkDataExits() {
        customEntry = helperDatabase.getAllCategory();
        if (customEntry != null && !customEntry.isEmpty()) {
            String title = SavedPrefManager.getStringPreferences(context, AppConstant.TITLE);
            String subTitle = SavedPrefManager.getStringPreferences(context, AppConstant.SUB_TITLE);
            toolbar.setTitle(title);
            toolbar.setSubtitle(subTitle);
            adapter = new CurrencyAdapter(customEntry, R.layout.home_item, context);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setClickListener(MainActivity.this::OnDeleteItemClicked);
        } else {
            getDataFromServer();
        }
    }

    private void getDataFromServer() {
        if (AppUtils.isNetworkAvailable(context)) {
            indicatorView.smoothToShow();
            ApiManager apiManager = new ApiManager(context, false);
            ApiCallBack<CurrencyResponse> callBack = new ApiCallBack<>(new ApiResponseListener<CurrencyResponse>() {
                @Override
                public void onApiSuccess(CurrencyResponse response, String apiName) {
                    try {
                        listOfHomeData = response.getRates();
                        if (listOfHomeData != null) {
                            SavedPrefManager.saveStringPreferences(context, AppConstant.TITLE, response.getBase());
                            SavedPrefManager.saveStringPreferences(context, AppConstant.SUB_TITLE, response.getDate());
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map<String, String> meMap = objectMapper.convertValue(listOfHomeData, Map.class);

                            Set<Map.Entry<String, String>> set =  meMap.entrySet();
                            List<Map.Entry<String , String>> list = new ArrayList<Map.Entry<String , String>>    (set);
                            helperDatabase.addCurrency(list);
                            checkDataExits();
                        } else {
                            Toast.makeText(context.getApplicationContext(), "No Data Found!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    indicatorView.smoothToHide();
                }

                @Override
                public void onApiError(String responses, String apiName) {
                    indicatorView.smoothToHide();
                    Toast.makeText(context.getApplicationContext(), "The information you have entered is incorrect. Please try again.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onApiFailure(String failureMessage, String apiName) {
                    indicatorView.smoothToHide();
                }
            }, AppConstant.GET_RATE_API, context);
            apiManager.getCurrencyRateApi(callBack);
        }
    }

    @Override
    public void OnDeleteItemClicked(View view, int position, String title) {
        String id = customEntry.get(position).getId();
        helperDatabase.deleteEntry(id);
        adapter.notifyDataSetChanged();
        checkDataExits();
        Toast.makeText(MainActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            helperDatabase.deleteCurrencyTable();
            checkDataExits();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_button:
                if (validate()) {
                    try {
                        String myStrValue = editText.getText().toString().trim();
                        double doubleValue = Double.parseDouble(myStrValue);
                        for (int i=0; i < customEntry.size(); i++) {
                            double oldValue = Double.parseDouble(customEntry.get(i).getaDouble());
                            String newValue = String.valueOf(oldValue * doubleValue);
                            helperDatabase.update(customEntry.get(i).getId(), customEntry.get(i).getStrKey(), newValue);
                        }
                        checkDataExits();
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        String mValue = editText.getText().toString().trim();

        if (mValue.isEmpty()) {
            editText.setError("Enter value");
            valid = false;
        } else {
            editText.setError(null);
        }

        return valid;
    }
}