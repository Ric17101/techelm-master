package admin4.techelm.com.techelmtechnologies.utility.dialog;


import android.app.DatePickerDialog;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by admin 4 on 26/04/2017.
 *  Android Display Dialog from another Dialog
 */

public interface OpenDialog {
    void showDialog(DialogFragment dialog);
    void showDialog(DatePickerDialog dialog);
    void showDialog(MaterialDialog dialog);
}
