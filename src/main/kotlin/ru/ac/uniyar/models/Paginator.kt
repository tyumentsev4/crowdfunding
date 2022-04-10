package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery

class Paginator(
    val pageCount: Int,
    val pageNumber: Int,
    val uri: Uri,
) {
    fun canNavigate(): Boolean {
        return pageCount > 1
    }

    fun canGoBackward(): Boolean {
        return pageNumber > 1
    }

    fun canGoFroward(): Boolean {
        return pageNumber != pageCount
    }

    fun previousPageLink(): String {
        return uri.removeQuery("page").query("page", (pageNumber - 1).toString()).toString()
    }

    fun previousPageLinks(): Iterable<Pair<Int, Uri>> {
        val res = ArrayList<Pair<Int, Uri>>()
        for (i in 1 until pageNumber)
            res.add(Pair(i, uri.removeQuery("page").query("page", i.toString())))
        return res.asIterable()
    }

    fun nextPageLink(): String {
        return uri.removeQuery("page").query("page", (pageNumber + 1).toString()).toString()
    }

    fun nextPageLinks(): Iterable<Pair<Int, Uri>> {
        val res = ArrayList<Pair<Int, Uri>>()
        for (i in pageNumber + 1..pageCount)
            res.add(Pair(i, uri.removeQuery("page").query("page", i.toString())))
        return res.asIterable()
    }

    fun currentPage(): Int {
        return pageNumber
    }
}
