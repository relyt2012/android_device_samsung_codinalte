package com.meticulus.codinalteparts.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.meticulus.codinalteparts.app.FunctionsMain;

public class MainActivity extends Activity {

    Switch incallaudio, bttether, cpu2, LMKNKP, autologcat, autokmsg, autoril;
    ImageView whatis_incallaudio, whatis_bttether, whatis_cpu2, whatis_LMKNKP,
            whatis_autologcat,whatis_autokmsg, whatis_autorillog;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Assign all switches */
        incallaudio = (Switch) findViewById(R.id.switch_incallaudio);
        bttether = (Switch) findViewById(R.id.switch_bttether);
        cpu2 = (Switch) findViewById(R.id.switch_cpu2);
        LMKNKP = (Switch) findViewById(R.id.switch_LMKNKP);
        autologcat = (Switch) findViewById(R.id.switch_autologcat);
        autokmsg = (Switch) findViewById(R.id.switch_autokmsg);
        autoril = (Switch)findViewById(R.id.switch_autorillog);
        /* Assign all switches onCheckChanged*/
        incallaudio.setOnCheckedChangeListener(switchListener);
        bttether.setOnCheckedChangeListener(switchListener);
        cpu2.setOnCheckedChangeListener(switchListener);
        LMKNKP.setOnCheckedChangeListener(switchListener);
        autologcat.setOnCheckedChangeListener(switchListener);
        autokmsg.setOnCheckedChangeListener(switchListener);
        autoril.setOnCheckedChangeListener(switchListener);

        whatis_incallaudio = (ImageView) findViewById(R.id.whatis_incallaudio);
        whatis_incallaudio.setOnClickListener(switchClickListener);

        whatis_bttether = (ImageView) findViewById(R.id.whatis_bttether);
        whatis_bttether.setOnClickListener(switchClickListener);

        whatis_cpu2 = (ImageView) findViewById(R.id.whatis_cpu2);
        whatis_cpu2.setOnClickListener(switchClickListener);

        whatis_LMKNKP = (ImageView) findViewById(R.id.whatis_LMKNKP);
        whatis_LMKNKP.setOnClickListener(switchClickListener);

        whatis_autologcat = (ImageView) findViewById(R.id.whatis_autologcat);
        whatis_autologcat.setOnClickListener(switchClickListener);

        whatis_autokmsg = (ImageView) findViewById(R.id.whatis_autokmsg);
        whatis_autokmsg.setOnClickListener(switchClickListener);

        whatis_autorillog = (ImageView) findViewById(R.id.whatis_autorillog);
        whatis_autorillog.setOnClickListener(switchClickListener);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        prepareUI();
    }

    private void prepareUI(){

        incallaudio.setChecked(sharedPref.getBoolean("incallaudio",true));
        bttether.setChecked(sharedPref.getBoolean("bttether",true));
        cpu2.setChecked(sharedPref.getBoolean("cpu2", true));
        LMKNKP.setChecked(sharedPref.getBoolean("LMKNKP", true));
        autologcat.setChecked(sharedPref.getBoolean("autologcat",false));
        autokmsg.setChecked(sharedPref.getBoolean("autokmsg",false));
        autoril.setChecked(sharedPref.getBoolean("autoril",false));
    }

    private View.OnClickListener switchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ImageView thisSwitch = (ImageView)view;
            if(thisSwitch == whatis_incallaudio){
                ShowDialog("In-Call Audio",getString(R.string.incallaudio_desc));
            }
            else if(thisSwitch == whatis_bttether){
                ShowDialog("Bluetooth Tether",getString(R.string.bttether_desc));
            }
            else if(thisSwitch == whatis_cpu2){
                ShowDialog("CPU2",getString(R.string.cpu2_desc));
            }
            else if(thisSwitch == whatis_LMKNKP){
                ShowDialog("Low Memory Killer",getString(R.string.LMKNKP_desc));
            }
            else if(thisSwitch == whatis_autologcat){
                ShowDialog("Auto Logcat",getString(R.string.autologcat_desc));
            }
            else if(thisSwitch == whatis_autokmsg){
                ShowDialog("Auto kmsg",getString(R.string.autokmsg_desc));
            }
            else if(thisSwitch == whatis_autorillog){
                ShowDialog("Auto kmsg",getString(R.string.autorillog_desc));
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            Switch thisSwitch = (Switch)compoundButton;
            SharedPreferences.Editor editor = sharedPref.edit();

            if(thisSwitch == incallaudio){
                if(b != sharedPref.getBoolean("incallaudio",true)) {
                    if (b)
                        FunctionsMain.startInCallAudioService(getApplicationContext());
                    else
                        ShowDialog("In-Call Audio", "Will NOT be started on the next reboot. " +
                                "Still running till then!");
                }
                editor.putBoolean("incallaudio",b);

            }
            else if(thisSwitch == bttether){
                editor.putBoolean("bttether", b);
            }
            else if(thisSwitch == cpu2){
                editor.putBoolean("cpu2", b);
                FunctionsMain.SetCPU2(b);
            }
            else if(thisSwitch == LMKNKP){
                if(b) {
                    FunctionsMain.enableLMKNKP();
                    FunctionsMain.setLMKNKPWhitelist();
                }
                else
                    FunctionsMain.disableLMKNKP();

                editor.putBoolean("LMKNKP", b);
            }
            else if(thisSwitch == autologcat){
                if(b != sharedPref.getBoolean("autologcat",false))
                {
                    if(b)
                        ShowDialog("Auto logcat","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto logcat","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autologcat", b);
            }
            else if(thisSwitch == autokmsg){
                if(b != sharedPref.getBoolean("autokmsg",false))
                {
                    if(b)
                        ShowDialog("Auto kmsg","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto kmsg","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autokmsg", b);
            }
            else if(thisSwitch == autoril){
                if(b != sharedPref.getBoolean("autoril",false))
                {
                    if(b)
                        ShowDialog("Auto RIL log","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto RIL log","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autoril", b);
            }
            editor.apply();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowDialog(String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
