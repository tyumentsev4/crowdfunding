package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery

class Paginator(
    private val pageCount: Int,
    private val pageNumber: Int,
    private val uri: Uri,
) {
    data class PageLink(
        val number: Int,
        val uri: Uri
    )

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

    fun previousPageLinks(): List<PageLink> {
        val range = 1.until(pageNumber)
        return range.map { PageLink(it, uri.removeQuery("page").query("page", it.toString())) }
    }

    fun nextPageLink(): String {
        return uri.removeQuery("page").query("page", (pageNumber + 1).toString()).toString()
    }

    fun nextPageLinks(): List<PageLink> {
        val range = pageNumber.inc().rangeTo(pageCount)
        return range.map { PageLink(it, uri.removeQuery("page").query("page", it.toString())) }
    }

    fun currentPage(): Int {
        return pageNumber
    }
}
