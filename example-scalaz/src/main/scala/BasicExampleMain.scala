import org.pure4s.logger4s.scalaz.Logger
import scalaz.zio.{IO, RTS}

case class User(email: String)

class UserService[F[_]: Logger] {
  def findByEmail(email: String): F[Unit] = {
    Logger[F].info(s"Hello word, functional logger ($email)")
  }
}

object BasicExampleMain extends App with RTS {
  implicit val instance = Logger.instance(classOf[UserService[IO[Nothing, ?]]])

  val service = new UserService[IO[Nothing, ?]]
  unsafeRun(service.findByEmail("example@example.com"))
}
