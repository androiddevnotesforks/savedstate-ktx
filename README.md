# SavedState KTX

![android-sdk](https://img.shields.io/badge/android-21+-brightgreen?logo=android)
![kotlin-version](https://img.shields.io/badge/kotlin-1.7.20-blueviolet?logo=kotlin)
![maven-central](https://img.shields.io/maven-central/v/io.woong.savedstate/savedstate-ktx)
![license](https://img.shields.io/badge/license-MIT-blue)
[![test](https://github.com/cheonjaewoong/savedstate-ktx/actions/workflows/test.yaml/badge.svg)](https://github.com/cheonjaewoong/savedstate-ktx/actions/workflows/test.yaml)

SavedState-KTX is an Android library that contains Kotlin extensions for `SavedStateHandle`.

## Installation

```groovy
dependencies {
    implementation "io.woong.savedstate:savedstate-ktx:1.1.0"
}
```

Add dependency to `build.gradle` in your android project.

## Usage

### Delegated Property

This library provides useful extensions for creating delegated properties.
You can save a value to `SavedStateHandle` via property.
In same way, you can get a value from `SavedStateHandle` via property.

All delegated things can used via `by` keyword of Kotlin.
For more information about Kotlin's delegation, see official documentations.
(About [delegation](https://kotlinlang.org/docs/delegation.html) & [delegated properties](https://kotlinlang.org/docs/delegated-properties.html))

```kotlin
class SampleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var property1: String? by savedStateHandle.nullable()
    var property2: String? by savedStateHandle.nullable("foo")
    var property3: String by savedStateHandle.notNull("bar")
}
```

You can create delegated properties like above sample code.
Delegated properties can divided into 2 types, nullable and not null.

Nullable property can be used by `nullable` function.
It has optional parameter, initial value.
If initial value is not set, the property will contains `null` at initial.

Not null property can be used by `notNull` function.
`notNull` must have one initial value.

### Delegated LiveData

```kotlin
class SampleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _liveData: MutableLiveData<String> by savedStateHandle.liveData()
    val liveData: LiveData<String>
        get() = _liveData
}
```

You can create delegated livedata like sample.
Delegated livedata stores value in the `SavedStateHandle` like delegated properties.

### Delegated StateFlow

```kotlin
class SampleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val stateFlow: StateFlow<String> by savedStateHandle.stateFlow("init")
}
```

Like delegated livedata, you can use delegated stateflow like this.

```kotlin
class SampleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    @OptIn(ExperimentalSavedStateKtxApi::class)
    val stateFlow: MutableStateFlow<String> by savedStateHandle.mutableStateFlow("init")
}
```

Delegated mutable stateflow is currently experimental feature.
You can use like above sample.

## License

SavedState KTX is under the [MIT License](./LICENSE.txt).
