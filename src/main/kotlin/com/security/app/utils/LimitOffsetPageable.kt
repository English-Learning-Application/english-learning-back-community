package com.security.app.utils

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class LimitOffsetPageable(
    private val limit: Int,
    private val offset: Int,
    private val sort: Sort
) : Pageable {

    constructor(limit: Int, offset: Int) : this(limit, offset, Sort.unsorted())

    override fun getPageNumber(): Int {
        return offset / limit
    }

    override fun getPageSize(): Int {
        return limit
    }

    override fun getOffset(): Long {
        return offset.toLong()
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun next(): Pageable {
        return LimitOffsetPageable(
            pageSize,
            offset + pageSize,
            sort
        )
    }

    fun previous(): Pageable {
        return LimitOffsetPageable(
            pageSize,
            offset - pageSize,
            sort
        )
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) previous() else first()
    }

    override fun first(): Pageable {
        return LimitOffsetPageable(
            pageSize,
            0,
            sort
        )
    }

    override fun withPage(pageNumber: Int): Pageable {
        return LimitOffsetPageable(
            pageSize,
            pageNumber * pageSize,
            sort
        )
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }

}