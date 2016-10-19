package siri.android.recyclerviewendlessscrolling;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostModelHelper {

    private final static String TAG = PostModelHelper.class.getSimpleName();

    /**
     * Converts posts json array string to posts list
     *
     * @param jsonString jsonString obtained from service call
     * @return returns PostModel list
     */
    public List<PostModel> postsJsonDserializer(String jsonString) throws JSONException {
        List<PostModel> postModelList = new ArrayList<>();

        if (jsonString != null) {
            try {
                Gson gson = new Gson();
                JSONArray array = new JSONArray(jsonString); // convert the returned string into a JSONArray
                JSONObject jsonObject;
                for (int i = 0; i < array.length(); i++) { // loop through each object in the array
                    PostModel post;
                    jsonObject = array.getJSONObject(i);
                    if (jsonObject != null) {
                        post = gson.fromJson(jsonObject.toString(), PostModel.class); // convert a JSONObject into row_bbpost data
                        if (post != null) {

                            postModelList.add(post);
                        }
                        Log.d(TAG, "END OF POST" + postModelList.size());
                    }
                }
                Log.d(TAG, "END OF FOR and TRY" + postModelList.size());
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server." + postModelList.size());
        }

        Log.d(TAG, "END OF FOR and TRY" + postModelList.size());
        return postModelList;
    }
}
