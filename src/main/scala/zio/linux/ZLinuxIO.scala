package zio.linux

import com.sun.jna.{NativeLong, Platform, Pointer, Native => JnaNative}
import zio.blocking._
import zio.{ZIO, ZManaged}

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

  def closeZIO(fd: Int): ZIO[Blocking, IOException, Int] = ZIO.succeed(close(fd))

  def openZIO(path: String, flags: Int): ZIO[Blocking, IOException, Int] = ZIO.succeed(open(path, flags))

  def ioctlZIO(fd: Int, request: NativeLong, arg: Int): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(ioctl(fd, request, arg))

  def ioctlZIO(fd: Int, request: NativeLong, arg: Long): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(ioctl(fd, request, arg))
  
  def ioctlZIO(fd: Int, request: NativeLong, arg: NativeLong): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(ioctl(fd, request, arg))

  def ioctlZIO(fd: Int, request: NativeLong, arg: Pointer): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(ioctl(fd, request, arg))

  def lseek64ZIO(fd: Int, offset: Long, whence: Int): ZIO[Blocking, IOException, Long] =
    effectBlockingIO(lseek64(fd, offset, whence))

  def open64ZIO(path: String, flags: Int): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(open64(path, flags))

  def open64ZIO(path: String, flags: Int, mode: Int): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(open64(path, flags, mode))
  
  def openat64ZIO(dirFD: Int, path: String, flags: Int): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(openat64(dirFD, path, flags))

  def openat64ZIO(dirFD: Int, path: String, flags: Int, mode: Int): ZIO[Blocking, IOException, Int] =
    effectBlockingIO(openat64(dirFD, path, flags, mode))

  def readZIO(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[Blocking, IOException, NativeSignedSizeT] =
    effectBlockingIO(read(fd, buffer, size))

  def readZIO(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[Blocking, IOException, NativeSignedSizeT] =
    effectBlockingIO(read(fd, buffer, size))

  def writeZIO(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[Blocking, IOException, NativeSignedSizeT] =
    effectBlockingIO(write(fd, buffer, size))

  def writeZIO(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[Blocking, IOException, NativeSignedSizeT] =
    effectBlockingIO(write(fd, buffer, size))

  protected def failZIO: ZIO[Blocking, IOException, IOException] = effectBlockingIO(new IOException("Error"))
}

object ZLinuxIO {

  type Native = JnaNative

  def make: ZManaged[Blocking, IOException, ZLinuxIO[Native]] =
    ZManaged.make(
      effectBlocking {
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