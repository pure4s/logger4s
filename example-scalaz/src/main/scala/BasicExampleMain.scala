import org.pure4s.logger4s.LazyLogging
import scalaz.zio.{IO, RTS}
import org.pure4s.logger4s.scalaz.Logger

object BasicExampleMain extends App with RTS with LazyLogging {

  unsafeRun(Logger[IO[Nothing, ?]].info(s"Hello word, purely functional logger"))
}
