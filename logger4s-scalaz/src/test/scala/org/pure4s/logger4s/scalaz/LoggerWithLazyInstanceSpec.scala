package org.pure4s.logger4s.scalaz

import org.mockito.Mockito._
import org.pure4s.logger4s.LazyLogging
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, _}
import org.slf4j.{Logger => Underlying}
import scalaz.zio.{IO, _}
import Logger._
import scalaz.Show
import scalaz.syntax.show._

class LoggerWithLazyInstanceSpec extends FunSpec with Matchers with MockitoSugar with RTS with FakeLazyLogging {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msg))
      verify(logger).info(msg)
    }
  }

  describe("Logger[F[_]].info with generic message") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msgObj))
      verify(logger).info(msgObj.shows)
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

  describe("Logger[F[_]].warn with generic message") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].warn(msgObj))
      verify(logger).warn(msgObj.shows)
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

  describe("Logger[F[_]].debug with generic message") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].debug(msgObj))
      verify(logger).debug(msgObj.shows)
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

  describe("Logger[F[_]].error with generic message") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msgObj))
      verify(logger).error(msgObj.shows)
    }
    it(s"Success expected error generic message with Throwable") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msgObj, exception))
      verify(logger).error(msgObj.shows, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {
      val msg      = "msg"
      val msgValue = "msgValue"
      case class Msg(value: String)

      implicit val showAs: Show[Msg] = new Show[Msg] { override def shows(t: Msg): String = t.value }

      val msgObj = Msg(msgValue)
      when(p(logger)).thenReturn(isEnabled)
    }
}

trait FakeLazyLogging extends LazyLogging with MockitoSugar {
  override implicit lazy val logger: Underlying = mock[org.slf4j.Logger]
}
