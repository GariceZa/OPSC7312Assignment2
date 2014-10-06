package gareth.example.com.opsc7312assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Gareth on 14/08/2014.
 */
public class Q1ExitDialog extends DialogFragment {

    static Q1ExitDialog newInstance(String title) {
    /*
       allows a new instance of the fragment to be created
       and accepts arguments to be displayed
    */
        Q1ExitDialog EDF = new Q1ExitDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        EDF.setArguments(args);

        return EDF;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            /*
                Creating an alert dialog with an OK and cancel button
                 OK and Exit buttons call the doPositiveClick()
                and doNegativeClick() methods in the Question1 class
            */
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Question1)getActivity()).doPositiveClick();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Question1)getActivity()).doNegativeClick();
                    }
                }).create();
    }


}
