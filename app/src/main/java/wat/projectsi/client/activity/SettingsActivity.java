package wat.projectsi.client.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import wat.projectsi.R;
import wat.projectsi.client.Misc;
import wat.projectsi.client.SharedOurPreferences;

public  class SettingsActivity extends BaseSettingChangeActivity implements AdapterView.OnItemSelectedListener {

    private Spinner languageChanger;
    private ArrayAdapter<CharSequence> languagesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        languageChanger = findViewById(R.id.language_changer);
        languagesAdapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);

        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageChanger.setAdapter(languagesAdapter);

        languageChanger.setSelection(findLanguageID(SharedOurPreferences.getDefaults(Misc.preferenceLanguageStr, this)));

        languageChanger.setOnItemSelectedListener(this);
    }

    private int findLanguageID(String languageCode)
    {
        for(int i=0; i<Misc.suportedLocaleCodes.length;i++)
            if(Misc.suportedLocaleCodes[i].equals(languageCode))return i;

            return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SharedOurPreferences.setDefaults(Misc.preferenceLanguageStr, Misc.suportedLocaleCodes[(int) id], this);
            setLanguage(Misc.suportedLocaleCodes[(int) id]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
