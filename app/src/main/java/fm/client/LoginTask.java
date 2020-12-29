package fm.client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import Model.AuthToken;
import Requests.LoginRequest;
import Result.LoginResult;

import static android.app.PendingIntent.getActivity;

class LoginTask extends AsyncTask<LoginRequest, Integer, LoginResult>{
    private Context mcontext;
    interface LoginTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }
    public LoginTask(Context context){
        mcontext = context;
    }
    private final List<LoginTaskListener> listeners = new ArrayList<>();


    void registerListener(LoginTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(LoginTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(LoginTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected LoginResult doInBackground(LoginRequest... r) {
        LoginResult loginResult = new LoginResult();
        ServerProxy serverProxy = new ServerProxy();
        try {
            loginResult = serverProxy.runLogin(r[0]);





        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return loginResult;
    }



    @Override
    protected void onPostExecute(LoginResult result) {
        if (result.getSuccess() == false) {
            Toast.makeText(mcontext, "Login Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();
            AuthToken authToken = new AuthToken(result.getUserName(),result.getAuthToken());
            data.setAuthToken(authToken);
            PeopleTask task = new PeopleTask(mcontext);
            task.execute(data.getAuthToken().getAuthToken());
            EventsTask tasks = new EventsTask(mcontext);
            tasks.execute(data.getAuthToken().getAuthToken());
            FragmentManager fm = MainActivity.getInstance().getSupportFragmentManager();
            MapFragment frag = (MapFragment) fm.findFragmentById(R.id.fragment_map);
            if (frag == null) {
                frag = MainActivity.createMapFragment(data.getUsername());
                fm.beginTransaction()
                        .remove((LoginFragment) fm.findFragmentById(R.id.fragment_login))
                        .commit();
                fm.beginTransaction()
                        .add(R.id.fragment_map,frag)
                        .commit();
            }

        }
    }
}
