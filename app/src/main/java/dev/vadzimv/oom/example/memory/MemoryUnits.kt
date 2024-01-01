package dev.vadzimv.oom.example.memory

fun Int.gigabytesInBytes() = (this * 1024).megabytesInBytes()

fun Int.megabytesInBytes() = this * 1024 * 1024

@JvmInline
value class Bytes(
    private val value: Long
) {
    init {
        assert(value >= 0) {
            "Bytes size can't be negative"
        }
    }

    operator fun minus(other: Bytes): Bytes {
        return Bytes(this.value - other.value)
    }

    fun formatMb() = String.format("%.2f Mb", value.toDouble() / (1024 * 1024))
}

fun Long.bytes() = Bytes(this)
