import cats.effect.IO
import org.pure4s.logger4s.LazyLogging
import org.pure4s.logger4s.cats.Logger
import org.pure4s.logger4s.cats.Logger._

object BasicExampleMain extends App with LazyLogging {
  Logger[IO].info(s"Hello word, purely functional logger").unsafeRunSync()
}
