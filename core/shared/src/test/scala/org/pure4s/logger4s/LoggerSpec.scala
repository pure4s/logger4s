package org.pure4s.logger4s
import org.pure4s.logger4s.Logger.showString
import cats.effect.IO
import org.scalatest._

class LoggerSpec extends FunSpec with Matchers {

  implicit val instance: Logger[IO] = Logger.instance[IO](classOf[LoggerSpec])

  describe("Logger[F[_]].info") {
    it(s"Success expected info message") {
      Logger[IO].info("Hello Word Info").unsafeRunSync()
    }
  }
}