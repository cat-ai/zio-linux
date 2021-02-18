package zio

import com.sun.jna._

import zio.blocking._

import java.io.IOException

package object linux {

  type LinuxMemory = Has[ZLinuxMemory[ZLinuxMemory.Native]]
  type LinuxIO     = Has[ZLinuxIO[ZLinuxIO.Native]]

  type size_t  = NativeSizeT
  type ssize_t = NativeSignedSizeT

  def lastError: ZIO[Blocking, IOException, Int] =
    effectBlockingIO(Native.getLastError)

  def setLastError(number: Int): ZIO[Blocking, IOException, Unit] =
    effectBlockingIO(Native.setLastError(number))

  def nativeSizeT: ZIO[Blocking, IOException, Unit] =
    effectBlockingIO(Native.SIZE_T_SIZE)

  def alloc(size: Int): ZIO[Blocking, IOException, Memory] =
    effectBlockingIO(new Memory(size))

  def free(pointer: Pointer): ZIO[Blocking, IOException, Unit] =
    effectBlockingIO(Native.free(Pointer.nativeValue(pointer)))

  object LinuxMemory {
    
    def memcmp(ptr1: Pointer, ptr2: Pointer, n: NativeSizeT): ZIO[LinuxMemory with Blocking, IOException, Int] =
      ZIO.accessM(_.get.memcmpZIO(ptr1, ptr2, n))

    def memcpy(src: Memory, dest: Pointer): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.memcpyZIO(src, dest))

    def memset(ptr: Pointer, b: Int, n: NativeSizeT): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.memsetZIO(ptr, b, n))

    def memset(ptr: Memory, b: Int): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.memsetZIO(ptr, b))

    def equal(ptr: Pointer, ptr2: Pointer, n: NativeSizeT): ZIO[LinuxMemory with Blocking, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(ptr, ptr2, n))

    def equal(ptr: Pointer, memory: Memory): ZIO[LinuxMemory with Blocking, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(ptr, memory))
      
    def equal(memory: Memory, ptr: Pointer): ZIO[LinuxMemory with Blocking, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(memory, ptr))

    def equal(mem1: Memory, mem2: Memory): ZIO[LinuxMemory with Blocking, IOException, Boolean] =
      ZIO.accessM(_.get.equalZIO(mem1, mem2))

    def unsignedByte(ptr: Pointer, offset: Long): ZIO[LinuxMemory with Blocking, IOException, Int] =
      ZIO.accessM(_.get.unsignedByteZIO(ptr, offset))

    def setUnsignedByte(ptr: Pointer, offset: Long, b: Int): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedByteZIO(ptr, offset, b))

    def unsignedShort(ptr: Pointer, offset: Long): ZIO[LinuxMemory with Blocking, IOException, Int] =
      ZIO.accessM(_.get.unsignedShortZIO(ptr, offset))

    def setUnsignedShort(ptr: Pointer, offset: Long, s: Int): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedShortZIO(ptr, offset, s))

    def unsignedInt(ptr: Pointer, offset: Long): ZIO[LinuxMemory with Blocking, IOException, Long] =
      ZIO.accessM(_.get.unsignedIntZIO(ptr, offset))

    def setUnsignedInt(ptr: Pointer, offset: Long, i: Long): ZIO[LinuxMemory with Blocking, IOException, Pointer] =
      ZIO.accessM(_.get.setUnsignedIntZIO(ptr, offset, i))
  }
  
  object LinuxIO {

    def close(fd: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.closeZIO(fd))

    def ioctl(fd: Int, request: NativeLong, arg: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: Long): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: NativeLong): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def ioctl(fd: Int, request: NativeLong, arg: Pointer): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.ioctlZIO(fd, request, arg))

    def lseek64(fd: Int, offset: Long, whence: Int): ZIO[LinuxIO with Blocking, IOException, Long] =
      ZIO.accessM(_.get.lseek64ZIO(fd, offset, whence))

    def open64(path: String, flags: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.open64ZIO(path, flags))

    def open64(path: String, flags: Int, mode: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.open64ZIO(path, flags, mode))

    def openat64(dirFD: Int, path: String, flags: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.openat64ZIO(dirFD, path, flags))

    def openat64(dirFD: Int, path: String, flags: Int, mode: Int): ZIO[LinuxIO with Blocking, IOException, Int] =
      ZIO.accessM(_.get.openat64ZIO(dirFD, path, flags, mode))

    def readfd(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[LinuxIO with Blocking, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.readZIO(fd, buffer, size))

    def read(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[LinuxIO with Blocking, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.readZIO(fd, buffer, size))

    def write(fd: Int, buffer: Array[Byte], size: NativeSizeT): ZIO[LinuxIO with Blocking, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.writeZIO(fd, buffer, size))

    def write(fd: Int, buffer: Pointer, size: NativeSizeT): ZIO[LinuxIO with Blocking, IOException, NativeSignedSizeT] =
      ZIO.accessM(_.get.writeZIO(fd, buffer, size))
  }

  def memory: ZLayer[Blocking, IOException, LinuxMemory] =
    ZLayer.fromManaged(ZLinuxMemory.make)

  def io: ZLayer[Blocking, IOException, LinuxIO] =
    ZLayer.fromManaged(ZLinuxIO.make)
}