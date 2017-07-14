package master.com.annotationpro;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.Item;
import com.example.Preference;

/**
 * Created by Pankaj Sharma on 13/7/17.
 */

@Preference(name = "ABC")
public class LikeActivity extends Activity {

    @Item
    String abe;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
