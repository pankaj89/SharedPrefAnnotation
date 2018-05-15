package master.com.annotationpro;

import com.example.Item;
import com.example.Preference;

import java.util.ArrayList;

/**
 * Created by Pankaj Sharma on 13/7/17.
 */

@Preference(name = "MyPref")
public class User {

    @Item
    public String name;

    @Item
    public int age;

    @Item
    public long timestamp;

    @Item
    public long sallary;

}