package org.pure4s.logger4s.cats

import cats.Show
import cats.implicits._
import cats.effect.IO
import org.mockito.Mockito._
import org.scalatest.{Matchers, _}
import org.scalatest.mockito.MockitoSugar
import org.slf4j.{Logger => Underlying}

class LoggerWithClassInstanceSpec extends FunSpec with Matchers with MockitoSugar {

  describe("Logger[F[_]].info") {
    val f = fixture(_.isInfoEnabled, isEnabled = true)
    import f._
    it(s"Success expected info message") {
      Logger[IO].info(msg).unsafeRunSync()
      verify(underlying).info(msg)
    }
    it(s"Success expected info generic message") {
      Logger[IO].info(msgObj).unsafeRunSync()
      verify(underlying).info(msgObj.show)
    }
  }

  describe("Logger[F[_]].warn") {
    val f = fixture(_.isWarnEnabled, isEnabled = true)
    import f._
    it(s"Success expected warn message") {
      Logger[IO].warn(msg).unsafeRunSync()
      verify(underlying).warn(msg)
    }
    it(s"Success expected warn generic message") {
      Logger[IO].warn(msgObj).unsafeRunSync()
      verify(underlying).warn(msgObj.show)
    }
  }

  describe("Logger[F[_]].debug") {
    val f = fixture(_.isDebugEnabled, isEnabled = true)
    import f._
    it(s"Success expected debug message") {
      Logger[IO].debug(msg).unsafeRunSync()
      verify(underlying).debug(msg)
    }
    it(s"Success expected debug generic message") {
      Logger[IO].debug(msgObj).unsafeRunSync()
      verify(underlying).debug(msgObj.show)
    }
  }

  describe("Logger[F[_]].error") {
    val f = fixture(_.isErrorEnabled, isEnabled = true)
    import f._
    lazy val exception = new Exception
    it(s"Success expected error message") {
      Logger[IO].error(msg).unsafeRunSync()
      verify(underlying).error(msg)
    }
    it(s"Success expected error message with Throwable") {
      Logger[IO].error(msg, exception).unsafeRunSync()
      verify(underlying).error(msg, exception)
    }
    it(s"Success expected error generic message") {
      Logger[IO].error(msgObj).unsafeRunSync()
      verify(underlying).error(msgObj.show)
    }
    it(s"Success expected error generic message with Throwable") {
      Logger[IO].error(msgObj, exception).unsafeRunSync()
      verify(underlying).error(msgObj.show, exception)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {

      val msg = "msg"
      case class Msg(value: String)
      implicit val showMsg = new Show[Msg] { def show(t: Msg): String = t.toString }
      val msgObj = Msg(msg)

      implicit val underlying: Underlying = mock[org.slf4j.Logger]
      implicit val logger: Logger[IO] = Logger.instance[IO](classOf[LoggerWithClassInstanceSpec])

      when(p(underlying)).thenReturn(isEnabled)
    }
}