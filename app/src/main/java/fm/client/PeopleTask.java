package fm.client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Model.Person;
import Requests.LoginRequest;

import Result.PeopleResult;

class PeopleTask extends AsyncTask<String, Integer, PeopleResult> {
    private Context mcontext;
    interface PeopleTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }
    public PeopleTask(Context context){
        mcontext = context;
    }
    private final List<PeopleTask.PeopleTaskListener> listeners = new ArrayList<>();


    void PeopleListener(PeopleTask.PeopleTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(PeopleTask.PeopleTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(PeopleTask.PeopleTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected PeopleResult doInBackground(String... authToken) {
        PeopleResult PeopleResult = new PeopleResult();
        ServerProxy serverProxy = new ServerProxy();
        try {
            PeopleResult = serverProxy.runPeople(authToken[0]);





        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return PeopleResult;
    }



    @Override
    protected void onPostExecute(PeopleResult result) {
        if (result.getSuccess() == false) {
            Toast.makeText(mcontext, "People Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();
            data.setPeople(result.getPeople());
            Toast.makeText(mcontext, data.getUser().getFirstName() + " " + data.getUser().getLastName(), Toast.LENGTH_LONG).show();
        }
    }
}
