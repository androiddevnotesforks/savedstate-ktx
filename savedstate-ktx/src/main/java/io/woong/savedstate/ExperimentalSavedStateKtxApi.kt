package io.woong.savedstate

/**
 * Marker for experimental features of `savedstate-ktx` library.
 */
@RequiresOptIn(
    message = "This is experimental API. It could be removed or changed in the future.",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
public annotation class ExperimentalSavedStateKtxApi
