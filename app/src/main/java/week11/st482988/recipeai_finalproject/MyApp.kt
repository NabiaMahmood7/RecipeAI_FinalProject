
package week11.st482988.recipeai_finalproject

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.firebase.FirebaseApp
import okhttp3.OkHttpClient

class MyApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    // Wikimedia (and many other image hosts) reject requests with no descriptive
    // User-Agent header — Coil's default OkHttp client sends a generic one, which
    // gets a silent 403. This applies a real User-Agent to every image load.
    override fun newImageLoader(): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "RecipeAI-Android/1.0 (college project; contact: $packageName)")
                    .build()
                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(client)
            .build()
    }
}
