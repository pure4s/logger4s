package org.pure4s.logger4s.scalaz

import org.mockito.Mockito._
import org.pure4s.logger4s.LazyLogging
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, _}
import org.slf4j.{Logger => Underlying}
import scalaz.zio.{IO, _}
import Logger._

class LoggerWithLazySpec extends FunSpec with Matchers with MockitoSugar with RTS with FakeLazyLogging {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msg))
      verify(logger).info(msg)
    }
  }

  describe("Logger[F[_]].warn") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn message") {
      unsafeRun(Logger[IO[Nothing, ?]].warn(msg))
      verify(logger).warn(msg)
    }
  }

  describe("Logger[F[_]].debug") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug message") {
      unsafeRun(Logger[IO[Nothing, ?]].debug(msg))
      verify(logger).debug(msg)
    }
  }

  describe("Logger[F[_]].error") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error message") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msg))
      verify(logger).error(msg)
    }
    it(s"Success expected error message with Throwable") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msg, exception))
      verify(logger).error(msg, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {
      val msg = "msg"
      when(p(logger)).thenReturn(isEnabled)
    }
}

trait FakeLazyLogging extends LazyLogging with MockitoSugar {
  override implicit lazy val logger: Underlying = mock[org.slf4j.Logger]
}