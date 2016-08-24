package jimmysharp.kanaclogger.presenter;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

import jimmysharp.kanaclogger.BuildConfig;
import twitter4j.TwitterFactory;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        TwitterFactory.getSingleton().setOAuthConsumer(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET);
    }
}
