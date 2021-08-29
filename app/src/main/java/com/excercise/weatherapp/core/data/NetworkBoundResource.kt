package com.excercise.weatherapp.core.data

import android.util.Log
import com.excercise.weatherapp.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbSource = loadFromDB().first()
        Log.d("DBSOURCE->>", dbSource.toString())
        if (shouldFetch(dbSource)) {
            emit(Resource.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    Log.d("SUCCESS->>", "Success")
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ApiResponse.Empty -> {
                    Log.d("EMPTY->>", "Empty")
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }
                is ApiResponse.Error -> {
                    Log.d("ERROR->>", "Error")
                    onFetchFailed()
                    emit(Resource.Error<ResultType>(apiResponse.errorMessage))
                }
            }
        } else {
            emitAll(loadFromDB().map {
                Log.d("PRODUCTDB", it.toString())
                Resource.Success(it)
            })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}