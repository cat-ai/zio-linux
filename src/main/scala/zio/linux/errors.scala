package zio.linux

import com.sun.jna.Native

import java.io.IOException

abstract class LinuxException(msg: String) extends IOException(msg)

case class PlatformException(msg: String) extends LinuxException(msg)

case class NativeLinuxException(errorNumber: Int, msg: String) extends LinuxException(msg) {
  def error: Int = errorNumber
}

object NativeLinuxException {

  def apply: NativeLinuxException = {
    val error = Native.getLastError
    new NativeLinuxException(error, s"Error number: $error")
  }

  def fromError(error: Int): NativeLinuxException = new NativeLinuxException(error, s"Error number: $error")
}

object Error {

  /**
   * Operation not permitted.
   */
  val EPERM = 1

  /**
   * No such file or directory.
   */
  val ENOENT = 2

  /**
   * No such process.
   */
  val ESRCH = 3

  /**
   * Interrupted system call.
   */
  val EINTR = 4

  /**
   * Input/output error.
   */
  val EIO = 5

  /**
   * No such device or address.
   */
  val ENXIO = 6

  /**
   * Argument list too long.
   */
  val E2BIG = 7

  /**
   * Exec format error.
   */
  val ENOEXEC = 8

  /**
   * Bad file descriptor.
   */
  val EBADF = 9

  /**
   * No child processes.
   */
  val ECHILD = 10

  /**
   * Resource temporarily unavailable  (same code as EWOULDBLOCK).
   */
  val EAGAIN = 11

  /**
   * Cannot allocate memory.
   */
  val ENOMEM = 12

  /**
   * Permission denied.
   */
  val EACCES = 13

  /**
   * Bad address.
   */
  val EFAULT = 14

  /**
   * Block device required.
   */
  val ENOTBLK = 15

  /**
   * Device or resource busy.
   */
  val EBUSY = 16

  /**
   * File exists.
   */
  val EEXIST = 17

  /**
   * Invalid cross-device link.
   */
  val EXDEV = 18

  /**
   * No such device.
   */
  val ENODEV = 19

  /**
   * Not a directory.
   */
  val ENOTDIR = 20

  /**
   * Is a directory.
   */
  val EISDIR = 21

  /**
   * Invalid argument.
   */
  val EINVAL = 22

  /**
   * Too many open files in system.
   */
  val ENFILE = 23

  /**
   * Too many open files.
   */
  val EMFILE = 24

  /**
   * Inappropriate ioctl for device.
   */
  val ENOTTY = 25

  /**
   * Text file busy.
   */
  val ETXTBSY = 26

  /**
   * File too large.
   */
  val EFBIG = 27

  /**
   * No space left on device.
   */
  val ENOSPC = 28

  /**
   * Illegal seek.
   */
  val ESPIPE = 29

  /**
   * Read-only file system.
   */
  val EROFS = 30

  /**
   * Too many links.
   */
  val EMLINK = 31

  /**
   * Broken pipe.
   */
  val EPIPE = 32

  /**
   * Numerical argument out of domain.
   */
  val EDOM = 33

  /**
   * Numerical result out of range.
   */
  val ERANGE = 34

  /**
   * Resource deadlock avoided.
   */
  val EDEADLK = 35

  /**
   * File name too long.
   */
  val ENAMETOOLONG = 36

  /**
   * No locks available.
   */
  val ENOLCK = 37

  /**
   * Function not implemented.
   */
  val ENOSYS = 38

  /**
   * Directory not empty.
   */
  val ENOTEMPTY = 39

  /**
   * Too many levels of symbolic links.
   */
  val ELOOP = 40

  /**
   * Resource temporarily unavailable (same code as EAGAIN).
   */
  val EWOULDBLOCK = 11

  /**
   * No message of desired type.
   */
  val ENOMSG = 42

  /**
   * Identifier removed.
   */
  val EIDRM = 43

  /**
   * Channel number out of range.
   */
  val ECHRNG = 44

  /**
   * Level 2 not synchronized.
   */
  val EL2NSYNC = 45

  /**
   * Level 3 halted.
   */
  val EL3HLT = 46

  /**
   * Level 3 reset.
   */
  val EL3RST = 47

  /**
   * Link number out of range.
   */
  val ELNRNG = 48

  /**
   * Protocol driver not attached.
   */
  val EUNATCH = 49

  /**
   * No CSI structure available.
   */
  val ENOCSI = 50

  /**
   * Level 2 halted.
   */
  val EL2HLT = 51

  /**
   * Invalid exchange.
   */
  val EBADE = 52

  /**
   * Invalid request descriptor.
   */
  val EBADR = 53

  /**
   * Exchange full.
   */
  val EXFULL = 54

  /**
   * No anode.
   */
  val ENOANO = 55

  /**
   * Invalid request code.
   */
  val EBADRQC = 56

  /**
   * Invalid slot.
   */
  val EBADSLT = 57

  /**
   * Bad font file format.
   */
  val EBFONT = 59

  /**
   * Device not a stream.
   */
  val ENOSTR = 60

  /**
   * No data available.
   */
  val ENODATA = 61

  /**
   * Timer expired.
   */
  val ETIME = 62

  /**
   * Out of streams resources.
   */
  val ENOSR = 63

  /**
   * Machine is not on the network.
   */
  val ENONET = 64

  /**
   * Package not installed.
   */
  val ENOPKG = 65

  /**
   * Object is remote.
   */
  val EREMOTE = 66

  /**
   * Link has been severed.
   */
  val ENOLINK = 67

  /**
   * Advertise error.
   */
  val EADV = 68

  /**
   * Srmount error.
   */
  val ESRMNT = 69

  /**
   * Communication error on send.
   */
  val ECOMM = 70

  /**
   * Protocol error.
   */
  val EPROTO = 71

  /**
   * Multihop attempted.
   */
  val EMULTIHOP = 72

  /**
   * RFS specific error.
   */
  val EDOTDOT = 73

  /**
   * Bad message.
   */
  val EBADMSG = 74

  /**
   * Value too large for defined data type.
   */
  val EOVERFLOW = 75

  /**
   * Name not unique on network.
   */
  val ENOTUNIQ = 76

  /**
   * File descriptor in bad state.
   */
  val EBADFD = 77

  /**
   * Remote address changed.
   */
  val EREMCHG = 78

  /**
   * Can not access a needed shared library.
   */
  val ELIBACC = 79

  /**
   * Accessing a corrupted shared library.
   */
  val ELIBBAD = 80

  /**
   * .lib section in a.out corrupted.
   */
  val ELIBSCN = 81

  /**
   * Attempting to link in too many shared libraries.
   */
  val ELIBMAX = 82

  /**
   * Cannot exec a shared library directly.
   */
  val ELIBEXEC = 83

  /**
   * Invalid or incomplete multibyte or wide character.
   */
  val EILSEQ = 84

  /**
   * Interrupted system call should be restarted.
   */
  val ERESTART = 85

  /**
   * Streams pipe error.
   */
  val ESTRPIPE = 86

  /**
   * Too many users.
   */
  val EUSERS = 87

  /**
   * Socket operation on non-socket.
   */
  val ENOTSOCK = 88

  /**
   * Destination address required.
   */
  val EDESTADDRREQ = 89

  /**
   * Message too long.
   */
  val EMSGSIZE = 90

  /**
   * Protocol wrong type for socket.
   */
  val EPROTOTYPE = 91

  /**
   * Protocol not available.
   */
  val ENOPROTOOPT = 92

  /**
   * Protocol not supported.
   */
  val EPROTONOSUPPORT = 93

  /**
   * Socket type not supported.
   */
  val ESOCKTNOSUPPORT = 94

  /**
   * Operation not supported.
   */
  val EOPNOTSUPP = 95

  /**
   * Protocol family not supported.
   */
  val EPFNOSUPPORT = 96

  /**
   * Address family not supported by protocol.
   */
  val EAFNOSUPPORT = 97

  /**
   * Address already in use.
   */
  val EADDRINUSE = 98

  /**
   * Cannot assign requested address.
   */
  val EADDRNOTAVAIL = 99

  /**
   * Network is down.
   */
  val ENETDOWN = 100

  /**
   * Network is unreachable.
   */
  val ENETUNREACH = 101

  /**
   * Network dropped connection on reset.
   */
  val ENETRESET = 102

  /**
   * Software caused connection abort.
   */
  val ECONNABORTED = 103

  /**
   * Connection reset by peer.
   */
  val ECONNRESET = 104

  /**
   * No buffer space available.
   */
  val ENOBUFS = 105

  /**
   * Transport endpoint is already connected.
   */
  val EISCONN = 106

  /**
   * Transport endpoint is not connected.
   */
  val ENOTCONN = 107

  /**
   * Cannot send after transport endpoint shutdown.
   */
  val ESHUTDOWN = 108

  /**
   * Too many references: cannot splice.
   */
  val ETOOMANYREFS = 109

  /**
   * Connection timed out.
   */
  val ETIMEDOUT = 110

  /**
   * Connection refused.
   */
  val ECONNREFUSED = 111

  /**
   * Host is down.
   */
  val EHOSTDOWN = 112

  /**
   * No route to host.
   */
  val EHOSTUNREACH = 113

  /**
   * Operation already in progress.
   */
  val EALREADY = 114

  /**
   * Operation now in progress.
   */
  val EINPROGRESS = 115

  /**
   * Stale file handle.
   */
  val ESTALE = 116

  /**
   * Structure needs cleaning.
   */
  val EUCLEAN = 117

  /**
   * Not a XENIX named type file.
   */
  val ENOTNAM = 118

  /**
   * No XENIX semaphores available.
   */
  val ENAVAIL = 119

  /**
   * Is a named type file.
   */
  val EISNAM = 120

  /**
   * Remote I/O error.
   */
  val EREMOTEIO = 121

  /**
   * Disk quota exceeded.
   */
  val EDQUOT = 122

  /**
   * No medium found.
   */
  val ENOMEDIUM = 123

  /**
   * Wrong medium type.
   */
  val EMEDIUMTYPE = 124

  /**
   * Operation canceled.
   */
  val ECANCELED = 125

  /**
   * Required key not available.
   */
  val ENOKEY = 126

  /**
   * Key has expired.
   */
  val EKEYEXPIRED = 127

  /**
   * Key has been revoked.
   */
  val EKEYREVOKED = 128

  /**
   * Key was rejected by service.
   */
  val EKEYREJECTED = 129

  /**
   * Owner died.
   */
  val EOWNERDEAD = 130

  /**
   * State not recoverable.
   */
  val ENOTRECOVERABLE = 131

  /**
   * Operation not possible due to RF-kill.
   */
  val ERFKILL = 132

  /**
   * Memory page has hardware error.
   */
  val EHWPOISON = 133
}