package zio.linux

import com.sun.jna.{NativeLong, Platform, Pointer, Native => JnaNative}

import zio.{ZEnv, ZIO, ZManaged}

import java.io.IOException

class ZLinuxIO[N <: JnaNative] {

  lazy val load = JnaNative.register("c")

  object Mode {
    val O_APPEND    = 1024
    val O_ASYNC     = 8192
    val O_CLOEXEC   = 524288
    val O_CREAT     = 64
    val O_DIRECT    = 16384
    val O_DIRECTORY = 65536
    val O_DSYNC     = 4096
    val O_EXCL      = 128
    val O_LARGEFILE = 0
    val O_NOATIME   = 262144
    val O_NONBLOCK  = 2048
    val O_NOCTTY    = 256
    val O_NOFOLLOW  = 131072
    val O_PATH      = 2097152
    val O_RDONLY    = 0
    val O_RDWR      = 2
    val O_SYNC      = 1052672
    val O_TMPFILE   = 4259840
    val O_TRUNC     = 512
    val O_WRONLY    = 1
  }

  object Permission {
    val S_IRWXU = 448
    val S_IRUSR = 256
    val S_IWUSR = 128
    val S_IXUSR = 64
    val S_IRWXG = 56
    val S_IRGRP = 32
    val S_IWGRP = 16
    val S_IXGRP = 8
    val S_IRWXO = 7
    val S_IROTH = 4
    val S_IWOTH = 2
    val S_IXOTH = 1
  }

  object Indicator {
    val SEEK_SET = 0
    val SEEK_CUR = 1
    val SEEK_END = 2
  }

  @native
  private def close(fd: Int): Int

  @native
  private def open(path: String, flags: Int): Int

  @native
  private def ioctl(fd: Int, request: NativeLong, arg: Int): Int

  @native
  private def ioctl(fd: Int, request: NativeLong, arg: Long): Int

  @native
  private def ioctl(fd: Int, request: NativeLong, arg: NativeLong): Int

  @native
  private def ioctl(fd: Int, request: NativeLong, arg: Pointer): Int

  @native
  private def lseek64(fd: Int, offset: Long, whence: Int): Long

  @native
  private def open64(path: String, flags: Int): Int

  @native
  private def open64(path: String, flags: Int, mode: Int): Int

  @native
  private def openat64(dirFD: Int, path: String, flags: Int): Int

  @native
  private def openat64(dirFD: Int, path: String, flags: Int, mode: Int): Int

  @native
  private def read(fd: Int, buffer: Array[Byte], size: NativeSizeT): NativeSignedSizeT

  @native
  private def read(fd: Int, buffer: Pointer, size: NativeSizeT): NativeSignedSizeT

  @native
  private def write(fd: Int, buffer: Array[Byte], size: NativeSizeT): NativeSignedSizeT

  @native
  private def write(fd: Int, buffer: Pointer, size: NativeSizeT): NativeSignedSizeT

  def closeZIO(fd: Int): ZIO[ZEnv, IOException, Int] = ZIO.succeed(close(fd))

  def openZIO(path: String, flags: Int): ZIO[ZEnv, IOException, Int] = ZIO.succeed(open(path, flags))

  def ioctlZIO(fd: Int, request: NativeLong, arg: Int): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(ioctl(fd, request, arg)).refineToOrDie[IOException]

  def ioctlZIO(fd: Int, request: NativeLong, arg: Long): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(ioctl(fd, request, arg)).refineToOrDie[IOException]
  
  def ioctlZIO(fd: Int, request: NativeLong, arg: NativeLong): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(ioctl(fd, request, arg)).refineToOrDie[IOException]

  def ioctlZIO(fd: Int, request: NativeLong, arg: Pointer): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(ioctl(fd, request, arg)).refineToOrDie[IOException]

  def lseek64ZIO(fd: Int, offset: Long, whence: Int): ZIO[ZEnv, IOException, Long] =
    ZIO.effect(lseek64(fd, offset, whence)).refineToOrDie[IOException]

  def open64ZIO(path: String, flags: Int): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(open64(path, flags)).refineToOrDie[IOException]

  def open64ZIO(path: String, flags: Int, mode: Int): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(open64(path, flags, mode)).refineToOrDie[IOException]
  
  def openat64ZIO(dirFD: Int, path: String, flags: Int): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(openat64(dirFD, path, flags)).refineToOrDie[IOException]

  def openat64ZIO(dirFD: Int, path: String, flags: Int, mode: Int): ZIO[ZEnv, IOException, Int] =
    ZIO.effect(openat64(dirFD, path, flags, mode)).refineToOrDie[IOException]

  def readZIO(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[ZEnv, IOException, NativeSignedSizeT] =
    ZIO.effect(read(fd, buffer, size)).refineToOrDie[IOException]

  def readZIO(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[ZEnv, IOException, NativeSignedSizeT] =
    ZIO.effect(read(fd, buffer, size)).refineToOrDie[IOException]

  def writeZIO(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[ZEnv, IOException, NativeSignedSizeT] =
    ZIO.effect(write(fd, buffer, size)).refineToOrDie[IOException]

  def writeZIO(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[ZEnv, IOException, NativeSignedSizeT] =
    ZIO.effect(write(fd, buffer, size)).refineToOrDie[IOException]

  protected def failZIO: ZIO[ZEnv, IOException, Unit] = ZIO.fail(new IOException("Error"))
}

object ZLinuxIO {

  type Native = JnaNative

  def make: ZManaged[ZEnv, IOException, ZLinuxIO[Native]] =
    ZManaged.make(
      ZIO.effect {
        val zLinuxIO = new ZLinuxIO[Native]
        zLinuxIO.load
        zLinuxIO
      } mapError(ex =>
        if (!Platform.isLinux)
          PlatformException("Not LINUX")
        else
          new IOException(ex)
        )
    ) {
      _.failZIO.ignore
    }
}