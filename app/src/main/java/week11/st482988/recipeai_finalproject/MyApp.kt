package week11.st482988.recipeai_finalproject

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import week11.st482988.recipeai_finalproject.utils.FirebaseDataUploader

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Upload sample recipes on first app launch
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseDataUploader.uploadSampleRecipes()
            } catch (e: Exception) {
                Log.e("DataUpload", "Error: ${e.message}")
            }
        }
    }
}