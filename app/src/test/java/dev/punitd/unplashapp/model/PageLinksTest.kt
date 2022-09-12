package dev.punitd.unplashapp.model

import okhttp3.Headers
import org.junit.Assert.assertEquals
import org.junit.Test

class PageLinksTest {
    @Test
    fun `pageLinks with empty link header returns next as null`() {
        val pageLinks = PageLinks()
        assertEquals(pageLinks.next, null)
    }

    @Test
    fun `pageLinks with link header for 1st page returns valid next & last`() {
        val headers = Headers.Builder().add(
            "link",
            "<https://api.unsplash.com/photos?page=13760&per_page=20>; rel=\"last\", <https://api.unsplash.com/photos?page=2&per_page=20>; rel=\"next\""
        ).build()

        val pageLinks = PageLinks(headers)
        assertEquals(pageLinks.next, "https://api.unsplash.com/photos?page=2&per_page=20")
        assertEquals(pageLinks.last, "https://api.unsplash.com/photos?page=13760&per_page=20")
    }

    @Test
    fun `pageLinks with link header for subsequent pages returns valid values for prev, next, last and first`() {
        val headers = Headers.Builder().add(
            "link",
            "<https://api.unsplash.com/photos?page=1&per_page=20&per_page=20>; rel=\"first\", <https://api.unsplash.com/photos?page=1&per_page=20&per_page=20>; rel=\"prev\", <https://api.unsplash.com/photos?page=13760&per_page=20&per_page=20>; rel=\"last\", <https://api.unsplash.com/photos?page=3&per_page=20&per_page=20>; rel=\"next\""
        ).build()

        val pageLinks = PageLinks(headers)
        assertEquals(
            pageLinks.first,
            "https://api.unsplash.com/photos?page=1&per_page=20&per_page=20"
        )
        assertEquals(
            pageLinks.prev,
            "https://api.unsplash.com/photos?page=1&per_page=20&per_page=20"
        )
        assertEquals(
            pageLinks.last,
            "https://api.unsplash.com/photos?page=13760&per_page=20&per_page=20"
        )
        assertEquals(
            pageLinks.next,
            "https://api.unsplash.com/photos?page=3&per_page=20&per_page=20"
        )
    }
}