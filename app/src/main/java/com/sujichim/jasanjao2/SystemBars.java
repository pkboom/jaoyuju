package com.sujichim.jasanjao2;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Keeps the activity's layout clear of the status bar, the gesture bar and the action bar.
 *
 * Android 15 forces edge-to-edge on apps that target SDK 35 or newer: the window stops
 * insetting its content and hands the layout the whole screen instead, along with the insets
 * describing what is drawn over it. Nothing in this app read those insets, so the layout
 * stayed at the top of the screen: the action bar covered the first row of every screen and
 * the banner ran under the gesture pill. Applying them as padding on the content frame is
 * all that is missing — the frame's top inset already accounts for the action bar.
 *
 * Older releases inset the content themselves and need nothing from us.
 */
final class SystemBars {

    private SystemBars() {
    }

    static void applyTo(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            return;
        }

        View content = activity.findViewById(android.R.id.content);
        if (content == null || handlesItsOwnInsets(content)) {
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(content, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
                Insets bars = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return WindowInsetsCompat.CONSUMED;
            }
        });
        ViewCompat.requestApplyInsets(content);
    }

    /** True for a layout that asks for the insets itself, e.g. a CoordinatorLayout app bar. */
    private static boolean handlesItsOwnInsets(View content) {
        View layout = content instanceof ViewGroup && ((ViewGroup) content).getChildCount() > 0
                ? ((ViewGroup) content).getChildAt(0)
                : null;
        return layout != null && layout.getFitsSystemWindows();
    }
}
