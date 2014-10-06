package gareth.example.com.opsc7312assignment2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gareth on 16/08/2014.
 */
public class Q2ContentProvider extends Activity {

    protected List<String> getContacts(Context context) {
        /*
            This method returns an arraylist of the contacts names stored on the device
            using the contact content provider.
        */
        List<String> contacts = new ArrayList<String>();
        Cursor cur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts._ID);

        if(cur.moveToFirst())
        {
            do
            {
                if(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1)
                {
                    contacts.add(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            }
            while(cur.moveToNext());
        }

        return contacts;
    }

    protected List<String> getNumbers(Context context) {
        /*
            This method returns an arraylist of the contacts numbers stored on the device
            using the contact content provider.
        */
        List<String> numbers = new ArrayList<String>();

        Cursor cur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts._ID);

        if(cur.moveToFirst())
        {
            do
            {
                numbers.add(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            while(cur.moveToNext());
        }

        return numbers;
    }
}
