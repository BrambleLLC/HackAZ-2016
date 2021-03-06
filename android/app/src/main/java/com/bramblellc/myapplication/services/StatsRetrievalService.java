package com.bramblellc.myapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bramblellc.myapplication.data.Globals;
import com.stevex86.napper.http.connection.ConnectionHandler;
import com.stevex86.napper.http.elements.content.JsonBodyContent;
import com.stevex86.napper.http.elements.content.TextBodyContent;
import com.stevex86.napper.http.elements.method.Get;
import com.stevex86.napper.http.elements.method.Post;
import com.stevex86.napper.http.elements.route.Route;
import com.stevex86.napper.request.Request;
import com.stevex86.napper.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class StatsRetrievalService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StatsRetrievalService(String name) {
        super(name);
    }

    public StatsRetrievalService() {
        this("RemoveContactService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Route route = new Route("http://guarddog.stevex86.com/stats");
            Request request = new Request(route, new Get());


            ConnectionHandler connectionHandler = new ConnectionHandler(request);

            Response response = connectionHandler.getResponse();
            JSONObject responseObject = new JSONObject(response.getBodyContent().getOutputString());
            Log.d("Guard-Dog", responseObject.toString());
            Intent localIntent = new Intent(ActionConstants.STATS_ACTION);
            localIntent.putExtra("users-count", responseObject.getInt("users-count"));
            localIntent.putExtra("contacts-count", responseObject.getInt("contacts-count"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
        catch (JSONException e) {
            Log.d("Guard-Dog", "Ayy lmao, JSON Exception thrown");
        }

        catch (IOException e) {
            e.printStackTrace();
            Log.d("Guard-Dog", "Ayy lmao, IOException thrown");
        }
    }


}
