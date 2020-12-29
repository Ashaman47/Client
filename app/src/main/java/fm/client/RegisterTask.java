package fm.client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Model.AuthToken;
import Model.Person;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Result.RegisterResult;

class RegisterTask extends AsyncTask<RegisterRequest, Integer, RegisterResult> {
private Context mcontext;
interface RegisterTaskListener {
    void progressUpdated(int progress);
    void taskCompleted(long result);
}
    public RegisterTask(Context context){
        mcontext = context;
    }
    private final List<RegisterTask.RegisterTaskListener> listeners = new ArrayList<>();


    void registerListener(RegisterTask.RegisterTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(RegisterTask.RegisterTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(RegisterTask.RegisterTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected RegisterResult doInBackground(RegisterRequest... r) {
        RegisterResult RegisterResult = new RegisterResult();
        ServerProxy serverProxy = new ServerProxy();
        try {
            RegisterResult = serverProxy.runRegister(r[0]);





        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return RegisterResult;
    }



    @Override
    protected void onPostExecute(RegisterResult result) {
        if (result.getSuccess() == false) {
            Toast.makeText(mcontext, "Register Failed",Toast.LENGTH_LONG).show();
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
