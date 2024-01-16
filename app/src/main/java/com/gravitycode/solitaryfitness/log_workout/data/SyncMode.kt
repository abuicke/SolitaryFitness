package com.gravitycode.solitaryfitness.log_workout.data

enum class SyncMode {

    /**
     * If a record already exists in the destination repository,
     * don't replace it with the record from the source directory.
     * */
    PRESERVE,

    /**
     * If a record already exists in the destination repository,
     * replace it with the record from the source repository.
     * */
    OVERWRITE
}