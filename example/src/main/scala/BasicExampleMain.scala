import cats.effect.{IO, Sync}
import cats.implicits._
import org.pure4s.logger4s.Logger

case class User(email: String)

class UserService[F[_] : Sync : Logger] {
  def findByEmail(email: String): F[Option[User]] = {
    Logger[F].info(s"Hello word, functional logger ($email)") *> Option(User(email)).pure[F]
  }
}

object BasicExampleMain extends App {
  implicit val sync: Logger[IO] = Logger.sync[IO](classOf[UserService[IO]])

  val service = new UserService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
}
