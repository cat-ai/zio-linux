package zio

import com.sun.jna._

import zio._

import java.io.IOException

package object linux {

  type LinuxMemory = Has[ZLinuxMemory[ZLinuxMemory.Native]]
  type LinuxIO     = Has[ZLinuxIO[ZLinuxIO.Native]]

  type size_t  = NativeSizeT
  type ssize_t = NativeSignedSizeT

  def lastError: ZIO[ZEnv, IOException, Int] =
    ZIO.effect(Native.getLastError).refineToOrDie[IOException]

  def setLastError(number: Int): ZIO[ZEnv, IOException, Unit] =
    ZIO.effect(Native.setLastError(number)).refineToOrDie[IOException]

  def nativeSizeT: ZIO[ZEnv, IOException, Int] =
    ZIO.effect(Native.SIZE_T_SIZE).refineToOrDie[IOException]

  def alloc(size: Int): ZIO[ZEnv, IOException, Memory] =
    ZIO.effect(new Memory(size)).refineToOrDie[IOException]

  def free(pointer: Pointer): ZIO[ZEnv, IOException, Unit] =
    ZIO.effect(Native.free(Pointer.nativeValue(pointer))).refineToOrDie[IOException]

  object LinuxMemory {
    
    def memcmp(ptr1: Pointer, ptr2: Pointer, n: NativeSizeT): ZIO[LinuxMemory with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.memcmpZIO(ptr1, ptr2, n))

    def memcpy(src: Memory, dest: Pointer): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.memcpyZIO(src, dest))

    def memset(ptr: Pointer, b: Int, n: NativeSizeT): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.memsetZIO(ptr, b, n))

    def memset(ptr: Memory, b: Int): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.memsetZIO(ptr, b))

    def equal(ptr: Pointer, ptr2: Pointer, n: NativeSizeT): ZIO[LinuxMemory with ZEnv, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(ptr, ptr2, n))

    def equal(ptr: Pointer, memory: Memory): ZIO[LinuxMemory with ZEnv, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(ptr, memory))
      
    def equal(memory: Memory, ptr: Pointer): ZIO[LinuxMemory with ZEnv, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(memory, ptr))

    def equal(mem1: Memory, mem2: Memory): ZIO[LinuxMemory with ZEnv, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(mem1, mem2))

    def unsignedByte(ptr: Pointer, offset: Long): ZIO[LinuxMemory with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.unsignedByteZIO(ptr, offset))

    def setUnsignedByte(ptr: Pointer, offset: Long, b: Int): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedByteZIO(ptr, offset, b))

    def unsignedShort(ptr: Pointer, offset: Long): ZIO[LinuxMemory with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.unsignedShortZIO(ptr, offset))

    def setUnsignedShort(ptr: Pointer, offset: Long, s: Int): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedShortZIO(ptr, offset, s))

    def unsignedInt(ptr: Pointer, offset: Long): ZIO[LinuxMemory with ZEnv, IOException, Long] =
      ZIO.accessM(_.get.unsignedIntZIO(ptr, offset))

    def setUnsignedInt(ptr: Pointer, offset: Long, i: Long): ZIO[LinuxMemory with ZEnv, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedIntZIO(ptr, offset, i))
  }
  
  object LinuxIO {

    def close(fd: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.closeZIO(fd))

    def open(path: String, flags: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.openZIO(path, flags))

    def ioctl(fd: Int, request: NativeLong, arg: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: Long): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: NativeLong): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: Pointer): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def lseek64(fd: Int, offset: Long, whence: Int): ZIO[LinuxIO with ZEnv, IOException, Long] =
      ZIO.accessM(_.get.lseek64ZIO(fd, offset, whence))

    def open64(path: String, flags: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.open64ZIO(path, flags))

    def open64(path: String, flags: Int, mode: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.open64ZIO(path, flags, mode))

    def openat64(dirFD: Int, path: String, flags: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.openat64ZIO(dirFD, path, flags))

    def openat64(dirFD: Int, path: String, flags: Int, mode: Int): ZIO[LinuxIO with ZEnv, IOException, Int] =
      ZIO.accessM(_.get.openat64ZIO(dirFD, path, flags, mode))

    def read(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[LinuxIO with ZEnv, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.readZIO(fd, buffer, size))

    def read(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[LinuxIO with ZEnv, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.readZIO(fd, buffer, size))

    def write(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[LinuxIO with ZEnv, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.writeZIO(fd, buffer, size))

    def write(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[LinuxIO with ZEnv, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.writeZIO(fd, buffer, size))
  }

  def memory: ZLayer[ZEnv, IOException, LinuxMemory] =
    ZLayer.fromManaged(ZLinuxMemory.make)

  def io: ZLayer[ZEnv, IOException, LinuxIO] =
    ZLayer.fromManaged(ZLinuxIO.make)
}