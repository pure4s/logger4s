package org.pure4s.logger4s
import cats.effect.IO
import org.mockito.Mockito._
import org.scalatest.{Matchers, _}
import org.scalatest.mockito.MockitoSugar
import org.slf4j.{Logger => Underlying}

class LoggerSpec extends FunSpec with Matchers with MockitoSugar{

//  implicit val underlying: Underlying = mock[org.slf4j.Logger]
//  implicit val instance: Logger[IO] = Logger.instance[IO](classOf[LoggerSpec])

  describe("Logger[F[_]].info") {
    it(s"Success expected info message") {
      val f = fixture(_.isInfoEnabled, isEnabled = true)
      import f._
      logger.info(msg).unsafeRunSync()
      verify(underlying).error(msg)
    }
  }

  def fixture(p: Underlying => Boolean, isEnabled: Boolean) =
    new {
      val msg = "Hello Word"
      val cause = new RuntimeException("cause")
      val arg1 = "arg1"
      val arg2 = new Integer(1)
      val arg3 = "arg3"
      val arg4 = 4
      val arg4ref = arg4.asInstanceOf[AnyRef]
      val arg5 = true
      val arg5ref = arg5.asInstanceOf[AnyRef]
      val arg6 = 6L
      val arg6ref = arg6.asInstanceOf[AnyRef]
      implicit val underlying: Underlying = mock[org.slf4j.Logger]
      when(p(underlying)).thenReturn(isEnabled)
      implicit val logger: Logger[IO] = Logger.instance[IO](classOf[LoggerSpec])
    }
}