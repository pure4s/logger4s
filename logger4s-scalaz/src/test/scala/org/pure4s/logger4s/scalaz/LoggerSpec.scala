package org.pure4s.logger4s.scalaz

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, _}
import org.slf4j.{Logger => Underlying}
import scalaz.zio.IO
import scalaz.zio._

class LoggerSpec extends FunSpec with Matchers with MockitoSugar with RTS {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msg))
      verify(underlying).info(msg)
    }
  }

  describe("Logger[F[_]].warn") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn message") {
      unsafeRun(Logger[IO[Nothing, ?]].warn(msg))
      verify(underlying).warn(msg)
    }
  }

  describe("Logger[F[_]].debug") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug message") {
      unsafeRun(Logger[IO[Nothing, ?]].debug(msg))
      verify(underlying).debug(msg)
    }
  }

  describe("Logger[F[_]].error") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error message") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msg))
      verify(underlying).error(msg)
    }
    it(s"Success expected error message with Throwable") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msg, exception))
      verify(underlying).error(msg, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {
      val msg = "msg"
      implicit val underlying: Underlying = mock[org.slf4j.Logger]
      implicit val logger = Logger.instance(classOf[LoggerSpec])

      when(p(underlying)).thenReturn(isEnabled)
    }
}