package zio.linux

import com.sun.jna.Memory
import zio._
import zio.blocking.Blocking
import zio.duration.durationInt
import zio.test.Assertion.equalTo
import zio.test._
import zio.test.environment.TestEnvironment

import java.io.IOException

object ZioLinuxTest extends DefaultRunnableSpec {

  val linuxMemory: ZLayer[Blocking, TestFailure[IOException], LinuxMemory] = memory.mapError(TestFailure.fail)
  val linuxIO:     ZLayer[Blocking, TestFailure[IOException], LinuxIO]     = io.mapError(TestFailure.fail)

  override def spec: Spec[TestEnvironment, TestFailure[Exception], TestSuccess] =
    suite("Common Linux Tests")(
      testM("memcpy") {
        val src  = new Memory(1 << 4)
        val dest = new Memory(1 << 4)

        for {
          copied <- LinuxMemory.memcpy(src, dest)
          result <- LinuxMemory.equal(src, copied).map(_ => true)
        } yield assert(result)(equalTo(true))
      }
    ).provideCustomLayerShared(linuxMemory ++ linuxIO ++ Blocking.live) @@ TestAspect.timeout(30.seconds)
}
