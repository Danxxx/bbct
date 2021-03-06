/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.activity.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import bbct.android.common.R;

/**
 * Static utility methods for showing common dialogs.
 */
public class DialogUtil {

    /**
     * Show an error message in a dialog box. The dialog box is displayed using
     * the string resources with the given ids for the title and error message.
     *
     * @param context
     *            The Android context.
     * @param titleId
     *            The string resource for the title.
     * @param errorId
     *            The string resource for the error message.
     */
    public static void showErrorDialog(Context context, int titleId, int errorId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(titleId);
        dialogBuilder.setMessage(errorId);
        dialogBuilder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogBuilder.show();
    }
}
