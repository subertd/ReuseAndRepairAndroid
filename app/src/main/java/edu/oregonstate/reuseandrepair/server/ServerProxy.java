package edu.oregonstate.reuseandrepair.server;

import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Donald on 5/23/2015.
 */
public class ServerProxy {
    private static final String TAG = ServerProxy.class.getName();

    private static final String PROTOCOL_HTTP = "http://";

    private static final String API_ROOT =
            "web.engr.oregonstate.edu/~subertd/ReuseAndRepairServer/public_html/data.php";

    public JSONObject syncDatabase() throws ServerException {
        try {
            // Create and parameterize the http get request
            HttpGet request = new HttpGet(PROTOCOL_HTTP + API_ROOT);
            request.setHeader("Accept", "application/json");

            // Execute the request and get the response body as a string
            DefaultHttpClient httpClient = new DefaultHttpClient();
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpClient.execute(request, responseHandler);

            Log.i(TAG, "Response: '" + response + "'");

            final JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse;
        }
        catch (final JSONException | IOException e) {
            Log.e(TAG, e.getMessage());
            final String message = "Unable to sync database";
            throw new ServerException(message, e);
        }
    }
}
