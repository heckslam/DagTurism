package ru.devtron.dagturism.Utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;
import ru.devtron.dagturism.model.WaylineModel;

/**
 * Класс для хранения констант (постоянных значений)
 *
 * @created 20.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.1
 */

public class Constants {
    public static final String NA = "Недоступно";
    public static final int NORMAL = 0;
    public static final int BEGIN = 1;
    public static final int END = 2;
    public static final int ONLYONE = 3;

    public static final int STATE_CAFE = 0;
    public static final int STATE_HOTELS = 1;

    public static final String STATE_OPEN_PLACE = "state_open_place";


    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_ITEM = "item";
    public static final String TAG_LAT = "latitude";
    public static final String TAG_LNG = "longitude";
    public static final String TAG_DESC = "place_desc";
    public static final String TAG_PRICE = "price";
    public static final String TAG_POINTS = "points";
    public static final String TAG_CAPTION = "point_caption";
    public static final String TAG_POINT_LAT = "point_latitude";
    public static final String TAG_POINT_LNG = "point_longitude";
    public static final String TAG_POINT_NUMBER = "point_number";
    public static final String TAG_ITEMS = "items";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_IMAGES = "images";

    public static final String TAG_CITY = "place_city";

    public static final String TAG_PID = "place_id";
    public static final String TAG_NAME = "place_name";


    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortMapByValue(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static void displayErrors(TextView textVolleyError, VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth);
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_server);
        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.no_network);
        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_server);
        }
    }

}
