/*
 * MaxLock, an Xposed applock module for Android
 * Copyright (C) 2014-2015  Maxr1998
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.Maxr1998.xposed.maxlock.tasker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import de.Maxr1998.xposed.maxlock.Common;
import de.Maxr1998.xposed.maxlock.R;

public class TaskActionReceiver extends BroadcastReceiver {

    @SuppressWarnings("deprecation")
    @SuppressLint({"WorldReadableFiles", "CommitPrefEdits"})
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences prefsPackages = context.getSharedPreferences(Common.PREFS_PACKAGES, Context.MODE_WORLD_READABLE);
        SharedPreferences prefsIModTemp = context.getSharedPreferences(Common.PREFS_IMOD_TEMP, Context.MODE_WORLD_READABLE);

        if (!intent.getAction().equals("com.twofortyfouram.locale.intent.action.FIRE_SETTING") || !prefs.getBoolean(Common.TASKER_ENABLED, false)) {
            return;
        }
        BundleScrubber.scrub(intent);
        final Bundle extra = intent.getBundleExtra("com.twofortyfouram.locale.intent.extra.BUNDLE");
        BundleScrubber.scrub(extra);

        switch (extra.getInt(ConfigActivity.STATE_EXTRA_KEY, 0)) {
            case R.id.radio_toggle_ms:
                prefsPackages.edit().putBoolean(Common.MASTER_SWITCH_ON, !prefsPackages.getBoolean(Common.MASTER_SWITCH_ON, true)).commit();
                Toast.makeText(context, prefsPackages.getBoolean(Common.MASTER_SWITCH_ON, true) ? context.getString(R.string.toast_master_switch_on) : context.getString(R.string.toast_master_switch_off), Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_ms_on:
                prefsPackages.edit().putBoolean(Common.MASTER_SWITCH_ON, true).commit();
                Toast.makeText(context, context.getString(R.string.toast_master_switch_on), Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_ms_off:
                prefsPackages.edit().putBoolean(Common.MASTER_SWITCH_ON, false).commit();
                Toast.makeText(context, context.getString(R.string.toast_master_switch_off), Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_imod_reset:
                prefsIModTemp.edit().clear().commit();
                break;
        }
    }
}
