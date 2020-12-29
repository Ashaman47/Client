package fm.client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Model.Person;
import Requests.LoginRequest;

import Result.EventsResult;

class EventsTask extends AsyncTask<String, Integer, EventsResult> {
    private Context mcontext;
    interface EventsTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }
    public EventsTask(Context context){
        mcontext = context;
    }
    private final List<EventsTask.EventsTaskListener> listeners = new ArrayList<>();


    void EventsListener(EventsTask.EventsTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(EventsTask.EventsTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(EventsTask.EventsTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected EventsResult doInBackground(String... authToken) {
        EventsResult EventsResult = new EventsResult();
        ServerProxy serverProxy = new ServerProxy();
        try {
            EventsResult = serverProxy.runEvents(authToken[0]);





        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return EventsResult;
    }



    @Override
    protected void onPostExecute(EventsResult result) {
        if (result.getSuccess() == false) {
            Toast.makeText(mcontext, "Events Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();
            data.setOriginalEvents(result.getEvents());
        }
    }
}
