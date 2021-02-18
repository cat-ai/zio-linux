package zio.linux

import com.sun.jna.{IntegerType, Native}

class NativeSizeT(value: Long = 0) extends IntegerType(Native.SIZE_T_SIZE, value, true)