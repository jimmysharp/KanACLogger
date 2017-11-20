package jimmysharp.kanaclogger.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterManager {
    private static final String TAG = TwitterManager.class.getSimpleName();
    private static final String PREFERENCE_NAME = "twitter_setting";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String TOKEN_SECRET_KEY = "token_secret";
    private static final String OAUTH_CALLBACK_URL = "kanactwit://oauth";
    private static final int TWITTER_MAX_CHARS = 140;

    Twitter twitter;
    Context context;

    public TwitterManager(Context context){
        this.context = context;
        twitter = TwitterFactory.getSingleton();
        AccessToken token = loadAccessToken();
        twitter.setOAuthAccessToken(token);
    }

    public void dispose(){
        this.context = null;
        this.twitter = null;
    }

    public boolean isAccessTokenStored(){
        return loadAccessToken() != null;
    }

    public AccessToken loadAccessToken(){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        String accessToken, tokenSecret;

        accessToken = pref.getString(ACCESS_TOKEN_KEY,null);
        tokenSecret = pref.getString(TOKEN_SECRET_KEY,null);

        if (accessToken != null && tokenSecret != null){
            return new AccessToken(accessToken,tokenSecret);
        } else{
            return null;
        }
    }

    public void saveAccessToken(AccessToken accessToken){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(ACCESS_TOKEN_KEY,accessToken.getToken());
        editor.putString(TOKEN_SECRET_KEY,accessToken.getTokenSecret());

        editor.apply();
    }

    public void clearAccessToken(){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(TOKEN_SECRET_KEY);

        editor.apply();
    }

    public Single<RequestToken> getAccessTokenUrl(){
        return Single.create(subscriber -> {
            try {
                RequestToken token = twitter.getOAuthRequestToken(OAUTH_CALLBACK_URL);
                subscriber.onSuccess(token);
            } catch (TwitterException e) {
                subscriber.onError(e);
            }
        });
    }

    public Single<AccessToken> getAccessToken(RequestToken requestToken, Uri uri){
        return Single.create(subscriber -> {
            try {
                String verifier = uri.getQueryParameter("oauth_verifier");
                AccessToken token = twitter.getOAuthAccessToken(requestToken,verifier);
                subscriber.onSuccess(token);
            } catch (TwitterException e){
                subscriber.onError(e);
            }
        });
    }

    public Single<AccessToken> getAndSaveAccessToken(Context context, RequestToken requestToken, Uri uri){
        return Single.create(subscriber -> {
            try {
                String verifier = uri.getQueryParameter("oauth_verifier");
                AccessToken token = twitter.getOAuthAccessToken(requestToken,verifier);
                saveAccessToken(token);
                twitter.setOAuthAccessToken(token);
                subscriber.onSuccess(token);
            } catch (TwitterException e){
                subscriber.onError(e);
            }
        });
    }

    public Single<Status> tweet(String message){
        return Single.create(subscriber -> {
            try {
                Status status = twitter.updateStatus(message);
                subscriber.onSuccess(status);
            } catch (TwitterException e){
                subscriber.onError(e);
            }
        });
    }

    public List<Single<Status>> tweet(List<String> lines, String hashtag){
        Log.v(TAG,"Tweet preparation start");
        int maxChars;
        List<Single<Status>> tweetList = new LinkedList<>();

        if (hashtag == null || hashtag.equals("")){
            hashtag = "";
        } else {
            hashtag = "#" + hashtag;
        }
        maxChars = TWITTER_MAX_CHARS - hashtag.length();
        Log.v(TAG,"Tweet max length = "+maxChars);

        int charCount = 0;
        List<String> buffer = new LinkedList<>();
        for (String line : lines){
            if (charCount + line.length() > maxChars){
                if (buffer.size() == 0){
                    tweetList.add(Single.create(subscriber -> { subscriber.onError(new RuntimeException("Tweet too long: "+line.length()+" chars")); }));
                    continue;
                } else {
                    buffer.add(hashtag);
                    tweetList.add(tweet(joinString(buffer,"\n")));
                    buffer.clear();
                    charCount = 0;
                }
            }
            buffer.add(line);
            charCount += line.length() + 1;
        }

        if (buffer.size() > 0){
            buffer.add(hashtag);
            tweetList.add(tweet(joinString(buffer, "\n")));
        }

        Log.v(TAG,"Tweet preparation end");
        Log.v(TAG,"Tweet List size:"+tweetList.size());
        return tweetList;
    }

    private String joinString(List<String> strings, String separator){
        return Stream.of(strings).collect(Collectors.joining(separator));
    }
}
