package com.example.jetnews.presentation.ui.book

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.presentation.model.BookModel
import com.example.jetnews.ui.theme.JetnewsTheme
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun BookRow(
    book: BookModel,
    onClickBook: (BookModel) -> Unit,
    onClickBookmark: (BookModel) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(6.dp)
            .clickable { onClickBook(book) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier.height(50.dp).width(50.dp),
            imageModel = book.thumbUrl,
            contentScale = ContentScale.FillWidth,
            placeHolder = ImageBitmap.imageResource(R.mipmap.sym_def_app_icon),
            error = ImageBitmap.imageResource(R.mipmap.sym_def_app_icon),
            previewPlaceholder = R.mipmap.sym_def_app_icon
        )
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = book.title, style = MaterialTheme.typography.subtitle1)
            Text(text = book.authors.joinToString(", "), style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.padding(horizontal = 3.dp))
        IconToggleButton(
            checked = book.isFavorite,
            onCheckedChange = { onClickBookmark(book) },
            modifier = Modifier
        ) {
            Icon(
                imageVector = if (book.isFavorite) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null // handled by click label of parent
            )
        }
    }
}

@Preview("BookRow Preview")
@Composable
fun PreviewBookRow() {
    val book = BookModel(
        id = "",
        thumbUrl = "",
        title = "Sample Book",
        content = "Sample Content",
        authors = listOf("Arthur", "Hwang"),
        translators = listOf("?????????"),
        publisher = "?????? ?????????",
        price = 1000L,
        released = System.currentTimeMillis(),
        isFavorite = true
    )
    JetnewsTheme {
        BookRow(
            book = book,
            onClickBook = {},
            onClickBookmark = {}
        )
    }
}