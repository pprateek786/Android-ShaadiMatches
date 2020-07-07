package com.shaadi.shaadimatches;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shaadi.shaadimatches.adapters.ShowListAdapter;
import com.shaadi.shaadimatches.communication.APIClient;
import com.shaadi.shaadimatches.communication.APIEndPointInterface;
import com.shaadi.shaadimatches.database.ProfileDatabase;
import com.shaadi.shaadimatches.database.model.Profile;
import com.shaadi.shaadimatches.interfaces.OnProfileButtonClick;
import com.shaadi.shaadimatches.models.Result;
import com.shaadi.shaadimatches.models.ShaadiMatchesResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private APIEndPointInterface _apiInterface;
    private ProgressBar          _progressBar;
    private RecyclerView         _recyclerView;
    private ProfileDatabase      _profileDatabase;
    private ShaadiMatchesResult  _shaadiMatchesResult;
    private ShowListAdapter      _showListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        callWebService();
    }

    /**
     * Initialize views
     */
    private void init()
    {
        _progressBar = findViewById(R.id.progressBar);
        _recyclerView = findViewById(R.id.recyclerView);
        _profileDatabase = ProfileDatabase.getInstance(MainActivity.this);
    }

    /**
     * Call random user web service
     */
    private void callWebService()
    {
        _apiInterface = APIClient.getClient().create(APIEndPointInterface.class);
        _progressBar.setVisibility(View.VISIBLE);
        final Call<ShaadiMatchesResult> resultCall = _apiInterface.getUserList("10");
        resultCall.enqueue(new Callback<ShaadiMatchesResult>()
        {
            @Override
            public void onResponse(Call<ShaadiMatchesResult> call, Response<ShaadiMatchesResult> response)
            {
                _progressBar.setVisibility(View.GONE);
                _shaadiMatchesResult = response.body();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                _recyclerView.setLayoutManager(linearLayoutManager);
                 _showListAdapter = new ShowListAdapter(_shaadiMatchesResult.getResults(),onProfileButtonClick);
                _recyclerView.setAdapter(_showListAdapter);
                new InsertTask(_shaadiMatchesResult).execute();
            }

            @Override
            public void onFailure(Call<ShaadiMatchesResult> call, Throwable t)
            {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"API Failure",Toast.LENGTH_SHORT).show();
                resultCall.cancel();
            }
        });
    }

    OnProfileButtonClick onProfileButtonClick = new OnProfileButtonClick()
    {
        @Override
        public void onAcceptButtonClicked(int positionClicked)
        {
            Result result = _shaadiMatchesResult.getResults().get(positionClicked);
            _profileDatabase.getProfileDao().updateProfile(result.getLogin().getUsername(),true);
            _showListAdapter.notifyDataSetChanged();

        }

        @Override
        public void onDeclineButtonClicked(int positionClicked)
        {
            Toast.makeText(MainActivity.this,getResources().getString(R.string.decline),Toast.LENGTH_SHORT).show();
            Result result = _shaadiMatchesResult.getResults().get(positionClicked);
            _profileDatabase.getProfileDao().updateProfile(result.getLogin().getUsername(),false);
            _showListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _recyclerView = null;
        _progressBar = null;
        _profileDatabase.cleanUp();
        _shaadiMatchesResult = null;
        _showListAdapter = null;
        _apiInterface = null;
    }

    /**
     * Add result in database
     */
    private class InsertTask extends AsyncTask<Void, Void, Boolean>
    {
        private ShaadiMatchesResult matchesResult;
        InsertTask(ShaadiMatchesResult matchesResult) {
            this.matchesResult = matchesResult;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            for(int i = 0 ;i < matchesResult.getResults().size(); i++)
            {
                Profile profile = new Profile();
                profile.setUserID(matchesResult.getResults().get(i).getLogin().getUsername());
                profile.setUpdatedStatus(false);
                _profileDatabase.getProfileDao().insertProfile(profile);
            }
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool)
            {

            }
        }
    }
}
