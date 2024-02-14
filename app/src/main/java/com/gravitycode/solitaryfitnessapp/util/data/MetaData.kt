package com.gravitycode.solitaryfitnessapp.util.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface typically exposed on a repository for easy querying of its contents without the same overhead.
 *
 * This allows the caller to get low-overhead information such as how many entries/records are in repository and
 * the types of records (if the key is well selected and offers useful information about the data it points to).
 * */
interface MetaData<K> {

    /**
     * Returns a [Flow] of keys which each indicate the existence of a record. It is up the implementation
     * to provide sensible keys which the caller can work with.
     *
     * The keys do not need to be provided in any particular order and shouldn't be expected to be.
     * */
    fun getRecords(): Flow<K>

    /**
     * Check if a record for the specified key exists. This function is expected to work on the basis of
     * structural equality between keys (i.e. [Any.equals]), not referential equality.
     * */
    fun containsRecord(key: K): Boolean
}