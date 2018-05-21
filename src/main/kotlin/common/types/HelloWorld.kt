package common.types

import kotlinx.serialization.Serializable

@Serializable
data class HelloWorld(val message: String = "Hello World!")