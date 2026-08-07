package week11.st482988.recipeai_finalproject.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import week11.st482988.recipeai_finalproject.ui.theme.LightGray
import week11.st482988.recipeai_finalproject.ui.theme.MutedText

private const val DATA_URI_PREFIX = "data:image"

/**
 * Recipe photo with a local, no-network fallback for both missing URLs
 * (AI-invented dishes with no Wikimedia match) and failed loads.
 */
@Composable
fun RecipeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isBlank()) {
        RecipeImagePlaceholder(modifier)
        return
    }

    // AI-generated recipes store their photo as a base64 data: URI (no image hosting
    // required); Coil only resolves network/file/content URIs, so decode it ourselves.
    val model = if (imageUrl.startsWith(DATA_URI_PREFIX)) {
        remember(imageUrl) { decodeDataUri(imageUrl) } ?: run {
            RecipeImagePlaceholder(modifier)
            return
        }
    } else {
        imageUrl
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Error -> RecipeImagePlaceholder(Modifier)
            else -> SubcomposeAsyncImageContent()
        }
    }
}

private fun decodeDataUri(dataUri: String) = try {
    val base64 = dataUri.substringAfter(",", "")
    if (base64.isBlank()) {
        null
    } else {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
} catch (e: Exception) {
    null
}

@Composable
private fun RecipeImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(LightGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Restaurant,
            contentDescription = null,
            tint = MutedText,
            modifier = Modifier.size(32.dp)
        )
    }
}