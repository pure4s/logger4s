package org.pure4s.logger4s

import cats.Show
import cats.implicits._
import cats.effect.IO
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, _}
import org.slf4j.{Logger => Underlying}
import Logger._

class LoggerWithLazySpec extends FunSpec with Matchers with MockitoSugar with FakeLazyLogging {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      Logger[IO].info(msg).unsafeRunSync()
      verify(logger).info(msg)
    }
    it(s"Success expected info generic message") {
      Logger[IO].info(msgObj).unsafeRunSync()
      verify(logger).info(msgObj.show)
    }
  }

  describe("Logger[F[_]].warn") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn message") {
      Logger[IO].warn(msg).unsafeRunSync()
      verify(logger).warn(msg)
    }
    it(s"Success expected warn generic message") {
      Logger[IO].warn(msgObj).unsafeRunSync()
      verify(logger).warn(msgObj.show)
    }
  }

  describe("Logger[F[_]].debug") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug message") {
      Logger[IO].debug(msg).unsafeRunSync()
      verify(logger).debug(msg)
    }
    it(s"Success expected debug generic message") {
      Logger[IO].debug(msgObj).unsafeRunSync()
      verify(logger).debug(msgObj.show)
    }
  }

  describe("Logger[F[_]].error") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error message") {
      Logger[IO].error(msg).unsafeRunSync()
      verify(logger).error(msg)
    }
    it(s"Success expected error message with Throwable") {
      Logger[IO].error(msg, exception).unsafeRunSync()
      verify(logger).error(msg, exception)
    }
    it(s"Success expected error generic message") {
      Logger[IO].error(msgObj).unsafeRunSync()
      verify(logger).error(msgObj.show)
    }
    it(s"Success expected error generic message with Throwable") {
      Logger[IO].error(msgObj, exception).unsafeRunSync()
      verify(logger).error(msgObj.show, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {
      val msg = "msg"
      case class Msg(value: String)
      implicit lazy val showMsg = new Show[Msg] { def show(t: Msg): String = t.toString }
      val msgObj = Msg(msg)

      when(p(logger)).thenReturn(isEnabled)
    }
}

trait FakeLazyLogging extends LazyLogging with MockitoSugar {
  override implicit lazy val logger: Underlying = mock[org.slf4j.Logger]
}

