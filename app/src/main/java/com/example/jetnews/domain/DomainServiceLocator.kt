package com.example.jetnews.domain

import com.example.jetnews.data.DataServiceLocator
import com.example.jetnews.domain.usecase.GetBookSearchPagingDataUseCase
import com.example.jetnews.domain.usecase.GetBookUseCase
import com.example.jetnews.domain.usecase.UpdateBookLikeUseCase

interface DomainServiceLocator {
    val data: DataServiceLocator

    val pagingUseCase: GetBookSearchPagingDataUseCase get() = GetBookSearchPagingDataUseCase(data.repository)
    val bookUseCase: GetBookUseCase get() = GetBookUseCase(data.repository)
    val updateUseCase: UpdateBookLikeUseCase get() = UpdateBookLikeUseCase(data.repository)
}

class DomainServiceLocatorImpl(
    override val data: DataServiceLocator
) : DomainServiceLocator