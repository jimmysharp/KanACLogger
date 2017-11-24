package jimmysharp.kanaclogger.presenter;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import jimmysharp.kanaclogger.R;
import jimmysharp.kanaclogger.model.TwitterManager;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final String LICENSE_DIALOG_TAG = "licenseDialog";
    private CompositeDisposable subscription;
    private TwitterManager twitter;
    private RequestToken requestToken = null;

    public SettingsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscription = new CompositeDisposable();

        addPreferencesFromResource(R.xml.preference);
        twitter = new TwitterManager(this.getActivity());

        onSharedPreferenceChanged(null,"hashtag_text");
        onSharedPreferenceChanged(null,"twitter_authentication");

        Preference twitterAuthPref = findPreference("twitter_authentication");
        twitterAuthPref.setOnPreferenceClickListener(preference1 -> {
            subscription.add(twitter.getAccessTokenUrl()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<RequestToken>() {
                @Override
                public void onSuccess(RequestToken token) {
                    requestToken = token;
                    Log.v(TAG,"Move to browser for Twitter authentication");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(token.getAuthorizationURL()));
                    startActivity(intent);
                }
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getActivity(),getString(R.string.msg_twitter_oauth_url_failed),Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Failed to get Twitter oauth URL",error);
                }
            }));
            return true;
        });

        Preference licensePref = findPreference("license");
        licensePref.setOnPreferenceClickListener(preference -> {
            final LicenseDialog licenseDialog = new LicenseDialog();
            licenseDialog.setTargetFragment(this,100);
            licenseDialog.show(getChildFragmentManager(),LICENSE_DIALOG_TAG);

            return true;
        });
    }

    public void setAccessToken(Uri uri){
        if (uri != null && requestToken != null){
            subscription.add(twitter.getAndSaveAccessToken(getActivity(),requestToken,uri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<AccessToken>() {
                @Override
                public void onSuccess(AccessToken token) {
                    Toast.makeText(getActivity(),getString(R.string.msg_twitter_oauth_success),Toast.LENGTH_SHORT).show();
                    onSharedPreferenceChanged(null,"twitter_authentication");
                }

                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getActivity(),getString(R.string.msg_twitter_oauth_failed),Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Failed to get Twitter access token",error);
                }
            }));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Fragment dialog = getChildFragmentManager().findFragmentByTag(LICENSE_DIALOG_TAG);
        if (dialog != null) {
            getChildFragmentManager().beginTransaction().remove(dialog).commit();
        }

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        subscription.dispose();
        subscription = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(twitter != null){
            twitter.dispose();
            twitter = null;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "hashtag_text":
                EditTextPreference editTextPreference = (EditTextPreference) findPreference("hashtag_text");
                editTextPreference.setSummary(editTextPreference.getText());
                break;
            case "twitter_authentication":
                Preference preference = findPreference("twitter_authentication");
                if (twitter.isAccessTokenStored()){
                    preference.setSummary(getString(R.string.text_pref_twitter_account_registered));
                } else {
                    preference.setSummary(getString(R.string.text_pref_twitter_account_unregistered));
                }
                break;
            default:
                break;
        }
    }
}
