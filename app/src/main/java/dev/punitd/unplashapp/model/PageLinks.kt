package dev.punitd.unplashapp.model

import okhttp3.Headers

data class PageLinks(val headers: Headers? = null) {
    /**
     * @return first
     */
    var first: String? = null
        private set

    var last: String? = null
        private set

    var next: String? = null
        private set

    var prev: String? = null
        private set

    companion object {
        private const val DELIM_LINKS = ","
        private const val DELIM_LINK_PARAM = ";"
        private const val HEADER_LINK = "link"
        private const val META_FIRST = "first"
        private const val META_NEXT = "next"
        private const val META_PREV = "prev"
        private const val META_LAST = "last"
        private const val META_REL = "rel"

    }

    init {
        val linkHeader = headers?.get(HEADER_LINK)
        if (linkHeader != null) {
            val links = linkHeader.split(DELIM_LINKS.toRegex()).toTypedArray()
            for (link in links) {
                val segments = link.split(DELIM_LINK_PARAM.toRegex()).toTypedArray()
                if (segments.size < 2) continue

                var linkPart = segments[0].trim { it <= ' ' }
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) continue

                linkPart = linkPart.substring(1, linkPart.length - 1)

                for (i in 1 until segments.size) {
                    val rel = segments[i].trim { it <= ' ' }.split("=".toRegex()).toTypedArray()
                    if (rel.size < 2 || META_REL != rel[0]) continue

                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) {
                        relValue = relValue.substring(1, relValue.length - 1)
                    }

                    when (relValue) {
                        META_FIRST -> {
                            first = linkPart
                        }
                        META_LAST -> {
                            last = linkPart
                        }
                        META_NEXT -> {
                            next = linkPart
                        }
                        META_PREV -> {
                            prev = linkPart
                        }
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "PageLinks(headers=$headers)"
    }

    override fun hashCode(): Int {
        val prev = (if (prev != null) prev.hashCode() else 0) * 31
        val next = (if (next != null) next.hashCode() else 0) * 31
        val first = (if (first != null) first.hashCode() else 0) * 31
        val last = (if (last != null) last.hashCode() else 0)
        return prev + next + first + last
    }

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            if (other is PageLinks) {
                val prev = other.prev
                val next = other.next
                val first = other.first
                val last = other.last
                if (this.prev == prev && this.next == next && this.first == first && this.last == last) {
                    return true
                }
            }
            false
        } else {
            true
        }
    }
}