package dev.punitd.unplashapp.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import dev.punitd.unplashapp.R
import dev.punitd.unplashapp.model.*
import dev.punitd.unplashapp.ui.theme.MoleculeUnplashAppTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UnsplashItem(unsplashImage: UnsplashImage) {
    val painter = rememberImagePainter(data = unsplashImage.urls.regularImage) {
        crossfade(durationMillis = 1000)
        error(R.drawable.ic_placeholder)
        placeholder(R.drawable.ic_placeholder)
    }

    val profileImagePainter = rememberImagePainter(data = unsplashImage.user.profileImage.medium) {
        crossfade(durationMillis = 1000)
        error(R.drawable.ic_placeholder)
        placeholder(R.drawable.ic_placeholder)
    }

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .clickable {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(unsplashImage.user.userLinks.html)
                )
                ContextCompat.startActivity(context, browserIntent, null)
            }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
        ) {
            Image(
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp)
                    .clip(CircleShape),
                painter = profileImagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier= Modifier.padding(top = 4.dp),
                text = unsplashImage.user.username,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
        Row(modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${unsplashImage.likes}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUnsplashItem() {
    MoleculeUnplashAppTheme {
        UnsplashItem(
            unsplashImage = UnsplashImage(
                id = "122",
                urls = Urls(regularImage = "https://images.unsplash.com/photo-1657664042448-c955b411d9d0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNDQyNTJ8MXwxfGFsbHwxfHx8fHx8Mnx8MTY2MjQ1OTAxNA&ixlib=rb-1.2.1&q=80&w=1080"),
                likes = 20,
                user = User(
                    username = "oppofindx5pro",
                    userLinks = UserLinks(html = "https://unsplash.com/@oppofindx5pro"),
                    profileImage = ProfileImage(medium = "https://images.unsplash.com/profile-1657663575361-5e8d57088720image?ixlib=rb-1.2.1&crop=faces&fit=crop&w=64&h=64")
                )
            )
        )
    }
}