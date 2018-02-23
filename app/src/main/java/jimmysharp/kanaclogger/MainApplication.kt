package jimmysharp.kanaclogger

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import twitter4j.TwitterFactory

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        TwitterFactory.getSingleton().setOAuthConsumer(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET)
    }
}
