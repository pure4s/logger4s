package org.pure4s.logger4s.scalaz

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, _}
import org.slf4j.{Logger => Underlying}
import scalaz.zio.IO
import scalaz.zio._
import scalaz.Show
import scalaz.syntax.show._

class LoggerWithClassInstanceSpec extends FunSpec with Matchers with MockitoSugar with RTS {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msg))
      verify(underlying).info(msg)
    }
  }

  describe("Logger[F[_]].info with generic message") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].info(msgObj))
      verify(underlying).info(msgObj.shows)
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

  describe("Logger[F[_]].warn with generic message") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].warn(msgObj))
      verify(underlying).warn(msgObj.shows)
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

  describe("Logger[F[_]].debug with generic message") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].debug(msgObj))
      verify(underlying).debug(msgObj.shows)
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

  describe("Logger[F[_]].error with generic message") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error generic message") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msgObj))
      verify(underlying).error(msgObj.shows)
    }
    it(s"Success expected error generic message with Throwable") {
      unsafeRun(Logger[IO[Nothing, ?]].error(msgObj, exception))
      verify(underlying).error(msgObj.shows, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {

      val msg = "msg"
      val msgValue = "msgValue"

      case class Msg(value: String)

      implicit val showAs: Show[Msg] = new Show[Msg] { override def shows(t: Msg): String = t.value }

      val msgObj = Msg(msgValue)

      implicit val underlying: Underlying = mock[org.slf4j.Logger]
      implicit val logger                 = Logger.instance(classOf[LoggerWithClassInstanceSpec])

      when(p(underlying)).thenReturn(isEnabled)
    }
}
